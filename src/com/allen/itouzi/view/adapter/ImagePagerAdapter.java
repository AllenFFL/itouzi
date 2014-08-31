package com.allen.itouzi.view.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.activity.HomeWebActivity;
import com.allen.itouzi.activity.MainActivity;
import com.allen.itouzi.activity.WebActivity;
import com.allen.itouzi.bean.HomeImageMap;
import com.allen.itouzi.engine.ImageLoader;
import com.allen.itouzi.net.HttpsUtil;
import com.allen.itouzi.net.NetUtil;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.RecyclingPagerAdapter;

public class ImagePagerAdapter extends RecyclingPagerAdapter {
	private static final String TAG = "ImagePagerAdapter";
	private Activity context;
	private int size;
	private boolean isInfiniteLoop;
	private List<HomeImageMap> imageList;
	private ImageView homeImage=null;
	private LayoutParams layoutParams;
	private ViewHolder viewHolder;

	public ImagePagerAdapter(Activity context,List<HomeImageMap> imageList) {
		this.context = context;
		this.imageList = imageList;
		this.size = imageList.size();
		isInfiniteLoop = false;
		layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : size;
	}
	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(int position, View view, ViewGroup container) {
//		if(view!=null){
//			viewHolder=(ViewHolder)view.getTag();
//		}else{
//			viewHolder = new ViewHolder();
//			view=new ImageView(context);
//			viewHolder.imageView=(ImageView)view;
//			view.setLayoutParams(layoutParams);
//			view.setTag(viewHolder);
//		}
		homeImage=new ImageView(context);
		homeImage.setClickable(true);
		//先设置默认的图片
		Log.i(TAG, "ImagePagerAdapter getView() setNativeBitmap");
		if(position==1){
			ImageLoader.getInstance(context).setNativeBitmap(homeImage);
		}
		final HomeImageMap imageMap = imageList.get(getPosition(position));
		ImageLoader.getInstance(context)
		.setImageView(imageMap.getImgUrl(),homeImage);
		Log.i(TAG, "ImagePagerAdapter getView() setImageView"+imageMap.getImgUrl());
		//（2）设置相关连接响应onclick事件
		homeImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!NetUtil.checkNetState(context)){
					LogUtil.showAliert(context, "网络未连接！");
				}else{
					String webURL = imageMap.getUrl();
					Intent intent = new Intent(context, HomeWebActivity.class);
					intent.putExtra("homeURL", webURL);
					context.startActivity(intent);
				}
			}
		});
		return homeImage;
	}
	class ViewHolder {
		ImageView imageView;
	}
	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
