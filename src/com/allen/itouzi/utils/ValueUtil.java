package com.allen.itouzi.utils;

import android.content.Context;

public class ValueUtil {
	public static boolean isLogin = false;

	public static String PROXY_IP;

	public static int PROXY_PORT;
	public static final String[] recordTitles = { "全部记录", "充值记录", "提现记录",
			"投资记录", "收益记录", "回本记录" };
	public static final String[] propertyTitles = { "可用余额", "待收收益", "待收本金",
			"冻结资金", "可用投资券", "资产统计" };
	public static final String serverURL = "https://www.itouzi.com";// 手机代理http://192.168.23.123
																	// 8888
	// public static final String serverURL = "http://192.168.23.8";
	public static final String APIVERSION = "1.0";
	public static final String pageURL = "?page=";
	public static final String idURL = "?id=";
	public static final String homeURL = "/api/index/index";
	public static final String projectsIdanbaoURL = "/api/invest/tenders";
	public static final String projectsIrongzuURL = "/api/invest/leases";
	public static final String idanbaoURL = "/api/invest/tender";
	public static final String irongzuURL = "/api/invest/lease";
	public static final String recordURL = "/api/invest/borrowTender";

	public static final String bindURL = "/deviceToken/api/index/index";
	public static final String loginURL = "/api/user/login";
	public static final String privateKey = "c86b3247c1edf5ed29fb0";
	public static final String registerURL = "/api/user/reg";
	public static final String contractRegisterURL = "file:///android_asset/agreement.html";// /itouzi/assets/agreement.html
	public static final String contractPrivacyURL = "file:///android_asset/privacy_policy.html";
	public static final String checkURL = "/api/user/phoneCheck";
	/**
	 * post 签名 password md5(password=123private_key) +token
	 */
	public static final String verifyURL = "/api/user/verifyPassword";
	public static final String investServer = "https://www.itouzi.com";
	// public static final String investServer = "http://old.itouzi.com";
	public static final String investToken = "/dinvest/index/jump?type=ios&token=";
	public static final String investWeb = "/dinvest/invest/confirm?id=";

	public static final String updateURL = "/api/user/updatepwd";
	public static final String userURL = "/api/user/index";
	public static final String accountURL = "/api/user/account";
	public static final String monthURL = "/api/user/getLineDataByMonth";// 月收益
																			// time
	public static final String readedURL = "/api/user/setMessage";// message_id：
																	// get
	public static final String urecordURL = "/api/user/trade";
	public static final String uinvestURL = "/api/user/invest";
	public static final String udetailURL = "/api/user/tenderInfo";
	public static final int SECONDS = 60;
	public static final int MINUTES = 3600;

}
