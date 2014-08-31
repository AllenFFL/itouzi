package com.allen.itouzi.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.activity.LockActivity;
import com.allen.itouzi.activity.RegisterActivity;

public class LogUtil {
	public static void showAliert(Context context,String text) {
		final AlertDialog builder = new Builder(context).create();
		builder.show();
		builder.setCancelable(false);
		builder.getWindow().setContentView(R.layout.custom_dialog);
		TextView dialog_text =(TextView) builder.getWindow().findViewById(R.id.dialog_text);
		dialog_text.setText(text);
		 builder.getWindow().findViewById(R.id.dialog_button).setOnClickListener(
				 new OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
						
					}
				});
	}
	public static void showAliertFinish(final Activity context,String text) {
		final AlertDialog builder = new Builder(context).create();
		builder.show();
		builder.setCancelable(false);
		builder.getWindow().setContentView(R.layout.custom_dialog);
		TextView dialog_text =(TextView) builder.getWindow().findViewById(R.id.dialog_text);
		dialog_text.setText(text);
		 builder.getWindow().findViewById(R.id.dialog_button).setOnClickListener(
				 new OnClickListener() {
					@Override
					public void onClick(View v) {
						builder.dismiss();
						FileUtil.clearToken(context);
						context.startActivity(new Intent(context, RegisterActivity.class));
						context.finish();
					}
				});
	}
	public static void showRegister(final Context context,String text) {
		final AlertDialog builder = new Builder(context).create();
		builder.show();
		builder.setCancelable(false);
		builder.getWindow().setContentView(R.layout.custom_dialog);
		TextView dialog_text =(TextView) builder.getWindow().findViewById(R.id.dialog_text);
		dialog_text.setText(text);
		 builder.getWindow().findViewById(R.id.dialog_button).setOnClickListener(
				 new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent register = new Intent(context, RegisterActivity.class);
						context.startActivity(register);
						builder.dismiss();
						
					}
				});
	}
	public static void showToast(Context context, String text) {
		Toast.makeText(context, text, 1).show();
	}
	
}
