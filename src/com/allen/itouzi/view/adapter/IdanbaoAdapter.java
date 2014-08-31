package com.allen.itouzi.view.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.activity.ProjectDetailActivity;
import com.allen.itouzi.activity.ProjectFragment;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.view.CircleProgressView;

/**
 * public IdanbaoMain(String id,String project_name,String
 * project_gruantee,String project_gruantee_tab, String project_profit,String
 * project_time,int project_amount,String lowest_account,long
 * project_investdate, String project_paydate,String project_progress,int
 * account_remain,int account_yes,int isForNewUser,int status){
 * 
 * @author allen
 * 
 */
public class IdanbaoAdapter extends BaseAdapter {
	private static final String TAG = "IdanbaoAdapter";
	private ViewHolder holder;
	private ProjectFragment pf;
	private LruCache<Integer, ArrayList<IdanbaoMain>> idanbaoCache;
	private ExecutorService fixedThreadPool;

	public IdanbaoAdapter(ProjectFragment pf,
			LruCache<Integer, ArrayList<IdanbaoMain>> idanbaoCache) {
		this.pf = pf;
		this.idanbaoCache = idanbaoCache;
	}

	@Override
	public int getCount() {
		int count=(ProjectFragment.IDANBAO_P-1)*ProjectFragment.IDANBAO_S
				+ProjectFragment.IDANBAO_L;
		return count;
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
			convertView = View.inflate(pf.getActivity(), R.layout.project_idanbao_item,
					null);
			holder.project_name = (TextView) convertView
					.findViewById(R.id.project_name);
			holder.tv_profit = (TextView) convertView
					.findViewById(R.id.tv_profit);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_amount = (TextView) convertView
					.findViewById(R.id.tv_amount);
			holder.project_ruantorTab = (TextView) convertView
					.findViewById(R.id.project_ruantorTab);
			holder.project_circle_progress = (CircleProgressView) convertView
					.findViewById(R.id.project_circle_progress);
			holder.project_new_user = (ImageView) convertView
					.findViewById(R.id.project_new_user);
			convertView.setTag(holder);
		}
		// 获取bean 第几页的第几个bean 填充页面  
		int PAGE = position/ ProjectFragment.IDANBAO_S+1;
		int iposition=position%(ProjectFragment.IDANBAO_S);
		ArrayList<IdanbaoMain> idanbaoMainList =null;
		if(idanbaoCache.get(PAGE)!=null){
			idanbaoMainList = idanbaoCache.get(PAGE);
			initHolder(idanbaoMainList,iposition,holder);
		}else{
			//如果回收了，重新下载,显示本条目 TODO:
			getIdanbaos(PAGE,iposition,holder);
		}
		return convertView;
	}
	private void initHolder(ArrayList<IdanbaoMain> idanbaoMainList,
			int iposition, ViewHolder holder2) {
		IdanbaoMain idanbaoMain = idanbaoMainList.get(iposition);
//		Log.i(TAG, "PAGE:"+PAGE+" position:"+position+
//				" index"+position %(ProjectFragment.IDANBAO_S));
		holder2.project_name.setText(idanbaoMain.getProject_name());
		holder2.tv_profit.setText(idanbaoMain.getProject_profit());
		holder2.tv_time.setText(idanbaoMain.getProject_time());
		holder2.tv_amount.setText(idanbaoMain.getProject_amount() / 10000 + "");
		holder2.project_ruantorTab
				.setText(idanbaoMain.getProject_gruantor_tab());
		if (idanbaoMain.getForNewUser() == 2) {
			holder2.project_new_user.setVisibility(View.VISIBLE);
		}
		// TODO:判断项目预告 4 已还款3已投满 101 预告 投资时间 100 预发布 显示倒计时 1 正在投
		switch (idanbaoMain.getStatus()) {
		case 1:
			holder2.project_circle_progress.setProgress((int) Float
					.parseFloat(idanbaoMain.getProject_progress()));
			break;
		case 3:
			holder2.project_circle_progress.setProgress(100);
			break;
		case 4:
			holder2.project_circle_progress.setProgress(104);
			break;
		case 100:
			holder2.project_circle_progress.setProgress(102);
			break;
		case 101:
			holder2.project_circle_progress.setProgress(101);
			break;
		default:
			break;
		}
		
	}

	public void getIdanbaos(final int page,final int iposition,final ViewHolder reholder) {
		//下载 添加cache 更新UI
		if(fixedThreadPool==null)
		fixedThreadPool = Executors.newFixedThreadPool(3);
		fixedThreadPool.execute(new Runnable() {
			private ArrayList<IdanbaoMain> reidanbaoMainList;

			@Override
			public void run() {
				 reidanbaoMainList = ProjectsEngine.getInstance().getIdanbaoList(
						 pf.getActivity(),page);
				 pf.getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						if(reidanbaoMainList!=null){
							pf.putIdanbaoCache(page, reidanbaoMainList);
							initHolder(reidanbaoMainList, iposition, reholder);
						}
					}
				});
			}
		});
		
	}
	public void shutThreads(){
		if(null!=fixedThreadPool)
			fixedThreadPool.shutdown();
	}

	class ViewHolder {
		TextView project_name, tv_profit, tv_time, tv_amount,
				project_ruantorTab;
		CircleProgressView project_circle_progress;
		ImageView project_new_user;
	}

}
