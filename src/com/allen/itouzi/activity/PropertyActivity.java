package com.allen.itouzi.activity;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.allen.itouzi.R;
import com.allen.itouzi.activity.RecordActivity.ContentAdapter;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.bean.UserYearIn;
import com.allen.itouzi.bean.UserProperty;
import com.allen.itouzi.bean.UserTrade;
import com.allen.itouzi.engine.HomeEngine;
import com.allen.itouzi.engine.UserEngine;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.ValueUtil;
import com.allen.itouzi.view.adapter.HomeAdapter;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PropertyActivity extends Activity {
	private static final String TAG = "PropertyActivity";
	private ListView property_listview;
	private ImageView user_back;
	private LinearLayout property_loading;
	private UserProperty property;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		property = (UserProperty) intent.getSerializableExtra("property");
		setContentView(R.layout.user_property);
		findView();
		property_listview.setAdapter(new PropertyAdapter());
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
	private void findView() {
		property_listview = (ListView) findViewById(R.id.property_listview);
		user_back = (ImageView) findViewById(R.id.user_back);
		user_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public class PropertyAdapter extends BaseAdapter {

		private TextView property_title, property_amount;

		@Override
		public int getCount() {
			return ValueUtil.propertyTitles.length;
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
			View property_item = View.inflate(PropertyActivity.this,
					R.layout.property_item, null);
			property_title = (TextView) property_item
					.findViewById(R.id.property_title);
			property_amount = (TextView) property_item
					.findViewById(R.id.property_amount);

			property_title.setText(ValueUtil.propertyTitles[position]);
			switch (position) {
			case 0:
				property_amount.setText(property.getUse_money() + "Ԫ");
				break;
			case 1:
				property_amount.setText(property.getNo_all_sum() + "Ԫ");
				break;
			case 2:
				property_amount.setText(property.getNo_all_benjin_sum() + "Ԫ");
				break;
			case 3:
				property_amount.setText(property.getNo_use_money() + "Ԫ");
				break;
			case 4:
				property_amount.setText(property.getCouponAmount() + "Ԫ");
				break;
			case 5:
				property_amount.setText(DataFormat.getDoubleThousand(property
						.getSum()) + "Ԫ");
				break;
			default:
				break;
			}

			return property_item;
		}

	}
}
