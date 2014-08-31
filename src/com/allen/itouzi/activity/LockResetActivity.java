package com.allen.itouzi.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.LockPatternView;
import com.allen.itouzi.view.LockPatternView.Cell;
import com.umeng.analytics.MobclickAgent;

/**
 * 输入5次 密码错误 退出登录
 * 
 * @author zeng
 * 
 */
public class LockResetActivity extends Activity implements
		LockPatternView.OnPatternListener,OnClickListener{
	private static final String TAG = "LockActivity";
	/**
	 * 输入当前密码
	 */
	private static final int STEP_1 = 1;
	/**
	 *设置手势密码
	 */
	private static final int STEP_2 = 2;
	private int step;
	private int WRONG_TIME=0;
	private List<Cell> lockPattern=null;
	private LockPatternView lockPatternView;

	private TextView reset_forget,reset_cancle;

	private TextView reset_hint;
	private Animation shake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		step=STEP_1;
		String lockPatter=FileUtil.getLockPatter(getApplicationContext());
		if (lockPatter == null) {
			finish();
		}
		lockPattern = LockPatternView.stringToPattern(lockPatter);
		setContentView(R.layout.more_lockpattern_reset);
		lockPatternView = (LockPatternView) findViewById(R.id.reset_pattern);
		reset_forget = (TextView) findViewById(R.id.reset_forget);
		reset_hint = (TextView) findViewById(R.id.reset_hint);
		reset_cancle = (TextView) findViewById(R.id.reset_cancle);
		reset_cancle.setOnClickListener(this);
		reset_forget.setOnClickListener(this);
		lockPatternView.setOnPatternListener(this);
		shake = AnimationUtils.loadAnimation(this,R.drawable.shake);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPatternStart() {
		Log.d(TAG, "onPatternStart");
	}

	@Override
	public void onPatternCleared() {
		Log.d(TAG, "onPatternCleared");
	}

	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		Log.d(TAG, "onPatternCellAdded");
		Log.e(TAG, LockPatternView.patternToString(pattern));
		// Toast.makeText(this, LockPatternView.patternToString(pattern),
		// Toast.LENGTH_LONG).show();
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		Log.d(TAG, "onPatternDetected");
		//密码是否匹配
		//
		if(step==STEP_1){
			if (pattern.equals(lockPattern)) {
				//设置新密码
				
				reset_hint.setText("设置新密码");
				lockPatternView.clearPattern();
				lockPattern.clear();
				lockPattern=null;
				reset_forget.setVisibility(View.GONE);
				step=STEP_2;
				return;
			} else {
//				lockPatternView.setDisplayMode(DisplayMode.Wrong);
				reset_hint.setText("密码不正确，请重新绘制");
				reset_hint.startAnimation(shake);
				if(WRONG_TIME==2){
					reset_forget.setVisibility(View.VISIBLE);
				}
				if(WRONG_TIME>5){
					reset_hint.setText("错误超过5次，将退出登录");
					lockPatternView.disableInput();
					WRONG_TIME=0;
					LogUtil.showAliertFinish(this, "错误超过5次，请登录后设置");
//					AlertDialog.Builder dialog=new Builder(this);
//					dialog.setMessage("错误超过5次，请重新登录后设置");
//					dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//						
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							FileUtil.clearToken(LockResetActivity.this);
//							startActivity(new Intent(LockResetActivity.this, RegisterActivity.class));
//							finish();
//						}
//					});
//					dialog.show();
				}else{
					WRONG_TIME++;
				}
			}
		}
		if(step==STEP_2){
			if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
				reset_hint.setText("至少连接4个点，请重试");
				reset_hint.startAnimation(shake);
				lockPatternView.enableInput();
				return;
			}
			if (lockPattern == null) {
				lockPattern = new ArrayList<Cell>(pattern);
				reset_hint.setText("请再次输入手势密码");
				lockPatternView.clearPattern();
				lockPatternView.enableInput();
				return;
			}
			if (lockPattern.equals(pattern)) {
				lockPatternView.disableInput();
				// 存储手势密码
				FileUtil.saveLockPatter(getApplicationContext(),
						LockPatternView.patternToString(lockPattern));
				reset_hint.setText("设置成功！");
				finish();
			}else{
				reset_hint.setText("两次密码不一致，请重新设置");
				reset_hint.startAnimation(shake);
				lockPatternView.clearPattern();
				lockPattern.clear();
				lockPattern=null;
				lockPatternView.enableInput();
			}
			
		}
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
		case R.id.reset_forget://忘记密码-退出登录
			LogUtil.showAliertFinish(this, "忘记手势密码，将退出登录");
			break;
		case R.id.reset_cancle://取消设置
			if(lockPattern!=null){
				lockPattern.clear();
				lockPattern=null;
			}
			finish();
			break;
		default:
			break;
		}
		
	}
}
