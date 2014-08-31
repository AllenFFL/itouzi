package com.allen.itouzi.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.InvestRecord;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DateUtil;

public class RecordAdapter extends BaseAdapter {
	private List<InvestRecord> recordList;
	private Context context;
	private android.widget.LinearLayout.LayoutParams params;
	private android.widget.LinearLayout.LayoutParams textParams;
	private ViewHolder holder;

	public RecordAdapter(Context context, List<InvestRecord> recordList) {
		this.recordList = recordList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return recordList.size();
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
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context,
					R.layout.project_more_record_item, null);
			holder.user = (TextView) convertView.findViewById(R.id.record_user);
			holder.amount = (TextView) convertView
					.findViewById(R.id.record_amount);
			holder.time = (TextView) convertView.findViewById(R.id.record_time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		InvestRecord investRecord = recordList.get(position);
		// TODO:字符串匹配 时间?
		String user = investRecord.getUser();
		String userStart = user.substring(0, 1);
		String userReplace = user.substring(2);
		String userName = userReplace.replaceAll(".", "*");
		holder.user.setText(userStart + userName);
		holder.amount.setText((int) Float.parseFloat(investRecord.getAmount())
				+ "元");
		String time = investRecord.getTime();
		// String[] splits = time.split("-");
		// String day=Dataformat.transInte(Integer.parseInt(splits[1]));
		// if(day!=null){
		// holder.time.setText("周"+day+" "+DateUtil.getStringToHHMM(splits[3]+"000"));
		// }else{
		// holder.time.setText("     "+DateUtil.getStringToHHMM(splits[3]));
		// }
		holder.time
				.setText(DateUtil.getDateToHHMM(Long.parseLong(time + "000")));
		return convertView;
	}

	class ViewHolder {
		RelativeLayout layout;
		TextView user, amount, time;
	}
}