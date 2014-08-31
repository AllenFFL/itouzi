package com.allen.itouzi.engine;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import com.allen.itouzi.R;
import com.allen.itouzi.net.HttpsUtil;
import com.allen.itouzi.utils.DeviceInfoUtil;
import com.allen.itouzi.utils.FileUtil;

/**
 * �첽���صĺ����࣬����ͼƬ���ֻ����棬��ͼƬ����LruCache�еȵ�
 * 
 * @author zeng
 * 
 */
public class ImageLoader {
	private static final String TAG = "ImageLoader";
	/**
	 * LruCache����Image���࣬���洢Image�Ĵ�С����LruCache�趨��ֵ��ϵͳ�Զ��ͷ��ڴ�
	 */
	private LruCache<String, Bitmap> lruCache;
	private ExecutorService mImageThreadPool;
	private Activity context;
	private String subUrl;
	private int reqWidth, reqHeight = 0;
	private Bitmap netBitmap = null;
	private Bitmap nativeBitmap = null;
	private ImageView image = null;
	private Boolean FIRST_IMAGE = true;
	private float density;
	private ExecutorService downThreads;
	private static ImageLoader loader=null;
	
	public static ImageLoader getInstance(Activity context){
		if(loader==null)
			synchronized (ImageLoader.class) {
				if(loader==null)
					loader=new ImageLoader(context);
				return loader;
			}
		return loader;
	}
	private ImageLoader(Activity context) {
		this.context = context;
		// �õ���Ļ���--Imageview���
		DisplayMetrics dm = DeviceInfoUtil.getMetrics(context);
		density = dm.density;
		reqWidth = dm.widthPixels;
		// ��ȡϵͳ�����ÿ��Ӧ�õ�����ڴ�32M
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// final int cacheSize = maxMemory / 10;
		final int cacheSize = 3 * 1024 * 1024;
		Log.i(TAG, "reqWidth:" + reqWidth + "--cacheSize:" + cacheSize);
		lruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			// ������д�˷�����������Bitmap�Ĵ�С
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
				// return bitmap.getByteCount()/1024;
			};
		};
	}


	/**
	 * �洢����LruCache��
	 * 
	 * @param key
	 * @param bitmap
	 */
	public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
		if (lruCache.get(key) == null && bitmap != null) {
			lruCache.put(key, bitmap);
		}
	}

	/**
	 * ��ȡBitmap, �ڴ���û�о�ȥ�ֻ�����sd���л�ȡ����һ����getView�л���ã��ȽϹؼ���һ��
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getCacheBitmap(String subUrl) {
		if (lruCache.get(subUrl) != null) {
			return lruCache.get(subUrl);
		} else if (FileUtil.isFileExits(context,subUrl)) {
			Bitmap bitmap = FileUtil.getBitmap(context,subUrl);
			// ��Bitmap �����ڴ滺��
			addBitmapToMemoryCache(subUrl, bitmap);
			return bitmap;
		}
		return null;
	}

	/**
	 * �ȴ��ڴ��ж�Bitmap��û�оʹ��ֻ������SD���ж����������ڴ棻��û�о�ȥ���̳߳�����
	 * 
	 * @param url
	 * @param listener
	 * @return
	 */
	public void downLoadBitmap(final String url) {
		subUrl = url.replaceAll("[^\\w]", "");
		Log.i(TAG, "downLoadBitmap:"+subUrl);
		if (lruCache.get(subUrl) == null&&
				!FileUtil.isFileExits(context, subUrl)) {
			if(downThreads==null)
			downThreads = Executors.newFixedThreadPool(5);
			downThreads.execute(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = decodeNetBitmap(url);
					if (bitmap != null){
						Log.i(TAG, "downLoadBitmap:"+url);
						addBitmapToMemoryCache(subUrl, bitmap);
						FileUtil.saveBitmap(context,subUrl, bitmap);
					}
					}
				});
		}
	}

	public void setImageView(String url, ImageView image) {
		subUrl = url.replaceAll("[^\\w]", "");
		if(this.image != image)
			this.image = image;
			Bitmap cacheBitmap = getCacheBitmap(subUrl);
			if (cacheBitmap!= null) {
				setBitmap(cacheBitmap);
				Log.i(TAG, "setImageView:"+subUrl+" cacheBitmap:"+cacheBitmap);
			} else {
				new BitmapTask().execute(url);
			}
	}

	class BitmapTask extends AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = decodeNetBitmap(params[0]);
			if (bitmap != null){
				addBitmapToMemoryCache(subUrl, bitmap);
				FileUtil.saveBitmap(context,subUrl, bitmap);
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			if (image != null) {
				if (result != null) {
					//��ʾ�¼��ص�ͼƬ
					setBitmap(result);
				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * ����ԭͼ������Ŀ����ѹ��
	 * 
	 * @param is
	 * @param reqWidth
	 * @return
	 */
	public Bitmap decodeNetBitmap(String url) {
		InputStream is = null;
		int inSampleSize = 1;
		try {
			if (FIRST_IMAGE) {
				is = HttpsUtil.getHttpsBitmap(url);
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				BitmapFactory.decodeStream(is, null, options);
				int width = options.outWidth;
				int height = options.outHeight;
				float ration = (float) width / (float) height; // ����Ŀ��߶�
				reqHeight = (int) ((float) reqWidth / ration);
				if (width > reqWidth) {
					inSampleSize = width / reqWidth;
				}
				Log.i(TAG, "width:" + width + "--height:" + height
						+ "--reqWidth:" + reqWidth + "--reqHeight:" + reqHeight);
				is.close();
				FIRST_IMAGE = false;
			}
			is = HttpsUtil.getHttpsBitmap(url);
			BitmapFactory.Options options2 = new BitmapFactory.Options();
			options2.inSampleSize = inSampleSize;
			/**
			 * ALPHA_8 ÿ������ֻҪ1�ֽ�~��ϧֻ�ܴ���͸����,û����ɫ���� ARGB_4444
			 * ÿ������Ҫ2�ֽ�~��͸���ȵ���ɫ~��ϧ�ٷ����Ƽ�ʹ���� ARGB_8888 ÿ������Ҫ4�ֽ�~��͸���ȵ���ɫ, Ĭ��ɫ��
			 * RGB_565ÿ������Ҫ2�ֽ�~����͸���ȵ���ɫ
			 */
			options2.inPreferredConfig = Bitmap.Config.RGB_565;// ����ɫ����ʽ������ѹ��
			options2.inJustDecodeBounds = false;
			netBitmap = BitmapFactory.decodeStream(is, null, options2);
			is.close();
		} catch (IOException e) {
			Log.i(this.getClass().getName(),
					"getHttpsBitmap IOException!" + e.getLocalizedMessage());
		} catch (Exception e) {
			Log.i(this.getClass().getName(),
					"getHttpsBitmap exception!" + e.getLocalizedMessage());
		}

		return netBitmap;
	}

	public void setNativeBitmap(ImageView image) {
		this.image=image;
		if(nativeBitmap==null){
			int inSampleSize = 1;
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(context.getResources(),
					R.drawable.slider2, options);
			int width = options.outWidth;
			int height = options.outHeight;
			if (width > reqWidth) {
				inSampleSize = width / reqWidth;
			}
			options.inJustDecodeBounds =false;
			options.inSampleSize = inSampleSize;
			nativeBitmap = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.slider2, options);
		}
		setBitmap(nativeBitmap);
	}
	public void setBitmap(Bitmap result) {
		BitmapDrawable bd=(BitmapDrawable)image.getDrawable();
		if(null!=bd){
			Bitmap ibitmap = bd.getBitmap();
			if(null!=ibitmap&&!ibitmap.isRecycled()){
				ibitmap.recycle();
			}
		}
		image.setImageBitmap(result);
	}
	/**
	 * �õ�Ŀ��Image��չʾ�߶�
	 * @return
	 */
	public int getHeight() {
		if (reqHeight != 0) {
			return reqHeight;
		} else {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(context.getResources(),
					R.drawable.slider2, options);
			int width = options.outWidth;
			int height = options.outHeight;
			float ration = (float) width / (float) height; // ����Ŀ��߶�
			reqHeight = (int) ((float) reqWidth / ration);
			return reqHeight;
		}
	}
	/**
	 * ����ѹ������
	 * 
	 * @param options
	 *            ԭͼ����
	 * @param reqWidth
	 *            Ŀ����
	 * @param reqHeight
	 *            Ŀ��߶�
	 * @return
	 */
	public static int getInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
			final int HeihgtRadio = Math.round((float) height
					/ (float) reqHeight);
			final int widthnRadio = Math
					.round((float) width / (float) reqWidth);
			inSampleSize = HeihgtRadio > widthnRadio ? widthnRadio
					: HeihgtRadio;
			// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
			// // һ��������ڵ���Ŀ��Ŀ�͸ߡ�
		}
		return inSampleSize;
	}

	public static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}
	public void shutThreads(){
		if(downThreads!=null)
			downThreads.shutdown();
	}
}
