package com.allen.itouzi.activity;

import java.util.List;

import com.allen.itouzi.R;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.LockPatternView;
import com.allen.itouzi.view.LockPatternView.Cell;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * 监听锁屏 解锁 广播 跳出手势密码界面
 * @author zeng
 *
 */
public class LockActivity extends Activity implements OnClickListener,LockPatternView.OnPatternListener{
	private static final String TAG = "LockActivity";
	private static int WRONG_TIME = 0;
	private List<Cell> lockPattern=null;
	private LockPatternView lockPatternView;
	private TextView reset_forget;
	private TextView reset_hint;
	private TextView reset_title;
	private Animation shake;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		String lockPatter=FileUtil.getLockPatter(getApplicationContext());
		lockPattern = LockPatternView.stringToPattern(lockPatter);
		setContentView(R.layout.more_lockpattern_reset);
		lockPatternView = (LockPatternView) findViewById(R.id.reset_pattern);
		reset_title = (TextView) findViewById(R.id.reset_title);
		reset_title.setText("爱投资");
		reset_forget = (TextView) findViewById(R.id.reset_forget);
		reset_hint = (TextView) findViewById(R.id.reset_hint);
		findViewById(R.id.reset_cancle).setVisibility(View.GONE);
		reset_forget.setOnClickListener(this);
		lockPatternView.setOnPatternListener(this);
		shake = AnimationUtils.loadAnimation(this,R.drawable.shake);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// disable back key
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
		if(v.getId()==R.id.reset_forget){
			LogUtil.showAliertFinish(this, "忘记手势密码，将退出登录");
		}
		
	}
	@Override
	public void onPatternStart() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPatternCleared() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPatternCellAdded(List<Cell> pattern) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onPatternDetected(List<Cell> pattern) {
		if (pattern.equals(lockPattern)) {
			reset_hint.setText("手势密码正确");
			lockPatternView.clearPattern();
			lockPattern.clear();
			lockPattern=null;
			WRONG_TIME=0;
			finish();
			return;
		} else {
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
			}else{
				WRONG_TIME++;
			}
		
	}
}
}
