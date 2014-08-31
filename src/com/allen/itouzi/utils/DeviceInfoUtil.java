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
	 * 设备型号
	 */
	public static String getDeviceModel() {
		return Build.MODEL;
	}

	/*
	 * 设备型号
	 */
	public static String getDevice() {
		return Build.DEVICE;
	}

	public static String getName() {
		return Build.MANUFACTURER;
	}

	/*
	 * OS版本号
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
	 * 获取手机mac地址<br/>
	 * 错误返回12个0
	 */
	public static String getMacAddress(Context context) {
		// 获取mac地址：
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
	 * 设备Id GSM IMEI CDMA MEID
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
	 * 设备的软件版本号：
	 */
	public static String getSoftwareVersion(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getDeviceSoftwareVersion();
	}

	/**
	 * 手机号： GSM手机的 MSISDN.
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
	 * 当前使用的网络类型：
	 * 
	 * @param context
	 * @return 例如： NETWORK_TYPE_UNKNOWN 网络类型未知 0 NETWORK_TYPE_GPRS GPRS网络 1
	 *         NETWORK_TYPE_EDGE EDGE网络 2 NETWORK_TYPE_UMTS UMTS网络 3
	 *         NETWORK_TYPE_HSDPA HSDPA网络 8 NETWORK_TYPE_HSUPA HSUPA网络 9
	 *         NETWORK_TYPE_HSPA HSPA网络 10 NETWORK_TYPE_CDMA CDMA网络,IS95A 或
	 *         IS95B. 4 NETWORK_TYPE_EVDO_0 EVDO网络, revision 0. 5
	 *         NETWORK_TYPE_EVDO_A EVDO网络, revision A. 6 NETWORK_TYPE_1xRTT
	 *         1xRTT网络 7
	 */
	public static int getNetType(Context context) {
		TelephonyManager phoneManager = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		return phoneManager.getNetworkType();
	}

	/**
	 * 唯一的用户ID：IMSI(国际移动用户识别码) for a GSM phone.
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
		// 生成登录签名
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
