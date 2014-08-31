package com.allen.itouzi.receiver;

import com.allen.itouzi.activity.LockActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	private static final String TAG = "ScreenReceiver";
	String action =null;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "收到广播了！");
			action = intent.getAction();
			if(action.equals(Intent.ACTION_SCREEN_ON)){
				Log.i(TAG, "ACTION_SCREEN_ON");
				context.startActivity(new Intent(context, LockActivity.class));
			}else if(action.equals(Intent.ACTION_SCREEN_OFF)){
				Log.i(TAG, "ACTION_SCREEN_OFF");
			}
	}

}
