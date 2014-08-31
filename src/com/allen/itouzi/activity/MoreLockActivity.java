package com.allen.itouzi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.view.ToggleButton;
import com.allen.itouzi.view.ToggleButton.OnSwitchStatusListener;
import com.umeng.analytics.MobclickAgent;

public class MoreLockActivity extends Activity implements OnClickListener{
	private ImageView gestrue_bck;
	private View gestrue_resets;
	private View gestrue_sets;
	private ToggleButton more_switch1,more_switch2;
	private boolean isLock=false;
	private CheckBox more_checkBox;
	private String TAG="MoreLockActivity";
	private static final int LOCK_CODE=8;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.more_lockset);
		gestrue_bck = (ImageView)findViewById(R.id.gestrue_back);
		more_checkBox =(CheckBox) findViewById(R.id.more_checkBox);
		more_checkBox.setFocusable(false);
		more_checkBox.setClickable(false);
		more_checkBox.setFocusableInTouchMode(false);
		gestrue_sets = findViewById(R.id.gestrue_sets);
		gestrue_resets = findViewById(R.id.gestrue_resets);
		
		gestrue_sets.setOnClickListener(this);
		gestrue_resets.setOnClickListener(this);
		gestrue_bck.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(FileUtil.getToken(getApplicationContext())==null){
			finish();
			return;
		}
		if(FileUtil.getLockHint(getApplicationContext())){
			more_checkBox.setChecked(true);
			gestrue_resets.setVisibility(View.VISIBLE);
		}else{
			more_checkBox.setChecked(false);
			
			gestrue_resets.setVisibility(View.GONE);
		}
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
		case R.id.gestrue_back:
			finish();
			break;
		case R.id.gestrue_sets://设置密码
//			setLock();
			//TODO:测试：
			startActivityForResult(new Intent(MoreLockActivity.this,MoreLoginActivity.class),RESULT_FIRST_USER);
			break;
		case R.id.gestrue_resets://修改密码
			//输入原有手势密码
			startActivity(new Intent(MoreLockActivity.this,LockResetActivity.class));
			break;
			
		default:
			break;
		}
		
	}
	private void setLock(){
		if(FileUtil.getLockHint(getApplicationContext())){
			//关闭手势密码 清楚
			more_checkBox.setChecked(false);
			FileUtil.saveLockHint(this, false);
			FileUtil.clearLockPatter(this);
			gestrue_resets.setVisibility(View.GONE);
			Toast.makeText(this, "已关闭", 1).show();
		}else{
			more_checkBox.setChecked(true);
			FileUtil.saveLockHint(this, true);
			gestrue_resets.setVisibility(View.VISIBLE);
	        Intent intent = new Intent(this, LockSetActivity.class);
	        startActivity(intent);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(TAG, "resultCode:"+resultCode+"requestCode:"+requestCode);
		if(requestCode==RESULT_FIRST_USER){
			if(resultCode==RESULT_OK){
				String verifyResult=data.getStringExtra("verifyResult");
			if(verifyResult.equals("OK!")){
				setLock();
			}
		}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
