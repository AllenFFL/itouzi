package com.allen.itouzi.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
/**
 *table0 homedata table1 idanbaoproject table2 irongzuproject
 * @author zeng
 *
 */
public class IDBHelper extends SQLiteOpenHelper {
	final String CREATE_TABLE_IDANBAO="CREATE TABLE idanbao"
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "page INTEGER,idanbaosJson VARCHAR(20))";
	
	final String CREATE_TABLE_IRONGZU="CREATE TABLE irongzu"
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"page INTEGER,irongzusJson VARCHAR(20))";
	
	final String CREATE_TABLE_HOME="CREATE TABLE home"
			+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
			"homeJson VARCHAR(20))";
	final static String TABLE_NAME="itouzi.db";
	private static IDBHelper mInstance=null;
	private IDBHelper(Context context) {
		super(context, TABLE_NAME, null, 1);
	}
	public static synchronized IDBHelper getInstance(Context context) { 
		if(mInstance==null){
			mInstance=new IDBHelper(context);
		}
		return mInstance;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_IDANBAO);
		db.execSQL(CREATE_TABLE_IRONGZU);
		db.execSQL(CREATE_TABLE_HOME);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	/**
	 * É¾³ýÊý¾Ý¿â
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context){
		return context.deleteDatabase(TABLE_NAME);
	}

}
