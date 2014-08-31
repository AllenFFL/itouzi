package com.allen.itouzi.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.R.color;
import com.allen.itouzi.bean.User;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.utils.ValueUtil;
import com.umeng.analytics.MobclickAgent;

public class RegisterActivity extends Activity implements OnClickListener {
	private static final String TAG = "RegisterActivity";
	private TextView itouzi_contract;
	private EditText phone_numberET;
	private ImageView register_back;
	private ImageView register_agree;
	private boolean agreeFlag = true;
	private boolean checkFlag = false;
	private boolean passWord = false;
	private boolean registFlag = false;
	private boolean loginFlag = false;
	private Button register_button;
	private String number = null;
	private ExecutorService phoneTheads;
	private TextView phone_number_tv;
	private TextView phone_checkhint;
	private String check_number;
	private ExecutorService checkThreads;
	private TextView phone_passwordTV;
	private EditText phone_passwordET;
	private View register_line3;
	private LinearLayout phone_number_agree;
	private Animation shake;
	private ExecutorService passThreads;
	private User userInfo;
	private TextView login_title;
	private TextView registe_title;
	private ExecutorService loginThreads;
	private String loginInfo;
	private String userName3;
	private String passWord3;
	private String checkInfo;
	private int phoneRespond;
	private final static int SEND_PHONE=1;
	/**
	 * 1 在登录后转跳 userfragment -1 不需要
	 */
	private int extra;
	Handler reHandler=new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SEND_PHONE:
				if(msg.obj!=null){
					phoneRespond=(Integer)msg.obj;
				setCheckView();
				}
				break;

			default:
				break;
			}
		};
	};

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		extra=getIntent().getIntExtra("main", -1);
		Log.i(TAG, "extra:"+extra);
		setContentView(R.layout.user_register);
		initView();
		// 显示软键盘
		WindowManager.LayoutParams params = getWindow().getAttributes();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		// 隐藏WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
		params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		shake = AnimationUtils.loadAnimation(this, R.drawable.shake);
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}
	protected void initView() {
		registe_title = (TextView) findViewById(R.id.registe_title);
		login_title = (TextView) findViewById(R.id.login_title);
		phone_number_tv = (TextView) findViewById(R.id.phone_number_tv);
		phone_numberET = (EditText) findViewById(R.id.phone_number);
		register_back = (ImageView) findViewById(R.id.register_back);
		register_button = (Button) findViewById(R.id.register_button);
		register_agree = (ImageView) findViewById(R.id.register_agree);
		itouzi_contract = (TextView) findViewById(R.id.itouzi_contract);
		phone_checkhint = (TextView) findViewById(R.id.phone_checkhint);
		phone_passwordTV = (TextView) findViewById(R.id.phone_passwordTV);
		phone_passwordET = (EditText) findViewById(R.id.phone_passwordET);
		phone_passwordET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		phone_number_agree = (LinearLayout) findViewById(R.id.phone_number_agree);
		register_line3 = findViewById(R.id.register_line3);
		login_title.setOnClickListener(this);
		register_back.setOnClickListener(this);
		register_button.setOnClickListener(this);
		// TODO:判断是要登陆还是注册 根据本地存储的信息 默认登录
		setLogin();
		// setRegister();
	}

	private void setRegister() {
		registFlag = true;
		loginFlag = false;
		registe_title.setText("注册");
		login_title.setText("登陆");
		register_button.setText("下一步");

		phone_number_tv.setText("手机号码");
		phone_numberET.setText("");
		phone_numberET.setHint("填写您的手机号");
		phone_passwordTV.setVisibility(View.GONE);
		phone_passwordET.setVisibility(View.GONE);
		register_line3.setVisibility(View.GONE);
		phone_number_agree.setVisibility(View.VISIBLE);
		SpannableString contract = new SpannableString("同意《用户注册协议》和《隐私条款》");
		contract.setSpan(new URLSpan(ValueUtil.contractRegisterURL), 3, 9,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		contract.setSpan(new URLSpan(ValueUtil.contractPrivacyURL), 12, 17,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		itouzi_contract.setText(contract);
		itouzi_contract.setMovementMethod(new LinkMovementMethod());
		SpannableString content = (SpannableString) itouzi_contract.getText();
		URLSpan[] urlSpans = content.getSpans(0, content.length(),
				URLSpan.class);
		SpannableStringBuilder style = new SpannableStringBuilder(content);
		style.clearSpans();
		for (URLSpan urlSpan : urlSpans) {
			MyURLSpan myURLSpan = new MyURLSpan(urlSpan.getURL());
			style.setSpan(myURLSpan, content.getSpanStart(urlSpan),
					content.getSpanEnd(urlSpan),
					Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		itouzi_contract.setText(style);

		register_agree.setOnClickListener(this);
	}

	private class MyURLSpan extends ClickableSpan {
		private String mUrl;

		MyURLSpan(String url) {
			mUrl = url;
		}

		@Override
		public void onClick(View widget) {
			Intent intent = new Intent(RegisterActivity.this,
					HomeWebActivity.class);
			intent.putExtra("registerURL", mUrl);
			RegisterActivity.this.startActivity(intent);
			// if(mUrl.equals(ValueUtil.contractRegisterURL)){
			//
			// }
		}
	}

	private void sendPhone() {
			number = phone_numberET.getText().toString().trim();
		if (TextUtils.isEmpty(number)) {
			phone_numberET.setText("");
			phone_numberET.setHint("号码不能为空,请输入");
			phone_numberET.startAnimation(shake);
		} else if (!DataFormat.isMobile(number)) {
			phone_numberET.setText("");
			phone_numberET.setHint("号码格式有误，请重新输入");
			phone_numberET.startAnimation(shake);
		} else {
			phoneThread();
		}
	}

	private void phoneThread() {
		if(phoneTheads==null)
		phoneTheads = Executors.newFixedThreadPool(2);
		phoneTheads.execute(new Runnable() {

			@Override
			public void run() {
				phoneRespond = UserEngine.getInstance().sendPhone(number);
				Message msg = reHandler.obtainMessage();
				msg.what=SEND_PHONE;
				msg.obj=phoneRespond;
				reHandler.sendMessage(msg);
			}
		});
	}
	protected void setCheckView() {
		if (phoneRespond == 3) {// 已注册
			phone_numberET.setText("");
			phone_numberET.setHint("号码已注册，请登陆");
			phone_numberET.startAnimation(shake);
		} else if (phoneRespond == 100) {// 失败
			phone_numberET.setText("");
			phone_numberET.setHint("注册未成功，请重新输入");
			phone_numberET.startAnimation(shake);
		} else if (phoneRespond == 0) {
			// 输入验证码
			registe_title.setText("填写验证码");
			phone_checkhint.setText("已将验证码发送到：" + number);
			phone_checkhint.setVisibility(View.VISIBLE);
			phone_number_tv.setText("验证码");
			phone_numberET.setText("");
			phone_numberET.setHint("输入验证码");
			agreeFlag = false;
			checkFlag = true;
			register_agree.setVisibility(View.GONE);
			itouzi_contract.setTextSize(17);
			itouzi_contract.setTextColor(getResources()
					.getColor(R.color.i_gray_text));
			// 倒计时60s
			new CountDownTimer(1000 * 60, 1000) {

				@Override
				public void onTick(long millisUntilFinished) {
					SpannableString contract2;
					if ((int) millisUntilFinished / 1000 < 10) {
						contract2 = new SpannableString(
								"接收短信大约需要0"
										+ millisUntilFinished
										/ 1000 + "秒");
						contract2
								.setSpan(
										new ForegroundColorSpan(
												getResources()
														.getColor(
																R.color.i_blue)),
										8,
										10,
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else {
						contract2 = new SpannableString(
								"接收短信大约需要"
										+ millisUntilFinished
										/ 1000 + "秒");
						contract2
								.setSpan(
										new ForegroundColorSpan(
												getResources()
														.getColor(
																R.color.i_blue)),
										8,
										10,
										Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					itouzi_contract.setText(contract2);
				}

				@Override
				public void onFinish() {
					itouzi_contract
							.setTextColor(getResources()
									.getColor(
											R.color.i_blue));
					itouzi_contract.setTextSize(17);
					itouzi_contract.setText("点击重新发送验证码");
					itouzi_contract
							.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									phoneThread();
								}
							});
				}
			}.start();
		}
		
	}

	private void checkPhone() {
		check_number = phone_numberET.getText().toString().trim();
		if (TextUtils.isEmpty(check_number)) {
			phone_numberET.setHint("验证码不能为空,请输入");
			phone_numberET.startAnimation(shake);
		} else {
			//获取设备信息
			String uuid = DeviceInfoUtil.getDeviceId(this);
			String model = DeviceInfoUtil.getDeviceModel();
			String deviceName = "android";
			String system = DeviceInfoUtil.getDeviceVersion();
			
			//生成url 信息字段
			StringBuilder urlKey1 = new StringBuilder();
			try {
				urlKey1.append("deviceName=")
						.append(URLEncoder.encode(deviceName, "utf-8"))
						.append("&model=")
						.append(URLEncoder.encode(model, "utf-8"))
						.append("&phone=")
						.append(URLEncoder.encode(number, "utf-8"))
						.append("&system=")
						.append(URLEncoder.encode(system, "utf-8"))
						.append("&uuid=")
						.append(URLEncoder.encode(uuid, "utf-8"))
						.append("&valicode=")
						.append(URLEncoder.encode(check_number, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 生成签名sign
			StringBuilder urlKey2 = new StringBuilder();
			urlKey2.append("deviceName=").append(deviceName)
					.append("&model=").append(model).append("&phone=")
					.append(number).append("&system=").append(system)
					.append("&uuid=").append(uuid).append("&valicode=")
					.append(check_number);
			String deviceInfo = urlKey2.toString();
			String loginInfos = deviceInfo
					+ (FileUtil.textFlat + DataFormat.isPhoneNumber("utf-8"));
			String sign = DataFormat.md5(loginInfos);
			
			checkInfo = urlKey1.append("&sign=").append(sign).toString();

			// 发送验证签名
			checkThreads = Executors.newFixedThreadPool(2);
			checkThreads.execute(new Runnable() {
				@Override
				public void run() {
					userInfo = UserEngine.getInstance().checkPhone(checkInfo);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (userInfo == null) {
								phone_numberET.setText("");
								phone_numberET.setHint("验证未通过,请重新输入");
								phone_numberET.startAnimation(shake);
							} else {
								FileUtil.homeTag(getApplicationContext(),
										number, check_number, userInfo);
								setPassWord();
							}
						}
					});
				}
			});
		}
	}

	/**
	 * 设置密码
	 */
	private void setPassWord() {
		checkFlag = false;
		passWord = true;
		registe_title.setText("设置登陆密码");
		register_button.setText("完成注册");
		phone_number_tv.setText("设置密码");
		phone_numberET.setText("");
		phone_numberET.setHint("6-16位英文字母、数字或符号");//
		phone_numberET.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);
		phone_checkhint.setVisibility(View.INVISIBLE);
		phone_passwordTV.setText("确认密码");
		phone_passwordET.setHint("6-16位英文字母、数字或符号");//
		phone_passwordTV.setVisibility(View.VISIBLE);
		phone_passwordET.setVisibility(View.VISIBLE);
		register_line3.setVisibility(View.VISIBLE);
		phone_number_agree.setVisibility(View.GONE);
	}

	/**
	 * 发送密码
	 */
	private void sendPassWord() {
		// 校验 是否为空
		String passWord1 = phone_numberET.getText().toString();
		final String passWord2 = phone_passwordET.getText().toString();
		if (TextUtils.isEmpty(passWord1) || TextUtils.isEmpty(passWord2)) {
			if (TextUtils.isEmpty(passWord1)) {
				phone_numberET.setHint("6-16位英文字母、数字或符号");
				phone_numberET.startAnimation(shake);
			}
			if (TextUtils.isEmpty(passWord2)) {
				phone_passwordET.setHint("密码不能为空，请输入");
				phone_passwordET.startAnimation(shake);
			}
		} else {
			// 校验 是否一致
			if (!passWord1.equals(passWord2)) {
				phone_numberET.setText("");
				phone_numberET.setHint("6-16位英文字母、数字或符号");
				phone_passwordET.setText("");
				phone_passwordET.setHint("两次输入不一致，请重新输入");
				phone_passwordET.startAnimation(shake);
			} else {
				// 是否符合要求
				if (!DataFormat.isPassWord(passWord2)) {
					phone_numberET.setText("");
					phone_numberET.setHint("6-16位英文字母、数字或符号");
					phone_passwordET.setText("");
					phone_passwordET.setHint("密码不符合要求，请重新输入");
					phone_passwordET.startAnimation(shake);
				} else {
					SharedPreferences itouziFFL = getSharedPreferences(
							"itouziFFL", Activity.MODE_PRIVATE);
					String homeNenkot = itouziFFL.getString("homeNenkot", null);
					String homeEman = itouziFFL.getString("homeEman", null);
					int homeGat = itouziFFL.getInt("homeGat", -1);
					if (homeNenkot == null || homeGat == -1) {
						setRegister();
					} else {
						final String token = DataFormat.getUuid(homeNenkot,
								homeGat);
						passThreads = Executors.newFixedThreadPool(2);
						passThreads.execute(new Runnable() {

							private boolean isRegist;

							@Override
							public void run() {
								isRegist = UserEngine.getInstance().passWord(
										passWord2, token);
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										if (isRegist) {
											//UMENG 
											MobclickAgent.onEvent(getApplicationContext(),
													"Regist");
											LogUtil.showToast(getApplicationContext(), "注册成功!");
											if(extra==1){
												Intent fragment = new Intent();
												fragment.putExtra("fragment", 2);
												setResult(RESULT_OK, fragment);
											}
											finish();
										} else {
											phone_numberET.setText("");
											phone_numberET
													.setHint("6-16位英文字母、数字或符号");
											phone_passwordET.setText("");
											phone_passwordET
													.setHint("密码设置未成功，请重新输入");
										}
									}
								});
							}
						});
					}
				}
			}
		}
	}

	private void setLogin() {
		registFlag = false;
		loginFlag = true;
		registe_title.setText("登陆");
		login_title.setText("注册");
		register_button.setText("立即登陆");
		phone_checkhint.setVisibility(View.INVISIBLE);
		phone_number_agree.setVisibility(View.GONE);

		phone_number_tv.setText("用户名");
		phone_numberET.setText("");
		phone_numberET.setHint("手机号/用户名");

		phone_passwordTV.setText("密码");
		phone_passwordET.setHint("6-16位英文字母、数字或符号");
		phone_passwordTV.setVisibility(View.VISIBLE);
		phone_passwordET.setVisibility(View.VISIBLE);
		register_line3.setVisibility(View.VISIBLE);
	}

	private void userLogin() {
		userName3 = phone_numberET.getText().toString().trim();
		passWord3 = phone_passwordET.getText().toString();
		if (TextUtils.isEmpty(userName3) || TextUtils.isEmpty(passWord3)) {
			if (TextUtils.isEmpty(userName3)) {
				phone_numberET.setHint("用户名不能为空，请输入");
				phone_numberET.startAnimation(shake);
			}
			if (TextUtils.isEmpty(passWord3)) {
				phone_passwordET.setHint("密码不能为空，请输入");
				phone_passwordET.startAnimation(shake);
			}
		} else if (!DataFormat.isPassWord(passWord3)) {
			phone_numberET.setText("");
			phone_numberET.setHint("手机号/用户名");
			phone_passwordET.setText("");
			phone_passwordET.setHint("密码不符合要求，请重新输入");
			phone_passwordET.startAnimation(shake);
		} else {
			// 登录中显示
			phone_numberET.setText("");
			phone_numberET.setHint("正在登录...");
			phone_passwordET.setText("");
			phone_passwordET.setHint("用户名与密码格式已通过验证");
			loginInfo=DeviceInfoUtil.getLoginInfo(this, userName3, passWord3);
			phone_passwordET.setHint("正在发送您的登录信息");
			// 发送登录签名
			loginThreads = Executors.newFixedThreadPool(2);
			loginThreads.execute(new Runnable() {

				private User loginResult=null;

				@Override
				public void run() {

					loginResult = UserEngine.getInstance().userLogin(loginInfo);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if (loginResult != null) {
								FileUtil.homeTag(getApplicationContext(),
										userName3, passWord3, loginResult);
								phone_numberET.setHint("恭喜");
								phone_passwordET.setHint("登录成功！");
								//UMENG
								MobclickAgent.onEvent(getApplicationContext(),
										"Login");
								// TODO:通知其他模块 修改登录状态！ 广播！
								if(extra==1){
									Intent fragment = new Intent();
									fragment.putExtra("fragment", 2);
									setResult(RESULT_OK, fragment);
								}
								finish();
							} else {
								phone_numberET.setText("");
								phone_numberET.setHint("手机号/用户名");
								phone_passwordET.setText("");
								phone_passwordET.setHint("登录未成功，请重试或注册");
							}
						}
					});
				}
			});
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.register_agree:
			if (agreeFlag) {
				register_agree.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.disagree));
				register_button.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.button_gray));
				agreeFlag = false;
			} else {
				register_agree.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.agree));
				register_button.setBackgroundDrawable(getResources()
						.getDrawable(R.drawable.button_red));
				agreeFlag = true;
			}
			break;
		case R.id.register_back:
			finish();
			break;
		case R.id.login_title:
			if (loginFlag) {
				setRegister();
			} else if (registFlag) {
				setLogin();
			}
			break;
		case R.id.register_button:
			if (registFlag) {
				if (agreeFlag) {
					sendPhone();
				} else if (checkFlag) {
					checkPhone();
				} else if (passWord) {
					sendPassWord();
				}
			} else if (loginFlag) {
				userLogin();
			}
			break;
		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
		if (phoneTheads != null)
			phoneTheads.shutdown();
		if (checkThreads != null)
			checkThreads.shutdown();
		super.onDestroy();
	}

}
