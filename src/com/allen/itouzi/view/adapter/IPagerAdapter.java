package com.allen.itouzi.view.adapter;

import java.util.ArrayList;

import com.allen.itouzi.view.RefreshListview;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class IPagerAdapter extends PagerAdapter {

	private ArrayList<View> listViews;

	public IPagerAdapter(ArrayList<View> listViews) {
		this.listViews = listViews;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(listViews.get(position));
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		container.addView(listViews.get(position), 0);
		return listViews.get(position);
	}

	@Override
	public int getCount() {
		return listViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
