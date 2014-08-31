package com.allen.itouzi.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class DeviceInfoUtil {
	/*
	 * �豸�ͺ�
	 */
	public static String getDeviceModel() {
		return Build.MODEL;
	}

	/*
	 * �豸�ͺ�
	 */
	public static String getDevice() {
		return Build.DEVICE;
	}

	public static String getName() {
		return Build.MANUFACTURER;
	}

	/*
	 * OS�汾��
	 */
	public static String getDeviceVersion() {
		return Build.VERSION.RELEASE;
	}
	public static DisplayMetrics getMetrics(Activity context) {
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm;
	}
	/**
	 * ��ȡ�ֻ�mac��ַ<br/>
	 * ���󷵻�12��0
	 */
	public static String getMacAddress(Context context) {
		// ��ȡmac��ַ��
		String macAddress = "000000000000";
		try {
			WifiManager wifiMgr = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = (null == wifiMgr ? null : wifiMgr
					.getConnectionInfo());
			if (null != info) {
				if (!TextUtils.isEmpty(info.getMacAddress()))
					macAddress = info.getMacAddress().replace(":", "");
				else
					return macAddress;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return macAddress;
		}
		return macAddress;
	}

	/**
	 * �豸Id GSM IMEI CDMA MEID
	 * 
	 * @param context
	 * @return
	 */
	public static String getDeviceId(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getDeviceId();
	}

	public static int getAppVersionCode(Context context) {
		PackageManager pm = context.getPackageManager();
		int versionCode = 0;
		try {
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}

	public static String getAppVersionName(Context context) {
		PackageManager pm = context.getPackageManager();
		String versionName = "";
		try {
			PackageInfo packageInfo = pm.getPackageInfo(
					context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/*
	 * �豸������汾�ţ�
	 */
	public static String getSoftwareVersion(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getDeviceSoftwareVersion();
	}

	/**
	 * �ֻ��ţ� GSM�ֻ��� MSISDN.
	 * 
	 * @param context
	 * @return
	 */
	public static String getNumber(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getLine1Number();
	}

	/**
	 * ��ǰʹ�õ��������ͣ�
	 * 
	 * @param context
	 * @return ���磺 NETWORK_TYPE_UNKNOWN ��������δ֪ 0 NETWORK_TYPE_GPRS GPRS���� 1
	 *         NETWORK_TYPE_EDGE EDGE���� 2 NETWORK_TYPE_UMTS UMTS���� 3
	 *         NETWORK_TYPE_HSDPA HSDPA���� 8 NETWORK_TYPE_HSUPA HSUPA���� 9
	 *         NETWORK_TYPE_HSPA HSPA���� 10 NETWORK_TYPE_CDMA CDMA����,IS95A ��
	 *         IS95B. 4 NETWORK_TYPE_EVDO_0 EVDO����, revision 0. 5
	 *         NETWORK_TYPE_EVDO_A EVDO����, revision A. 6 NETWORK_TYPE_1xRTT
	 *         1xRTT���� 7
	 */
	public static int getNetType(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getNetworkType();
	}

	/**
	 * Ψһ���û�ID��IMSI(�����ƶ��û�ʶ����) for a GSM phone.
	 * 
	 * @param context
	 * @return
	 */
	public static String getSubscriberId(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getSubscriberId();
	}
	public static String getLoginInfo(Context context,String userName3,String passWord3) {
		// ���ɵ�¼ǩ��
		String uuid =getDeviceId(context);
		String model = getDeviceModel();
		String deviceName = "android";
		String system = getDeviceVersion();
		StringBuilder loginKey1 = new StringBuilder();
		loginKey1.append("deviceName=").append(deviceName)
				.append("&model=").append(model).append("&password=")
				.append(passWord3).append("&system=").append(system)
				.append("&username=").append(userName3).append("&uuid=")
				.append(uuid);
		String deviceInfo = loginKey1.toString();
	
		StringBuilder loginKey2 = new StringBuilder();
		try {
			loginKey2.append("deviceName=")
					.append(URLEncoder.encode(deviceName, "utf-8"))
					.append("&model=")
					.append(URLEncoder.encode(model, "utf-8"))
					.append("&password=")
					.append(URLEncoder.encode(passWord3, "utf-8"))
					.append("&system=")
					.append(URLEncoder.encode(system, "utf-8"))
					.append("&username=")
					.append(URLEncoder.encode(userName3, "utf-8"))
					.append("&uuid=")
					.append(URLEncoder.encode(uuid, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String loginInfos = deviceInfo
				+ (FileUtil.textFlat + DataFormat.isPhoneNumber("utf-8"));
		String sign = DataFormat.md5(loginInfos);
	
		return loginKey2.append("&sign=").append(sign).toString();
	}

	public static String getDeviceInfo(Context context) {
	    try{
	      org.json.JSONObject json = new org.json.JSONObject();
	      android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
	          .getSystemService(Context.TELEPHONY_SERVICE);
	  
	      String device_id = tm.getDeviceId();
	      
	      android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	          
	      String mac = wifi.getConnectionInfo().getMacAddress();
	      json.put("mac", mac);
	      
	      if( TextUtils.isEmpty(device_id) ){
	        device_id = mac;
	      }
	      
	      if( TextUtils.isEmpty(device_id) ){
	        device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);
	      }
	      
	      json.put("device_id", device_id);
	      
	      return json.toString();
	    }catch(Exception e){
	      e.printStackTrace();
	    }
	  return null;
	}
                  
}
