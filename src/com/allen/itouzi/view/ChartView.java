package com.allen.itouzi.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.UserMonthIn;
import com.allen.itouzi.bean.UserYearIn;
import com.allen.itouzi.utils.DataFormat;

/**
 * 图标
 * 
 * @author allen
 * 
 */
public class ChartView extends View {
	private static final String TAG = "ChartView";
	public int XPoint; // 原点的X坐标
	public int YPoint; // 原点的Y坐标
	public int XScale; // X的刻度长度
	public int YScale; // Y的刻度长度
	public int XLength; // X轴的长度
	public int YLength; // Y轴的长度
	public String[] XLabel; // X的刻度
	public String[] YLabel; // Y的刻度
	public float[] Data; // 数据
	public String sumIncome; // 显示的总收益
	private int height;
	private int width;
	private float maxY;
	private float perY;
	private float density;
	private Context context;
	private Paint paintY;
	private Paint paintW;
	private Paint paintR;
	/**
	 *  true 为Year false为month
	 */
	private boolean flag;
	private boolean isChartFragment=true;
	public ChartView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
	}

	public void setInfo(Handler pHandler, float density, int width, int height,
			ArrayList<? extends Object> incomeList) {
		this.density = density;
		this.height=height;
		XPoint = (int) (50f * density);
		YPoint = height - (int) (155f * density);
		XLength = width - (int) (70f * density);
		YLength = YPoint - (int) (5f * density);
		if (incomeList.get(0) instanceof UserYearIn) {
			flag = true;
		} else if (incomeList.get(0) instanceof UserMonthIn) {
			flag = false;
		}
		if (flag) {
			XScale = XLength / 13;
		} else {
			XScale = XLength / (incomeList.size() - 1);
		}
		YScale = YLength / 5;

		// x轴 数据
		XLabel = new String[incomeList.size()];
		Data = new float[incomeList.size()];
		float max = -1, sumAmount = 0;
		if (flag) {// year
			for (int i = 0; i < incomeList.size(); i++) {
				UserYearIn income = (UserYearIn) incomeList.get(i);
				String mouth = income.getMonth();
				XLabel[i] = mouth.substring(mouth.indexOf("/") + 1,
						mouth.lastIndexOf("/"));
				float amount = Float.parseFloat(income.getAmount());
				Data[i] = amount;
				if (max == -1) {
					max = amount;
				} else {
					if (max < amount) {
						max = amount;
					}
				}
				sumAmount = (sumAmount / 2 + amount / 2) * 2;
			}

		} else {// month
			for (int i = 0; i < incomeList.size() - 1; i++) {
				UserMonthIn income = (UserMonthIn) incomeList.get(i);
				XLabel[i] = income.getDay();
				float amount = income.getAmount();
				Data[i] = amount;
				if (max == -1) {
					max = amount;
				} else {
					if (max < amount) {
						max = amount;
					}
				}
				sumAmount = (sumAmount / 2 + amount / 2) * 2;
			}
		}
		// 总收益 values之和
		if(pHandler!=null){
			isChartFragment=false;
			sumIncome = DataFormat.get2Float(sumAmount + "");
			Message message4 = pHandler.obtainMessage();
			message4.what = 4;
			message4.obj = sumIncome;
			pHandler.sendMessage(message4);
		}
		// Y轴
		maxY = max + max / 4f;
		perY = (float) YLength / maxY;
		YLabel = new String[5];
		for (int i = 0; i < 5; i++) {
			if (maxY < 10) {
				YLabel[i] = (float) (Math.round((float) i * (maxY / 5f) * 10))
						/ 10 + "";
			} else {
				YLabel[i] = (int) (i * (maxY / 5f)) + "";
			}
			Log.i(TAG, "YLabel[i]:" + YLabel[i] + "  maxY:" + maxY);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 黄色画笔
		paintY = new Paint();
		paintY.setStyle(Paint.Style.FILL);
		paintY.setAntiAlias(true);// 去锯齿
		paintY.setStrokeWidth(2);
		paintY.setColor(Color.YELLOW);
		paintY.setTextSize((15f * density));
		// 白色画笔
		paintW = new Paint();
		paintW.setStyle(Paint.Style.FILL);
		paintW.setAntiAlias(true);
		paintW.setStrokeWidth(5);
		paintW.setTypeface(Typeface.DEFAULT_BOLD);
		paintW.setColor(Color.WHITE);
		paintW.setTextSize((50f * density)); // 设置轴文字大小
		// 小圆圈画笔
		paintR = new Paint();
		paintR.setStyle(Paint.Style.FILL);
		paintR.setAntiAlias(true);
		paintR.setStrokeWidth(5);
		paintR.setColor(Color.WHITE);

		canvas.drawColor(context.getResources().getColor(R.color.dark_red));// 设置背景颜色

		// 设置Y轴
		canvas.drawLine(XPoint, YPoint - YLength, XPoint, YPoint, paintY); // 轴线
		for (int i = 1; i < 5; i++) {
			canvas.drawLine(XPoint, YPoint - i * YScale, XPoint
					+ (int) (10f * density), YPoint - i * YScale, paintY); // 刻度
			canvas.drawText(YLabel[i], (XPoint - (int) (40f * density)),
					(YPoint - i * YScale + (int) (5f * density)), paintY); // 文字
		}
		canvas.drawLine(XPoint, YPoint - YLength,
				XPoint - (int) (5f * density), YPoint - YLength
						+ (int) (10f * density), paintY); // 箭头
		canvas.drawLine(XPoint, YPoint - YLength,
				XPoint + (int) (5f * density), YPoint - YLength
						+ (int) (10f * density), paintY);

		// 设置X轴
		canvas.drawLine(XPoint, YPoint, XPoint + XLength, YPoint, paintY); // 轴线
		int i;
		for (i = 0; i * XScale < XLength - XScale + 1; i++) {
			if (flag) {
				canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i
						* XScale, YPoint - YLength, paintY); // 刻度

				canvas.drawText(XLabel[i],
						(XPoint + i * XScale - (10f * density)),
						(YPoint + (20f * density)), paintY); // 文字
			} else {
				if (i % 2 == 0) {
					canvas.drawLine(XPoint + i * XScale, YPoint, XPoint + i
							* XScale, YPoint - YLength, paintY); // 刻度

					canvas.drawText(XLabel[i],
							(XPoint + i * XScale - (10f * density)),
							(YPoint + (20f * density)), paintY); // 文字
				}
			}
			// 数据值
			if (i > 0)
				canvas.drawLine((float) (XPoint + (i - 1) * XScale),
						(float) YPoint - (Data[i - 1] * perY),
						(float) (XPoint + i * XScale), (float) YPoint
								- (Data[i] * perY), paintW);
		}

		// 小圆圈
		canvas.drawCircle((float) (XPoint + (i - 1) * XScale), (float) YPoint
				- (Data[i - 1] * perY), 10, paintR);

		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength
				- (int) (10f * density), YPoint - (int) (5f * density), paintY); // 箭头
		canvas.drawLine(XPoint + XLength, YPoint, XPoint + XLength
				- (int) (10f * density), YPoint + (int) (5f * density), paintY);
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		//判断是否横屏
		if(!isChartFragment){
			return true;
		}else{
			return super.dispatchTouchEvent(event);
		}
	}

	float startX=0,moveX=0;
	PopupWindow popupWindow=null;
	float disX;
	int Yoff;
	private TextView popup_text;
	/**
	 * 按住显示具体收益
	 * 1.显示高度 随手指滑动 Y轴收益变化
	 * 2.显示收益内容随手指滑动 x轴位置变化
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		Log.i(TAG, "onTouchEvent event"+event.getAction());
			if(popupWindow==null){
				popupWindow = new PopupWindow(LayoutParams.WRAP_CONTENT, (int)(30*density));
				View chart_popup = View.inflate(context, 
						R.layout.user_chartpop, null);
				popup_text = (TextView)chart_popup.findViewById(R.id.popup_text);
				popupWindow.setContentView(chart_popup);
				popupWindow.setTouchable(false);
				Yoff=getHeight()-YPoint+(int)(35*density);
				Log.i(TAG, "height:"+height+" getHeight:"+getHeight()+" YPoint:"+YPoint);
			}
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(startX==0){
						startX=event.getRawX()-XScale;
					//根据downX 计算pop高度
					if(startX>=XPoint&&startX<=(XLength+XPoint-XScale+XScale/2)){
						int i=(int)(startX-XPoint)/XScale;
						popup_text.setText(Data[i]+"元");
						popupWindow.setHeight((int)(Data[i]*perY+30*density));
						popupWindow.showAtLocation(ChartView.this,
							Gravity.BOTTOM|Gravity.LEFT, 
							(int)(startX-30*density),Yoff);
					}
					}
					break;
				case MotionEvent.ACTION_MOVE:
					moveX=event.getRawX()-XScale;
					Log.i(TAG, "downX:"+startX+"moveX:"+moveX+" XPoint:"+XPoint);
					if(moveX>XPoint&&moveX<(XLength+XPoint-XScale+XScale/2)){
						int j=(int)(moveX-XPoint)/XScale;
						popup_text.setText(Data[j]+"元");
						popupWindow.update((int)(moveX-30*density),
									Yoff, -1, (int)(Data[j]*perY+30*density));
					}
					break;
				case MotionEvent.ACTION_UP:
				default:
					startX=0;
					moveX=0;
					if(null!=popupWindow&&popupWindow.isShowing()){
						popupWindow.dismiss();
					}
					break;
				}
		return true;
	}
}