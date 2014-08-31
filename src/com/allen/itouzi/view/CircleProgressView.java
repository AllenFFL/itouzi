package com.allen.itouzi.view;

import com.allen.itouzi.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressView extends View {

	private TypedArray mTypedArray;
	private int circleColor;
	private int circleProgressColor;
	private int textColor;
	private float textSize;
	private float circleWidth;
	private boolean textIsDisplayable;
	private int max;
	private int progress;
	private Paint paint;
	/**
	 * 进度风格 1实心或0空心
	 */
	private int style;
	private String centerText = "%";
	public static final int STROKE = 0;
	public static final int FILL = 1;

	public CircleProgressView(Context context) {
		this(context, null);
	}

	public CircleProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		paint = new Paint();
		// 获取自定义属性并赋值
		mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleProgressView);
		// 如果没有给自定义的View定义颜色，他就会使用第二个参数中的默认值，即Color.RED
		circleColor = mTypedArray.getColor(
				R.styleable.CircleProgressView_circleColor, Color.RED);
		circleProgressColor = mTypedArray
				.getColor(R.styleable.CircleProgressView_circleProgressColor,
						Color.GREEN);
		textColor = mTypedArray.getColor(
				R.styleable.CircleProgressView_textColor, Color.GREEN);
		textSize = mTypedArray.getDimension(
				R.styleable.CircleProgressView_textSize, 15);
		circleWidth = mTypedArray.getDimension(
				R.styleable.CircleProgressView_circleWidth, 5);
		max = mTypedArray.getInteger(R.styleable.CircleProgressView_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(
				R.styleable.CircleProgressView_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.CircleProgressView_style, 0);
		mTypedArray.recycle();
	}

	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * 
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if (max < 0) {
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		// TODO:project_progress 预告 50 已投满
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		} else if (progress >= max) {
			if (progress == max) {
				this.progress = progress;
				centerText = "已投满";
			} else if (progress == 101) {// 预告
				this.progress = 0;
				centerText = "预告";
			} else if (progress == 102) {
				this.progress = 0;
				centerText = "倒计时";
			} else if (progress == 104) {
				this.progress = 0;
				centerText = "已还款";
			}
		} else if (progress < max) {
			this.progress = progress;
			int percent = (int) (((float) progress / (float) max) * 100);
			centerText = percent + "%";
			postInvalidate(); // 能在非UI线程刷新
		}
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步 刷新界面调用postInvalidate()能在非UI线程刷新
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(float progress) {
		// TODO:project_progress 预告 50 已投满
		if ((int) progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		} else if ((int) progress >= max) {
			if ((int) progress == max) {
				this.progress = (int) progress;
				centerText = "已投满";
			} else if ((int) progress == 101) {// 预告
				this.progress = 0;
				centerText = "预告";
			} else if ((int) progress == 102) {
				this.progress = 0;
				centerText = "倒计时";
			} else if ((int) progress == 104) {
				this.progress = 0;
				centerText = "已还款";
			}
		} else if ((int) progress < max) {
			this.progress = (int) progress;
			// int percent = (int)(((float)progress / (float)max) * 100);
			centerText = progress + "%";
			postInvalidate(); // 能在非UI线程刷新
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		/**
		 * 画最外层的大圆环
		 */
		int centre = getWidth() / 2; // 获取圆心的x坐标
										// http://developer.android.com/reference/android/view/View.html
		int radius = (int) (centre - circleWidth / 2); // 圆环的半径
		paint.setColor(circleColor); // 设置圆环的颜色
		paint.setStyle(Paint.Style.STROKE); // 设置空心
		paint.setStrokeWidth(circleWidth); // 设置圆环的宽度
		paint.setAntiAlias(true); // 消除锯齿
		canvas.drawCircle(centre, centre, radius, paint);// ****
		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // 设置字体

		float textWidth = paint.measureText(centerText); // 测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		if (textIsDisplayable && centerText != null && style == STROKE) {
			canvas.drawText(centerText, centre - textWidth / 2, centre
					+ textSize / 2, paint); // 画出进度百分比
		}
		/**
		 * 画圆弧 ，画圆环的进度
		 */
		// 设置进度是实心还是空心
		paint.setStrokeWidth(circleWidth); // 设置圆环的宽度
		paint.setColor(circleProgressColor); // 设置进度的颜色
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // 用于定义的圆弧的形状和大小的界限

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // 根据进度画圆弧
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // 根据进度画圆弧
			break;
		}
		}
	}

	public int getCricleColor() {
		return circleColor;
	}

	public void setCricleColor(int cricleColor) {
		this.circleColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return circleProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.circleProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getcircleWidth() {
		return circleWidth;
	}

	public void setcircleWidth(float circleWidth) {
		this.circleWidth = circleWidth;
	}

}
