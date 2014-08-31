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
	 * ����״̬
	 */
	public static final int PULL_TO_REFRESH = 0;

	/**
	 * ����ˢ��״̬
	 */
	public static final int RELEASE_TO_REFRESH = 1;

	/**
	 * ����ˢ��״̬
	 */
	public static final int REFRESH_REFRESHING = 2;

	/**
	 * ˢ����ɻ�δˢ��״̬
	 */
	public static final int REFRESH_FINISHED = 3;

	/**
	 * ����ͷ���ع����ٶ�
	 */
	public static final int SCROLL_SPEED = -20;

	/**
	 * һ���ӵĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_MINUTE = 60 * 1000;

	/**
	 * һСʱ�ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_HOUR = 60 * ONE_MINUTE;

	/**
	 * һ��ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_DAY = 24 * ONE_HOUR;

	/**
	 * һ�µĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_MONTH = 30 * ONE_DAY;

	/**
	 * һ��ĺ���ֵ�������ж��ϴεĸ���ʱ��
	 */
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	/**
	 * �ϴθ���ʱ����ַ���������������ΪSharedPreferences�ļ�ֵ
	 */
	private static final String UPDATED_AT = "updated_at";

	private static final String TAG = "RefreshListview";

	/**
	 * ����ˢ�µĻص��ӿ�
	 */
	private PullToRefreshListener mListener;

	/**
	 * ���ڴ洢�ϴθ���ʱ��
	 */
	private SharedPreferences preferences;

	/**
	 * ����ͷ��View
	 */
	private View header;

	/**
	 * ��Ҫȥ����ˢ�µ�ListView
	 */
	private ListView listView;

	/**
	 * ˢ��ʱ��ʾ�Ľ�����
	 */
	private ProgressBar progressBar;

	/**
	 * ָʾ�������ͷŵļ�ͷ
	 */
	private ImageView arrow;

	/**
	 * ָʾ�������ͷŵ���������
	 */
	private TextView description;

	/**
	 * �ϴθ���ʱ�����������
	 */
	private TextView updateAt;

	/**
	 * ����ͷ�Ĳ��ֲ���
	 */
	private MarginLayoutParams headerLayoutParams;

	/**
	 * �ϴθ���ʱ��ĺ���ֵ
	 */
	private long lastUpdateTime;

	/**
	 * Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ��ʹ��id��������
	 */
	private int mId = -1;

	/**
	 * ����ͷ�ĸ߶�
	 */
	private int hideHeaderHeight;

	/**
	 * ��ǰ����ʲô״̬����ѡֵ��STATUS_PULL_TO_REFRESH, STATUS_RELEASE_TO_REFRESH,
	 * STATUS_REFRESHING �� STATUS_REFRESH_FINISHED
	 */
	private int currentStatus = REFRESH_FINISHED;

	/**
	 * ��¼��һ�ε�״̬��ʲô����������ظ�����
	 */
	private int lastStatus = currentStatus;

	/**
	 * ��ָ����ʱ����Ļ������
	 */
	private float yDown;

	/**
	 * �ڱ��ж�Ϊ����֮ǰ�û���ָ�����ƶ������ֵ��
	 */
	private int touchSlop;

	/**
	 * �Ƿ��Ѽ��ع�һ��layout������onLayout�еĳ�ʼ��ֻ�����һ��
	 */
	private boolean loadOnce;

	/**
	 * ��ǰ�Ƿ����������ֻ��ListView������ͷ��ʱ�����������
	 */
	private boolean ableToPull=false;

	/**
	 * ����ˢ�¿ؼ��Ĺ��캯������������ʱ��̬���һ������ͷ�Ĳ��֡�
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
	 * ����һЩ�ؼ��Եĳ�ʼ�����������磺������ͷ����ƫ�ƽ������أ���ListViewע��touch�¼���
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
	 * ��ListView������ʱ���ã����д����˸�������ˢ�µľ����߼���
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
				// �����ָ���ϻ�״̬����������ͷ����ȫ���صģ������������¼�
				if (distance <= 0
						&& headerLayoutParams.topMargin <= hideHeaderHeight) {
					return false;
				}
				if (distance < touchSlop) {
					return false;
				}
				if (currentStatus != REFRESH_REFRESHING) {
					// ͨ��ƫ������ͷ��topMarginֵ����ʵ������Ч��
					headerLayoutParams.topMargin = (distance / 2)
							+ hideHeaderHeight;
					header.setLayoutParams(headerLayoutParams);
					//����Header����
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
				// ����ʱ,���������ˢ�£�ˢ��
				if (currentStatus == RELEASE_TO_REFRESH) {
					new RefreshingTask().execute();
					return true;
				// ����ʱ,���������״̬����������ͷ
				} else if (currentStatus == PULL_TO_REFRESH) {
					hideHeader();
					return true;
				}
			}
			// ���������״̬������ˢ�£�����ListViewʧȥ���㣬���򱻵������һ���һֱ����ѡ��״̬
			if (currentStatus ==RELEASE_TO_REFRESH
					||currentStatus== PULL_TO_REFRESH) {
				listView.setPressed(false);
				listView.setFocusable(false);
				listView.setFocusableInTouchMode(false);
				// ��ǰ�������������ͷ�״̬��ͨ������true���ε�ListView�Ĺ����¼�
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
	 * ������ˢ�¿ؼ�ע��һ����������
	 * 
	 * @param listener
	 *            ��������ʵ�֡�
	 * @param id
	 *            Ϊ�˷�ֹ��ͬ���������ˢ�����ϴθ���ʱ���ϻ����г�ͻ�� �벻ͬ������ע������ˢ�¼�����ʱһ��Ҫ���벻ͬ��id��
	 */
	public void setOnRefreshListener(PullToRefreshListener listener, int id) {
		mListener = listener;
		mId = id;
	}

	/**
	 * ����ˢ�µ������ڴ������л�ȥ�ص�ע�����������ˢ�¼�������
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
	 * �����е�ˢ���߼���ɺ󣬼�¼����һ�£��������ListView��һֱ��������ˢ��״̬��
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
	 * ��������ͷ�е���Ϣ��
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
	 * ���ݵ�ǰ��״̬����ת��ͷ��
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
	 * ˢ������ͷ���ϴθ���ʱ�������������
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
			String value = timeIntoFormat + "����";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			timeIntoFormat = timePassed / ONE_HOUR;
			String value = timeIntoFormat + "Сʱ";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			timeIntoFormat = timePassed / ONE_DAY;
			String value = timeIntoFormat + "��";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			timeIntoFormat = timePassed / ONE_MONTH;
			String value = timeIntoFormat + "����";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		} else {
			timeIntoFormat = timePassed / ONE_YEAR;
			String value = timeIntoFormat + "��";
			updateAtValue = String.format(
					getResources().getString(R.string.updated_at), value);
		}
		updateAt.setText(updateAtValue);
	}
	/**
	 * ����ˢ�µļ�������ʹ������ˢ�µĵط�Ӧ��ע��˼���������ȡˢ�»ص���
	 * 
	 * @author allen
	 */
	public interface PullToRefreshListener {

		/**
		 * ˢ��ʱ��ȥ�ص��˷������ڷ����ڱ�д�����ˢ���߼���ע��˷����������߳��е��õģ� ����Բ������߳������к�ʱ������
		 */
		void onRefresh();
	}

}
