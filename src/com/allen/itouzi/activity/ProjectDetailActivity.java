package com.allen.itouzi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.Idanbao;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.net.NetUtil;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DateUtil;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.CircleProgressView;
import com.allen.itouzi.view.ICounter;
import com.umeng.analytics.MobclickAgent;

/**
 * 項目详情
 * 
 * @author allen
 */
public class ProjectDetailActivity extends Activity implements OnClickListener {
	private static final String TAG = "ProjectDetailActivity";
	private CircleProgressView project_circle_progress;
	private ImageView idanbao_back;
	private TextView project_detail_clock, project_detail_more;
	private TextView project_detail_iclock;
	private TextView project_detail_profit;
	private TextView project_detail_time;
	private TextView project_detail_iname;
	private TextView project_detail_idate;
	private TextView project_detail_imin;
	private TextView project_detail_igruantor;
	private Button project_idanbao_invest;
	private TextView project_iamount;
	private TextView invest_amount_iremain;
	private TextView invest_amount_iyes;
	private TextView invest_time_hint;
	private TextView project_invest_time;
	private RelativeLayout project_invest_amount;
	private TextView project_ipersons;
	private TextView project_persons;
	private View project_detail_iline;
	private TextView invest_amount_remain;
	private TextView invest_amount_yes;
	private String irongzu_id = null, idanbao_id = null;
	private Idanbao idanbao = null;
	private LinearLayout project_detail_loading;
	private RelativeLayout project_idanbao_detail;
	private ImageView detail_new;
	private float progress;
	private ScrollView detail_sl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		irongzu_id = intent.getStringExtra("irongzu_id");
		idanbao_id = intent.getStringExtra("idanbao_id");
		setContentView(R.layout.project_idanbao_detail);
		project_detail_loading = (LinearLayout) findViewById(R.id.project_detail_loading);
		detail_sl = (ScrollView) findViewById(R.id.detail_sl);
		idanbao_back = (ImageView) findViewById(R.id.idanbao_back);
		idanbao_back.setOnClickListener(this);
		if(!NetUtil.checkNetState(this)){
			LogUtil.showAliert(this, "网络未连接！");
		}else{
			initData();
		}
	}
	
	public void initData() {
		if (irongzu_id != null) {
			new GetIrongzuTask().execute(irongzu_id);
		}
		if (idanbao_id != null) {
			new GetIdanbaoTask().execute(idanbao_id);
		}
	}

	class GetIdanbaoTask extends AsyncTask<String, Void, Idanbao> {
		@Override
		protected void onPreExecute() {
			detail_sl.setVisibility(View.GONE);
			project_detail_loading.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Idanbao doInBackground(String... params) {
			Idanbao idanbao = ProjectsEngine.getInstance()
					.getIdanbao(params[0]);
			return idanbao;
		}

		@Override
		protected void onPostExecute(Idanbao result) {
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(ProjectDetailActivity.this, "信息获取失败！",
						Toast.LENGTH_LONG).show();
			} else {
				idanbao = result;
				detail_sl.setVisibility(View.VISIBLE);
				project_detail_loading.setVisibility(View.GONE);
				initView();
			}
		}
	}

	class GetIrongzuTask extends AsyncTask<String, Void, Idanbao> {
		@Override
		protected void onPreExecute() {
			detail_sl.setVisibility(View.GONE);
			project_detail_loading.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected Idanbao doInBackground(String... params) {
			Idanbao idanbao = ProjectsEngine.getInstance()
					.getIrongzu(params[0]);
			return idanbao;
		}

		@Override
		protected void onPostExecute(Idanbao result) {
			super.onPostExecute(result);
			if (result == null) {
				Toast.makeText(ProjectDetailActivity.this, "信息获取失败！",
						Toast.LENGTH_LONG).show();
			} else {
				idanbao = result;
				initView();
				detail_sl.setVisibility(View.VISIBLE);
				project_detail_loading.setVisibility(View.GONE);
			}
		}
	}

	private void initView() {
		// 投资进度 金额等
		project_circle_progress = (CircleProgressView) findViewById(R.id.project_circle_progress);
		project_invest_amount = (RelativeLayout) findViewById(R.id.project_invest_amount);
		project_iamount = (TextView) findViewById(R.id.project_iamount);
		detail_new = (ImageView) findViewById(R.id.detail_new);

		invest_amount_remain = (TextView) findViewById(R.id.invest_amount_remain);
		invest_amount_iremain = (TextView) findViewById(R.id.invest_amount_iremain);
		invest_amount_yes = (TextView) findViewById(R.id.invest_amount_yes);
		invest_amount_iyes = (TextView) findViewById(R.id.invest_amount_iyes);
		project_detail_iline = (View) findViewById(R.id.project_detail_iline);

		project_persons = (TextView) findViewById(R.id.project_persons);
		project_ipersons = (TextView) findViewById(R.id.project_ipersons);

		// 预告
		invest_time_hint = (TextView) findViewById(R.id.invest_time_hint);
		project_invest_time = (TextView) findViewById(R.id.project_invest_time);

		// 倒计时
		project_detail_clock = (TextView) findViewById(R.id.project_detail_clock);
		project_detail_iclock = (TextView) findViewById(R.id.project_detail_iclock);
		// 项目信息
		project_idanbao_detail = (RelativeLayout) findViewById(R.id.project_idanbao_detail);
		project_detail_profit = (TextView) findViewById(R.id.project_detail_profit);
		project_detail_time = (TextView) findViewById(R.id.project_detail_time);
		project_detail_iname = (TextView) findViewById(R.id.project_detail_iname);
		project_detail_idate = (TextView) findViewById(R.id.project_detail_idate);
		project_detail_imin = (TextView) findViewById(R.id.project_detail_imin);
		project_detail_igruantor = (TextView) findViewById(R.id.project_detail_igruantor);
		project_detail_more = (TextView) findViewById(R.id.project_detail_more);

		project_idanbao_invest = (Button) findViewById(R.id.project_idanbao_invest);

		project_idanbao_detail.setOnClickListener(this);
		project_invest_amount.setOnClickListener(this);
		project_circle_progress.setOnClickListener(this);
		project_idanbao_invest.setOnClickListener(this);
		// project_detail_more.setOnClickListener(this);

		project_detail_profit.setText(idanbao.getProject_profit());
		project_detail_time.setText(idanbao.getProject_time());
		project_detail_iname.setText(idanbao.getProject_name());
		project_detail_idate.setText(idanbao.getProject_paydate());
		project_detail_imin.setText(idanbao.getLowest_account() + "元");
		project_detail_igruantor.setText(idanbao.getGuarantor_name());
		if (idanbao.getForNewUser() == 2) {
			detail_new.setVisibility(View.VISIBLE);
		}
		// TODO:判断项目预告 4 已还款3已投满 101 预告 时间 100 预发布 显示倒计时 1 正在投
		switch (idanbao.getStatus()) {
		case 1:// 正在投
			progress = Float.parseFloat(idanbao.getProject_progress());
			showProgress(progress);
			break;
		case 3:// 已投满 project_ipersons 不限时进度
			showFull();
			break;
		case 4:// 已还款 列表页处理 无详情

			break;
		case 100:// 倒计时
			showClock();
			break;
		case 101:// 预告 TODO:不显示更多
			showData(Float.parseFloat("101"));
			break;
		default:
			break;
		}
	}

	private void showFull() {
		project_idanbao_invest.setFocusable(false);
		project_idanbao_invest.setFocusableInTouchMode(false);
		project_idanbao_invest.clearFocus();
		project_idanbao_invest.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.button_gray));
		Animation scaleAnimation = new ScaleAnimation(2.0f, 1.0f, 2.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		scaleAnimation.setDuration(600);
		scaleAnimation.setInterpolator(new AccelerateInterpolator());
		View imageMan = findViewById(R.id.invest_touman);
		imageMan.setVisibility(View.VISIBLE);
		imageMan.startAnimation(scaleAnimation);
		project_circle_progress.setVisibility(View.GONE);
		project_iamount.setText(DataFormat.getThousand(idanbao
				.getProject_amount()));
		project_persons.setVisibility(View.VISIBLE);
		project_ipersons.setText(idanbao.getInvest_persons());
		project_ipersons.setVisibility(View.VISIBLE);
		project_invest_amount.setVisibility(View.VISIBLE);
		project_invest_amount.setFocusable(false);
		project_invest_amount.setFocusableInTouchMode(false);
		project_detail_iline.setVisibility(View.INVISIBLE);
		invest_amount_remain.setVisibility(View.INVISIBLE);
		invest_amount_iremain.setVisibility(View.INVISIBLE);
		invest_amount_iyes.setVisibility(View.INVISIBLE);
		invest_amount_yes.setVisibility(View.INVISIBLE);
	}

	private void showClock() {
		project_idanbao_invest.setFocusable(false);
		project_idanbao_invest.setFocusableInTouchMode(false);
		project_idanbao_invest.clearFocus();
		project_idanbao_invest.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.button_gray));
		project_invest_amount.setVisibility(View.GONE);
		project_circle_progress.setVisibility(View.INVISIBLE);
		project_detail_clock.setVisibility(View.VISIBLE);
		project_detail_iclock.setVisibility(View.VISIBLE);
		long invest_time = idanbao.getProject_investdate();
		long millisInFuture = invest_time - System.currentTimeMillis();
		new ICounter(this, millisInFuture, 1000, project_detail_iclock).start();
		project_detail_more.setVisibility(View.GONE);
		findViewById(R.id.project_detail_imore).setVisibility(View.GONE);
		findViewById(R.id.project_detail_line6).setVisibility(View.GONE);
	}

	private void showData(float i) {
		project_idanbao_invest.setFocusable(false);
		project_idanbao_invest.setFocusableInTouchMode(false);
		project_idanbao_invest.clearFocus();
		project_idanbao_invest.setBackgroundDrawable(getResources()
				.getDrawable(R.drawable.button_gray));
		project_circle_progress.setVisibility(View.INVISIBLE);
		invest_time_hint.setVisibility(View.VISIBLE);
		project_invest_time.setText(DateUtil.getDateToStrings(idanbao
				.getProject_investdate()));
		project_invest_time.setVisibility(View.VISIBLE);
		project_invest_amount.setVisibility(View.INVISIBLE);
	}

	public void showProgress(float i) {
		project_detail_clock.setVisibility(View.INVISIBLE);
		project_detail_iclock.setVisibility(View.INVISIBLE);
		invest_time_hint.setVisibility(View.INVISIBLE);
		project_invest_time.setVisibility(View.INVISIBLE);
		project_invest_amount.setVisibility(View.INVISIBLE);
		project_circle_progress.setVisibility(View.VISIBLE);
		project_circle_progress.setProgress(i);
	}

	private void dismissProgress() {
		project_circle_progress.setVisibility(View.INVISIBLE);
		project_iamount.setText(DataFormat.getThousand(idanbao
				.getProject_amount()));
		project_invest_amount.setVisibility(View.VISIBLE);
		invest_amount_iremain.setText(DataFormat.getThousand(idanbao
				.getAccount_remain()));
		invest_amount_iyes.setText(DataFormat.getThousand(idanbao
				.getAccount_yes()));
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
		case R.id.idanbao_back:
			finish();
			break;
		case R.id.project_circle_progress:
			dismissProgress();
			break;
		case R.id.project_invest_amount:
			if (idanbao.getStatus() != 3)
				showProgress(Float.parseFloat(idanbao.getProject_progress()));
			break;
		case R.id.project_idanbao_detail:
			if (idanbao.getStatus() != 100) {
				Intent intent = new Intent(this, ProjectMoreActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("PojectDesc", idanbao);
				intent.putExtras(bundle);
				startActivity(intent);
			}
			break;
		case R.id.project_idanbao_invest:
			if (idanbao != null && idanbao.getStatus() == 1) {
				if (FileUtil.getToken(getApplicationContext()) != null) {
					Intent intent = new Intent(getApplicationContext(),
							InvestActivity.class);
					String[] projectInfo = { idanbao.getId(),
							idanbao.getAccount_remain() + "",
							idanbao.getLowest_account() };
					intent.putExtra("projectInfo", projectInfo);
					startActivity(intent);
				} else {
					Intent register = new Intent(this, RegisterActivity.class);
					startActivity(register);
				}
			}
			break;
		default:
			break;
		}
	}

}
