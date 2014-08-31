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
		String[] intStrs = { "һ", "��", "��", "��", "��", "��", "��" };
		if (index > 0 && index < 8) {
			return intStrs[index + 1];
		} else {
			return null;
		}
	}

	public static String isPhoneNumber(String number) {
		/*
		 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
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
		 * �ƶ���134��135��136��137��138��139��150��151��157(TD)��158��159��187��188
		 * ��ͨ��130��131��132��152��155��156��185��186 ���ţ�133��153��180��189����1349��ͨ��
		 * �ܽ��������ǵ�һλ�ض�Ϊ1���ڶ�λ�ض�Ϊ3��5��8������λ�õĿ���Ϊ0-9
		 */
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(number);
		return m.matches();
	}

	// String regex="((\(\d{3}\))|(\d{3}\-))?13[0-9]\d{8}|15[89]\d{8}";
	// String telRegex =
	// "[1][358]\\d{9}";//"[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
	// boolean matches = number.matches(telRegex);
	// return matches;

	/*
	 * * һ��������ʽ��֤����ǿ��private int CheckSecurity(string pwd) { return
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
