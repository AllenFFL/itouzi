package com.allen.itouzi.activity;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.R.color;
import com.allen.itouzi.R.drawable;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.bean.UserMonthIn;
import com.allen.itouzi.bean.UserYearIn;
import com.allen.itouzi.bean.UserProperty;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.utils.DateUtil;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.ChartView;
import com.umeng.analytics.MobclickAgent;

public class ChartFragment extends Fragment implements OnClickListener {
	protected static final String TAG = "ChartFragment";
	private ChartView user_chart;
	private LinearLayout chart_loading;
	private String token;
	private float denstiy;
	private int width;
	private int height;
	private TextView chart_year;
	private TextView chart_mouth;
	private TextView chart_title;
	private RelativeLayout chart_buttons,chart_banners;
	private ArrayList<UserMonthIn> monthList;
	private ArrayList<UserYearIn> yearList;
	private TextView chart_lastMonth;
	private TextView chart_nextMonth;
	private String curTime;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.user_chart, container, false);
		chart_loading = (LinearLayout) rootView
				.findViewById(R.id.chart_loading);
		user_chart = (com.allen.itouzi.view.ChartView) rootView
				.findViewById(R.id.user_chart);
		chart_title = (TextView) rootView.findViewById(R.id.chart_title);
		chart_banners = (RelativeLayout) rootView
				.findViewById(R.id.chart_banners);
		chart_buttons = (RelativeLayout) rootView
				.findViewById(R.id.chart_buttons);
		chart_lastMonth = (TextView) rootView
				.findViewById(R.id.chart_lastMonth);
		chart_nextMonth = (TextView) rootView
				.findViewById(R.id.chart_nextMonth);
		chart_year = (TextView) rootView.findViewById(R.id.chart_year);
		chart_year.setOnClickListener(this);
		chart_mouth = (TextView) rootView.findViewById(R.id.chart_mouth);
		chart_mouth.setOnClickListener(this);
		// 获取收益信息
		token = FileUtil.getToken(getActivity());
		if (token != null) {
			new YearTask().execute(token);
		} else {
			LogUtil.showRegister(getActivity(), "查看收益信息，请登录！");
		}
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		denstiy = dm.density;
		height = dm.heightPixels;
		width = dm.widthPixels;
		return rootView;
	}// monthURL

	/**
	 * 获取用户受益信息 年收益
	 * 
	 * @author allen
	 * 
	 */
	class YearTask extends AsyncTask<String, Void, ArrayList<UserYearIn>> {
		@Override
		protected void onPreExecute() {
			chart_loading.setVisibility(View.VISIBLE);
			user_chart.setVisibility(View.GONE);
			chart_buttons.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected ArrayList<UserYearIn> doInBackground(String... params) {
			return UserEngine.getInstance().getYearIn(params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<UserYearIn> result) {
			super.onPostExecute(result);
			if (result == null) {
				chart_loading.setVisibility(View.GONE);
				LogUtil.showAliert(ChartFragment.this.getActivity(), "信息获取失败！");
			} else {
				yearList = result;
				chart_loading.setVisibility(View.GONE);
				user_chart.setVisibility(View.VISIBLE);
				chart_buttons.setVisibility(View.VISIBLE);
				String month = yearList.get(0).getMonth();
				String startY = month.substring(0, month.indexOf("/"));
				String startM = month.substring(month.indexOf("/") + 1,
						month.lastIndexOf("/"));
				String last = yearList.get(yearList.size() - 1).getMonth();
				String endY = last.substring(0, last.indexOf("/"));
				String endM = last.substring(last.indexOf("/") + 1,
						last.lastIndexOf("/"));
				chart_title.setText(startY + "年" + startM + "月至" + endY + "年"
						+ endM + "月收益");
//				int bannerh=chart_banners.getHeight()+chart_buttons.getHeight();
//				Log.i(TAG, "bannerh:"+bannerh);
//				user_chart.setLayoutParams(new LinearLayout.LayoutParams(width,
//						(int) (height - bannerh)));//TODO:120 * denstiy
				user_chart
						.setInfo(null, denstiy, width, height, result);
				user_chart.invalidate();
			}
		}
	}

	/**
	 * 获取用户受益信息 月收益 params[0]月份,params[1]token
	 * 
	 * @author allen
	 * 
	 */
	class MonthTask extends AsyncTask<String, Void, ArrayList<UserMonthIn>> {
		@Override
		protected void onPreExecute() {
			chart_loading.setVisibility(View.VISIBLE);
			user_chart.setVisibility(View.GONE);
			chart_buttons.setVisibility(View.GONE);
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
				LogUtil.showAliert(getActivity(),"月收益获取失败，请检查网络！");
			} else {
				monthList = result;
				UserMonthIn monthIn = monthList.get(monthList.size() - 1);
				curTime = monthIn.getDay();
				String[] strings = curTime.split("-");
				chart_title.setText(strings[0]+"年"+strings[1]+"月收益");
				chart_loading.setVisibility(View.GONE);
				user_chart.setVisibility(View.VISIBLE);
				chart_buttons.setVisibility(View.VISIBLE);
				user_chart
						.setInfo(null, denstiy, width, height, result);
				user_chart.invalidate();
			}
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart(TAG);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chart_year:
			chartYear();
			break;
		case R.id.chart_mouth:
			chartMouth(null);
			break;
		case R.id.chart_lastMonth:
			String lastmonth=DateUtil.minMonth(curTime,1);
			chartMouth(lastmonth);
			break;
		case R.id.chart_nextMonth:
			String nextmonth=DateUtil.addMonth(curTime,1);
			chartMouth(nextmonth);
			break;

		default:
			break;
		}
	}

	private void chartMouth(String time) {
		chart_mouth.setBackgroundDrawable(getResources().getDrawable(
				drawable.white_corner_rectangle));
		chart_mouth.setTextColor(getResources().getColor(color.dark_red));
		chart_year.setBackgroundDrawable(getResources().getDrawable(
				drawable.red_corner_rectangle));
		chart_year.setTextColor(getResources().getColor(color.i_white));
		chart_lastMonth.setVisibility(View.VISIBLE);
		chart_nextMonth.setVisibility(View.VISIBLE);
		chart_lastMonth.setOnClickListener(this);
		chart_nextMonth.setOnClickListener(this);
		if (time == null && monthList != null) {
			user_chart.setInfo(null, denstiy, width, height, monthList);
			user_chart.invalidate();
		} else {
			new MonthTask().execute(time, token);
		}

	}

	private void chartYear() {
		chart_year.setBackgroundDrawable(getResources().getDrawable(
				drawable.white_corner_rectangle));
		chart_year.setTextColor(getResources().getColor(color.dark_red));
		chart_mouth.setBackgroundDrawable(getResources().getDrawable(
				drawable.red_corner_rectangle));
		chart_mouth.setTextColor(getResources().getColor(color.i_white));
		chart_lastMonth.setVisibility(View.INVISIBLE);
		chart_nextMonth.setVisibility(View.INVISIBLE);
		if (yearList != null) {
			user_chart.setInfo(null, denstiy, width, height, yearList);
			user_chart.invalidate();
		} else {
			new YearTask().execute(token);
		}

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

}