package com.allen.itouzi.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.InvestRecord;

public class RecordAdapter1 extends BaseAdapter {
	private List<InvestRecord> recordList;
	private Context context;
	private android.widget.LinearLayout.LayoutParams params;
	private android.widget.LinearLayout.LayoutParams textParams;
	private ViewHolder holder;

	public RecordAdapter1(Context context, List<InvestRecord> recordList) {
		this.recordList = recordList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return recordList.size() + 1;// recordList.size()+1
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
			textParams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f);
			LinearLayout layout = getLinearLayout();
			layout.setBackgroundColor(context.getResources().getColor(
					R.color.i_gray_line2));
			layout.addView(getTextTile("�û�"), textParams);
			layout.addView(getTextTile("Ͷ�ʽ��"), textParams);
			layout.addView(getTextTile("Ͷ��ʱ��"), textParams);
			return layout;
		} else {
			InvestRecord investRecord = recordList.get(position - 1);
			if (convertView == null) {
				holder = new ViewHolder();
				holder.layout = getLinearLayout();
				holder.user = getTextContent(investRecord.getUser());
				holder.amount = getTextContent(investRecord.getAmount());
				holder.time = getTextContent(investRecord.getTime());
				holder.layout.addView(holder.user, textParams);
				holder.layout.addView(holder.amount, textParams);
				holder.layout.addView(holder.time, textParams);
				convertView = holder.layout;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				holder.user.setText(investRecord.getUser());
				holder.user.setText(investRecord.getAmount());
				holder.user.setText(investRecord.getTime());
			}
			return convertView;
		}
	}

	static class ViewHolder {
		LinearLayout layout;
		TextView user, amount, time;
	}

	private LinearLayout getLinearLayout() {
		LinearLayout layout = new LinearLayout(context);
		layout.setBackgroundColor(context.getResources().getColor(
				R.color.i_white));
		layout.setOrientation(LinearLayout.HORIZONTAL);
		return layout;
	}

	private TextView getTextTile(String content) {
		TextView desc_title = new TextView(context);
		desc_title.setPadding(20, 20, 20, 20);
		desc_title.setTextSize(15);
		desc_title.setTextColor(context.getResources().getColor(
				R.color.i_gray_text));
		desc_title.setText(content);
		return desc_title;
	}

	private TextView getTextContent(String content) {
		TextView desc_title = new TextView(context);
		desc_title.setPadding(15, 15, 15, 15);
		desc_title.setTextSize(15);
		desc_title.setTextColor(context.getResources().getColor(
				R.color.i_gray_text));
		desc_title.setText(content);
		return desc_title;
	}

	private TextView getTextTime(String content) {
		TextView desc_title = new TextView(context);
		desc_title.setPadding(15, 15, 15, 15);
		desc_title.setTextSize(15);
		desc_title.setTextColor(context.getResources().getColor(
				R.color.i_gray_text));
		desc_title.setText(content);
		return desc_title;
	}
}

// layout.addView(desc_title);
// if(idanbao.getUse_detail()!=null){
// TextView desc_title1=new TextView(context);
// desc_title1.setLayoutParams(params);
// desc_title1.setBackgroundColor(context.getResources().getColor(R.color.i_white));
// desc_title1.setPadding(10, 10, 10, 10);
// desc_title1.setTextSize(20);
// desc_title1.setTextColor(context.getResources().getColor(R.color.i_black));
// desc_title1.setText("�ʽ���;");
// layout.addView(desc_title1);
//
// TextView desc_content1=new TextView(context);
// desc_content1.setLayoutParams(params);
// desc_content1.setBackgroundColor(context.getResources().getColor(R.color.i_white));
// desc_content1.setPadding(10, 10, 10, 10);
// desc_content1.setTextSize(15);
// desc_content1.setTextColor(context.getResources().getColor(R.color.i_black));
// desc_content1.setText(idanbao.getUse_detail());
// layout.addView(desc_content1);
// }
//
// TextView desc_title2=new TextView(context);
// desc_title2.setLayoutParams(params);
// desc_title2.setBackgroundColor(context.getResources().getColor(R.color.i_white));
// desc_title2.setPadding(10, 10, 10, 10);
// desc_title2.setTextSize(20);
// desc_title2.setTextColor(context.getResources().getColor(R.color.i_black));
// desc_title2.setText("������Դ");
// layout.addView(desc_title2);
//
// TextView desc_content2=new TextView(context);
// desc_content2.setLayoutParams(params);
// desc_content2.setBackgroundColor(context.getResources().getColor(R.color.i_white));
// desc_content2.setPadding(10, 10, 10, 10);
// desc_content2.setTextSize(15);
// desc_content2.setTextColor(context.getResources().getColor(R.color.i_black));
// desc_content2.setText(idanbao.getMortgage_info());
// layout.addView(desc_content2);
//
// TextView line1=new TextView(context);
// line1.setLayoutParams(new
// LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,1));
// line1.setBackgroundColor(context.getResources().getColor(R.color.i_gray_line3));
// line1.setPadding(10, 10, 10, 10);
// layout.addView(line1);
// convertView=layout;

/*
 * 
 * <TextView android:id="@+id/desc_title" android:layout_width="match_parent"
 * android:layout_height="wrap_content" android:background="@color/i_gray_back"
 * android:padding="5dp" android:textSize="20sp"
 * android:textColor="@color/i_black" android:text="��Ŀ����"/> <TextView
 * android:id="@+id/desc_title1" android:layout_width="match_parent"
 * android:layout_height="wrap_content" android:background="@color/i_white"
 * android:padding="5dp" android:textSize="20sp"
 * android:textColor="@color/i_black" android:text="�ʽ���;"/>
 * 
 * <TextView android:id="@+id/desc_content1" android:layout_width="match_parent"
 * android:layout_height="wrap_content" android:background="@color/i_white"
 * android:padding="5dp" android:textSize="15sp" android:text="ҵ��Ա��ҵ����Ϊһλҵ��Ա"/>
 * <View android:layout_width="match_parent" android:layout_height="0.5dp"
 * android:background="@color/i_gray_line3"/> private static final String TAG =
 * "AppManagerAdapter";
 * 
 * @Override public int getCount() { // �û�������� + ϵͳ������� return
 * userAppinfos.size() + 1 + systemAppinfos.size() + 1; }
 * 
 * @Override public View getView(int position, View convertView, ViewGroup
 * parent) { AppInfo appInfo; if (position == 0) {// ��ʾһ��textview�ı�ǩ ,
 * �����û��û������ж��ٸ� TextView tv = new TextView(getApplicationContext());
 * tv.setBackgroundColor(Color.GRAY); tv.setTextColor(Color.WHITE);
 * tv.setText("�û�����:" + userAppinfos.size() + "��"); return tv; } else if
 * (position == (userAppinfos.size() + 1)) { TextView tv = new
 * TextView(getApplicationContext()); tv.setBackgroundColor(Color.GRAY);
 * tv.setTextColor(Color.WHITE); tv.setText("ϵͳ����:" + systemAppinfos.size() +
 * "��"); return tv; } else if (position <= userAppinfos.size()) {// �û����� appInfo
 * = userAppinfos.get(position - 1); } else {// ϵͳ���� appInfo =
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
 * holder.tv_location.setText("�ֻ��ڴ�"); } else {
 * holder.tv_location.setText("�ⲿ�洢"); } return view; }
 * 
 * @Override public Object getItem(int position) { AppInfo appInfo; if (position
 * == 0) {// ��ʾһ��textview�ı�ǩ , �����û��û������ж��ٸ� return null; } else if (position ==
 * (userAppinfos.size() + 1)) { return null; } else if (position <=
 * userAppinfos.size()) {// �û����� appInfo = userAppinfos.get(position - 1); }
 * else {// ϵͳ���� appInfo = systemAppinfos.get(position - 1 - userAppinfos.size()
 * - 1); } return appInfo; }
 * 
 * @Override public long getItemId(int position) { return 0; }
 * 
 * }
 * 
 * static class ViewHolder { ImageView iv_icon; TextView tv_name; TextView
 * tv_location; }
 */
