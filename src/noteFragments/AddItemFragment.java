package noteFragments;

import utils.SQLiteDBUtil;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.easyrank.BaseFragment;
import com.example.easyrank.R;

public class AddItemFragment extends BaseFragment implements OnClickListener {

	private View rootview;

	private static Context context;
	private SharedPreferences pref;
	
	private Button btn_add_item;
	private EditText edit_add_name;

	public AddItemFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview = inflater.inflate(R.layout.fragment_add_item, container,
				false);
		return rootview;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		context = getActivity().getApplicationContext();

		btn_add_item = (Button) rootview.findViewById(R.id.id_add_btn);
		edit_add_name = (EditText) rootview.findViewById(R.id.id_add_item_name);
	}

	@Override
	protected void initEvents() {//
		// TODO Auto-generated method stub
		super.initEvents();
		btn_add_item.setOnClickListener(this);
	}
	
	private String readCurrentType() {
		pref = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
		String type = pref.getString("type", "type");
		;
		return type;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_add_btn:

			String name = edit_add_name.getText().toString();
			if ("".equals(name)) {
				show("姓名为必输入项哦");
			} else {
				SQLiteDBUtil db = new SQLiteDBUtil(getActivity()
						.getApplicationContext());
				SQLiteDatabase sd = db.getWritableDatabase();
				String sql = "insert into rankTable values(null,?,?,?,?,?,?)";
				sd.execSQL(sql, new Object[] { readCurrentType() , name, 1000.0, 0, 0, 0 });
				// 进行关闭
				db.close();
				show("添加成功！");
			}

			break;

		default:
			break;
		}

	}

}
