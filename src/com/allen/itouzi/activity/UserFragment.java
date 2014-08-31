package com.allen.itouzi.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.R.color;
import com.allen.itouzi.R.drawable;
import com.allen.itouzi.bean.UserMonthIn;
import com.allen.itouzi.bean.UserYearIn;
import com.allen.itouzi.bean.UserProperty;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.net.NetUtil;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.ChartView;
import com.umeng.analytics.MobclickAgent;

public class UserFragment extends Fragment implements OnClickListener {

	protected static final String TAG = "UserFragment";
	private TextView use_iremain, total_imoney, income_mouth, income_year,
			income_sum,income_rmb;
	private RelativeLayout income_totals, income_remains;
	private LinearLayout user_loading;
	private LinearLayout user_views;
	private String token;
	private ExecutorService userThreads=null;
	private UserProperty property;
	private ArrayList<UserYearIn> incomeList;
	private ChartView income_chart;
	private View root;
	private int height;
	private int width;
	private float denstiy;
	private ArrayList<UserMonthIn> monthList;
	public Handler pHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				property = (UserProperty) msg.obj;
				if (property == null) {
					LogUtil.showAliert(getActivity(), "资产信息获取失败，请检查网络！");
				} else {
					// 填充资产数据
					use_iremain.setText(property.getUse_money());
					total_imoney.setText(property.getSum() + "");
					disLoading();
				}
				break;
			case 2:
				incomeList = (ArrayList<UserYearIn>) msg.obj;
				// TODO:填充收益曲线数据
				if (incomeList == null) {
					LogUtil.showAliert(getActivity(), "收益信息获取失败，请检查网络！");
				} else {
					initChartView(incomeList);
				}
				break;
			case 3:
				LogUtil.showAliert(getActivity(), "信息获取失败，请检查网络！");
				break;
			case 4:
				income_sum.setText((String) msg.obj);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "fragment onCreateView!");
		if(FileUtil.getToken(getActivity())==null){
			Intent register = new Intent(getActivity(),
					RegisterActivity.class);
			startActivity(register);
			return null;
		}else{
			DisplayMetrics dm =DeviceInfoUtil.getMetrics(getActivity());
			denstiy = dm.density;
			height = dm.heightPixels;
			width = dm.widthPixels;
			Log.i(TAG, "denstiy:" + denstiy + "height:" + height + "width:" + width);
			root = inflater.inflate(R.layout.user_fragment, container, false);
			income_remains = (RelativeLayout) root.findViewById(R.id.income_remains);
			income_totals = (RelativeLayout) root.findViewById(R.id.income_totals);
			user_loading = (LinearLayout) root.findViewById(R.id.user_loading);
			user_loading.setLayoutParams(new LinearLayout.LayoutParams(width, height-50*(int)denstiy));
			user_views = (LinearLayout) root.findViewById(R.id.user_views);
			income_sum = (TextView) root.findViewById(R.id.income_sum);
			income_rmb = (TextView) root.findViewById(R.id.income_rmb);
			income_year = (TextView) root.findViewById(R.id.income_year);
			income_mouth = (TextView) root.findViewById(R.id.income_mouth);
			use_iremain = (TextView) root.findViewById(R.id.use_iremain);
			total_imoney = (TextView) root.findViewById(R.id.total_imoney);
			if(!NetUtil.checkNetState(getActivity())){
				LogUtil.showAliert(getActivity(), "网络未连接！");
			}else{
				getActivity().setRequestedOrientation(
						ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
				income_remains.setOnClickListener(this);
				income_totals.setOnClickListener(this);
				income_year.setOnClickListener(this);
				income_mouth.setOnClickListener(this);
				yearData();
			}
			return root;
		}
	}

	protected void initChartView(ArrayList<? extends Object> incomeList) {
		income_chart = (ChartView) root.findViewById(R.id.income_chart);
		income_chart.setLayoutParams(new LinearLayout.LayoutParams(
				width, (int) (height / 2 - 120 * denstiy)));
		income_chart.setInfo(pHandler, denstiy, width, height / 2,
				incomeList);
		income_chart.setVisibility(View.VISIBLE);
		income_chart.invalidate();
		Log.i(TAG, "income_chart:" + "  setInfo!");
	}

	private void yearData() {
		showLoading();
		token = FileUtil.getToken(getActivity());
		if(userThreads==null)
		userThreads = Executors.newFixedThreadPool(3);
		userThreads.execute(new Runnable() {
			@Override
			public void run() {
				UserEngine.getInstance().userIndex(pHandler, token);
			}
		});
	}
	@Override
	public void onResume() {
		super.onResume();
		if(FileUtil.getToken(getActivity())==null){
			Intent register = new Intent(getActivity(),
					RegisterActivity.class);
			startActivity(register);
		}
		MobclickAgent.onPageStart(TAG);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
	@Override
	public void onDestroy() {
		if (userThreads != null)
			userThreads.shutdown();
		super.onDestroy();
		
	}

	private void chartMonth() {
		income_mouth.setBackgroundDrawable(getResources().getDrawable(
				drawable.white_corner_rectangle));
		income_mouth.setTextColor(getResources().getColor(color.dark_red));
		income_year.setBackgroundDrawable(getResources().getDrawable(
				drawable.red_corner_rectangle));
		income_year.setTextColor(getResources().getColor(color.i_white));
		if (monthList != null) {
			initChartView(monthList);
		} else {
			new MonthTask().execute(null, token);
		}
	}

	private void chartYear() {
		income_year.setBackgroundDrawable(getResources().getDrawable(
				drawable.white_corner_rectangle));
		income_year.setTextColor(getResources().getColor(color.dark_red));
		income_mouth.setBackgroundDrawable(getResources().getDrawable(
				drawable.red_corner_rectangle));
		income_mouth.setTextColor(getResources().getColor(color.i_white));
		if (incomeList != null) {
			income_chart.setLayoutParams(new LinearLayout.LayoutParams(width,
					(int) (height / 2 - 120 * denstiy)));
			income_chart.setInfo(pHandler, denstiy, width, height / 2,
					incomeList);
			income_chart.invalidate();
		} else {
			yearData();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.income_remains:
			// 转跳交易记录页
			Intent recordIntent = new Intent(getActivity(),
					RecordActivity.class);
			startActivity(recordIntent);
			break;
		case R.id.income_totals:
			// 转跳资产统计页
			Intent propertyIntent = new Intent(getActivity(),
					PropertyActivity.class);
			if (property != null) {
				propertyIntent.putExtra("property", property);
				startActivity(propertyIntent);
			}
			break;
		case R.id.income_year:
			chartYear();
			break;
		case R.id.income_mouth:
			chartMonth();
			break;
		default:
			break;
		}
	}
	private void showLoading(){
		user_views.setVisibility(View.GONE);
		income_sum.setVisibility(View.GONE);
		income_rmb.setVisibility(View.GONE);
		user_loading.setVisibility(View.VISIBLE);
	}
	private void disLoading(){
		user_loading.setVisibility(View.GONE);
		user_views.setVisibility(View.VISIBLE);
		income_sum.setVisibility(View.VISIBLE);
		income_rmb.setVisibility(View.VISIBLE);
	}
//	private void showDialog(String content) {
//		user_loading.setVisibility(View.GONE);
//		AlertDialog.Builder builder = new Builder(getActivity());
//		builder.setMessage(content);
//		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				// TODO:
//				onDestroy();
//			}
//		});
//		builder.show();
//	}

	/**
	 * 获取用户受益信息 月收益 params[0]月份,params[1]token
	 * 
	 * @author allen
	 * 
	 */
	class MonthTask extends AsyncTask<String, Void, ArrayList<UserMonthIn>> {
		@Override
		protected void onPreExecute() {
			showLoading();
			super.onPreExecute();
		}

		@Override
		protected ArrayList<UserMonthIn> doInBackground(String... params) {
			return UserEngine.getInstance().getMonthIn(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(ArrayList<UserMonthIn> result) {
			super.onPostExecute(result);
			if (result == null) {
				LogUtil.showAliert(getActivity(), "收益信息获取失败，请检查网络！");
			} else {
				monthList = result;
				disLoading();
				income_chart.setLayoutParams(new LinearLayout.LayoutParams(
						width, (int) (height / 2 - 120 * denstiy)));
				income_chart.setInfo(pHandler, denstiy, width, height / 2,
						result);
			}
		}
	}

}
