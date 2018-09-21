package utils;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDBUtil extends SQLiteOpenHelper {

	public static final String NAME = "easyrank";
	public static final int VERSION = 1;//��Ϊ����һ�����ݿ⣬���Ը��°汾��Ϊ2

	/**
	 * content�����Ķ��� name���ݿ����� factory���ݿ⹤�� version�汾
	 * 
	 * @param context
	 */
	public SQLiteDBUtil(Context context) {
		super(context, NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String userSQL = "create table rankTable"
				+ "(id integer primary key autoincrement,"
				+ "type varchar(20),"
				+ "name varchar(20)," 
				+ "score float," 
				+ "vcount int," 
				+ "dcount int," 
				+ "allcount int)";
		db.execSQL(userSQL);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		

	}

}
