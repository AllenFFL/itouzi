package com.allen.itouzi.view.adapter;

import java.util.ArrayList;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.bean.IrongzuMain;
import com.allen.itouzi.view.CircleProgressView;
import com.allen.itouzi.view.adapter.IdanbaoAdapter.ViewHolder;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class IrongzuAdapter extends BaseAdapter {
	private ArrayList<IrongzuMain> irongzuMainList;
	private ViewHolder holder;
	private Activity context;

	public IrongzuAdapter(Activity context,
			ArrayList<IrongzuMain> irongzuMainList) {
		this.context = context;
		this.irongzuMainList = irongzuMainList;
	}

	@Override
	public int getCount() {
		return 10;
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
			// TODO:暂时借用idanbao的Item 有变化再写irongzu_item
			convertView = View.inflate(context, R.layout.project_idanbao_item,
					null);
			holder.project_name = (TextView) convertView
					.findViewById(R.id.project_name);
			holder.tv_profit = (TextView) convertView
					.findViewById(R.id.tv_profit);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.project_ruantorTab = (TextView) convertView
					.findViewById(R.id.project_ruantorTab);
			holder.tv_amount = (TextView) convertView
					.findViewById(R.id.tv_amount);
			holder.project_circle_progress = (CircleProgressView) convertView
					.findViewById(R.id.project_circle_progress);
			holder.project_new_user = (ImageView) convertView
					.findViewById(R.id.project_new_user);
			convertView.setTag(holder);
		}
		// 获取bean 填充页面
		IrongzuMain irongzuMain = irongzuMainList.get(0);// TODO:测试数据 预告 预发布
		holder.project_name.setText(irongzuMain.getProject_name());
		holder.tv_profit.setText(irongzuMain.getProject_profit());
		holder.tv_time.setText(irongzuMain.getProject_time());
		holder.tv_amount.setText(irongzuMain.getProject_amount());
		if (irongzuMain.getForNewUser() == 2) {
			holder.project_new_user.setVisibility(View.VISIBLE);
		}
		// TODO:判断项目预告 4 已还款3已投满 101 预告 投资时间 100 预发布 显示倒计时 1 正在投
		switch (irongzuMain.getStatus()) {
		case 1:
			holder.project_circle_progress.setProgress(Integer
					.parseInt(irongzuMain.getProject_progress()));
			break;
		case 3:
			holder.project_circle_progress.setProgress(100);
			break;
		case 4:

			break;
		case 100:
			holder.project_circle_progress.setProgress(102);
			break;
		case 101:
			holder.project_circle_progress.setProgress(101);
			break;
		default:
			break;
		}
		// holder.project_pb.setBackground(R.drawable.abc_ab_solid_dark_holo);
		return convertView;
	}

	class ViewHolder {
		TextView project_name, tv_profit, tv_time, tv_amount,
				project_ruantorTab;
		CircleProgressView project_circle_progress;
		ImageView project_new_user;
	}

}
