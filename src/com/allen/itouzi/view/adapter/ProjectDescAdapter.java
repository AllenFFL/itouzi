package com.allen.itouzi.view.adapter;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.Idanbao;

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

public class ProjectDescAdapter extends BaseAdapter {
	private Idanbao idanbao;
	private Context context;

	public ProjectDescAdapter(Context context, Idanbao idanbao) {
		this.idanbao = idanbao;
		this.context = context;
	}

	@Override
	public int getCount() {
		return 1;
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
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		LinearLayout layoutA = new LinearLayout(context);
		layoutA.setOrientation(LinearLayout.VERTICAL);
		layoutA.addView(getTextTitle(R.color.i_gray_line2, "项目描述"));
		String useInfo = idanbao.getUse_detail();
		if (useInfo != null) {
			// 字符串过滤
			String replace1 = useInfo.replace("<p>", "");
			String replace2 = replace1.replace("</p>", "");
			layoutA.addView(getContentTitle("资金用途"));
			layoutA.addView(getTextContent(replace2));
		}
		layoutA.addView(getLine());
		String replayment_source = idanbao.getRepayment_source();
		if (replayment_source != null) {
			String replayment_source1 = replayment_source.replaceAll("<p>", "")
					.replaceAll("</p>", "").replaceAll("&.*?;", "");
			layoutA.addView(getContentTitle("还款来源"));
			layoutA.addView(getTextContent(replayment_source1));
		}
		layout.addView(layoutA);

		LinearLayout layoutB = new LinearLayout(context);
		layoutB.setOrientation(LinearLayout.VERTICAL);
		layoutB.addView(getTextTitle(R.color.i_gray_line2, "企业信息"));
		String content1 = idanbao.getContent();
		String content2 = content1.replaceAll("<p>", "").replaceAll("</p>", "")
				.replaceAll("\\s*", "");
		if (content1 != null) {
			layoutB.addView(getContentTitle("经营范围"));
			layoutB.addView(getTextContent(content2));
		}
		layoutB.addView(getLine());
		String content = idanbao.getContent();
		if (content != null) {// TODO:字段整理 内容过滤
			String content4 = content.replaceAll("<p>", "").replaceAll("</p>",
					"");
			layoutB.addView(getContentTitle("经营状况"));
			layoutB.addView(getTextContent(content4));
		}
		layout.addView(layoutB);

		LinearLayout layoutC = new LinearLayout(context);
		layoutC.setOrientation(LinearLayout.VERTICAL);
		layoutC.addView(getTextTitle(R.color.i_gray_line2, "担保信息"));
		String guarantor_summary = idanbao.getGuarantor_summary();
		if (guarantor_summary != null) {
			String guarantor_summary1 = guarantor_summary.replaceAll("<p>", "")
					.replaceAll("</p>", "").replaceAll("<br>", "");
			String guarantor_summary2 = guarantor_summary1.replaceAll("&.*?;",
					"");
			layoutC.addView(getContentTitle("担保公司"));
			layoutC.addView(getTextContent(guarantor_summary2));
		}
		layoutC.addView(getLine());
		if (idanbao.getGuarantor_bankbranch() != null) {// TODO:字段整理 内容过滤
			layoutC.addView(getContentTitle("共管帐号开户行"));
			layoutC.addView(getTextContent(idanbao.getGuarantor_bankbranch()));
		}
		layoutC.addView(getLine());
		if (idanbao.getGuarantor_bankcardid() != null) {
			layoutC.addView(getContentTitle("共管帐号"));
			layoutC.addView(getTextContent(idanbao.getGuarantor_bankcardid()));
		}
		layoutC.addView(getLine());
		String mortgage_info = idanbao.getMortgage_info();
		if (mortgage_info != null) {// TODO:字段整理 内容过滤
			String mortgage_info1 = mortgage_info.replaceAll("<p>", "")
					.replaceAll("</p>", "");
			layoutC.addView(getContentTitle("抵质押物信息"));
			layoutC.addView(getTextContent(mortgage_info1));
		}
		layoutC.addView(getLine());
		String risk_control = idanbao.getRisk_control();
		if (risk_control != null) {
			String risk_control1 = risk_control.replaceAll("<p>", "")
					.replaceAll("</p>", "");
			layoutC.addView(getContentTitle("风险控制措施"));
			layoutC.addView(getTextContent(risk_control1));
		}
		layoutC.addView(getLine());
		String guarantor_opinion = idanbao.getGuarantor_opinion();
		if (guarantor_opinion != null) {// TODO:字段整理 内容过滤
			String guarantor_opinion1 = guarantor_opinion.replaceAll("<p>", "")
					.replaceAll("</p>", "");
			layoutC.addView(getContentTitle("出租物评估意见"));
			layoutC.addView(getTextContent(guarantor_opinion1));
		}
		layout.addView(layoutC);
		convertView = layout;
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
		desc_title.setPadding(20, 20, 20, 20);
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
		desc_title.setPadding(20, 20, 20, 20);
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
