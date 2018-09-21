package userFragments;

import java.util.ArrayList;
import java.util.List;

import utils.SQLiteDBUtil;
import adapter.BaseViewHolder;
import adapter.CommonAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import bean.Rank;

import com.example.easyrank.BaseFragment;
import com.example.easyrank.R;

public class UserFragment extends BaseFragment implements OnClickListener {
	private View rootview;

	private static Context context;
	private ListView term_list;
	private TextView text_currentTerm;
	private EditText edit_Term;
	private Button btn_addTerm;
	CommonAdapter<Rank> myAdapter;
	private SharedPreferences pref;// 用于保存当前学期

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_user, container, false);
		// TODO Auto-generated method stub

		return rootview;
	}

	@Override
	protected void initView(View view) {
		// TODO Auto-generated method stub
		super.initView(view);

		context = getActivity().getApplicationContext();
		btn_addTerm = (Button) rootview.findViewById(R.id.id_type_add);
		edit_Term = (EditText) rootview.findViewById(R.id.id_current_type);
		term_list = (ListView) rootview.findViewById(R.id.type_list);
		text_currentTerm = (TextView) rootview
				.findViewById(R.id.text_currentType);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		super.initEvents();
		btn_addTerm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pref = getActivity().getSharedPreferences("data",
						Context.MODE_PRIVATE);

				String currentTerm = edit_Term.getText().toString();
				SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
						.getApplicationContext());
				SQLiteDatabase sd = db.getWritableDatabase();
				String sql = "insert into rankTable values(null,?,?,?,?,?,?)";
				sd.execSQL(sql, new Object[] {  currentTerm,"", 1000.0, 0, 0, 0 });
				// 进行关闭
				db.close();
				show(sql+"1");
				edit_Term.setText("");// 将文本框设置为空

				// 接下来跟新界面显示
				List<Rank> cs = new ArrayList<Rank>();
				Rank c = new Rank(0, currentTerm, "", 1, 0, 0,0);
				cs.add(c);
				myAdapter.append(cs);

			}
		});
		
		String type = readCurrentType();
		text_currentTerm.setText("当前学期为:"+type);

		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

		List<Rank> courses = new ArrayList<Rank>();

		// select distinct term from kebiao;
		// 从数据库中查询出一共有哪些学期
		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		// String sql =
		// "select distinct term from kebiao";////////////这行代码被下一行所替代，因为这行代码的话，，患一个账号登录，学期还是之前的学期。
		String sql = "select distinct type from ranktable";
		Cursor cursor = sd.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			String receive_rank=cursor.getString(0);
			Rank c = new Rank(0, receive_rank, "", 1, 0, 0, 0);
			courses.add(c);
		}
		db.close();
		cursor.close();

		term_list.setAdapter(myAdapter = new CommonAdapter<Rank>(courses,
				context, R.layout.list_item) {
			@Override
			public void setListener(BaseViewHolder holder) {
				holder.setOnItemClickListener();
				holder.setOnItemLongClickListener();
			}

			@Override
			public void setData(BaseViewHolder holder, Rank item) {
				holder.setText(R.id.list_item_tv, item.getType() + "");
			}

			@Override
			public void onClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				switch (v.getId()) {

				default:
					Rank c = (Rank) term_list.getItemAtPosition(position);
					String type = c.getType();
					String s = "您点击了" + type + "，当前学期设置为" + type;
					show(s);
					setType(type);
					text_currentTerm.setText("当前学期为：" + type);// 更新界面显示学期

					// 发送广播，更新数据
//					Intent intent = new Intent("jerry");
//					intent.putExtra("change", "termDetail");
//					LocalBroadcastManager.getInstance(getActivity())
//							.sendBroadcast(intent);
					break;
				}
			}

			@Override
			public boolean onLonClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				// TODO Auto-generated method stub

				Rank c = (Rank) term_list.getItemAtPosition(position);
				final String type = c.getType();

				String currentType = readCurrentType();
				if (type == currentType) {// 如果想要删除的学期，是正在使用的学期，那么提示用户不能删除，
					show("你所删除的学期是正在使用的学期哦，请更改当前的学期再删除哦");
				} else {
					new AlertDialog.Builder(rootview.getContext())
							// /在这里加入对话框，提醒用户在进行删除数据操作
							.setTitle("确认框")
							.setMessage("你确定要删除该学期吗？你将删除此账号下所有该学期的课程哦！！请慎重")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											SQLiteDBUtil db = new SQLiteDBUtil(
													getActivity()
															.getApplicationContext());
											SQLiteDatabase sd = db
													.getWritableDatabase();
											String sql = "delete from ranktable where type=?";
											show("恭喜您刪除成功！"+sql);
											sd.execSQL(sql,new Object[] {type});
											// 这里进行关闭
											db.close();
											show("恭喜您刪除成功！");

											// 这个，好像不用发送广播。。
											// //发送广播，更新数据
											// Intent intent = new
											// Intent("jerry");
											// intent.putExtra("change",
											// "termDelete");
											// LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											show("恩，你点击了取消");
										}
									}).show();
				}

				return true;// 此处返回真，，那么在长按事件触发后，就不会触发短按事件了
			}
		});
	}

	private static class MyAdapter extends BaseAdapter {
		private List<Rank> courses = new ArrayList<Rank>();

		public MyAdapter(List<Rank> courses) {
			if (courses != null) {
				this.courses.addAll(courses);
			}
		}

		@Override
		public int getCount() {
			return courses.size();
		}

		@Override
		public Rank getItem(int position) {
			if (position > -1 && position < getCount()) {
				return courses.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item, parent, false);
				holder = new ViewHolder(convertView);

			}
			Rank item = getItem(position);
			if (item != null) {
				holder.tv.setText(item.getType() + "");
			}

			return convertView;
		}
	}

	private static class ViewHolder {
		TextView tv;

		public ViewHolder(View convertView) {
			if (convertView != null) {
				convertView.setTag(this);
				tv = (TextView) convertView.findViewById(R.id.list_item_tv);
			}

		}
	}

	private String readCurrentType() {
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String type = pref.getString("type", "type");
		;
		return type;
	}

	private void setType(String type) {
		SharedPreferences.Editor editor = getActivity().getSharedPreferences(
				"data", Context.MODE_PRIVATE).edit();
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		editor.putString("type", type);
		editor.apply();
	}

	@Override
	public void onClick(View v) {

	}

}