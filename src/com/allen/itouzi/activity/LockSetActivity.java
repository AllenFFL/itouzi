package com.allen.itouzi.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.view.LockPatternView;
import com.allen.itouzi.view.LockPatternView.Cell;
import com.allen.itouzi.view.LockPatternView.DisplayMode;
import com.umeng.analytics.MobclickAgent;

public class LockSetActivity extends Activity implements
		LockPatternView.OnPatternListener, OnClickListener {

	private static final String TAG = "LockSetupActivity";
	private LockPatternView lockPatternView;
	/**
	 * 开始
	 */
	private static final int STEP_1 = 1;
	/**
	 * 第一次设置手势完成
	 */
	private static final int STEP_2 = 2;
	/**
	 * 按下继续按钮
	 */
	private static final int STEP_3 = 3; //
	/**
	 * 第二次设置手势完成
	 */
	private static final int STEP_4 = 4; //

	private int step;

	private List<Cell> choosePattern=null;

	private boolean confirm = false;
	private TextView lock_hint, lock_cancle;
	private Animation shake;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.more_lockpattern_set);
		lockPatternView = (LockPatternView) findViewById(R.id.lock_pattern);
		lockPatternView.setOnPatternListener(this);
		lock_hint = (TextView) findViewById(R.id.lock_hint);
		lock_cancle = (TextView) findViewById(R.id.lock_cancle);
		lock_cancle.setOnClickListener(this);
		step = STEP_1;
		shake = AnimationUtils.loadAnimation(this,R.drawable.shake);
		updateView();
	}
	private void updateView() {
		switch (step) {
		case STEP_1:
			choosePattern = null;
			confirm = false;
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_2:
			lock_hint.setText("请再次输入手势密码");
			lockPatternView.clearPattern();
			lockPatternView.enableInput();
			break;
		case STEP_4:
			if (confirm) {
				lockPatternView.disableInput();
				// 存储手势密码
				FileUtil.saveLockPatter(getApplicationContext(),
						LockPatternView.patternToString(choosePattern));
				lock_hint.setText("设置成功！");
				finish();
			} else {
				lock_hint.setText("两次密码不一致，请重新设置");
				lock_hint.startAnimation(shake);
				step = STEP_1;
				lockPatternView.enableInput();
			}
			choosePattern.clear();
			choosePattern=null;
			break;
		default:
			break;
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
		case R.id.lock_cancle:
			if(choosePattern!=null){
				choosePattern.clear();
				choosePattern=null;
			}
			finish();
			break;
		default:
			break;
		}
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
	}

	@Override
	public void onPatternDetected(List<Cell> pattern) {
		Log.d(TAG, "onPatternDetected");

		if (pattern.size() < LockPatternView.MIN_LOCK_PATTERN_SIZE) {
			lock_hint.setText("至少连接4个点，请重试");
			lock_hint.startAnimation(shake);
//			lockPatternView.setDisplayMode(DisplayMode.Wrong);
			lockPatternView.enableInput();
			return;
		}

		if (choosePattern == null) {
			choosePattern = new ArrayList<Cell>(pattern);
			step = STEP_2;
			updateView();
			return;
		}
		if (choosePattern.equals(pattern)) {
			confirm = true;
		} else {
			confirm = false;
		}
		step = STEP_4;
		updateView();

	}
}
