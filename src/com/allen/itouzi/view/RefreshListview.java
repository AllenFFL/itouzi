package com.allen.itouzi.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.itouzi.R;

public class RefreshListview extends LinearLayout implements OnTouchListener {

	/**
	 * 下拉状态
	 */
	public static final int PULL_TO_REFRESH = 0;

	/**
	 * 松手刷新状态
	 */
	public static final int RELEASE_TO_REFRESH = 1;

	/**
	 * 正在刷新状态
	 */
	public static final int REFRESH_REFRESHING = 2;

	/**
	 * 刷新完成或未刷新状态
	 */
	public static final int REFRESH_FINISHED = 3;

	/**
	 * 下拉头部回滚的速度
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * 一分钟的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * 一小时的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * 一天的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * 一月的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * 一年的毫秒值，用于判断上次的更新时间
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * 上次更新时间的字符串常量，用于作为SharedPreferences的键值
	 */
	private static final String UPDATED_AT = "updated_at";

	private static final String TAG = "RefreshListview";

	/**
	 * 下拉刷新的回调接口
	 */
	private PullToRefreshListener mListener;

	/**
	 * 用于存储上次更新时间
	 */
	private SharedPreferences preferences;

	/**
	 * 下拉头的View
	 */
	private View header;

	/**
	 * 需要去下拉刷新的ListView
	 */
	private ListView listView;

	/**
	 * 刷新时显示的进度条
	 */
	private ProgressBar progressBar;

	/**
	 * 指示下拉和释放的箭头
	 */
	private ImageView arrow;

	/**
	 * 指示下拉和释放的文字描述
	 */
	private TextView description;

	/**
	 * 上次更新时间的文字描述
	 */
	private TextView updateAt;

	/**
	 * 下拉头的布局参数
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * 上次更新时间的毫秒值
	 */
	private long lastUpdateTime;

	/**
	 * 为了防止不同界面的下拉刷新在上次更新时间上互相有冲突，使用id来做区分
	 */
	private int mId = -1;

	/**
	 * 下拉头的高度
	 */
	private int hideHeaderHeight;

	/**
	 * 当前处理什么状态，可选值有STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING 和 STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = REFRESH_FINISHED;

	/**
	 * 记录上一次的状态是什么，避免进行重复操作
	 */
	private int lastStatus = currentStatus;

	/**
	 * 手指按下时的屏幕纵坐标
	 */
	private float yDown;

	/**
	 * 在被判定为滚动之前用户手指可以移动的最大值。
	 */
	private int touchSlop;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 当前是否可以下拉，只有ListView滚动到头的时候才允许下拉
	 */
	private boolean ableToPull=false;

	/**
	 * 下拉刷新控件的构造函数，会在运行时动态添加一个下拉头的布局。
	 * 
	 * @param context
	 * @param attrs
	 */
	public RefreshListview(Context context, AttributeSet attrs) {
		super(context, attrs);
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		header = LayoutInflater.from(context).inflate(R.layout.refresh_header,
				null, true);
		arrow = (ImageView) header.findViewById(R.id.header_arrow);
		progressBar = (ProgressBar)header.findViewById(R.id.header_progress_bar);
		description = (TextView) header.findViewById(R.id.header_hint);
		updateAt = (TextView) header.findViewById(R.id.updated_at);
		touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
		refreshUpdatedAtValue();
		setOrientation(VERTICAL);
		addView(header, 0);
	}

	/**
	 * 进行一些关键性的初始化操作，比如：将下拉头向上偏移进行隐藏，给ListView注册touch事件。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			hideHeaderHeight = -header.getHeight();
			headerLayoutParams = (MarginLayoutParams) header.getLayoutParams();
			headerLayoutParams.topMargin = hideHeaderHeight;
			header.setLayoutParams(headerLayoutParams);
			listView = (ListView) getChildAt(1);
			listView.setOnTouchListener(this);
			loadOnce = true;
		}
	}

	/**
	 * 当ListView被触摸时调用，其中处理了各种下拉刷新的具体逻辑。
	 * 
	 */
	public boolean onTouch(View v, MotionEvent event) {
		if (IsAbleToPull(event)) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				yDown = event.getRawY();
				Log.i(TAG, "ACTION_DOWN:"+yDown);
				break;
			case MotionEvent.ACTION_MOVE:
				float yMove = event.getRawY();
				int distance = (int) (yMove - yDown);
				Log.i(TAG, "ACTION_MOVE:"+"yDown"+yDown+"yMove"+yMove);
				// 如果手指是上滑状态，并且下拉头是完全隐藏的，就屏蔽下拉事件
				if (distance <= 0
						&& headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != REFRESH_REFRESHING) {
					// 通过偏移下拉头的topMargin值，来实现下拉效果
					headerLayoutParams.topMargin = (distance / 2)
							+ hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
					//更新Header内容
					if (headerLayoutParams.topMargin > 0) {
						currentStatus = RELEASE_TO_REFRESH;
					} else {
						currentStatus = PULL_TO_REFRESH;
					}
					updateHeaderView();
					lastStatus=currentStatus;
				}
				break;
			case MotionEvent.ACTION_UP:
			default:
				// 松手时,如果是松手刷新，刷新
				if (currentStatus == RELEASE_TO_REFRESH) {
					new RefreshingTask().execute();
					return true;
				// 松手时,如果是下拉状态，隐藏下拉头
				} else if (currentStatus == PULL_TO_REFRESH) {
					hideHeader();
					return true;
				}
			}
			// 如果是下拉状态和松手刷新，都让ListView失去焦点，否则被点击的那一项会一直处于选中状态
			if (currentStatus ==RELEASE_TO_REFRESH
					||currentStatus== PULL_TO_REFRESH) {
				listView.setPressed(false);
				listView.setFocusable(false);
				listView.setFocusableInTouchMode(false);
				// 当前正处于下拉或释放状态，通过返回true屏蔽掉ListView的滚动事件
				return true;
			}
		}
		return false;
	}

	private boolean IsAbleToPull(MotionEvent event) {
		View firstChild = listView.getChildAt(0);
		if (firstChild != null) {
			int firstVisiblePos = listView.getFirstVisiblePosition();
			if (firstVisiblePos == 0 && firstChild.getTop() == 0) {
				if(!ableToPull)
				yDown = event.getRawY();
				ableToPull=true;
				return true;
			}else{
				ableToPull=false;
				return false;
			}
		}else{
			ableToPull=true;
			return true;
		}
	}

	/**
	 * 给下拉刷新控件注册一个监听器。
	 * 
	 * @param listener
	 *            监听器的实现。
	 * @param id
	 *            为了防止不同界面的下拉刷新在上次更新时间上互相有冲突， 请不同界面在注册下拉刷新监听器时一定要传入不同的id。
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * 正在刷新的任务，在此任务中会去回调注册进来的下拉刷新监听器。
	 * 
	 * @author allen
	 */
	class RefreshingTask extends AsyncTask<Void, Integer, Integer> {
		@Override
		protected void onPreExecute() {
			while (headerLayoutParams.topMargin>0) {
				headerLayoutParams.topMargin = 
						headerLayoutParams.topMargin + SCROLL_SPEED;
				header.setLayoutParams(headerLayoutParams);
			}
			headerLayoutParams.topMargin=0;
			header.setLayoutParams(headerLayoutParams);
			currentStatus = REFRESH_REFRESHING;
			updateHeaderView();
			super.onPreExecute();
		}
		@Override
		protected Integer doInBackground(Void... params) {
			if (mListener != null) {
				mListener.onRefresh();
			}
			return null;
		}
		@Override
		protected void onPostExecute(Integer result) {
			finishRefresh();
			super.onPostExecute(result);
		}
	}
	/**
	 * 当所有的刷新逻辑完成后，记录调用一下，否则你的ListView将一直处于正在刷新状态。
	 * 
	 */
	public void finishRefresh() {
		hideHeader();
		preferences.edit()
				.putLong(UPDATED_AT + mId, System.currentTimeMillis()).commit();
	}
	private void hideHeader() {
		while (headerLayoutParams.topMargin>hideHeaderHeight) {
			headerLayoutParams.topMargin = 
					headerLayoutParams.topMargin + SCROLL_SPEED;
				header.setLayoutParams(headerLayoutParams);
		}
		headerLayoutParams.topMargin=hideHeaderHeight;
		header.setLayoutParams(headerLayoutParams);
		currentStatus=REFRESH_FINISHED;
		updateHeaderView();
	}
	/**
	 * 更新下拉头中的信息。
	 */
	private void updateHeaderView() {
		if (lastStatus != currentStatus) {
			if (currentStatus == PULL_TO_REFRESH||
					currentStatus==REFRESH_FINISHED) {
				description.setText(getResources().getString(
						R.string.pull_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == RELEASE_TO_REFRESH) {
				description.setText(getResources().getString(
						R.string.release_to_refresh));
				arrow.setVisibility(View.VISIBLE);
				progressBar.setVisibility(View.GONE);
				rotateArrow();
			} else if (currentStatus == REFRESH_REFRESHING) {
				description.setText(getResources().getString(
						R.string.refreshing));
				progressBar.setVisibility(View.VISIBLE);
				arrow.clearAnimation();
				arrow.setVisibility(View.GONE);
			}
			refreshUpdatedAtValue();
		}
	}

	/**
	 * 根据当前的状态来旋转箭头。
	 */
	private void rotateArrow() {
		float pivotX = arrow.getWidth() / 2f;
		float pivotY = arrow.getHeight() / 2f;
		float fromDegrees = 0f;
		float toDegrees = 0f;
		if (lastStatus==REFRESH_FINISHED) {
			fromDegrees = 180f;
			toDegrees = 360f;
		} else if (currentStatus == RELEASE_TO_REFRESH) {
			fromDegrees = 0f;
			toDegrees = 180f;
		}
		RotateAnimation animation = new RotateAnimation(fromDegrees, toDegrees,
				pivotX, pivotY);
		animation.setDuration(100);
		animation.setFillAfter(true);
		arrow.startAnimation(animation);
	}

	/**
	 * 刷新下拉头中上次更新时间的文字描述。
	 */
	private void refreshUpdatedAtValue() {
		lastUpdateTime = preferences.getLong(UPDATED_AT + mId, -1);
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - lastUpdateTime;
		long timeIntoFormat;
		String updateAtValue;
		if (lastUpdateTime == -1) {
			updateAtValue = getResources().getString(R.string.not_updated_yet);
		} else if (timePassed < 0) {
			updateAtValue = getResources().getString(R.string.time_error);
		} else if (timePassed < ONE_MINUTE) {
			updateAtValue = getResources().getString(R.string.updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			timeIntoFormat = timePassed / ONE_MINUTE;
			String value = timeIntoFormat + "分钟";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "小时";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "天";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "个月";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "年";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}
	/**
	 * 下拉刷新的监听器，使用下拉刷新的地方应该注册此监听器来获取刷新回调。
	 * 
	 * @author allen
	 */
	public interface PullToRefreshListener {

		/**
		 * 刷新时会去回调此方法，在方法内编写具体的刷新逻辑。注意此方法是在子线程中调用的， 你可以不必另开线程来进行耗时操作。
		 */
		void onRefresh();
	}

}
