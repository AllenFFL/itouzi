package com.allen.itouzi.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.receiver.ScreenObserver;
import com.allen.itouzi.receiver.ScreenObserver.ScreenStateListener;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.FileUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

public class MainActivity extends FragmentActivity {// ActionBarActivity
	public static final String TAG = "MainActivity";

	// TODO:（1） 轮播图压缩 webview（2）立即投资button selector (3)退出 2s内按2次退出
	private ChartFragment chartFragment = null;
	private FragmentTabHost tab_host;
	private RadioGroup bottom_bar;
	/**
	 * 定义数组来存放按钮图片
	 */
	private int imageSelectors[] = { R.drawable.selector_home,
			R.drawable.selector_project, R.drawable.selector_user,
			R.drawable.selector_more };
	/**
	 * 定义数组来存放Fragment界面
	 */
	private Class fragments[] = { HomeFragment.class, ProjectFragment.class,
			UserFragment.class, MoreFragment.class, ChartFragment.class };

	private ScreenObserver mScreenObserver;

	private int fragmentExtra=-1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.home_activity);
		setScreenObserver();
		tab_host = (FragmentTabHost) findViewById(android.R.id.tabhost);
		tab_host.setup(this, getSupportFragmentManager(), R.id.home_fragment);
		int count = fragments.length;
		for (int i = 0; i < count; i++) {
			TabSpec tabSpec = tab_host.newTabSpec(i + "").setIndicator(i + "");
			tab_host.addTab(tabSpec, fragments[i], null);
		}
		bottom_bar = (RadioGroup) findViewById(R.id.tab_rg_menu);
//		bottom_bar.getBackground().setAlpha(220);
		Intent intent = getIntent();
		int fragment = intent.getIntExtra("fragment", -1);
		Log.i(TAG, "fragment:"+fragment);
		switch (fragment) {
		case 0:
			tab_host.setCurrentTab(0);
			bottom_bar.check(R.id.tab_rb_0);
			break;
		case 1:
			tab_host.setCurrentTab(1);
			bottom_bar.check(R.id.tab_rb_1);
			break;
		case 2:
			tab_host.setCurrentTab(2);
			bottom_bar.check(R.id.tab_rb_2);
			break;
		case 3:
			tab_host.setCurrentTab(3);
			bottom_bar.check(R.id.tab_rb_3);
			break;
		default:
			tab_host.setCurrentTab(0);
			bottom_bar.check(R.id.tab_rb_0);
			break;
		}
		bottom_bar.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_rb_0:
					tab_host.setCurrentTab(0);
					break;
				case R.id.tab_rb_1:
					tab_host.setCurrentTab(1);
					break;
				case R.id.tab_rb_2:
					if (FileUtil.getToken(getApplicationContext()) != null) {
						tab_host.setCurrentTab(2);
					} else {
						Intent register = new Intent(MainActivity.this,
								RegisterActivity.class);
						register.putExtra("main", 1);
						startActivityForResult(register, 10);
					}
					break;
				case R.id.tab_rb_3:
					tab_host.setCurrentTab(3);
					break;
				default:
					break;
				}
			}
		});
	}
	/**
	 * UMENG分享
	 */
	public final UMSocialService mController =UMServiceFactory.getUMSocialService("com.umeng.share");
	public UMSocialService getController(){
		return mController;
	}
	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if(requestCode==10&&resultCode==RESULT_OK){
					fragmentExtra = data.getIntExtra("fragment",-1);
				}
			    /**使用SSO授权必须添加如下代码 */
			    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
			    if(ssoHandler != null){
			       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			    }
			super.onActivityResult(requestCode, resultCode, data);
		}
	@Override
	protected void onResume() {
		super.onResume();
		String deviceInfo = DeviceInfoUtil.getDeviceInfo(getApplicationContext());
		Log.i(TAG, "deviceInfo:"+deviceInfo);
		MobclickAgent.onResume(this);
	}
	@Override
		protected void onPostResume() {
		if(fragmentExtra==2){
			tab_host.setCurrentTab(fragmentExtra);
			fragmentExtra=-1;
		}
			super.onPostResume();
		}
	@Override
		protected void onPause() {
			super.onPause();
			MobclickAgent.onPause(this);
		}
	private void setScreenObserver() {
		mScreenObserver=new ScreenObserver(getApplicationContext());
		mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
			
			@Override
			public void onScreenOn() {
				if(FileUtil.getLockHint(getApplicationContext())){
					startActivity(new Intent(getApplicationContext(), LockActivity.class));
				}
			}
			
			@Override
			public void onScreenOff() {
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 切换横屏表格fragment
			bottom_bar.setVisibility(View.GONE);
			tab_host.setCurrentTab(4);
		} else {
			tab_host.setCurrentTab(2);
			bottom_bar.setVisibility(View.VISIBLE);
			bottom_bar.check(R.id.tab_rb_2);
		}
	}
	private long exitTime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出",
                                Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
        } else {
        	ExitApplication.getInstance().exit();
        }
        return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
		protected void onDestroy() {
			super.onDestroy();
			mScreenObserver.stopScreenStateUpdate();
		}
}
