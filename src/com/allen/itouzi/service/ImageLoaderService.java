package com.allen.itouzi.service;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.net.ssl.HttpsURLConnection;

import com.allen.itouzi.net.HttpsUtil;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;

public class ImageLoaderService extends IntentService {
	public ArrayList<Bitmap> imageList = new ArrayList<Bitmap>();

	public ImageLoaderService() {
		super("ActivityPicService");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Õº∆¨œ¬‘ÿµÿ÷∑
		final String url = "https://www.itouzi.com/api/index/index";
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String result;
				try {
					result = HttpsUtil.doHttpsGet(url);
					if (result != null) {
						System.out.println("aaaaaa" + result);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("https unable to conect!"
							+ e.getLocalizedMessage());
				}
			}
		}).start();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return new MyBinder();
	}

	private class MyBinder extends Binder implements ImageLoaderT {

		@Override
		public ArrayList<Bitmap> getBitmap() {
			// TODO Auto-generated method stub
			return imageList;
		}

	}
}
