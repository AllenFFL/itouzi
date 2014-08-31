package com.allen.itouzi.view;

import java.util.LinkedList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ViewpagerAdapter extends PagerAdapter {
	LinkedList<ImageView> images;

	public ViewpagerAdapter(LinkedList<ImageView> images) {
		this.images = images;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return images.size();
	}

	@Override
	public Object instantiateItem(View arg0, final int arg1) {
		/*
		 * if (((ViewPager) arg0).getChildCount() == mListViews.size()) {
		 * ((ViewPager) arg0).removeView(mListViews.get(arg1 %
		 * mListViews.size())); } if(mListViews.get(arg1 %
		 * mListViews.size()).getParent() == null){ ((ViewPager)
		 * arg0).addView(mListViews.get(arg1 % mListViews.size()), 0); }
		 */
		// 优化之后
		if (images.get(arg1 % images.size()).getParent() == null
				&& ((ViewPager) arg0).getChildCount() < images.size()) {
			((ViewPager) arg0).addView(images.get(arg1 % images.size()), 0);
		}
		View view = images.get(arg1 % images.size());
		// 为viewpager中的图片设置监听
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 这里写监听响应的内容

			}
		});

		return images.get(arg1 % images.size());
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {

	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}
