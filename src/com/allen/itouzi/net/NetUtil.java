package com.allen.itouzi.net;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.allen.itouzi.utils.ValueUtil;

/**
 * ����ֻ��˵�����״̬
 * 
 * @author Administrator
 * 
 */
public class NetUtil {
	private static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");// 4.0ģ�������ε���Ȩ��

	public static boolean checkNetState(Context context){
		boolean flag=false;
		ConnectivityManager manager = (ConnectivityManager)context.getSystemService
				(Context.CONNECTIVITY_SERVICE);
		if(manager.getActiveNetworkInfo()!=null){
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		return flag;
	}
	/**
	 * �ж�����״̬
	 * 
	 * @return
	 */
	public static boolean checkNet(Context context) {
		// WIFI��������
		boolean isWIFI = isWIFIConnectivity(context);
		// Mobile(APN�б�)����
		boolean isMobile = isMobileConnectivity(context);

		if (isWIFI == false && isMobile == false) {
			// û������
			return false;
		}

		if (isMobile) {
			// �Ƿ���wap��ʽ,�������ip�Ͷ˿�д����010.000.000.172
			readAPN(context);
		}

		return true;

	}

	/**
	 * ��ȡ�����ip�Ͷ˿�
	 * 
	 * @param context
	 */
	private static void readAPN(Context context) {
		ContentResolver contentResolver = context.getContentResolver();
		Cursor query = contentResolver.query(PREFERRED_APN_URI, null, null,
				null, null);
		if (query != null && query.moveToFirst()) {
			ValueUtil.PROXY_IP = query.getString(query.getColumnIndex("proxy"));
			ValueUtil.PROXY_PORT = query.getInt(query.getColumnIndex("port"));
		}

	}

	/**
	 * Mobile(APN�б�)����
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isMobileConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}

	/**
	 * WIFI�Ƿ�������
	 * 
	 * @param context
	 * @return
	 */
	private static boolean isWIFIConnectivity(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (networkInfo != null) {
			return networkInfo.isConnected();
		}
		return false;
	}
}
