package com.allen.itouzi.view.adapter;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.Idanbao;
import com.allen.itouzi.utils.DataFormat;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DescAdapter extends BaseAdapter {
	private Idanbao idanbao;
	private Context context;

	public DescAdapter(Context context, Idanbao idanbao) {
		this.idanbao = idanbao;
		this.context = context;
	}

	@Override
	public int getCount() {
		return 3;
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
		if (position == 0) {
			LinearLayout layoutA = new LinearLayout(context);
			layoutA.setOrientation(LinearLayout.VERTICAL);
			layoutA.addView(getTextTitle(R.color.i_gray_line2, "项目描述"));
			String useInfo = idanbao.getUse_detail();
			if (useInfo != null) {
				// 字符串过滤
				layoutA.addView(getContentTitle("资金用途"));
				layoutA.addView(getTextContent(DataFormat.WashString(useInfo)));
			}
			layoutA.addView(getLine());
			String replayment_source = idanbao.getRepayment_source();
			if (replayment_source != null) {
				layoutA.addView(getContentTitle("还款来源"));
				layoutA.addView(getTextContent(DataFormat
						.WashString(replayment_source)));
			}
			convertView = layoutA;
			return convertView;
		} else if (position == 1) {
			LinearLayout layoutB = new LinearLayout(context);
			layoutB.setOrientation(LinearLayout.VERTICAL);
			layoutB.addView(getTextTitle(R.color.i_gray_line2, "企业信息"));
//			String content1 = idanbao.getContent();//TODO:经营范围字段缺失
//			if (content1 != null) {
//				layoutB.addView(getContentTitle("经营范围"));
//				layoutB.addView(getTextContent(DataFormat.WashString(content1)));
//			}
//			layoutB.addView(getLine());
			String content = idanbao.getContent();
			if (content != null) {// TODO:字段整理 内容过滤
				layoutB.addView(getContentTitle("经营状况"));
				layoutB.addView(getTextContent(DataFormat.WashString(content)));
			}
			convertView = layoutB;
			return convertView;
		} else if (position == 2) {
			LinearLayout layoutC = new LinearLayout(context);
			layoutC.setOrientation(LinearLayout.VERTICAL);
			layoutC.addView(getTextTitle(R.color.i_gray_line2, "担保信息"));
			String guarantor_summary = idanbao.getGuarantor_summary();
			if (guarantor_summary != null) {
				layoutC.addView(getContentTitle("担保公司"));
				layoutC.addView(getTextContent(DataFormat
						.WashString(guarantor_summary)));
			}
			layoutC.addView(getLine());
			if (idanbao.getGuarantor_bankbranch() != null) {// TODO:字段整理 内容过滤
				layoutC.addView(getContentTitle("共管帐号开户行"));
				TextView bankbranch = getTextContent(idanbao
						.getGuarantor_bankbranch());
				bankbranch.setGravity(Gravity.CENTER);
				layoutC.addView(bankbranch);
			}
			layoutC.addView(getLine());
			if (idanbao.getGuarantor_bankcardid() != null) {
				layoutC.addView(getContentTitle("共管帐号"));
				TextView bankcardid = getTextContent(idanbao
						.getGuarantor_bankcardid());
				bankcardid.setGravity(Gravity.CENTER);
				layoutC.addView(bankcardid);
			}
			layoutC.addView(getLine());
			String mortgage_info = idanbao.getMortgage_info();
			if (mortgage_info != null) {// TODO:字段整理 内容过滤
				layoutC.addView(getContentTitle("抵质押物信息"));
				layoutC.addView(getTextContent(DataFormat
						.WashString(mortgage_info)));
			}
			layoutC.addView(getLine());
			String risk_control = idanbao.getRisk_control();
			if (risk_control != null) {
				layoutC.addView(getContentTitle("风险控制措施"));
				layoutC.addView(getTextContent(DataFormat
						.WashString(risk_control)));
			}
			layoutC.addView(getLine());
			String guarantor_opinion = idanbao.getGuarantor_opinion();
			if (guarantor_opinion != null) {// TODO:字段整理 内容过滤
				layoutC.addView(getContentTitle("出租物评估意见"));
				layoutC.addView(getTextContent(DataFormat
						.WashString(guarantor_opinion)));
			}
			convertView = layoutC;
			return convertView;
		}
		return convertView;
	}

	private View getLine() {
		TextView line1 = new TextView(context);
		line1.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 1));
		line1.setBackgroundColor(context.getResources().getColor(
				R.color.i_gray_text));
		line1.setPadding(15, 15, 15, 15);
		return line1;
	}

	private TextView getTextTitle(int backColorID, String string) {
		TextView desc_title = new TextView(context);
		desc_title.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		desc_title.setBackgroundColor(context.getResources().getColor(
				backColorID));
		desc_title.setGravity(Gravity.CENTER_HORIZONTAL);
		desc_title.setPadding(15, 15, 15, 15);
		desc_title.setTextSize(17);
		desc_title.setTextColor(context.getResources()
				.getColor(R.color.i_black));
		desc_title.setText(string);
		return desc_title;
	}

	private TextView getContentTitle(String string) {
		TextView desc_title = new TextView(context);
		desc_title.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		desc_title.setBackgroundColor(context.getResources().getColor(
				R.color.i_white));
		desc_title.setGravity(Gravity.CENTER_HORIZONTAL);
		desc_title.setPadding(15, 15, 15, 15);
		desc_title.setTextSize(17);
		desc_title.setTextColor(context.getResources().getColor(
				R.color.i_gray_text));
		desc_title.setText(string);
		return desc_title;
	}

	private TextView getTextContent(String string) {
		TextView desc_content = new TextView(context);
		desc_content.setLayoutParams(new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		desc_content.setBackgroundColor(context.getResources().getColor(
				R.color.i_white));
		desc_content.setPadding(15, 15, 15, 15);
		desc_content.setTextSize(15);
		desc_content.setTextColor(context.getResources().getColor(
				R.color.i_gray_text));
		desc_content.setText(string);
		return desc_content;
	}

}
/*
 * private static final String TAG = "AppManagerAdapter";
 * 
 * @Override public int getCount() { // 用户程序个数 + 系统程序个数 return
 * userAppinfos.size() + 1 + systemAppinfos.size() + 1; }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) { AppInfo appInfo; if (position == 0) {// 显示一个textview的标签 ,
 * 告诉用户用户程序有多少个 TextView tv = new TextView(getApplicationContext());
 * tv.setBackgroundColor(Color.GRAY); tv.setTextColor(Color.WHITE);
 * tv.setText("用户程序:" + userAppinfos.size() + "个"); return tv; } else if
 * (position == (userAppinfos.size() + 1)) { TextView tv = new
 * TextView(getApplicationContext()); tv.setBackgroundColor(Color.GRAY);
 * tv.setTextColor(Color.WHITE); tv.setText("系统程序:" + systemAppinfos.size() +
 * "个"); return tv; } else if (position <= userAppinfos.size()) {// 用户程序 appInfo
 * = userAppinfos.get(position - 1); } else {// 系统程序 appInfo =
 * systemAppinfos.get(position - 1 - userAppinfos.size() - 1); } View view;
 * ViewHolder holder; if (convertView != null && convertView instanceof
 * RelativeLayout) { view = convertView; holder = (ViewHolder) view.getTag(); }
 * else { view = View.inflate(getApplicationContext(), R.layout.list_app_item,
 * null); holder = new ViewHolder(); holder.iv_icon = (ImageView) view
 * .findViewById(R.id.iv_app_icon); holder.tv_location = (TextView) view
 * .findViewById(R.id.tv_app_location); holder.tv_name = (TextView)
 * view.findViewById(R.id.tv_app_name); view.setTag(holder); }
 * 
 * holder.iv_icon.setImageDrawable(appInfo.getIcon());
 * holder.tv_name.setText(appInfo.getAppName()); if (appInfo.isInRom()) {
 * holder.tv_location.setText("手机内存"); } else {
 * holder.tv_location.setText("外部存储"); } return view; }
 * 
 * @Override public Object getItem(int position) { AppInfo appInfo; if (position
 * == 0) {// 显示一个textview的标签 , 告诉用户用户程序有多少个 return null; } else if (position ==
 * (userAppinfos.size() + 1)) { return null; } else if (position <=
 * userAppinfos.size()) {// 用户程序 appInfo = userAppinfos.get(position - 1); }
 * else {// 系统程序 appInfo = systemAppinfos.get(position - 1 - userAppinfos.size()
 * - 1); } return appInfo; }
 * 
 * @Override public long getItemId(int position) { return 0; }
 * 
 * }
 * 
 * static class ViewHolder { ImageView iv_icon; TextView tv_name; TextView
 * tv_location; }
 */
