package com.allen.itouzi.db;

import java.io.File;
import java.util.ArrayList;

import com.allen.itouzi.bean.IdanbaoMain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ItouziDAO {
	private Context context;
	private IDBHelper idbHelper;
	private SQLiteDatabase itouziDB;
	public ItouziDAO(Context context) {
		this.context=context;
		idbHelper = IDBHelper.getInstance(context);
	}
	public void deleteDB(){
		idbHelper.deleteDatabase(context);
	}
	public void inIdanbaos(int page,String idanbaosJson){
		itouziDB = idbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("page", page);
		values.put("idanbaosJson", idanbaosJson);
		itouziDB.delete("idanbao", "page=?", new String[]{page+""});
		itouziDB.insert("idanbao", null, values);
		itouziDB.close();
	}
	public String quIdanbaos(int page){
		itouziDB = idbHelper.getWritableDatabase();
//		itouziDB.rawQuery("select * from idanbao page=? ", new String[]{page+""});
		Cursor cursor = itouziDB.query("idanbao", new String[]{"idanbaosJson"},
				"page = ?", new String[]{""+page}, null, null, null);
		if(null!=cursor&&cursor.moveToFirst()){
			String idanbaosJson = cursor.getString(0);
			return idanbaosJson;
		}
		cursor.close();
		itouziDB.close();
		return null;
	}
	public void inIrongzu(int page,String irongzusJson){
		itouziDB = idbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("page", page);
		values.put("irongzusJson", irongzusJson);
		itouziDB.delete("irongzu", "page=?", new String[]{page+""});
		itouziDB.insert("irongzu", null, values);
		itouziDB.close();
	}
	public String quIrongzu(int page){
		itouziDB = idbHelper.getWritableDatabase();
		Cursor cursor = itouziDB.query("irongzu", new String[]{"irongzusJson"},
				"page = ?", new String[]{""+page}, null, null, null);
		if(null!=cursor&&cursor.moveToFirst()){
			String irongzusJson = cursor.getString(0);
			return irongzusJson;
		}
		cursor.close();
		itouziDB.close();
		return null;
	}
	public void inHome(String homeJson){
		itouziDB = idbHelper.getWritableDatabase();
		ContentValues values=new ContentValues();
		values.put("homeJson", homeJson);
//		itouziDB.delete("home", "page=?", new String[]{"1"});
		itouziDB.insert("home", null, values);
		itouziDB.close();
	}
	public String quHome(){
		itouziDB = idbHelper.getWritableDatabase();
		Cursor cursor = itouziDB.query("home", new String[]{"homeJson"},
				null, null, null, null, null);
		if(null!=cursor&&cursor.moveToFirst()){
			String homeJson = cursor.getString(0);
			return homeJson;
		}
		cursor.close();
		itouziDB.close();
		return null;
	}
}
