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
	private boolean isSlipping = false;//�Ƿ������ڻ���״̬
	private boolean isSwitchOn = true;//��ǰ���ص�״̬
	private boolean proSwitchStatus = true;//ԭ�ȿ��ص�״̬
	private OnSwitchStatusListener switchStatusListener;//���ؼ�����
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

	//ָ��������ʽ
	public void setSwitchStyle(int bkgSwitchOn, int bkgSwitchOff, int btnSlip,float density) {
		//����  �����ı���ͼ
		switch_on = BitmapFactory.decodeResource(getResources(), bkgSwitchOn);
		//����  �رյı���ͼ
		switch_off = BitmapFactory.decodeResource(getResources(), bkgSwitchOff);
		//������ͼƬ
		switch_slip = BitmapFactory.decodeResource(getResources(), btnSlip);
		this.density=density;
	}

	// **(���ƿؼ�)
	@Override
	protected void onDraw(Canvas canvas) {//����
		// ���Ʊ���
		Matrix matrix = new Matrix();// ָ��ͼ����ʽ
		Paint paint = new Paint();// ����
		
		if(currentX > switch_off.getWidth()/2){
			//����״̬
			canvas.drawBitmap(switch_on, matrix, paint);
		} else {
			//�ر�״̬
			canvas.drawBitmap(switch_off, matrix, paint);
		}
		
		// ������ť
		
		float left_slip = 200*density;
		if(isSlipping){
			//���Ի���
			if(currentX > switch_off.getWidth()){
				left_slip = switch_off.getWidth() - switch_slip.getWidth();
			} else {
				left_slip = currentX - switch_slip.getWidth()/2;
			}
		} else {
			//�ǻ���
			if(isSwitchOn){
				left_slip = switch_off.getWidth() - switch_slip.getWidth();
			} else {
				left_slip = 0;
			}
		}
		
		//���ر�Ե����
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
		case MotionEvent.ACTION_DOWN://����
			currentX = event.getX();
			
			isSlipping = true;
			break;
		case MotionEvent.ACTION_MOVE://�ƶ�
			
			currentX = event.getX();
			
			break;
		case MotionEvent.ACTION_UP://�ɿ�
			isSlipping = false;

			if(currentX > switch_off.getWidth()/2){
				isSwitchOn = true;
			} else {
				isSwitchOn = false;
			}
			
			//ע���˿��ؼ������� ͬʱ���ص�״̬�����˸ı��ʱ��
			if(switchStatusListener != null && proSwitchStatus != isSwitchOn ){
				switchStatusListener.onSwitch(isSwitchOn);
				proSwitchStatus = isSwitchOn;
			}
			
			break;
		}
		invalidate();//���»��ƿؼ�
		return true;
	}

	//ָ�����ص�Ĭ��״̬
	public void setSwitchStatus(boolean status) {
		isSwitchOn = status;
	}
	
	//����״̬�ı������
	public interface OnSwitchStatusListener{
		abstract void onSwitch(boolean state);
	}
	
	//������߻ص�����
	public void setOnSwitchStatusListener( OnSwitchStatusListener listener){
		switchStatusListener = listener;
	}
	
}
