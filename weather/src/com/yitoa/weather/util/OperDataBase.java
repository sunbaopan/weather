package com.yitoa.weather.util;

import java.util.ArrayList;
import java.util.List;

import com.yitoa.weather.entity.NineGrid;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/***
 * 操作数据库类
 * 
 * @author Administrator
 * 
 */
public class OperDataBase {

	private DataBaseHelper dataBaseHelper;

	public OperDataBase(Context context) {
		this.dataBaseHelper = new DataBaseHelper(context);
	}

	/**
	 * 将阅读的信息插入到数据库中
	 * 
	 * @param rf
	 */
	public void save(NineGrid ng) {
		Log.d("myDebug", "insertSql" + ng.getCity());
		SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
		database.execSQL("insert into weather(id,city) values (?,?)",
				new Object[] { ng.getId(), ng.getCity() });
		Log.d("myDebug", "添加数据库执行成功");
		database.close();
	}

	/**
	 * 根据文件名查找数据库中是否存在该信息
	 * 
	 * @param filename
	 * @return
	 */
	public NineGrid query(String city) {
		NineGrid ng = null;
		if (city != null) {
			SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
			Cursor cursor = database
					.rawQuery(
							"select id,city from weather where city=? order by id desc",
							new String[] { city });
			if (cursor.moveToFirst()) {
				ng = new NineGrid();
				ng.setCity(cursor.getString(cursor.getColumnIndex("city")));
				ng.setId(cursor.getInt(cursor.getColumnIndex("id")));
			}
			cursor.close();
			database.close();
		}

		return ng;
	}

	public int queryId(int id) {
		int qid = -1;
		SQLiteDatabase database = dataBaseHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery("select id from weather where id=? ",
				new String[] { String.valueOf(id) });
		if (cursor.moveToFirst()) {
			qid = cursor.getInt(cursor.getColumnIndex("id"));
		}
		if (cursor != null) {
			cursor.close();
		}
		database.close();
		return qid;
	}

	/**
	 * 获取保存九方格的所有的信息
	 * 
	 * @param filename
	 * @return
	 */
	public List<NineGrid> queryAll() {
		NineGrid ng = null;
		List<NineGrid> listNine = new ArrayList<NineGrid>();
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = dataBaseHelper.getReadableDatabase();
			cursor = database.rawQuery("select id,city from weather", null);

			while (cursor.moveToNext()) {
				ng = new NineGrid();
				ng.setId(cursor.getInt(cursor.getColumnIndex("id")));
				ng.setCity(cursor.getString(cursor.getColumnIndex("city")));
				listNine.add(ng);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (database != null) {
				database.close();
			}
		}
		return listNine;
	}
	
	
	/****
	 * 查找九宫格中空余的格子
	 * 数据库中的id是从0-8排列的
	 * @return
	 */
	public int nineNotAdd(){
		int count=-1;
		boolean bool=false;
		//查找现存的九宫格里面的信息
		List<NineGrid> list=this.queryAll();
		if(list!=null){
			for(int i=0;i<9;i++){
				for(int k=0;k<list.size();k++){
					NineGrid ng=list.get(k);
					if(i==ng.getId()){//如果id在数据库中存在继续查找
						bool=true;//找到存在的
					}
				}
				if(bool){
					bool=false;//在继续找
				}else{//i没有在九宫格里面找到
					count=i;
					break;
				}
			}
		}else{
			count=0;
		}
		return count;
	}

	/**
	 * 修改数据库信息
	 * 
	 * @param readFile
	 */
	public void update(NineGrid ng) {
		try {
			SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
			database.execSQL("update weather set city=? where id=?",
					new Object[] { ng.getCity(), ng.getId() });
			database.close();
			Log.d("myDebug", "修改数据库中数据成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/****
	 * 删除信息
	 * 
	 * @param id
	 */
	public void del(int id) {
		try {
			SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
			database.execSQL("delete from weather where id=?",
					new Object[] { id });
			database.close();
			Log.d("myDebug", "删除数据成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
