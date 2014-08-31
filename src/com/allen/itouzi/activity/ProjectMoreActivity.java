package com.allen.itouzi.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.Idanbao;
import com.allen.itouzi.bean.InvestRecord;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.view.RefreshListview;
import com.allen.itouzi.view.RefreshListview.PullToRefreshListener;
import com.allen.itouzi.view.adapter.IPagerAdapter;
import com.allen.itouzi.view.adapter.ProjectDescAdapter;
import com.allen.itouzi.view.adapter.DescAdapter;
import com.allen.itouzi.view.adapter.RecordAdapter;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

/**
 * �Ŀ�������Y����; ������Դ ��ҵ��Ϣ�� ��Ӫ��Χ ��Ӫ״�� ������Ϣ�������˺ſ����� �����˺� ����Ѻ����Ϣ ���տ��ƴ�ʩ �������������
 * 
 * Ͷ�ʼ�¼�� �û� Ͷ�ʽ�� Ͷ��ʱ�� t*** 100Ԫ ���� 16:29
 * 
 * @author Allen
 * 
 */
public class ProjectMoreActivity extends Activity implements OnClickListener {
	private static final String TAG = "ProjectMoreActivity";
	private ArrayList<View> pageViewList;
	private ListView desc_listview, record_listview;
	private ImageView project_back;
	private TextView desc_title;
	private TextView record_title;
	private int currIndex;
	private ViewPager project_viewpager;
	private Idanbao idanbao = null;
	private ArrayList<InvestRecord> recordList = null;
	private ExecutorService threadPool;
	private View project_more_loading;
	private View project_more_desc;
	private TextView desc_status;
	private View project_more_record;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		idanbao = (Idanbao) bundle.get("PojectDesc");
		if (idanbao == null) {
			// TODO:
			Toast.makeText(this, "û�л�ȡ����Ϣ��", 1).show();
		}
		setContentView(R.layout.project_more);
		initData();
	}

	private void initData() {
		project_more_loading = findViewById(R.id.project_more_loading);
		project_viewpager = (ViewPager) findViewById(R.id.project_viewpager);
		project_viewpager.setVisibility(View.GONE);
		project_more_loading.setVisibility(View.VISIBLE);
		project_more_desc = View
				.inflate(this, R.layout.project_more_desc, null);
		desc_listview = (ListView) project_more_desc
				.findViewById(R.id.more_desc_listview);
		desc_listview.setAdapter(new DescAdapter(getApplicationContext(),
				idanbao));
		desc_status = (TextView) project_more_desc
				.findViewById(R.id.desc_status);
		desc_listview.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// ������Ӧ��Item�ı�����
				if (idanbao != null) {
					if (firstVisibleItem == 0) {
						desc_status.setText("��Ŀ����");
					} else if (firstVisibleItem == 1) {
						desc_status.setText("��ҵ��Ϣ");
					} else if (firstVisibleItem == 2) {
						desc_status.setText("������Ϣ");
					}
				}
			}
		});
		if(threadPool==null)
		threadPool = Executors.newFixedThreadPool(3);
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				recordList = (ArrayList<InvestRecord>) ProjectsEngine
						.getInstance().getRecord(idanbao.getId(), null);

				runOnUiThread(new Runnable() {

					public void run() {
						// ����Ȧ
						project_more_loading.setVisibility(View.GONE);
						project_viewpager.setVisibility(View.VISIBLE);
						project_more_record = View.inflate(
								ProjectMoreActivity.this,
								R.layout.project_more_record, null);
						record_listview = (ListView) project_more_record
								.findViewById(R.id.more_record_listview);
						// record_listview=new
						// ListView(ProjectMoreActivity.this);
						// record_listview.setLayoutParams(new
						// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
						// record_listview.setDivider(getResources().getDrawable(R.color.i_gray_text));
						if (recordList != null) {
							record_listview.setAdapter(new RecordAdapter(
									ProjectMoreActivity.this, recordList));
						} else {
							// TODO:
						}
						findView();
					}
				});
			}
		});

	}

	private void findView() {
		// �������ؼ����л���ť
		project_back = (ImageView) findViewById(R.id.project_back);
		project_back.setOnClickListener(this);
		desc_title = (TextView) findViewById(R.id.desc_title);
		record_title = (TextView) findViewById(R.id.record_title);
		desc_title.setOnClickListener(this);
		record_title.setOnClickListener(this);
		// �м�viewpager����adapter
		pageViewList = new ArrayList<View>();
		pageViewList.add(project_more_desc);
		pageViewList.add(project_more_record);

		project_viewpager.setAdapter(new IPagerAdapter(pageViewList));
		project_viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		project_viewpager.setCurrentItem(0);
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
	protected void onDestroy() {
		super.onDestroy();
		if(threadPool!=null)
			threadPool.shutdown();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.project_back:
			finish();
			break;
		case R.id.desc_title:
			descTab();

			break;
		case R.id.record_title:
			recordTab();
			break;
		default:
			break;
		}

	}

	private void descTab() {
		currIndex = 0;
		desc_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.white_corner_rectangle));
		desc_title.setTextColor(getResources().getColor(R.color.dark_red));
		record_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_r));
		record_title.setTextColor(getResources().getColor(R.color.i_white));
		project_viewpager.setCurrentItem(currIndex);
	}

	private void recordTab() {
		currIndex = 1;
		record_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.white_corner_rectangle));
		record_title.setTextColor(getResources().getColor(R.color.dark_red));
		desc_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_l));
		desc_title.setTextColor(getResources().getColor(R.color.i_white));
		project_viewpager.setCurrentItem(currIndex);
	}

	class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			currIndex = arg0;
			if (currIndex == 0) {
				descTab();
			} else {
				recordTab();
			}
		}
	}

}
