package noteFragments;

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
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import bean.Rank;

import com.example.easyrank.BaseFragment;
import com.example.easyrank.R;

public class NoteFragment extends BaseFragment implements OnClickListener {
	private View rootview;
	private Button nameAdd;
	private ListView rankList;
	private TextView text;
	CommonAdapter<Rank> myAdapter;
	List<Rank> ranks;

	private SharedPreferences pref;
	private static Context context;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootview = inflater.inflate(R.layout.fragment_note, container, false);
		// TODO Auto-generated method stub

		return rootview;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		context = getActivity().getApplicationContext();

		text = (TextView) rootview.findViewById(R.id.text_currentType);
		nameAdd = (Button) rootview.findViewById(R.id.id_name_add);
		rankList = (ListView) rootview.findViewById(R.id.rank_list);
		
		text.setText("当前类型为："+readCurrentType());
//		text.setText("当前类型为：22");
	}

	@Override
	protected void initEvents() {
		super.initEvents();
		nameAdd.setOnClickListener(this);

		ranks = new ArrayList<Rank>();
//		for (int i = 1; i < 16; i++) {
//			Rank rank = new Rank(i, "好看的同学", "小明" + i, (float) (i + 0.1), i, i,
//					i);
//			Log.i("123", rank.toString());
//			ranks.add(rank);
//		}
		
		String currentType=readCurrentType();

		 SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
		 .getApplicationContext());
		 SQLiteDatabase sd = db.getReadableDatabase();
		 String sql = "select * from rankTable order by score desc";//
		 //查询的结果按照分数排序
		 Cursor cursor = sd.rawQuery(sql, null);
		 int i=1;
		 while (cursor.moveToNext()) {
		 int id =cursor.getInt(0);
		 float score=cursor.getFloat(3);
		 String type=cursor.getString(1);
		 String name=cursor.getString(2);
		 int vCount=cursor.getInt(4);
		 int dCount=cursor.getInt(5);
		 int allCount=cursor.getInt(6);
		 if(!"".equals(name)&&type.equals(currentType)){
			 Rank rank = new Rank(id, type, name, score, vCount, dCount,allCount);
			 i++;
			 ranks.add(rank);
		 }
//		 Rank rank = new Rank(id, type, name, score, vCount, dCount,allCount);
//		 i++;
//		 ranks.add(rank);
		 }
		 cursor.close();
		 db.close();

		rankList.setAdapter(myAdapter = new CommonAdapter<Rank>(ranks, context,
				R.layout.rank_item) {

			@Override
			public void setListener(BaseViewHolder holder) {
				holder.setOnItemClickListener();
				holder.setOnItemLongClickListener();

			}

			@Override
			public void setData(BaseViewHolder holder, Rank item) {
				// holder.setText(R.id.id_id, item.getId() +
				// "-"+(ranks.indexOf(item)+1));
				holder.setText(R.id.id_id, "" + (ranks.indexOf(item) + 1));
				holder.setText(R.id.id_score, item.getScore() + "");
				holder.setText(R.id.id_name, item.getName());
				holder.setText(R.id.id_type, item.getType());
				holder.setText(R.id.id_result,
						item.getVcount() + "-" + item.getDcount()+ "-" + item.getAllcount());

			}

			@Override
			public void onClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				// TODO Auto-generated method stub
				show("短按！");

			}

			@Override
			public boolean onLonClickCallback(View v, int position,
					BaseViewHolder viewHolder) {
				// TODO Auto-generated method stub

				final Rank note = (Rank) rankList.getItemAtPosition(position);
				final int id = note.getId();// /用于删除该笔记的时候用

				new AlertDialog.Builder(rootview.getContext())
						// /在这里加入对话框，提醒用户在进行删除数据操作
						.setTitle("确认框")
						.setMessage("你确定要删除数据吗？")
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SQLiteDBUtil db = new SQLiteDBUtil(
												getActivity()
														.getApplicationContext());
										SQLiteDatabase sd = db
												.getWritableDatabase();
										String sql = "delete from rankTable where id= "
												+ id;
										sd.execSQL(sql);
										// 这里进行关闭
										db.close();

										update(note);
										show("恭喜您刪除成功！");
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										show("恩，你点击了取消");
									}
								}).show();

				return true;
			}
		});
	}

	private static class MyAdapter extends BaseAdapter {

		public List<Rank> ranks = new ArrayList<Rank>();

		public MyAdapter(List<Rank> ranks) {
			if (ranks != null) {
				this.ranks.addAll(ranks);
			}
		}

		@Override
		public int getCount() {
			return ranks.size();
		}

		@Override
		public Rank getItem(int position) {
			if (position > -1 && position < getCount()) {
				return ranks.get(position);
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView != null) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.rank_item, parent, false);
				holder = new ViewHolder(convertView);
			}
			Rank item = getItem(position);
			if (item != null) {
				// holder.tv.setText(item.getName());
				holder.id.setText(item.getId() + "");
				holder.name.setText(item.getName() + "");
				holder.type.setText(item.getType() + "");
				holder.score.setText(item.getScore() + "");
				holder.result.setText(item.getVcount() + "");

			}
			return convertView;
		}

	}

	private static class ViewHolder {
		TextView id, name, type, score, result;

		public ViewHolder(View convertView) {
			if (convertView != null) {
				convertView.setTag(this);
				id = (TextView) convertView.findViewById(R.id.id_id);
				name = (TextView) convertView.findViewById(R.id.id_name);
				type = (TextView) convertView.findViewById(R.id.id_type);
				score = (TextView) convertView.findViewById(R.id.id_score);
				result = (TextView) convertView.findViewById(R.id.id_result);
			}
		}
	}
	
	private String readCurrentType() {
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String type = pref.getString("type", "type");
		return type;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_name_add:
			show("添加项目");

			FragmentTransaction transaction = getActivity()
					.getSupportFragmentManager().beginTransaction();
			AddItemFragment fragment = new AddItemFragment();
			transaction.add(android.R.id.content, fragment, "AddItemFragment");
			transaction.addToBackStack("AddItemFragment");// 添加fragment到Activity的回退栈中
			transaction.show(fragment);
			transaction.commit();
			break;
		default:
			break;
		}
	}

}
