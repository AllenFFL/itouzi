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
 * 异步下载的核心类，保存图片到手机缓存，将图片加入LruCache中等等
 * 
 * @author zeng
 * 
 */
public class ImageLoader {
	private static final String TAG = "ImageLoader";
	/**
	 * LruCache缓存Image的类，当存储Image的大小大于LruCache设定的值，系统自动释放内存
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
		// 得到屏幕宽度--Imageview宽度
		DisplayMetrics dm = DeviceInfoUtil.getMetrics(context);
		density = dm.density;
		reqWidth = dm.widthPixels;
		// 获取系统分配给每个应用的最大内存32M
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		// final int cacheSize = maxMemory / 10;
		final int cacheSize = 3 * 1024 * 1024;
		Log.i(TAG, "reqWidth:" + reqWidth + "--cacheSize:" + cacheSize);
		lruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			// 必须重写此方法，来测量Bitmap的大小
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
				// return bitmap.getByteCount()/1024;
			};
		};
	}


	/**
	 * 存储对象到LruCache中
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
	 * 获取Bitmap, 内存中没有就去手机或者sd卡中获取，这一步在getView中会调用，比较关键的一步
	 * 
	 * @param url
	 * @return
	 */
	public Bitmap getCacheBitmap(String subUrl) {
		if (lruCache.get(subUrl) != null) {
			return lruCache.get(subUrl);
		} else if (FileUtil.isFileExits(context,subUrl)) {
			Bitmap bitmap = FileUtil.getBitmap(context,subUrl);
			// 将Bitmap 加入内存缓存
			addBitmapToMemoryCache(subUrl, bitmap);
			return bitmap;
		}
		return null;
	}

	/**
	 * 先从内存中读Bitmap；没有就从手机缓存或SD卡中读，并存入内存；还没有就去开线程池下载
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
					//显示新加载的图片
					setBitmap(result);
				}
			}
			super.onPostExecute(result);
		}
	}

	/**
	 * 根据原图比例和目标宽度压缩
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
				float ration = (float) width / (float) height; // 计算目标高度
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
			 * ALPHA_8 每个像素只要1字节~可惜只能代表透明度,没有颜色属性 ARGB_4444
			 * 每个像素要2字节~带透明度的颜色~可惜官方不推荐使用了 ARGB_8888 每个像素要4字节~带透明度的颜色, 默认色样
			 * RGB_565每个像素要2字节~不带透明度的颜色
			 */
			options2.inPreferredConfig = Bitmap.Config.RGB_565;// 设置色彩样式，继续压缩
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
	 * 得到目标Image的展示高度
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
			float ration = (float) width / (float) height; // 计算目标高度
			reqHeight = (int) ((float) reqWidth / ration);
			return reqHeight;
		}
	}
	/**
	 * 计算压缩比例
	 * 
	 * @param options
	 *            原图参数
	 * @param reqWidth
	 *            目标宽度
	 * @param reqHeight
	 *            目标高度
	 * @return
	 */
	public static int getInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int HeihgtRadio = Math.round((float) height
					/ (float) reqHeight);
			final int widthnRadio = Math
					.round((float) width / (float) reqWidth);
			inSampleSize = HeihgtRadio > widthnRadio ? widthnRadio
					: HeihgtRadio;
			// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			// // 一定都会大于等于目标的宽和高。
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
