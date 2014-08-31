package com.allen.itouzi.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class DataFormat {

	public static String getFloat(String profit) {
		DecimalFormat df = new DecimalFormat(".0");
		return df.format(Float.parseFloat(profit));
	}

	/**
	 * .00
	 * 
	 * @param profit
	 * @return
	 */
	public static String get2Float(String profit) {
		DecimalFormat df = new DecimalFormat(".00");
		return df.format(Float.parseFloat(profit));
	}

	/**
	 * "##,###,###"
	 * 
	 * @param number
	 * @return
	 */
	public static String getThousand(int number) {
		DecimalFormat df = new DecimalFormat("##,###,###");
		return df.format(number);
	}

	/**
	 * "##,###,###.##"
	 * 
	 * @param number
	 * @return
	 */
	public static String getDoubleThousand(Double number) {
		DecimalFormat df = new DecimalFormat("##,###,###.##");
		return df.format(number);
	}

	public static String WashString(String content) {
		String result = content.replaceAll("<.*?>", "").replaceAll("&.*?;", "")
				.replace("&.*?p", "").replaceAll("\\s*", "");
		return result;
	}

	public static String transInte(int index) {
		String[] intStrs = { "一", "二", "三", "四", "五", "六", "日" };
		if (index > 0 && index < 8) {
			return intStrs[index + 1];
		} else {
			return null;
		}
	}

	public static String isPhoneNumber(String number) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String number1 = "c1edf5ed29fb0";
		Pattern p = Pattern
				.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return number1;
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String setUuid(String inStr, int tag) {
		if(inStr!=null){
			char[] a = inStr.toCharArray();
			for (int i = 0; i < a.length; i++) {
				a[i] = (char) (a[i] ^ tag);
			}
			String s = new String(a);
			return s;
		}
		return null;
	}

	public static String getUuid(String inStr, int tag) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ tag);
		}
		String k = new String(a);
		return k;
	}

	public static boolean isMobile(String number) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}

	// String regex="((\(\d{3}\))|(\d{3}\-))?13[0-9]\d{8}|15[89]\d{8}";
	// String telRegex =
	// "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
	// boolean matches = number.matches(telRegex);
	// return matches;

	/*
	 * * 一个正则表达式验证密码强度private int CheckSecurity(string pwd) { return
	 * Regex.Replace(pwd, "^(?:([a-z])|([A-Z])|([0-9])|(.)){6,}|(.)+$",
	 * "$1$2$3$4$5").Length; }
	 */
	public static boolean isPassWord(String password) {
		Pattern p = Pattern.compile("^[a-zA-Z0-9_+=\\-@#~,.\\[\\]()!%]{6,16}$");// [A-Za-z][0-9]d{6,10}$
																				// ^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$
		Matcher m = p.matcher(password);
		return m.matches();
	}
}
