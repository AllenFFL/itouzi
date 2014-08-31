package com.allen.itouzi;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import cn.jpush.android.api.JPushInterface;

import com.allen.itouzi.activity.MainActivity;
import com.allen.itouzi.engine.ImageLoader;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.FileUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

public class SplashActivity extends Activity {
	private static final String TAG = "SplashActivity";
	private boolean FIRST_IMAGE = true;
	private RelativeLayout splash_views;
	private GestureDetector mGesture;
	private static int[] splashIDs = { R.drawable.splash_1,
			R.drawable.splash_2, R.drawable.splash_3, R.drawable.splash_4,
			R.drawable.splash_5 };
	private static ArrayList<ImageView> splashs = new ArrayList<ImageView>();
	private static int currentID = 0;
	private LayoutParams lp;

	// 1.����splash
	// 2.Ԥ��������TODO:
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//UMENG
		MobclickAgent.openActivityDurationTrack(false); 
		MobclickAgent.updateOnlineConfig(this);
			//������
		UmengUpdateAgent.setUpdateCheckConfig(false);
		 UmengUpdateAgent.update(this);
		 //JPush
//		 JPushInterface.setDebugMode(true);
		 JPushInterface.init(getApplicationContext());
		//���ݴ洢���ֶ� �ж��Ƿ��ǵ�һ�ε�¼
		if (!FileUtil.isFirst(this)) {
			startMainActivity();
		} else {
			FileUtil.saveFirst(this);
			setContentView(R.layout.splash_home);
			lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			splash_views = (RelativeLayout) findViewById(R.id.splash_views);
			for (int i = 0; i < splashIDs.length; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setBackgroundDrawable(getResources().getDrawable(splashIDs[i]));
				splashs.add(imageView);
			}
			splash_views.addView(splashs.get(currentID),lp);
			mGesture = new GestureDetector(this, new SimpleOnGestureListener() {
				@Override
				public boolean onFling(MotionEvent e1, MotionEvent e2,
						float velocityX, float velocityY) {
					if (Math.abs(velocityX) < 100) {
						Log.i(TAG, "�ƶ���̫��,�������Ϸ�");
						return true;
					}
					if ((e2.getRawX() - e1.getRawX()) > 200) {
						showPre();
						return true;
					}
					if ((e1.getRawX() - e2.getRawX()) > 200) {
						showNext();
						return true;
					}
					return super.onFling(e1, e2, velocityX, velocityY);
				}
			});
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("SplashActivity");
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("SplashActivity");
		MobclickAgent.onPause(this);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (currentID == splashIDs.length - 1) {
			startMainActivity();
		}else{
			mGesture.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	private Bitmap decodeImage(int id) {
		//������Ļ�ߴ��������ͼƬ
		int inSampleSize =1;
		BitmapFactory.Options options = new BitmapFactory.Options();
		DisplayMetrics metrics = DeviceInfoUtil.getMetrics(this);
		if(FIRST_IMAGE){
			Log.i(TAG, " metrics.height:"+metrics.heightPixels
					+" metrics.width:"+metrics.widthPixels);
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(this.getResources(),id, options);
			inSampleSize = ImageLoader.getInSampleSize(
					options,metrics.widthPixels,metrics.heightPixels);
			FIRST_IMAGE=false;
		}
		options.inJustDecodeBounds = false;
		options.inSampleSize = inSampleSize;
		return BitmapFactory.decodeResource(this.getResources(),id, options);
	}

	private void startMainActivity() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}
	
	private void showNext() {
		Log.i(TAG, "showNext");
		currentID++;
		if (currentID < splashIDs.length) {
			splash_views.removeAllViews();
			splash_views.addView(splashs.get(currentID),lp);
		} else {
			currentID = 0;
			startMainActivity();
		}
	}

	private void showPre() {
		Log.i(TAG, "showPre");
		currentID--;
		if (currentID > -1) {
			splash_views.removeAllViews();
			splash_views.addView(splashs.get(currentID),lp);
		} else {
			currentID = 0;
		}
	}
}
