package com.allen.itouzi.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.utils.DataFormat;
import com.umeng.analytics.MobclickAgent;

public class InvestActivity extends Activity implements OnClickListener {
	private static final String TAG = "InvestActivity";
	private Animation shake;
	private EditText invest_iamount;
	private String amount;
	private ImageView invest_back;
	private Button invest_button;
	private TextView invest_hint;
	private String[] projectInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// String[] projectInfo={idanbao.getId(),idanbao.getAccount_remain()+"",
		// idanbao.getLowest_account()};
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		projectInfo = intent.getStringArrayExtra("projectInfo");
		setContentView(R.layout.user_invest);
		// ��ʾ�����
		WindowManager.LayoutParams params = getWindow().getAttributes();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		// ����WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
		params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		shake = AnimationUtils.loadAnimation(this, R.drawable.shake);
		initView();
	}

	private void initView() {
		invest_iamount = (EditText) findViewById(R.id.invest_iamount);
		invest_iamount.setText("100");
		invest_back = (ImageView) findViewById(R.id.invest_back);
		invest_back.setOnClickListener(this);
		invest_button = (Button) findViewById(R.id.invest_button);
		invest_hint = (TextView) findViewById(R.id.invest_hint);
		invest_hint.setText("����Ŀ��Ͷ���"
				+ DataFormat.getThousand(Integer.parseInt(projectInfo[1]))
				+ "Ԫ����Ͷ���"
				+ DataFormat.getThousand(Integer.parseInt(projectInfo[2]))
				+ "Ԫ������Ͷ�ʽ��"
				+ DataFormat.getThousand(Integer.parseInt(projectInfo[2]))
				+ "Ԫ");
		invest_button.setOnClickListener(this);
	}

	private void checkAmount() {
		amount = invest_iamount.getText().toString().trim();
		if (TextUtils.isEmpty(amount)) {
			invest_iamount.setText("");
			invest_iamount.setHint("������Ͷ�ʽ��");
			invest_iamount.startAnimation(shake);
		} else {
			if (Integer.parseInt(amount) < Integer.parseInt(projectInfo[2])) {
				// С����Ͷ���
				invest_iamount.setText("");
				invest_iamount.setHint("���ܵ�����Ͷ���");
				invest_iamount.startAnimation(shake);
			} else if (Integer.parseInt(amount) > Integer
					.parseInt(projectInfo[1])) {
				// ����Ͷ�ʽ��
				invest_iamount.setText("");
				invest_iamount.setHint("���ܳ�����Ͷ���");
				invest_iamount.startAnimation(shake);
			} else if (Integer.parseInt(amount)
					% Integer.parseInt(projectInfo[2]) != 0) {
				// �����ϵ���Ͷ�ʽ��
				invest_iamount.setText("");
				invest_iamount.setHint("�����ϵ���Ͷ�ʽ��");
				invest_iamount.startAnimation(shake);
			} else {
				//UMENG
				HashMap<String,String> map = new HashMap<String,String>();
				map.put("projectID",projectInfo[0]);
				MobclickAgent.onEventValue(this, "Invest_Amount", map, Integer.parseInt(amount));
				
				Intent intent = new Intent(getApplicationContext(),
						WebActivity.class);
				String[] webInfo = { projectInfo[0], amount };
				intent.putExtra("webInfo", webInfo);
				startActivity(intent);
				finish();
			}
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onPageStart(TAG);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
		MobclickAgent.onPause(this);
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.invest_button) {
			checkAmount();
		} else if (v.getId() == R.id.invest_back) {
			finish();
		}
	}

}
