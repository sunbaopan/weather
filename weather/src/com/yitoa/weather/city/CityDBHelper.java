package com.yitoa.weather.city;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.yitoa.weather.util.Constants;

public class CityDBHelper extends SQLiteOpenHelper {
	public CityDBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, Constants.DB_NAME, factory, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			String sql="create table city(id integer, hanzi varchar(50), pinyin varchar(50),province varchar(50));";
			db.execSQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}
	
	public List<City> getCities(){
		
		return null;
	}

}
