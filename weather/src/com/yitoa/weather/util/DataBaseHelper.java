package com.yitoa.weather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static int version = 1;

	public DataBaseHelper(Context context) {
		super(context,Constants.DB_NAME, null, version);
	}

	/***
	 * 创建数据库里的表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String sql = "Create table "
					+ Constants.TABLE_WEATHGE
					+ "(id integer,"
					+ "city varchar(50));";
			db.execSQL(sql);
		} catch (Exception e) {
			Log.d("myDebug", "创建reader表出现异常");
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
