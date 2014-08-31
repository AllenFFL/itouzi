package com.allen.itouzi.view;

import com.allen.itouzi.activity.ProjectDetailActivity;
import com.allen.itouzi.utils.ValueUtil;

import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.TextView;

public class ICounter extends CountDownTimer {
	private long first = 0, twice = 0, third = 0;
	private long mtmp = 0, mtmp2 = 0;
	private TextView textview;
	private ProjectDetailActivity context;

	public ICounter(Activity context, long millisInFuture,
			long countDownInterval, TextView textview) {
		super(millisInFuture, countDownInterval);
		this.context = (ProjectDetailActivity) context;
		this.textview = textview;
	}

	@Override
	public void onTick(long millisUntilFinished) {
		// ��ȡ��ǰʱ��������
		first = millisUntilFinished / 1000;
		if (first < ValueUtil.SECONDS) { // С��һ���� ֻ��ʾ��
			textview.setText("00:00:" + (first < 10 ? "0" + first : first));
		} else if (first < ValueUtil.MINUTES) { // ���ڻ����һ���ӣ���С��һСʱ����ʾ����
			twice = first % 60; // ����תΪ����ȡ�࣬����Ϊ��
			mtmp = first / 60; // ������תΪ����
			if (twice == 0) {
				textview.setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp)
						+ ":00"); // ֻ��ʾ����
			} else {
				textview.setText("00:" + (mtmp < 10 ? "0" + mtmp : mtmp) + ":"
						+ (twice < 10 ? "0" + twice : twice)); // ��ʾ���Ӻ���
			}
		} else {
			twice = first % 3600; // twiceΪ���� ���Ϊ0��СʱΪ����
			mtmp = first / 3600;
			if (twice == 0) {
				// ֻʣ��Сʱ
				textview.setText("0" + first / 3600 + ":00:00");
			} else {
				if (twice < ValueUtil.SECONDS) { // twiceС��60 Ϊ��
					textview.setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":00:"
							+ (twice < 10 ? "0" + twice : twice)); // ��ʾСʱ����
				} else {
					third = twice % 60; // thirdΪ0��ʣ�·��� ��������
					mtmp2 = twice / 60;
					if (third == 0) {
						textview.setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":"
								+ (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":00");
					} else {
						textview.setText((mtmp < 10 ? "0" + mtmp : mtmp) + ":"
								+ (mtmp2 < 10 ? "0" + mtmp2 : mtmp2) + ":"
								+ third); // ������
					}
				}
			}
		}
	}

	@Override
	public void onFinish() {
		context.initData();
	}

}
