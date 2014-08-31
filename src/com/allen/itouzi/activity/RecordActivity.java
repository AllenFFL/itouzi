package com.allen.itouzi.activity;

import java.util.ArrayList;

import com.allen.itouzi.R;
import com.allen.itouzi.R.color;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.bean.IrongzuMain;
import com.allen.itouzi.bean.UserTrade;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DateUtil;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.ValueUtil;
import com.allen.itouzi.view.CircleProgressView;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends Activity implements OnClickListener {

	public static final String TAG = "RecordActivity";
	private ListView record_titles;
	private ListView record_listview;
	private TextView record_tv;
	private ImageView record_hint;
	private ImageView record_back;
	private String token;
	private String params;
	private ArrayList<UserTrade> tradeList;
	private LinearLayout record_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.user_record);
		initView();
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
	private void initView() {
		record_tv = (TextView) findViewById(R.id.record_tv);
		record_hint = (ImageView) findViewById(R.id.record_hint);
		record_hint.setVisibility(View.GONE);
		// record_tv.setOnClickListener(this);
		record_listview = (ListView) findViewById(R.id.record_listview);
		record_loading = (LinearLayout) findViewById(R.id.record_loading);
		token = FileUtil.getToken(this);
		params = "page=1&limit=10&time=1";
		if (token != null) {
			new GetRecordTask().execute(params, token);
		} else {
			// TODO:跳登录

		}
		// TODO:点击相应title 设置相应的内容
		record_titles = (ListView) findViewById(R.id.record_titles);
		record_titles.setAdapter(new TitleAdapter());

		record_back = (ImageView) findViewById(R.id.record_back);
		record_back.setOnClickListener(this);
	}

	class GetRecordTask extends AsyncTask<String, Void, ArrayList<UserTrade>> {
		private ContentAdapter recordAdapter;

		@Override
		protected void onPreExecute() {
			record_loading.setVisibility(View.VISIBLE);
			record_listview.setVisibility(View.GONE);
			super.onPreExecute();
		}

		@Override
		protected ArrayList<UserTrade> doInBackground(String... params) {
			return UserEngine.getInstance().getTrade(params[0], params[1]);
		}

		@Override
		protected void onPostExecute(ArrayList<UserTrade> result) {
			super.onPostExecute(result);
			if (result == null) {
				showAliert();
			} else {
				tradeList = result;
				if (recordAdapter == null) {
					recordAdapter = new ContentAdapter();
					record_listview.setAdapter(recordAdapter);
				} else {
					recordAdapter.notifyDataSetChanged();
				}
			}
			record_loading.setVisibility(View.GONE);
			record_listview.setVisibility(View.VISIBLE);
		}

	}

	class ContentAdapter extends BaseAdapter {

		ViewHolder holder;

		@Override
		public int getCount() {
			return tradeList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView != null && convertView instanceof RelativeLayout) {
				holder = (ViewHolder) convertView.getTag();
			} else {
				holder = new ViewHolder();
				convertView = View.inflate(RecordActivity.this,
						R.layout.user_record_item, null);
				holder.record_title = (TextView) convertView
						.findViewById(R.id.record_title);
				holder.record_date = (TextView) convertView
						.findViewById(R.id.record_date);
				holder.record_amount = (TextView) convertView
						.findViewById(R.id.record_amount);
				holder.record_remain = (TextView) convertView
						.findViewById(R.id.record_remain);
				convertView.setTag(holder);
			}
			// 获取bean 填充页面ArrayList<UserTrade> record_amount
			UserTrade trade = tradeList.get(position);
			// String
			// amount=DataFormat.getThousand((int)Double.parseDouble(trade.getMoney()));
			String remain = DataFormat.getDoubleThousand(Double
					.parseDouble(trade.getUse_money()));
			holder.record_title.setText(trade.getType_cn() + "-"
					+ trade.getDetail_cn());
			holder.record_date.setText(DateUtil.getDateToString(Long
					.parseLong(trade.getAddtime()) * 1000));
			if (trade.getDirection().equals("1")) {
				holder.record_amount.setTextColor(getResources().getColor(
						R.color.i_green));
				holder.record_amount.setText("+"
						+ DataFormat.getDoubleThousand(Double.parseDouble(trade
								.getMoney())) + "元");
			} else {
				holder.record_amount.setTextColor(getResources().getColor(
						R.color.i_red));
				holder.record_amount.setText("-"
						+ DataFormat.getDoubleThousand(Double.parseDouble(trade
								.getMoney())) + "元");
			}
			holder.record_remain.setText("余额："
					+ DataFormat.getDoubleThousand(Double.parseDouble(trade
							.getUse_money())) + "元");
			return convertView;
		}

	}

	class ViewHolder {

		public TextView record_title, record_date, record_amount,
				record_remain;

	}

	class TitleAdapter extends BaseAdapter {

		private android.widget.LinearLayout.LayoutParams textParams;
		private TextView desc_check;

		@Override
		public int getCount() {
			return ValueUtil.recordTitles.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout layout = new LinearLayout(RecordActivity.this);
			layout.setBackgroundColor(RecordActivity.this.getResources()
					.getColor(R.color.i_white));
			layout.setOrientation(LinearLayout.HORIZONTAL);
			textParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
			layout.setLayoutParams(new ListView.LayoutParams(ListView.LayoutParams.MATCH_PARENT,
					ListView.LayoutParams.WRAP_CONTENT));
			TextView desc_title = new TextView(RecordActivity.this);
			desc_title.setPadding(15, 15, 15, 15);
			desc_title.setTextSize(20);
			desc_title.setTextColor(RecordActivity.this.getResources()
					.getColor(R.color.i_black));
			desc_title.setText(ValueUtil.recordTitles[position]);
			desc_title.setGravity(Gravity.LEFT);
			layout.addView(desc_title, textParams);

			desc_check = new TextView(RecordActivity.this);
			desc_check.setPadding(20, 20, 20, 20);
			desc_check.setTextSize(20);
			desc_check.setTextColor(RecordActivity.this.getResources()
					.getColor(R.color.i_red));
			desc_check.setText("√");
			desc_check.setGravity(Gravity.RIGHT);
			layout.addView(desc_check, textParams);
			if (position == 1) {
				desc_check.setVisibility(View.VISIBLE);
			} else {
				desc_check.setVisibility(View.INVISIBLE);
			}
			layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					desc_check.setVisibility(View.INVISIBLE);
				}
			});

			convertView = layout;
			return convertView;
		}

	}

	protected void showAliert() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setMessage("信息获取失败，请检查网络！");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.record_tv:
			if (record_titles.getVisibility() == View.GONE) {
				// TODO:旋转动画 顺时针90°
				// record_hint
				record_titles.setVisibility(View.VISIBLE);
			} else {
				// TODO:旋转动画 逆时针90°
				// record_hint
				record_titles.setVisibility(View.GONE);
			}
			break;
		// case R.id.rev:
		//
		// break;
		case R.id.record_back:
			finish();
			break;
		default:
			break;
		}

	}
}
