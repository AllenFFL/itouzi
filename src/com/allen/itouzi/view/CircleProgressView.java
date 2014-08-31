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
	 * ���ȷ�� 1ʵ�Ļ�0����
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
		// ��ȡ�Զ������Բ���ֵ
		mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.CircleProgressView);
		// ���û�и��Զ����View������ɫ�����ͻ�ʹ�õڶ��������е�Ĭ��ֵ����Color.RED
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
	 * ���ý��ȵ����ֵ
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
	 * ��ȡ����.��Ҫͬ��
	 * 
	 * @return
	 */
	public synchronized int getProgress() {
		return progress;
	}

	/**
	 * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ�� ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(int progress) {
		// TODO:project_progress Ԥ�� 50 ��Ͷ��
		if (progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		} else if (progress >= max) {
			if (progress == max) {
				this.progress = progress;
				centerText = "��Ͷ��";
			} else if (progress == 101) {// Ԥ��
				this.progress = 0;
				centerText = "Ԥ��";
			} else if (progress == 102) {
				this.progress = 0;
				centerText = "����ʱ";
			} else if (progress == 104) {
				this.progress = 0;
				centerText = "�ѻ���";
			}
		} else if (progress < max) {
			this.progress = progress;
			int percent = (int) (((float) progress / (float) max) * 100);
			centerText = percent + "%";
			postInvalidate(); // ���ڷ�UI�߳�ˢ��
		}
	}

	/**
	 * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ�� ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
	 * 
	 * @param progress
	 */
	public synchronized void setProgress(float progress) {
		// TODO:project_progress Ԥ�� 50 ��Ͷ��
		if ((int) progress < 0) {
			throw new IllegalArgumentException("progress not less than 0");
		} else if ((int) progress >= max) {
			if ((int) progress == max) {
				this.progress = (int) progress;
				centerText = "��Ͷ��";
			} else if ((int) progress == 101) {// Ԥ��
				this.progress = 0;
				centerText = "Ԥ��";
			} else if ((int) progress == 102) {
				this.progress = 0;
				centerText = "����ʱ";
			} else if ((int) progress == 104) {
				this.progress = 0;
				centerText = "�ѻ���";
			}
		} else if ((int) progress < max) {
			this.progress = (int) progress;
			// int percent = (int)(((float)progress / (float)max) * 100);
			centerText = progress + "%";
			postInvalidate(); // ���ڷ�UI�߳�ˢ��
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		/**
		 * �������Ĵ�Բ��
		 */
		int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
										// http://developer.android.com/reference/android/view/View.html
		int radius = (int) (centre - circleWidth / 2); // Բ���İ뾶
		paint.setColor(circleColor); // ����Բ������ɫ
		paint.setStyle(Paint.Style.STROKE); // ���ÿ���
		paint.setStrokeWidth(circleWidth); // ����Բ���Ŀ��
		paint.setAntiAlias(true); // �������
		canvas.drawCircle(centre, centre, radius, paint);// ****
		/**
		 * �����Ȱٷֱ�
		 */
		paint.setStrokeWidth(0);
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.DEFAULT_BOLD); // ��������

		float textWidth = paint.measureText(centerText); // ���������ȣ�������Ҫ��������Ŀ��������Բ���м�

		if (textIsDisplayable && centerText != null && style == STROKE) {
			canvas.drawText(centerText, centre - textWidth / 2, centre
					+ textSize / 2, paint); // �������Ȱٷֱ�
		}
		/**
		 * ��Բ�� ����Բ���Ľ���
		 */
		// ���ý�����ʵ�Ļ��ǿ���
		paint.setStrokeWidth(circleWidth); // ����Բ���Ŀ��
		paint.setColor(circleProgressColor); // ���ý��ȵ���ɫ
		RectF oval = new RectF(centre - radius, centre - radius, centre
				+ radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

		switch (style) {
		case STROKE: {
			paint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // ���ݽ��Ȼ�Բ��
			break;
		}
		case FILL: {
			paint.setStyle(Paint.Style.FILL_AND_STROKE);
			if (progress != 0)
				canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // ���ݽ��Ȼ�Բ��
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
