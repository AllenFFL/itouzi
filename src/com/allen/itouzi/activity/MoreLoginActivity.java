package com.allen.itouzi.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.User;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.FileUtil;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MoreLoginActivity extends Activity implements OnClickListener {
	private TextView negative, positive, name;
	private RelativeLayout more_logins;
	public static final int LOGIN_CODE=9;
	private static final String TAG = "MoreLoginActivity";
	private ExecutorService threadPool;
	private Animation shake;
	private TextView passwordET;
	private String loginInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.more_login);
		ExitApplication.getInstance().addActivity(this);
		Log.i(TAG, "MoreLoginActivity onCreate!");
		name = (TextView) findViewById(R.id.more_login_name);
		passwordET = (TextView) findViewById(R.id.more_login_text);
		more_logins = (RelativeLayout) findViewById(R.id.more_login_rl);
//		more_logins.getBackground().setAlpha(100);
		name.setText(FileUtil.getName(getApplicationContext()));
		negative = (TextView) findViewById(R.id.more_login_negative);
		positive = (TextView) findViewById(R.id.more_login_positive);

		negative.setOnClickListener(this);
		positive.setOnClickListener(this);
		// ��ʾ�����
		WindowManager.LayoutParams params = getWindow().getAttributes();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		// ����WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
		params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		shake = AnimationUtils.loadAnimation(this, R.drawable.shake);
		super.onCreate(savedInstanceState);
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.more_login_negative:
			finish();
			break;
		case R.id.more_login_positive:
			verifyLogin();
			break;
		default:
			break;
		}

	}
	/**
	 * ��֤���� �����Ƿ��¼�ɹ�
	 */
	private void verifyLogin() {
		final String passWord=passwordET.getText().toString().trim();
		if(TextUtils.isEmpty(passWord)){
			passwordET.setHint("���벻��Ϊ�գ�������");
			passwordET.startAnimation(shake);
		}else if(!DataFormat.isPassWord(passWord)){
			passwordET.setText("");
			passwordET.setHint("���벻����Ҫ������������");
			passwordET.startAnimation(shake);
		}else{
			passwordET.setText("");
			passwordET.setHint("���ڵ�¼...");
			loginInfo=DeviceInfoUtil.getLoginInfo(this,
					FileUtil.getName(getApplicationContext()), passWord);
		//����������ӿ�
			threadPool = Executors.newFixedThreadPool(2);
			threadPool.execute(new Runnable() {
				
				private User verifyResult=null;

				@Override
				public void run() {
					verifyResult = UserEngine.getInstance().userLogin(loginInfo);
					
//					verifyRsult = UserEngine.getInstance().verifyPassWord(passWord, FileUtil.getToken(getApplicationContext()));
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(verifyResult==null){
								passwordET.setHint("��¼ʧ�ܣ������µ�¼");
								passwordET.startAnimation(shake);
							}else{
								passwordET.setHint("��¼�ɹ���");
								
								//�´洢һ���ֶ� moreLogin��ʾ��¼�ɹ����
//								SharedPreferences logInfo = getSharedPreferences("itouziFFL",
//										Activity.MODE_PRIVATE);
//								Editor editor = logInfo.edit();
//								editor.putInt("moreLogin", RESULT_OK);
//								editor.commit();
//								
//								FileUtil.homeTag(getApplicationContext(),
//										FileUtil.getName(getApplicationContext()),
//										passWord, verifyResult);
//								Intent data=new Intent();
//								data.putExtra("verifyResult", true);
//								Intent resultIntent=new Intent(MoreLoginActivity.this,MoreLockActivity.class);
//								resultIntent.putExtra("verifyResult", RESULT_OK);
//								startActivity(resultIntent);
								setResult(RESULT_OK, new Intent().putExtra("verifyResult", "OK!"));
								finish();
							}
						}
					});
					
				}
			});
		}
	
		
	}
	@Override
	public void finish() {
		if(threadPool!=null)
			threadPool.shutdown();
		super.finish();
	}
}
