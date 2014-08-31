package com.allen.itouzi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.allen.itouzi.R;
import com.umeng.analytics.MobclickAgent;

public class HelpActivity extends Activity implements OnClickListener{
	private static final String TAG = "HelpActivity";
	private View help_back;
	private View help_rl1,help_rl2,help_rl3,help_rl5;
	private View help_tv11,help_tv22,help_tv33,help_tv55;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.more_help);
		help_back = findViewById(R.id.help_back);
		help_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		help_rl1=findViewById(R.id.help_rl1);
		help_rl2=findViewById(R.id.help_rl2);
		help_rl3=findViewById(R.id.help_rl3);
		help_rl5=findViewById(R.id.help_rl5);
		help_tv11=findViewById(R.id.help_tv11);
		help_tv22=findViewById(R.id.help_tv22);
		help_tv33=findViewById(R.id.help_tv33);
		help_tv55=findViewById(R.id.help_tv55);
		help_rl1.setOnClickListener(this);
		help_rl2.setOnClickListener(this);
		help_rl3.setOnClickListener(this);
		help_rl5.setOnClickListener(this);
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
		switch (v.getId()) {
		case R.id.help_rl1:
			help_tv11.setVisibility(View.VISIBLE);
			help_tv22.setVisibility(View.GONE);
			help_tv33.setVisibility(View.GONE);
			help_tv55.setVisibility(View.GONE);
			break;
		case R.id.help_rl2:
			help_tv11.setVisibility(View.GONE);
			help_tv22.setVisibility(View.VISIBLE);
			help_tv33.setVisibility(View.GONE);
			help_tv55.setVisibility(View.GONE);
			break;
		case R.id.help_rl3:
			help_tv11.setVisibility(View.GONE);
			help_tv22.setVisibility(View.GONE);
			help_tv33.setVisibility(View.VISIBLE);
			help_tv55.setVisibility(View.GONE);
			break;
		case R.id.help_rl5:
			help_tv11.setVisibility(View.GONE);
			help_tv22.setVisibility(View.GONE);
			help_tv33.setVisibility(View.GONE);
			help_tv55.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		
	}
	

}
