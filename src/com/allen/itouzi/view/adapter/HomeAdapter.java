package com.allen.itouzi.view.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.activity.ProjectDetailActivity;
import com.allen.itouzi.bean.HomeImageMap;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.engine.ImageLoader;
import com.allen.itouzi.utils.DataFormat;
import com.allen.itouzi.utils.DateUtil;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.AutoScrollViewPager;

public class HomeAdapter extends BaseAdapter implements OnClickListener {
	public static final String TAG = "HomeAdapter";
	private Activity context;
	private List<IdanbaoMain> idanbaoList;
	private List<HomeImageMap> imageList;
	private int size;
	private ImagePagerAdapter pagerAdapter=null;
	private static ArrayList<View> dotsList = new ArrayList<View>();
	private TextView home_project_name, tv_guarantor, tv_profit, tv_time,
			tv_amount, tv_rate;
	private ImageView iv_newuser, iv_reco;
	private ProgressBar pb_home;
	private Button btn_invest;
	private LinearLayout ll_dots;
	private AutoScrollViewPager view_pager;
	private IdanbaoMain idanbaoMain;
	private float density;

	public HomeAdapter(Activity context, List<HomeImageMap> imageList,
			List<IdanbaoMain> idanbaoList) {
		this.context = context;
		DisplayMetrics dm =DeviceInfoUtil.getMetrics(context);
		density = dm.density;
		this.size = imageList.size();
		this.imageList = imageList;
		this.idanbaoList = idanbaoList;
		if(pagerAdapter==null)
		pagerAdapter = new ImagePagerAdapter(context, imageList);
	}

	@Override
	public int getCount() {
		return 2;
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
		// 设置轮播图
		if (position == 0) {
			convertView = View.inflate(context, R.layout.home_viewpager, null);
			ll_dots = (LinearLayout) convertView.findViewById(R.id.ll_dots);

			for (int i = 0; i < size; i++) {
				View view = new View(context);
				view.setBackgroundResource(R.drawable.dot_normal);
				dotsList.add(view);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						(int)((float)8*density), (int)((float)8*density));
				lp.setMargins(0, 0, 10, 10);
				ll_dots.addView(view,lp);
			}
				//大图
			view_pager = (AutoScrollViewPager) convertView
					.findViewById(R.id.home_view_pager);
			view_pager.setLayoutParams(new FrameLayout.LayoutParams
					(LayoutParams.MATCH_PARENT, 
							ImageLoader.getInstance(context).getHeight()));
			view_pager.setAdapter(pagerAdapter.setInfiniteLoop(true));
			view_pager.setOnPageChangeListener(new IOnPageChangeListener());
			view_pager.setInterval(3000);
			view_pager.setCurrentItem(0);
			view_pager.startAutoScroll();
			return convertView;
			// 设置推荐项目
		} else if (position == 1) {
			convertView = View.inflate(context, R.layout.home_project, null);
			home_project_name = (TextView) convertView
					.findViewById(R.id.home_project_name);
			tv_guarantor = (TextView) convertView
					.findViewById(R.id.tv_guarantor);
			tv_profit = (TextView) convertView.findViewById(R.id.tv_profit);
			tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
			tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
			iv_newuser = (ImageView) convertView.findViewById(R.id.iv_newuser);
			iv_reco = (ImageView) convertView.findViewById(R.id.iv_reco);
			pb_home = (ProgressBar) convertView.findViewById(R.id.pb_home);
			btn_invest = (Button) convertView.findViewById(R.id.btn_invest);

			idanbaoMain = idanbaoList.get(0);
			home_project_name.setText(idanbaoMain.getProject_name());
			tv_guarantor.setText(idanbaoMain.getProject_gruantor());
			tv_profit.setText(idanbaoMain.getProject_profit());
			tv_time.setText(idanbaoMain.getProject_time());
			tv_amount.setText(idanbaoMain.getProject_amount() / 10000 + "");
			tv_rate.setText((int) Float.parseFloat(idanbaoMain
					.getProject_progress()) + "%");
			pb_home.setProgress((int) Float.parseFloat(idanbaoMain
					.getProject_progress()));
			if (idanbaoMain.getForNewUser() == 2) {
				iv_newuser.setVisibility(View.VISIBLE);
				iv_reco.setVisibility(View.INVISIBLE);
			} else if (idanbaoMain.getForNewUser() == 1) {
				iv_reco.setVisibility(View.VISIBLE);
				iv_newuser.setVisibility(View.INVISIBLE);
			}
			btn_invest.setOnClickListener(this);
			convertView.setOnClickListener(this);
			return convertView;
		} else {
			return null;
		}
	}

	public class IOnPageChangeListener implements OnPageChangeListener {
		private int currentPosition, oldPosition = 0;

		@Override
		public void onPageSelected(int position) {
			dotsList.get(oldPosition % size).setBackgroundResource(
					R.drawable.dot_normal);
			dotsList.get(position % size).setBackgroundResource(
					R.drawable.dot_focused);
			oldPosition = position;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, ProjectDetailActivity.class);
		intent.putExtra("idanbao_id", idanbaoMain.getId());
		context.startActivity(intent);

	}
}
