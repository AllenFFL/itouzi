package com.allen.itouzi.net;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManagerFactory;
import javax.security.cert.X509Certificate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.allen.itouzi.utils.ValueUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HttpsUtil {

	private static final int CONNECT_TIMEOUT = 10000;
	private static final String TAG = "HttpsUtils";

	/**
	 * 
	 * @param serverURL
	 * @return
	 * @throws Exception
	 */
	public static String doHttpGet(String serverURL) throws Exception {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, CONNECT_TIMEOUT);
		HttpClient hc = new DefaultHttpClient();
		HttpGet get = new HttpGet(serverURL);
		get.addHeader("Content-Type", "text/xml");
		get.setParams(httpParameters);
		HttpResponse response = null;
		try {
			response = hc.execute(get);
		} catch (UnknownHostException e) {
			throw new Exception("Unable to access " + e.getLocalizedMessage());
		} catch (SocketException e) {
			throw new Exception(e.getLocalizedMessage());
		}
		int sCode = response.getStatusLine().getStatusCode();
		if (sCode == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else
			throw new Exception("StatusCode is " + sCode);
	}

	/**
	 * 
	 * @param serverURL
	 * @param xmlString
	 * @return
	 * @throws Exception
	 */
	public static String doHttpPost(String serverURL, String xmlString)
			throws Exception {
		HttpParams httpParameters = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters,
				CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParameters, CONNECT_TIMEOUT);
		HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
		HttpClient hc = new DefaultHttpClient();
		HttpPost post = new HttpPost(serverURL);
		post.addHeader("Content-Type", "text/xml");
		post.setEntity(new StringEntity(xmlString, "UTF-8"));
		post.setParams(httpParameters);
		HttpResponse response = null;
		try {
			response = hc.execute(post);
		} catch (UnknownHostException e) {
			throw new Exception("Unable to access " + e.getLocalizedMessage());
		} catch (SocketException e) {
			throw new Exception(e.getLocalizedMessage());
		}
		int sCode = response.getStatusLine().getStatusCode();
		if (sCode == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else
			throw new Exception("StatusCode is " + sCode);
	}

	/**
	 * 
	 * @param serverURL
	 * @return
	 * @throws Exception
	 */
	public static String doHttpsGet(String serverURL) throws Exception {
		HttpParams params = new BasicHttpParams();
		// 设置连接管理器的超时
		ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(params, CONNECT_TIMEOUT);
		HttpClient httpclient = getNewHttpClient(params);

		HttpGet get = new HttpGet(serverURL);
		get.addHeader("APIVERSION", ValueUtil.APIVERSION);
		// get.addHeader("Content-Type","Application/json");
		get.setParams(params);
		HttpResponse response = null;
		try {
			response = httpclient.execute(get);

		} catch (ClientProtocolException e) {
			throw new Exception("doHttpsGet exception:"
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			throw new Exception("doHttpsGet exception:"
					+ e.getLocalizedMessage());
		}
		int scode = response.getStatusLine().getStatusCode();
		if (scode == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else {
			throw new Exception("StatusCode is " + scode);
		}
	}

	/**
	 * 带token的get请求
	 * 
	 * @param serverURL
	 * @return
	 * @throws Exception
	 */
	public static String doHttpsGetT(String serverURL, String token)
			throws Exception {
		HttpParams params = new BasicHttpParams();
		// 设置连接管理器的超时
		ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(params, CONNECT_TIMEOUT);
		HttpClient httpclient = getNewHttpClient(params);

		HttpGet get = new HttpGet(serverURL);
		get.addHeader("TOKEN", token);
		get.addHeader("APIVERSION", ValueUtil.APIVERSION);
		get.setParams(params);
		HttpResponse response = null;
		try {
			response = httpclient.execute(get);

		} catch (ClientProtocolException e) {
			throw new Exception("doHttpsGet exception:"
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			throw new Exception("doHttpsGet exception:"
					+ e.getLocalizedMessage());
		}
		int scode = response.getStatusLine().getStatusCode();
		if (scode == HttpStatus.SC_OK) {
			return EntityUtils.toString(response.getEntity());
		} else {
			throw new Exception("StatusCode is " + scode);
		}
	}

	/**
	 * 
	 * @param serverURL
	 * @param jsonPost
	 * @return
	 * @throws Exception
	 */
	public static String doHttpsPost(String serverURL, String jsonPost)
			throws Exception {
		HttpParams params = new BasicHttpParams();
		// 设置连接管理器的超时
		ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(params, CONNECT_TIMEOUT);
		DefaultHttpClient httpclient = getNewHttpClient(params);
		HttpPost post = new HttpPost(serverURL);
		post.addHeader("APIVERSION", ValueUtil.APIVERSION);
		// post.addHeader("Content-Type", "Application/json");//TODO:？？key:value
		// post.addHeader("Content-Type", "multipart/form-data");
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");// "key1=value1&key2=value2"
		post.setEntity(new StringEntity(jsonPost, "UTF-8"));

		post.setParams(params);
		HttpResponse response = null;
		try {
			response = httpclient.execute(post);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent()));
					StringBuffer sb = new StringBuffer();
					String str = null;
					while ((str = reader.readLine()) != null) {
						sb.append(str);
					}
					entity.consumeContent();
					return sb.toString().trim();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return "";

	}

	/**
	 * 带token的post请求
	 * 
	 * @param serverURL
	 * @param jsonPost
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public static String doHttpsPostT(String serverURL, String jsonPost,
			String token) throws Exception {
		HttpParams params = new BasicHttpParams();
		// 设置连接管理器的超时
		ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(params, CONNECT_TIMEOUT);
		DefaultHttpClient httpclient = getNewHttpClient(params);
		HttpPost post = new HttpPost(serverURL);
		post.addHeader("Content-Type", "application/x-www-form-urlencoded");// "key1=value1&key2=value2"
																			// APIVERSION
																			// 1.1
		if (token != null) {
			post.addHeader("TOKEN", token);
		}
		post.addHeader("APIVERSION", ValueUtil.APIVERSION);
		post.setEntity(new StringEntity(jsonPost, "UTF-8"));

		post.setParams(params);
		HttpResponse response = null;
		try {
			response = httpclient.execute(post);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent()));
					StringBuffer sb = new StringBuffer();
					String str = null;
					while ((str = reader.readLine()) != null) {
						sb.append(str);
					}
					entity.consumeContent();
					return sb.toString().trim();
				}
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return "";

	}

	private static DefaultHttpClient getNewHttpClient(HttpParams params) {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);// TODO:STRICT_HOSTNAME_VERIFIER
			// ALLOW_ALL_HOSTNAME_VERIFIER
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);

			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient(params);
		}
	}

	public static InputStream getHttpsBitmap(String imagURL) throws Exception {
		HttpParams params = new BasicHttpParams();
		// 设置连接管理器的超时
		ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(params, CONNECT_TIMEOUT);
		HttpClient httpclient = getNewHttpClient(params);

		HttpGet get = new HttpGet(imagURL);
		// get.addHeader("Content-Type","Application/json");
		get.setParams(params);
		HttpResponse response = null;
		try {
				response = httpclient.execute(get);
				int scode = response.getStatusLine().getStatusCode();
				if (scode == HttpStatus.SC_OK) {
					HttpEntity entity = response.getEntity();
					InputStream in = entity.getContent();
		//			BitmapFactory.Options option=new BitmapFactory.Options();
		//			option.inJustDecodeBounds=true;
		//			Bitmap bitmap = BitmapFactory.decodeStream(in, null, option);
		//			in.close();
					return in;
				} else {
					throw new Exception("StatusCode is " + scode);
				}
			} catch (ClientProtocolException e) {
				throw new Exception("https protocol exception:"
						+ e.getLocalizedMessage());
			} catch (IOException e) {
				throw new Exception(e.getLocalizedMessage());
			}
	}

	public static HttpEntity getHttpsEntity(String imagURL) throws Exception {
		HttpParams params = new BasicHttpParams();
		// 设置连接管理器的超时
		ConnManagerParams.setTimeout(params, CONNECT_TIMEOUT);
		// 设置连接超时
		HttpConnectionParams.setConnectionTimeout(params, CONNECT_TIMEOUT);
		// 设置socket超时
		HttpConnectionParams.setSoTimeout(params, CONNECT_TIMEOUT);
		HttpClient httpclient = getNewHttpClient(params);

		HttpGet get = new HttpGet(imagURL);
		// get.addHeader("Content-Type","Application/json");
		get.setParams(params);
		HttpResponse response = null;
		try {
			response = httpclient.execute(get);
		} catch (ClientProtocolException e) {
			throw new Exception("https protocol exception:"
					+ e.getLocalizedMessage());
		} catch (IOException e) {
			throw new Exception(e.getLocalizedMessage());
		}
		int scode = response.getStatusLine().getStatusCode();
		if (scode == HttpStatus.SC_OK) {
			HttpEntity entity = response.getEntity();
			return entity;
		} else {
			throw new Exception("StatusCode is " + scode);
		}
	}

	/**
	 * 从Url中获取Bitmap
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		Bitmap bitmap = null;
		HttpURLConnection connection = null;
		try {
			URL url2 = new URL(url);
			connection = (HttpURLConnection) url2.openConnection();
			connection.setConnectTimeout(50000);
			connection.connect();
			InputStream is = connection.getInputStream();
			// bitmap = BitmapFactory.decodeStream(is);1.6版本有bug
			// 解释http://blog.csdn.net/andypan1314/article/details/6670320
			if (is == null) {
				throw new RuntimeException("InputStream is null!");
			} else {
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					outStream.write(buffer, 0, len);
				}
				outStream.close();
				is.close();
				byte[] byteArray = outStream.toByteArray();
				BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return bitmap;
	}

	// 利用HttpsURLConnection联网
	// InputStream in;
	// try {
	// KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
	// keyStore.load(null, null);
	// String algorithm = TrustManagerFactory.getDefaultAlgorithm();
	// TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
	// tmf.init(keyStore);
	// SSLContext context = SSLContext.getInstance("TLS");
	// context.init(null, tmf.getTrustManagers(), null);
	//
	// URL url = new URL(imagURL);
	// HttpsURLConnection urlConnection = (HttpsURLConnection)
	// url.openConnection();
	// urlConnection.setSSLSocketFactory(context.getSocketFactory());
	// in = urlConnection.getInputStream();
	// Bitmap bitmap = BitmapFactory.decodeStream(in);
	// in.close();
	// return bitmap;
	// } catch (KeyManagementException e) {
	// throw new
	// Exception("getBitmap KeyManagementException:"+e.getLocalizedMessage());
	// } catch (KeyStoreException e) {
	// throw new
	// Exception("getBitmap KeyStoreException:"+e.getLocalizedMessage());
	// } catch (NoSuchAlgorithmException e) {
	// throw new
	// Exception("getBitmap NoSuchAlgorithmException:"+e.getLocalizedMessage());
	// } catch (CertificateException e) {
	// throw new
	// Exception("getBitmap CertificateException:"+e.getLocalizedMessage());
	// } catch (MalformedURLException e) {
	// throw new
	// Exception("getBitmap MalformedURLException:"+e.getLocalizedMessage());
	// } catch (IOException e) {
	// throw new Exception("getBitmap IOException:"+e.getLocalizedMessage());
	// }

	// }
}
