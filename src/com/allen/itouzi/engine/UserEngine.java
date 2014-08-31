package com.allen.itouzi.engine;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.allen.itouzi.bean.User;
import com.allen.itouzi.bean.UserAccount;
import com.allen.itouzi.bean.UserMonthIn;
import com.allen.itouzi.bean.UserYearIn;
import com.allen.itouzi.bean.UserProperty;
import com.allen.itouzi.bean.UserTrade;
import com.allen.itouzi.net.HttpsUtil;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.ValueUtil;

public class UserEngine {

	private String TAG = "UserEngine";

	private UserEngine() {
	}

	private static UserEngine userEngine = null;
	private ArrayList<UserYearIn> incomeList = null;

	public static UserEngine getInstance() {
		if (userEngine == null) {
			synchronized (ProjectsEngine.class) {
				if (userEngine == null)
					userEngine = new UserEngine();
				return userEngine;
			}
		}
		return userEngine;
	}

	/**
	 * 登录
	 * 
	 * @param loginInfo
	 * @return
	 */
	public User userLogin(String loginInfo) {
		try {
			String result = HttpsUtil.doHttpsPostT(ValueUtil.serverURL
					+ ValueUtil.loginURL, loginInfo, null);
			// TODO:删除所有泄露用户信息的Log
//			Log.i(TAG, "userLogin result:" + result + "  loginInfo:"
//					+ loginInfo);
			JSONObject resultObject = JSON.parseObject(result);
			if (resultObject.getString("code").equals("0")) {
				JSONObject dataObj = resultObject.getJSONObject("data");
				User user = JSON.parseObject(dataObj.toString(), User.class);
				return user;
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			Log.e(TAG, "userLogin 失败!" + e.getLocalizedMessage());
		}
		return null;
	}
	/**
	 * 登录验证
	 * @param passWord
	 * @param token
	 * @return
	 */
		public String verifyPassWord(String passWord, String token) {
			// api/user/verifyPassword post 
			//post 签名 password md5(password=123private_key) +token
			String sign = DataFormat.md5("passWord="+passWord+
					(FileUtil.textFlat + DataFormat.isPhoneNumber("utf-8")));
			try {
				String result = HttpsUtil.doHttpsPostT(ValueUtil.serverURL
						+ ValueUtil.verifyURL,"password="+passWord+"&sign="+sign , token);
				JSONObject resultObject = JSON.parseObject(result);
				//TODO: will store the result?
				return resultObject.getString("code");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	/**
	 * 用户中心首页
	 * 
	 * @param loginInfo
	 * @return
	 */
	public void userIndex(Handler handler, String token) {
		try {
			String result = HttpsUtil.doHttpsGetT(ValueUtil.serverURL
					+ ValueUtil.userURL, token);
//			Log.i(TAG, "userIndex result:" + result);
			JSONObject resultObject = JSON.parseObject(result);
			if (resultObject.getString("code").equals("0")) {
				JSONObject dataObj = resultObject.getJSONObject("data");
				JSONObject accountObject = dataObj.getJSONObject("accountInfo");
				JSONObject staticsObject = dataObj.getJSONObject("statics");
				JSONArray incomeObjects = dataObj
						.getJSONArray("each_month_income");
				// 解析到本地 可用余额 总资产
				UserProperty property = new UserProperty(
						accountObject.getString("use_money"),
						accountObject.getString("no_use_money"),
						staticsObject.getString("no_all_sum"),
						staticsObject.getString("no_all_benjin_sum"),
						dataObj.getString("couponAmount"));
				// 发送资产统计数据
				Message message = handler.obtainMessage();
				message.what = 1;
				message.obj = property;
				handler.sendMessage(message);

				incomeList = new ArrayList<UserYearIn>();
				for (int i = 0; i < incomeObjects.size(); i++) {
					JSONObject incomeObject = incomeObjects.getJSONObject(i);
					String month = incomeObject.getString("month");
					String amount = incomeObject.getString("amount");
					if (amount.equals("")) {
						incomeList.add(new UserYearIn(month, "0"));
					} else {
						incomeList.add(new UserYearIn(month, amount));
					}
				}
				// 发送收益曲线数据
				Message message2 = handler.obtainMessage();
				message2.what = 2;
				message2.obj = incomeList;
				handler.sendMessage(message2);
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// 发送错误信息
			Message message3 = handler.obtainMessage();
			message3.what = 3;
			handler.sendMessage(message3);
			Log.e(TAG, "userIndex 失败!" + e.getLocalizedMessage() + "--token:"
					+ token);
		}
	}

	/**
	 * 获取年收益
	 * 
	 * @param token
	 * @return
	 */
	public ArrayList<UserYearIn> getYearIn(String token) {
		if (incomeList != null) {
			return incomeList;
		} else {
			try {
				String result = HttpsUtil.doHttpsGetT(ValueUtil.serverURL
						+ ValueUtil.userURL, token);
				JSONObject resultObject = JSON.parseObject(result);
				if (resultObject.getString("code").equals("0")) {
					JSONObject dataObj = resultObject.getJSONObject("data");
					JSONArray incomeObjects = dataObj
							.getJSONArray("each_month_income");
					ArrayList<UserYearIn> incomeList = new ArrayList<UserYearIn>();
					for (int i = 0; i < incomeObjects.size(); i++) {
						JSONObject incomeObject = incomeObjects
								.getJSONObject(i);
						String month = incomeObject.getString("month");
						String amount = incomeObject.getString("amount");
						if (amount.equals("")) {
							incomeList.add(new UserYearIn(month, "0"));
						} else {
							incomeList.add(new UserYearIn(month, amount));
						}
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "getIncome 失败!" + e.getLocalizedMessage());
			}
			return null;
		}

	}

	/**
	 * 获取月收益
	 * 
	 * @param token
	 *            time时间戳月份
	 * @return
	 */
	public ArrayList<UserMonthIn> getMonthIn(String time, String token) {
		try {
			String result = "";
			if (time != null) {
				result = HttpsUtil.doHttpsGetT(ValueUtil.serverURL
						+ ValueUtil.monthURL + "?time=" + time, token);
			} else {
				result = HttpsUtil.doHttpsGetT(ValueUtil.serverURL
						+ ValueUtil.monthURL, token);
			}
//			Log.i(TAG, "getMonthIn result:" + result);
			JSONObject resultObject = JSON.parseObject(result);
			if (resultObject.getString("code").equals("0")) {
				JSONObject dataObj = resultObject.getJSONObject("data");
				String year = dataObj.getString("curYear");
				String month = dataObj.getString("curMonth");
				String day = dataObj.getString("curDay");
				JSONObject monthObj = dataObj.getJSONObject("line");
				ArrayList<UserMonthIn> monthList = new ArrayList<UserMonthIn>();
				JSONObject monthObject;
				for (int i = 1; i < monthObj.size() + 1; i++) {

					if (i < 10) {
						monthObject = monthObj.getJSONObject("0" + i);
					} else {
						monthObject = monthObj.getJSONObject(i + "");
					}
					monthList.add(JSON.parseObject(monthObject.toString(),
							UserMonthIn.class));
				}
				monthList.add(new UserMonthIn(year + "-" + month + "-" + day, 100));// 100 对应年月日
				return monthList;
			}
		} catch (Exception e) {
			Log.e(TAG, "getMonthIn 失败!" + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 注册
	 * 
	 * @param number
	 * @return
	 */
	public int sendPhone(String number) {
		try {
			String registerJson = HttpsUtil.doHttpsPost(ValueUtil.serverURL
					+ ValueUtil.registerURL, "phone=" + number);
			JSONObject registerObj = JSON.parseObject(registerJson);
			if (registerObj.getString("code").equals("0")) {
				return 0;
			} else if (registerObj.getString("code").equals("3")) {
				return 3;
			}
		} catch (Exception e) {
			Log.e(TAG, "sendPhone(String number)失败!" + e.getLocalizedMessage());
		}
		return 100;
	}

	/**
	 * 验证码
	 * 
	 * @param number
	 *            手机号
	 * @param check_number
	 *            验证码
	 * @return
	 */
	public User checkPhone(String checkInfo) {

		try {
			String result = HttpsUtil.doHttpsPost(ValueUtil.serverURL
					+ ValueUtil.checkURL, checkInfo);
//			Log.i(TAG, "checkPhone result:" + result);
			JSONObject checkObj = JSON.parseObject(result);
			if (checkObj.getString("code").equals("0")) {
				JSONObject dataObj = checkObj.getJSONObject("data");
				User user = JSON.parseObject(dataObj.toString(), User.class);
				return user;
			}
		} catch (Exception e) {
			Log.e(TAG, "checkPhone 失败!" + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * 设置密码
	 * 
	 * @param passWord2
	 * @param token
	 * @return
	 */
	public boolean passWord(String passWord2, String token) {
		try {
			String result = HttpsUtil.doHttpsPostT(ValueUtil.serverURL
					+ ValueUtil.updateURL, "newpassword=" + passWord2 + "&"
					+ "repassword=" + passWord2, token);
//			Log.i(TAG, "passWord result:" + result);
			// "174506"{"data":[],"code":0,"info":"\u767b\u5f55\u5bc6\u7801\u4fee\u6539\u6210\u529f"}
			JSONObject resultObject = JSON.parseObject(result);
			if (resultObject == null) {
				int start = result.indexOf("{");
				int last = result.lastIndexOf("}");
				String respond = result.substring(start, last);
				resultObject = JSON.parseObject(respond);
			}
			if (resultObject.getString("code").equals("0")) {
				return true;
			}
		} catch (Exception e) {
			Log.e(TAG, "passWord 失败!" + e.getLocalizedMessage());
		}
		return false;
	}

	// debt_type 默认
	public ArrayList<UserTrade> getTrade(String params, String token) {
		ArrayList<UserTrade> tradeList = new ArrayList<UserTrade>();
		try {
			String result = HttpsUtil.doHttpsGetT(ValueUtil.serverURL
					+ ValueUtil.urecordURL + "?" + params, token);
//			Log.i(TAG, "getTrade result:" + result);
			JSONObject resultObject = JSON.parseObject(result);
			if (resultObject.getString("code").equals("0")) {
				JSONObject dataObj = resultObject.getJSONObject("data");
				// JSONObject userInfoObj=dataObj.getJSONObject("userInfo");
				// String string = dataObj.getString("data_count"); 总记录条数
				JSONArray logArray = dataObj.getJSONArray("accountLogInfo");
				for (int i = 0; i < logArray.size(); i++) {
					JSONObject tradeObj = logArray.getJSONObject(i);
					UserTrade trade = new UserTrade(
							tradeObj.getString("type_cn"),
							tradeObj.getString("detail_cn"),
							tradeObj.getString("direction"),
							tradeObj.getString("money"),
							tradeObj.getString("use_money"),
							tradeObj.getString("addtime"));
					tradeList.add(trade);
				}
				return tradeList;
			}
		} catch (Exception e) {
			Log.e(TAG, "getTrade 失败!" + e.getLocalizedMessage());
		}
		return null;
	}

}
