package com.allen.itouzi.activity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.HomeImageMap;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.engine.HomeEngine;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.view.RefreshListview;
import com.allen.itouzi.view.RefreshListview.PullToRefreshListener;
import com.allen.itouzi.view.adapter.HomeAdapter;
import com.allen.itouzi.view.adapter.IdanbaoAdapter;
import com.allen.itouzi.view.adapter.ImagePagerAdapter;

public class MainActivity1 extends FragmentActivity implements OnClickListener {// ActionBarActivity
	public static final String TAG = "MainActivity";

	private static ImageView bottom_home, bottom_project, bottom_mine,
			bottom_more;

	// TODO:（1） 轮播图压缩 webview（2）立即投资button selector (3)退出 2s内按2次退出
	private HomeFragment homeFragment = null;
	private ProjectFragment projectFragment = null;
	private UserFragment userFragment = null;

	private ChartFragment chartFragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		if (savedInstanceState == null) {
			if (homeFragment == null)
				homeFragment = new HomeFragment();
			final FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction beginTransaction = fragmentManager
					.beginTransaction();
			beginTransaction.replace(R.id.home_fragment, homeFragment, "home")
					.commitAllowingStateLoss();

			// fragmentManager.addOnBackStackChangedListener(new
			// OnBackStackChangedListener() {
			//
			// @Override
			// public void onBackStackChanged() {
			// //TODO:UI变化
			// // Fragment fragment =
			// fragmentManager.findFragmentById(R.id.home_fragment);
			// // if(fragment.getTag().equals("home")){
			// // onClick(bottom_home);
			// // }else if(fragment.getTag().equals("project")){
			// // onClick(bottom_project);
			// // }else if(fragment.getTag().equals("user")){
			// // onClick(bottom_mine);
			// // }
			// }
			// });
		}
		// 设置底部导航栏透明度
		findViewById(R.id.rl_bottom).getBackground().setAlpha(220);
		bottom_home = (ImageView) findViewById(R.id.bottom_home);
		bottom_project = (ImageView) findViewById(R.id.bottom_project);
		bottom_mine = (ImageView) findViewById(R.id.bottom_mine);
		bottom_more = (ImageView) findViewById(R.id.bottom_more);
		bottom_home.setOnClickListener(this);
		bottom_project.setOnClickListener(this);
		bottom_mine.setOnClickListener(this);
		bottom_more.setOnClickListener(this);
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
			FragmentTransaction beginTransaction = getSupportFragmentManager()
					.beginTransaction();
			if (chartFragment == null) {
				chartFragment = new ChartFragment();
			}
			beginTransaction
					.replace(R.id.home_fragment, chartFragment, "chart");
			beginTransaction.commitAllowingStateLoss();
			findViewById(R.id.rl_bottom).setVisibility(View.GONE);
		} else {
			onClick(bottom_mine);
			findViewById(R.id.rl_bottom).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// 思路2:imageButton 设置selector 点击时 变换焦点 用ImageButton集合循环判断控制焦点
		switch (v.getId()) {
		case R.id.bottom_home:
			bottom_home.setBackgroundResource(R.drawable.home);
			bottom_project.setBackgroundResource(R.drawable.project1);
			bottom_mine.setBackgroundResource(R.drawable.mine1);
			bottom_more.setBackgroundResource(R.drawable.more1);
			FragmentTransaction beginTransaction = getSupportFragmentManager()
					.beginTransaction();
			// beginTransaction.remove(projectFragment);
			if (homeFragment == null) {
				if (homeFragment == null) {
					homeFragment = new HomeFragment();
				}
				homeFragment = new HomeFragment();
			}
			beginTransaction.replace(R.id.home_fragment, homeFragment, "home");
			// getSupportFragmentManager().popBackStack();
			// beginTransaction.addToBackStack(null);
			beginTransaction.commitAllowingStateLoss();
			break;

		case R.id.bottom_project:
			bottom_home.setBackgroundResource(R.drawable.home1);
			bottom_project.setBackgroundResource(R.drawable.project);
			bottom_mine.setBackgroundResource(R.drawable.mine1);
			bottom_more.setBackgroundResource(R.drawable.more1);
			// 转跳 产品列表页
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction beginTransaction2 = getSupportFragmentManager()
					.beginTransaction();
			if (projectFragment == null) {
				projectFragment = new ProjectFragment();
			}
			beginTransaction2.replace(R.id.home_fragment, projectFragment,
					"project");
			// beginTransaction2.addToBackStack(null);
			beginTransaction2.commitAllowingStateLoss();
			break;
		case R.id.bottom_mine:
			bottom_home.setBackgroundResource(R.drawable.home1);
			bottom_project.setBackgroundResource(R.drawable.project1);
			bottom_mine.setBackgroundResource(R.drawable.mine);
			bottom_more.setBackgroundResource(R.drawable.more1);
			// TODO:判断是否登陆 显示注册登陆页 或 用户账户页

			SharedPreferences itouziFFL = getSharedPreferences("itouziFFL",
					Activity.MODE_PRIVATE);

			// Editor editor = itouziFFL.edit();
			// editor.putInt("homeGat", -1);
			// editor.putString("homeNenkot", null);
			// editor.commit();

			String homeNenkot = itouziFFL.getString("homeNenkot", null);
			String homeEman = itouziFFL.getString("homeEman", null);
			int homeGat = itouziFFL.getInt("homeGat", -1);
			if (homeNenkot == null || homeGat == -1) {
				// 注册登录
				Intent register = new Intent(this, RegisterActivity.class);
				startActivity(register);
			} else {
				String token = DataFormat.getUuid(homeNenkot, homeGat);
				// TODO:验证登录信息 退出

				FragmentTransaction beginTransaction3 = getSupportFragmentManager()
						.beginTransaction();
				if (userFragment == null) {
					userFragment = new UserFragment();
				}
				// getSupportFragmentManager().popBackStack();
				beginTransaction3.replace(R.id.home_fragment, userFragment,
						"user");
				// beginTransaction3.addToBackStack(null);
				beginTransaction3.commitAllowingStateLoss();
			}

			break;
		case R.id.bottom_more:
			bottom_home.setBackgroundResource(R.drawable.home1);
			bottom_project.setBackgroundResource(R.drawable.project1);
			bottom_mine.setBackgroundResource(R.drawable.mine1);
			bottom_more.setBackgroundResource(R.drawable.more);
			break;
		default:
			break;
		}

	}
}
