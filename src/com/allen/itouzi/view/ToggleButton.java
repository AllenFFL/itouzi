package com.allen.itouzi.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ToggleButton extends View {

	private Bitmap switch_on;
	private Bitmap switch_off;
	private Bitmap switch_slip;
	private float currentX;
	private boolean isSlipping = false;//是否处于正在滑动状态
	private boolean isSwitchOn = true;//当前开关的状态
	private boolean proSwitchStatus = true;//原先开关的状态
	private OnSwitchStatusListener switchStatusListener;//开关监听器
	private float density;
	public ToggleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ToggleButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ToggleButton(Context context) {
		super(context);
	}

	//指定开关样式
	public void setSwitchStyle(int bkgSwitchOn, int bkgSwitchOff, int btnSlip,float density) {
		//开关  开启的背景图
		switch_on = BitmapFactory.decodeResource(getResources(), bkgSwitchOn);
		//开关  关闭的背景图
		switch_off = BitmapFactory.decodeResource(getResources(), bkgSwitchOff);
		//滑动块图片
		switch_slip = BitmapFactory.decodeResource(getResources(), btnSlip);
		this.density=density;
	}

	// **(绘制控件)
	@Override
	protected void onDraw(Canvas canvas) {//画布
		// 绘制背景
		Matrix matrix = new Matrix();// 指定图像样式
		Paint paint = new Paint();// 画笔
		
		if(currentX > switch_off.getWidth()/2){
			//开启状态
			canvas.drawBitmap(switch_on, matrix, paint);
		} else {
			//关闭状态
			canvas.drawBitmap(switch_off, matrix, paint);
		}
		
		// 滑动按钮
		
		float left_slip = 200*density;
		if(isSlipping){
			//可以滑动
			if(currentX > switch_off.getWidth()){
				left_slip = switch_off.getWidth() - switch_slip.getWidth();
			} else {
				left_slip = currentX - switch_slip.getWidth()/2;
			}
		} else {
			//非滑动
			if(isSwitchOn){
				left_slip = switch_off.getWidth() - switch_slip.getWidth();
			} else {
				left_slip = 0;
			}
		}
		
		//开关边缘处理
		if(left_slip < 0){
			left_slip = 0;
		} else if(left_slip > switch_off.getWidth() - switch_slip.getWidth()){
			left_slip = switch_off.getWidth() - switch_slip.getWidth();
		}
		canvas.drawBitmap(switch_slip, left_slip, 0, paint);

		super.onDraw(canvas);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN://按下
			currentX = event.getX();
			
			isSlipping = true;
			break;
		case MotionEvent.ACTION_MOVE://移动
			
			currentX = event.getX();
			
			break;
		case MotionEvent.ACTION_UP://松开
			isSlipping = false;

			if(currentX > switch_off.getWidth()/2){
				isSwitchOn = true;
			} else {
				isSwitchOn = false;
			}
			
			//注册了开关监听器， 同时开关的状态发生了改变的时候
			if(switchStatusListener != null && proSwitchStatus != isSwitchOn ){
				switchStatusListener.onSwitch(isSwitchOn);
				proSwitchStatus = isSwitchOn;
			}
			
			break;
		}
		invalidate();//重新绘制控件
		return true;
	}

	//指定开关的默认状态
	public void setSwitchStatus(boolean status) {
		isSwitchOn = status;
	}
	
	//开关状态改变监听器
	public interface OnSwitchStatusListener{
		abstract void onSwitch(boolean state);
	}
	
	//对外提高回调方法
	public void setOnSwitchStatusListener( OnSwitchStatusListener listener){
		switchStatusListener = listener;
	}
	
}
