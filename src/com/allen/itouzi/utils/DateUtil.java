package com.allen.itouzi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat sf = null;

	/* 获取系统时间 格式为："yyyy/MM/dd " */
	public static String getCurrentDate() {
		Date d = new Date();
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}

	/**
	 * 年月日
	 * 
	 * @param time
	 * @return
	 */
	public static String getStringDate(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}

	/* 时间戳转换成字符窜 */
	public static String getDateToString(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy-MM-dd");
		return sf.format(d);
	}

	/* 时间戳转换成字符窜 */
	public static String getDateToStrings(long time) {// 1404875126
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(d);
	}

	/**
	 * 增加一个月
	 * @param s
	 * @param n
	 * @return
	 */
	public static String addMonth(String s, int n) {
        try {   
                 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
  
                 Calendar cd = Calendar.getInstance();   
                 cd.setTime(sdf.parse(s));
//                 cd.add(Calendar.DATE, n);//增加一天 
                 cd.add(Calendar.MONTH, n);//增加一个月   
  
                 return sdf.format(cd.getTime());   
       
             } catch (Exception e) {   
                 return null;   
             }
     }
	/**
	 * 减少一个月
	 * @param s
	 * @param n
	 * @return
	 */
	public static String minMonth(String s, int n) {
        try {
              SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  
                 Calendar cd = Calendar.getInstance();   
                 cd.setTime(sdf.parse(s));
//                 cd.add(Calendar.DATE, n);//增加一天 
                 cd.add(Calendar.MONTH, -n);//增加一个月   
                 return sdf.format(cd.getTime());   
       
             } catch (Exception e) {   
                 return null;   
             }
     }  
	/* 时间戳转换成字符窜 */
	public static String getDateToStringss(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("HH:mm:ss");
		return sf.format(d);
	}

	/* 小时 分钟 转换 */
	public static String getDateToHHMM(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sf.format(d);
	}

	/* 将字符串转为时间戳 */
	public static long getStringToDate(String time) {
		sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sf.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date.getTime();
	}

	/* 计算期限 两个时间戳相减 */
	public static String getTimeInterval(String time1, String time2) {
		long time3 = Long.parseLong(time2) - Long.parseLong(time1);
		long mount = time3 / 2592000l;
		return (int) mount + "";
	}
}
