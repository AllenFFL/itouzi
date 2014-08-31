package com.allen.itouzi.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.allen.itouzi.R;
import com.allen.itouzi.bean.IdanbaoMain;
import com.allen.itouzi.engine.ProjectsEngine;
import com.allen.itouzi.utils.LogUtil;
import com.allen.itouzi.view.RefreshListview;
import com.allen.itouzi.view.RefreshListview.PullToRefreshListener;
import com.allen.itouzi.view.adapter.IPagerAdapter;
import com.allen.itouzi.view.adapter.IdanbaoAdapter;
import com.allen.itouzi.view.adapter.IdanbaoAdapter2;
import com.allen.itouzi.view.adapter.IrongzuAdapter;
import com.umeng.analytics.MobclickAgent;

public class ProjectFragment extends Fragment implements OnClickListener {
	protected static final String TAG = "ProjectFragment";
	private View idanbao_list, irongzu_list;
	private TextView idanbao_title, irongzu_title;
	private LinearLayout project_idanbao_loading, project_irongzu_loading;
	private ViewPager project_viewpager;
	private ArrayList<View> pageViewList = new ArrayList<View>();
	private int currIndex = 0;// 当前页卡编号
	private ListView danbao_list_view, rongzu_list_view;
	private RefreshListview project_idanbao_refresh, project_irongzu_refresh;
	private ArrayList<IdanbaoMain> idanbaoMainList, idanbaMainList2;
	private IdanbaoAdapter idanbaoAdapter = null;
	private IdanbaoAdapter2 idanbaoAdapter2 = null;
	private LruCache<Integer, ArrayList<IdanbaoMain>> idanbaoCache=null,
			irongzuCache=null;

	/**
	 * 当前拉取的页数
	 */
	public static Integer IDANBAO_P = 0, IRONGZU_P = 0;
	/**
	 * 丢失需要重新加载的页面，当lrucache中的某页数据被回收了，重新加载这个页面数据
	 */
	public static Integer IDANBAO_CP=0,IRONGZU_CP=0;
	/**
	 * 每页的ArrayList<IdanbaoMain>大小 (不是最后一页!)
	 */
	public static int IDANBAO_S = 0, IRONGZU_S = 0;
	/**
	 * 最后一页的ArrayList<IdanbaoMain>大小 (如果它不是IDANBAO_S)
	 */
	public static int IDANBAO_L = 0, IRONGZU_L;
	/**
	 * 当前Listview显示的item position
	 */
	public static int position1 = 0, position2=0;
	private IdanbaosTask idanbaoMore=null;//上拉加载任务
	private IrongzusTask irongzuMore=null;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(TAG, "fragment onCreateView!");
		View rootView = inflater.inflate(R.layout.project_fragment, container,
				false);
		// 初始化顶部按钮
		idanbao_title = (TextView) rootView.findViewById(R.id.idanbao_title);
		irongzu_title = (TextView) rootView.findViewById(R.id.irongzu_title);
		idanbao_title.setOnClickListener(this);
		irongzu_title.setOnClickListener(this);
		// 初始化两个下拉刷新控件
		if(idanbao_list==null)
		idanbao_list = View.inflate(getActivity(),
				R.layout.project_idanbao_list, null);
		if(irongzu_list==null)
		irongzu_list = View.inflate(getActivity(),
				R.layout.project_irongzu_list, null);

		project_idanbao_loading = (LinearLayout) idanbao_list
				.findViewById(R.id.project_idanbao_loading);
		project_irongzu_loading = (LinearLayout) irongzu_list
				.findViewById(R.id.project_irongzu_loading);
		if(project_idanbao_refresh==null)
		project_idanbao_refresh = (RefreshListview) idanbao_list
				.findViewById(R.id.project_idanbao_refresh);
		if(project_irongzu_refresh==null)
		project_irongzu_refresh = (RefreshListview) irongzu_list
				.findViewById(R.id.project_irongzu_refresh);
		if(danbao_list_view==null)
		danbao_list_view = (ListView) idanbao_list
				.findViewById(R.id.danbao_list_view);
		if(rongzu_list_view==null)
		rongzu_list_view = (ListView) irongzu_list
				.findViewById(R.id.rongzu_list_view);
		iniLruCache();// listview数据缓存
		if(idanbaoAdapter==null){
			IDANBAO_P=0;
			new IdanbaosTask().execute(IDANBAO_P+1);// 异步加载listview数据 缓存 添加adapter
		}else{
			idanbaoAdapter.notifyDataSetChanged();
		}
		if(idanbaoAdapter2==null){
			IRONGZU_P=0;
			new IrongzusTask().execute(IRONGZU_P+1);
		}else{
			idanbaoAdapter2.notifyDataSetChanged();
		}
		setRefreshListener();
		setLVListener();
		if(pageViewList.size()==0){
			pageViewList.add(idanbao_list);
			pageViewList.add(irongzu_list);
		}
		// 控件初始化 viewpager添加两个list
		project_viewpager = (ViewPager) rootView
				.findViewById(R.id.project_viewpager);
		project_viewpager.setAdapter(new IPagerAdapter(pageViewList));
		project_viewpager.setOnPageChangeListener(new MyOnPageChangeListener());
		project_viewpager.setCurrentItem(currIndex);
		return rootView;
	}

	private void iniLruCache() {
		final int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int maxSize = maxMemory / 10;
		int maxSize2 = maxMemory / 12;
		Log.i(TAG, "maxSize:" + maxSize);
		if(idanbaoCache==null)
		idanbaoCache = getLruCahe(maxSize);
		if(irongzuCache==null)
		irongzuCache = getLruCahe(maxSize2);
	}

	private LruCache getLruCahe(int maxSize) {
		LruCache lrucache = new LruCache<Integer, ArrayList<IdanbaoMain>>(
				maxSize) {
			@Override
			protected int sizeOf(Integer key, ArrayList<IdanbaoMain> value) {
				try {
					Field f = ArrayList.class.getDeclaredField("array");
					f.setAccessible(true);
					Object[] elementData = (Object[]) f.get(value);
					return elementData.length;
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return super.sizeOf(key, value);
			}
		};
		return lrucache;
	}

	public void putIdanbaoCache(Integer page,
			ArrayList<IdanbaoMain> idanbaoMainList) {
		if (idanbaoMainList != null && idanbaoCache.get(page) == null)
			idanbaoCache.put(page, idanbaoMainList);
	}

	public void putIrongzuCache(Integer page,
			ArrayList<IdanbaoMain> idanbaoMainList2) {
		if (idanbaoMainList2 != null && irongzuCache.get(page) == null)
			irongzuCache.put(page, idanbaoMainList2);
	}

	private void setLVListener() {
		// 设置item点击的监听
		danbao_list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position1=position;
				Log.v(TAG, "onItemClick position1----" + position1);
				int PAGE = position / IDANBAO_S + 1;
				ArrayList<IdanbaoMain> array = idanbaoCache.get(PAGE);
				if(array==null){
					//重新拉取放入缓存中 TODO:测试
					IDANBAO_CP=PAGE;
					new IdanbaosTask().execute(PAGE);
				}else{
					goIdanbaoDetail(array);
				}
			}
		});
		rongzu_list_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position2=position;
				int PAGE2 = position / IRONGZU_S + 1;
				ArrayList<IdanbaoMain> array2 =irongzuCache.get(PAGE2);
				//如果被回收了,重新加载放入缓存，显示缓冲圈，完成后转跳详情页
				if(array2==null){
					IRONGZU_CP=PAGE2;
					new IrongzusTask().execute(IRONGZU_CP);
				}else{
					goIrongzuDetail(array2);
				}
			}
		});
		if(scroll1==null){
				scroll1 = new OnScrollListener() {

					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						switch (scrollState) {
						case SCROLL_STATE_TOUCH_SCROLL:
		
							break;
						case SCROLL_STATE_FLING:
							break;
						case SCROLL_STATE_IDLE:
							// 检查是否滑到了缓存数据量的最后一个条目 是就改变startIndex再去加载
							int position1 = danbao_list_view.getLastVisiblePosition();
							Log.v(TAG, "OnScroll position1----" + position1);
							if (position1 + 1 == (IDANBAO_P - 1) * IDANBAO_S
									+ IDANBAO_L) {
								if (IDANBAO_L != 0) {//TODO:下拉到底 多次下拉问题
									if(null==idanbaoMore){
										idanbaoMore = new IdanbaosTask();
										idanbaoMore.execute(IDANBAO_P+1);
									}
								}
							}
							break;
						}
					}
		
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
					}
				};
		}
		danbao_list_view.setOnScrollListener(scroll1);
		if(scroll2==null){
			scroll2 = new OnScrollListener() {
	
				@Override
				public void onScrollStateChanged(AbsListView view, int scrollState) {
					switch (scrollState) {
					case SCROLL_STATE_TOUCH_SCROLL:
						break;
					case SCROLL_STATE_FLING:
						break;
					case SCROLL_STATE_IDLE:
						
						// 检查是否滑到了缓存数据量的最后一个条目 是就改变IRONGZU_P再去加载
						int position2 = rongzu_list_view.getLastVisiblePosition();
						Log.v(TAG, "position2----" + position2);
						if (position2 + 1 == (IRONGZU_P - 1) * IRONGZU_S
								+ IRONGZU_L) {
							if (IRONGZU_L != 0) {
								if(null==irongzuMore){
									irongzuMore = new IrongzusTask();
									irongzuMore.execute(IRONGZU_P+1);
								}
							}
						}
						break;
					}
				}
	
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
				}
			};
		}
		rongzu_list_view.setOnScrollListener(scroll2);
	}

	protected void goIdanbaoDetail(ArrayList<IdanbaoMain> array) {
		IdanbaoMain idanbaoMain = array.get(
				position1 % IDANBAO_S);
		if (idanbaoMain.getStatus() == 4) {
			LogUtil.showAliert(ProjectFragment.this.getActivity(),
					"为保护借款企业隐私，已还本付息项目不再公示项目详情。");
		} else {
			Intent intent = new Intent(getActivity(),
					ProjectDetailActivity.class);
			intent.putExtra("idanbao_id", idanbaoMain.getId());
			startActivity(intent);
		}
		
	}

	protected void goIrongzuDetail(ArrayList<IdanbaoMain> array2) {
		IdanbaoMain idanbaoMain2 =array2.get(
				position2 % IRONGZU_S);
		if (idanbaoMain2.getStatus() == 4) {
			AlertDialog.Builder builder = new Builder(getActivity());
			LogUtil.showAliert(ProjectFragment.this.getActivity(),
					"为保护借款企业隐私，已还本付息项目不再公示项目详情。");
		} else {
			Intent intent = new Intent(getActivity(),
					ProjectDetailActivity.class);
			intent.putExtra("irongzu_id", idanbaoMain2.getId());
			startActivity(intent);
		}
		
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
				idanbaoTab();
			} else {
				irongzuTab();
			}
		}
	}
	/**
	 * Handler 下拉刷新时的UI更新
	 */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				if (idanbaoMainList == null&&idanbaoMainList.size()==0) {
					LogUtil.showAliert(getActivity(), "没有获取到信息,请检查网络！");
				} else {
					IDANBAO_P = 1;
					IDANBAO_S = idanbaoMainList.size();
					IDANBAO_L = IDANBAO_S;
					idanbaoCache.remove(IDANBAO_P);
					putIdanbaoCache(IDANBAO_P, idanbaoMainList);
					setIdanbaoAdapter();
				}
				break;
			case 3:
				if (idanbaMainList2 == null&&idanbaMainList2.size()==0) {
					LogUtil.showAliert(getActivity(), "没有获取到信息,请检查网络！");
				} else {
					IRONGZU_P=1;
					IRONGZU_S=idanbaMainList2.size();
					IDANBAO_L=IRONGZU_S;
					irongzuCache.remove(IRONGZU_P);
					putIrongzuCache(IRONGZU_P, idanbaMainList2);
					setIrongzuAdapter();
				}
				break;
			default:
				break;
			}
		};
	};
	/**
	 * 设置下拉刷新监听
	 */
	private void setRefreshListener() {
		project_idanbao_refresh.setOnRefreshListener(
				new PullToRefreshListener() {
					@Override
					public void onRefresh() {
						idanbaoMainList = ProjectsEngine.getInstance()
								.getIdanbaoList(ProjectFragment.this.getActivity(),1);
						mHandler.sendEmptyMessage(2);
					}
				}, 2);
		project_irongzu_refresh.setOnRefreshListener(
				new PullToRefreshListener() {
					@Override
					public void onRefresh() {
						idanbaMainList2 = ProjectsEngine.getInstance()
								.getIrongzuList(ProjectFragment.this.getActivity(),1);
						mHandler.sendEmptyMessage(3);
					}
				}, 3);
	}

	// 爱担保产品 加载更多
	public class IdanbaosTask extends
			AsyncTask<Integer, Void, ArrayList<IdanbaoMain>> {
		@Override
		protected void onPreExecute() {
			if(IDANBAO_CP!=0){
				project_idanbao_refresh.setVisibility(View.GONE);
			}
			project_idanbao_loading.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected ArrayList<IdanbaoMain> doInBackground(Integer... params) {
			return ProjectsEngine.getInstance().getIdanbaoList(
					ProjectFragment.this.getActivity(),params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<IdanbaoMain> result) {
			super.onPostExecute(result);
			project_idanbao_loading.setVisibility(View.GONE);
			if (result == null || result.size() == 0) {
				LogUtil.showToast(getActivity(), "没有获取到爱担保产品信息！");
			} else {
				if (IDANBAO_S == 0) {
					IDANBAO_S = result.size();
					IDANBAO_L = IDANBAO_S;
				} else if (IDANBAO_S != result.size()) {//TODO:
					IDANBAO_L = result.size();
				}
				if(IDANBAO_CP==0){
					IDANBAO_P++;
					putIdanbaoCache(IDANBAO_P, result);
					setIdanbaoAdapter();
					if(null!=idanbaoMore){
						idanbaoMore.cancel(true);
						idanbaoMore=null;
					}
				}else{
					project_idanbao_refresh.setVisibility(View.VISIBLE);
					putIdanbaoCache(IDANBAO_CP, result);
					Log.i(TAG,"IDANBAO_CP:"+IDANBAO_CP);
					IDANBAO_CP=0;
					goIdanbaoDetail(result);
				}
				Log.i(TAG, "IDANBAO_P:" + IDANBAO_P + "  IDANBAO_S:"
						+ IDANBAO_S + "  IDANBAO_L:" + IDANBAO_L);
			}
		}
	}

	public class IrongzusTask extends
			AsyncTask<Integer, Void, ArrayList<IdanbaoMain>> {
		@Override
		protected void onPreExecute() {
			if(IRONGZU_CP!=0){
				project_irongzu_refresh.setVisibility(View.GONE);
			}
			project_irongzu_loading.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		@Override
		protected ArrayList<IdanbaoMain> doInBackground(Integer... params) {
			return ProjectsEngine.getInstance().getIrongzuList(
					ProjectFragment.this.getActivity(),params[0]);
		}

		@Override
		protected void onPostExecute(ArrayList<IdanbaoMain> result) {
			super.onPostExecute(result);
			project_irongzu_loading.setVisibility(View.GONE);
			if (result == null || result.size() == 0) {
				LogUtil.showToast(getActivity(), "没有获取到爱融租产品信息！");
			} else {
				if (IRONGZU_S == 0) {
					IRONGZU_S = result.size();
					IRONGZU_L = IDANBAO_S;
				} else if (IRONGZU_S != result.size()) {
					IRONGZU_L = result.size();
				}
				if(IRONGZU_CP==0){
					IRONGZU_P++;
					putIrongzuCache(IRONGZU_P, result);
					setIrongzuAdapter();
					if(null!=irongzuMore){
						irongzuMore.cancel(true);
						irongzuMore=null;
					}
				}else{
					project_irongzu_refresh.setVisibility(View.VISIBLE);
					putIrongzuCache(IRONGZU_CP, result);
					IRONGZU_CP=0;//重新添加进lrucache后，把这个临时页字段归0,并转跳详情页
					goIrongzuDetail(result);
				}
				Log.i(TAG, "IRONGZU_P:" + IRONGZU_P + "  IRONGZU_S:"
						+ IRONGZU_S + "  IRONGZU_L:" + IRONGZU_L);
			}
		}
	}
	private ExecutorService fixedThreadPool=null;
	private OnScrollListener scroll1=null;
	private OnScrollListener scroll2;
	/**
	 * 当某个page被回收了，重新开任务下载
	 * @param page
	 */
	public void getIdanbaos(final int page) {
		//下载 添加cache 更新UI
		if(fixedThreadPool==null)
		fixedThreadPool = Executors.newFixedThreadPool(3);
		fixedThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				 ArrayList<IdanbaoMain> idanbaoMainList = ProjectsEngine.getInstance().getIdanbaoList(
						getActivity(),page);
			}
		});
	}
	protected void setIdanbaoAdapter() {
		if (idanbaoAdapter != null) {
			idanbaoAdapter.notifyDataSetChanged();
			danbao_list_view.invalidate();
//			danbao_list_view.requestLayout();
		} else {
			idanbaoAdapter = new IdanbaoAdapter(ProjectFragment.this,
					idanbaoCache);
			danbao_list_view.setAdapter(idanbaoAdapter);
		}
	}

	protected void setIrongzuAdapter() {
		if (idanbaoAdapter2 != null) {
			idanbaoAdapter2.notifyDataSetChanged();
			rongzu_list_view.invalidate();
		} else {
			idanbaoAdapter2 = new IdanbaoAdapter2(ProjectFragment.this,
					irongzuCache);
			rongzu_list_view.setAdapter(idanbaoAdapter2);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.idanbao_title:
			idanbaoTab();
			break;
		case R.id.irongzu_title:
			irongzuTab();
			break;
		default:
			break;
		}
	}

	private void idanbaoTab() {
		currIndex = 0;
		idanbao_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.white_corner_rectangle));
		idanbao_title.setTextColor(getResources().getColor(R.color.dark_red));
		irongzu_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_r));
		irongzu_title.setTextColor(getResources().getColor(R.color.i_white));
		project_viewpager.setCurrentItem(currIndex);
	}

	private void irongzuTab() {
		currIndex = 1;
		irongzu_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.white_corner_rectangle));
		irongzu_title.setTextColor(getResources().getColor(R.color.dark_red));
		idanbao_title.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.button_l));
		idanbao_title.setTextColor(getResources().getColor(R.color.i_white));
		project_viewpager.setCurrentItem(currIndex);
	}


	@Override
	public void onResume() {
		super.onResume(); 
		getActivity().setRequestedOrientation(
				ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		MobclickAgent.onPageStart(TAG);
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd(TAG);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(null!=idanbaoAdapter)idanbaoAdapter.shutThreads();
		if(null!=idanbaoAdapter2)idanbaoAdapter2.shutThreads();
	}

}
