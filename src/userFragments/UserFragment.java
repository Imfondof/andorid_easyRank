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
	private SharedPreferences pref;// ���ڱ��浱ǰѧ��

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
				// ���йر�
				db.close();
				show(sql+"1");
				edit_Term.setText("");// ���ı�������Ϊ��

				// ���������½�����ʾ
				List<Rank> cs = new ArrayList<Rank>();
				Rank c = new Rank(0, currentTerm, "", 1, 0, 0,0);
				cs.add(c);
				myAdapter.append(cs);

			}
		});
		
		String type = readCurrentType();
		text_currentTerm.setText("��ǰѧ��Ϊ:"+type);

		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);

		List<Rank> courses = new ArrayList<Rank>();

		// select distinct term from kebiao;
		// �����ݿ��в�ѯ��һ������Щѧ��
		SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
				.getApplicationContext());
		SQLiteDatabase sd = db.getReadableDatabase();
		// String sql =
		// "select distinct term from kebiao";////////////���д��뱻��һ�����������Ϊ���д���Ļ�������һ���˺ŵ�¼��ѧ�ڻ���֮ǰ��ѧ�ڡ�
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
					String s = "�������" + type + "����ǰѧ������Ϊ" + type;
					show(s);
					setType(type);
					text_currentTerm.setText("��ǰѧ��Ϊ��" + type);// ���½�����ʾѧ��

					// ���͹㲥����������
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
				if (type == currentType) {// �����Ҫɾ����ѧ�ڣ�������ʹ�õ�ѧ�ڣ���ô��ʾ�û�����ɾ����
					show("����ɾ����ѧ��������ʹ�õ�ѧ��Ŷ������ĵ�ǰ��ѧ����ɾ��Ŷ");
				} else {
					new AlertDialog.Builder(rootview.getContext())
							// /���������Ի��������û��ڽ���ɾ�����ݲ���
							.setTitle("ȷ�Ͽ�")
							.setMessage("��ȷ��Ҫɾ����ѧ�����㽫ɾ�����˺������и�ѧ�ڵĿγ�Ŷ����������")
							.setPositiveButton("ȷ��",
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
											show("��ϲ���h���ɹ���"+sql);
											sd.execSQL(sql,new Object[] {type});
											// ������йر�
											db.close();
											show("��ϲ���h���ɹ���");

											// ����������÷��͹㲥����
											// //���͹㲥����������
											// Intent intent = new
											// Intent("jerry");
											// intent.putExtra("change",
											// "termDelete");
											// LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);

										}
									})
							.setNegativeButton("ȡ��",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											show("����������ȡ��");
										}
									}).show();
				}

				return true;// �˴������棬����ô�ڳ����¼������󣬾Ͳ��ᴥ���̰��¼���
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