package com.allen.itouzi.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.allen.itouzi.activity.RegisterActivity;
import com.allen.itouzi.bean.User;
import com.allen.itouzi.view.LockPatternView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
	/**
	 * 手机SD卡根目录
	 */
	private static String sdPath = Environment.getExternalStorageDirectory()
			.getPath();
	/**
	 * 手机缓存目录
	 */
	private static String cachePath = null;
	/**
	 * 图片存储文件名
	 */
	private final static String folder = "/ItouziImage";
	public final static String textFlat = "c86bae7";

	/**
	 * 获取储存Image的目录
	 * 
	 * @return
	 */
	public static String getStorageDir() {
		return (Environment.getExternalStorageState()
				.equals(Environment.MEDIA_MOUNTED)) ? sdPath + folder
				: cachePath + folder;
	}

	/**
	 * 存储图片Bitmap方法
	 * 
	 * @param fileName
	 * @param bitmap
	 */
	public static void saveBitmap(Context context,String fileName, Bitmap bitmap) {
		cachePath = context.getCacheDir().getPath();
		if (bitmap == null)
			return;
		String path = getStorageDir();
		File folderFile = new File(path);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		File file = new File(path + File.separator + fileName);
		// file.createNewFile();
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取图片Bitmap
	 * 
	 * @param fileName
	 * @return
	 */
	public static Bitmap getBitmap(Context context,String fileName) {
		cachePath = context.getCacheDir().getPath();
		return BitmapFactory.decodeFile(getStorageDir() + File.separator
				+ fileName);
	}

	public static boolean isFileExits(Context context,String fileName) {
		cachePath = context.getCacheDir().getPath();
		return new File(getStorageDir() + File.separator + fileName).exists();
	}

	public long getFileSize(String fileName) {
		return new File(getStorageDir() + File.separator + fileName).length();
	}

	/**
	 * 删除SD卡或者手机的缓存图片和目录
	 * 
	 * @return
	 */
	public boolean deleteFile() {
		File dirFile = new File(getStorageDir());
		if (!dirFile.exists()) {
			return false;
		}
		if (dirFile.isDirectory()) {
			String[] list = dirFile.list();
			for (int i = 0; i < list.length; i++) {
				new File(dirFile, list[i]).delete();
			}
		}
		dirFile.delete();
		return true;
	}

	public static void homeTag(Context context, String userName,
			String passWord, User userInfo) {
		int homeGat = (userName.length() + 5) << (passWord.length() >> 2);
		String homeNenkot = DataFormat.setUuid(userInfo.getToken(), homeGat);
		String homeDi = DataFormat.setUuid(userInfo.getUser_id(), homeGat);
		String homeEman = DataFormat.setUuid(userInfo.getUsername(), homeGat);
		SharedPreferences logInfo = context.getSharedPreferences("itouziFFL",
				Activity.MODE_PRIVATE);
		Editor editor = logInfo.edit();
		editor.putInt("homeGat", homeGat);
		editor.putString("homeNenkot", homeNenkot);
		editor.putString("homeDi", homeDi);
		editor.putString("homeEman", homeEman);
		editor.commit();
	}
	public static boolean isFirst(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"itouziSplash", Activity.MODE_PRIVATE);
		return preferences.getBoolean("isFirst", true);
	}
	public static void saveFirst(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"itouziSplash", Activity.MODE_PRIVATE);
		preferences.edit().putBoolean("isFirst", false).commit();
	}
	public static void saveLockPatter(Context context, String lock_patter) {
		SharedPreferences preferences = context.getSharedPreferences(
				"itouziFFL", Activity.MODE_PRIVATE);
		int homeGat = preferences.getInt("homeGat", -1);
		String lockPatter = DataFormat.setUuid(lock_patter, homeGat);
		preferences.edit().putString("lock_key", lockPatter).commit();
	}

	public static String getLockPatter(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"itouziFFL", Activity.MODE_PRIVATE);
		int homeGat = preferences.getInt("homeGat", -1);
		String lock_key = preferences.getString("lock_key", null);
		if (lock_key == null) {
			return null;
		} else {
			String lockPatter = DataFormat.getUuid(lock_key, homeGat);
			return lockPatter;
		}
	}
	public static void clearLockPatter(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"itouziFFL", Activity.MODE_PRIVATE);
		int homeGat = preferences.getInt("homeGat", -1);
		preferences.edit().putString("lock_key", null).commit();
	}
	public static boolean getLockHint(Context context) {
		return context.getSharedPreferences("itouziFFL", Activity.MODE_PRIVATE)
				.getBoolean("isGestrueLock", false);
	}

	public static void saveLockHint(Context context, boolean isLock) {
		Editor edit = context.getSharedPreferences("itouziFFL",
				Activity.MODE_PRIVATE).edit();
		edit.putBoolean("isGestrueLock", isLock);
		edit.commit();
	}

	public static void clearToken(Context context) {
		context.getSharedPreferences("itouziFFL", Activity.MODE_PRIVATE).edit()
				.clear().commit();
	}

	public static String getName(Context context) {
		SharedPreferences itouziFFL = context.getSharedPreferences("itouziFFL",
				Activity.MODE_PRIVATE);
		String homeEman = itouziFFL.getString("homeEman", null);
		int homeGat = itouziFFL.getInt("homeGat", -1);
		if (homeEman == null || homeGat == -1) {
			return "";
		} else {
			return DataFormat.getUuid(homeEman, homeGat);
		}
	}

	public static String getToken(Context context) {
		SharedPreferences itouziFFL = context.getSharedPreferences("itouziFFL",
				Activity.MODE_PRIVATE);
		String homeNenkot = itouziFFL.getString("homeNenkot", null);
		int homeGat = itouziFFL.getInt("homeGat", -1);
		if (homeNenkot == null || homeGat == -1) {
			return null;
		} else {
			return DataFormat.getUuid(homeNenkot, homeGat);
		}
	}
}
