package com.moon.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	private final static String DBNAME = "db_account.db";
	private final static int VERSION = 1; 
	private SQLiteDatabase db;
	//构造函数,生成数据库
	public MySQLiteOpenHelper(Context context) {
		super(context,DBNAME,null,VERSION);
		db = getReadableDatabase();//生成数据库的同时,返回一个SQLiteDatabase对象
	}
	//当数据库被首次创建时执行该方法，一般将创建表等初始化操作在该方法中执行
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS tb_income(_id INTEGER PRIMARY KEY,money,type,date,time,comments)");
		db.execSQL("CREATE TABLE IF NOT EXISTS tb_expand(_id INTEGER PRIMARY KEY,money,type,detail,date,time,comments)");
	}
	//当打开数据库时传入的版本号与当前的版本号不同时会调用该方法
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS tb_collections");
			onCreate(db);
		}
	}

	public Cursor selectCursor(String sql,String[] selectionArgs){
		return db.rawQuery(sql, selectionArgs);
	}
	
	public List<Map<String, Object>> selectList(String sql,String[] selectionArgs){
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		return cursorToList(cursor);
	}
	
	public int selectCount(String sql,String[] selectionArgs){
		Cursor cursor = db.rawQuery(sql, selectionArgs);
		if (cursor == null) {
			return 0;
		}
		int count = cursor.getCount();
		if (cursor != null) {
			cursor.close();
		}
		return count;
	}
	
	public boolean execData(String sql, Object[] bindArgs){
		try {
			db.execSQL(sql, bindArgs);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<Map<String, Object>> cursorToList(Cursor cursor){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		String[] arrColumnNames = cursor.getColumnNames();
		while(cursor.moveToNext()){
			Map<String,Object> map = new HashMap<String,Object>();
			for (int i = 0; i < arrColumnNames.length; i++) {
				map.put(arrColumnNames[i], cursor.getString(i));
			}
			list.add(map);
		}
		if (cursor != null) {
			cursor.close();
		}
		return list;
	}
	
}
