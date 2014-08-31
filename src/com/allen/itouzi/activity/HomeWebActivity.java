package com.allen.itouzi.activity;

import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.Enumeration;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.renderscript.Sampler.Value;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.itouzi.R;
import com.allen.itouzi.utils.FileUtil;
import com.allen.itouzi.utils.ValueUtil;
import com.umeng.analytics.MobclickAgent;

public class HomeWebActivity extends Activity implements OnClickListener {
	private static final String TAG = "HomeWebActivity";
	private WebView web_view;
	private ProgressDialog progDlg;
	private String Url;
	private PrivateKey mPrivateKey;
	private X509Certificate[] mX509Certificates;
	private ImageView web_back;
	private TextView web_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.web_view);
		web_title = (TextView) findViewById(R.id.web_title);
		web_back = (ImageView) findViewById(R.id.web_back);
		web_back.setOnClickListener(this);
		Intent intent = getIntent();
		Url = intent.getStringExtra("homeURL");
		if (Url == null) {
			Url = intent.getStringExtra("registerURL");
			if (Url.equals(ValueUtil.contractRegisterURL)) {
				web_title.setText("用户注册协议");
			} else if (Url.equals(ValueUtil.contractPrivacyURL)) {
				web_title.setText("隐私条款");
			}
		} else {
			web_title.setText("活动详情");
		}

		web_view = (WebView) findViewById(R.id.web_view);
		web_view.getSettings().setUseWideViewPort(true);
		web_view.getSettings().setBuiltInZoomControls(true);
		web_view.getSettings().setLoadWithOverviewMode(true);
		web_view.getSettings().setJavaScriptEnabled(true);
		// webSet.setSupportZoom(true);
		// webSet.setDefaultTextEncodingName("UTF-8");
		// webSet.setJavaScriptEnabled(true);
		web_view.loadUrl(Url);

		web_view.setWebViewClient(new WebViewClient() {
			// @Override
			// public boolean shouldOverrideUrlLoading(WebView view, String url)
			// {
			// final Uri uri = Uri.parse(url);
			// String scheme = uri.getScheme();
			//
			// if (scheme.equals("itouzi")) {
			// Log.i("host", uri.getHost());
			// if (uri.getHost().equals("alert")) {
			// AlertDialog.Builder builder = new AlertDialog.Builder(
			// HomeWebActivity.this);
			// builder.setMessage((String) uri
			// .getQueryParameter("content"));
			// builder.setPositiveButton("确定",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog,
			// int which) {
			// finish();
			// if (null != uri) {
			// //TODO:back？？
			// }
			// }
			// });
			// builder.show();
			// }
			// if (uri.getHost().equals("finished")) {
			// Intent intent= new Intent(getApplicationContext(),
			// MainActivity.class);
			// intent.putExtra("fragment", 2);
			// startActivity(intent);
			// finish();
			// }
			//
			// if (uri.getHost().equals("openpage")) { // open a page
			//
			// String openURL = uri.getQueryParameter("url");
			// Log.i("info", openURL);
			// if (openURL != null) {
			// view.loadUrl(openURL);
			// return true;
			// }
			// }
			// return false;
			// }
			// web_view.loadUrl(url);
			// return true;
			// }

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if (progDlg == null) {
					progDlg = new ProgressDialog(HomeWebActivity.this);
					progDlg.setMessage("正在加载，请稍候...");
				}
				progDlg.show();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				progDlg.dismiss();
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
				super.onReceivedSslError(view, handler, error);
			}
		});
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {
			web_view.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void showToast(String string) {
		Toast.makeText(getApplication(), string, 1).show();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.web_back) {
			finish();
		}
	}
}

// Uri uri = Uri.parse(url);
// if(uri.getHost().equals("10.0.2.2")){
// view.loadUrl(url);
// } else {
// Intent intent = new Intent();
// intent.setAction(Intent.ACTION_VIEW);
// intent.setData(uri);
// startActivity(intent);
// }

// web_view.setWebChromeClient(new WebChromeClient(){
//
// @Override
// public void onProgressChanged(WebView view, int newProgress) {
// progDlg.setMessage("已经加载 :"+newProgress+" % ,请稍候。。");
// }
//
// @Override
// public boolean onJsAlert(WebView view, String url, String message,
// final JsResult result) {
// AlertDialog.Builder dlg = new AlertDialog.Builder(getApplicationContext());
// dlg.setMessage(message);
// dlg.setTitle("提示");
// // dlg.setTitle(null);
// dlg.setCancelable(false);
// dlg.setPositiveButton(android.R.string.ok,
// new AlertDialog.OnClickListener() {
// public void onClick(DialogInterface dialog, int which) {
// result.confirm();
// }
// });
// dlg.create();
// dlg.show();
// return true;
// }
//
// @Override
// public boolean onJsConfirm(WebView view, String url,
// String message, final JsResult result) {
// AlertDialog.Builder dlg = new AlertDialog.Builder(getApplicationContext());
// dlg.setMessage(message);
// dlg.setTitle("提示");
// dlg.setCancelable(true);
// dlg.setPositiveButton(android.R.string.ok,
// new DialogInterface.OnClickListener() {
// public void onClick(DialogInterface dialog, int which) {
// result.confirm();
// }
// });
// dlg.setNegativeButton(android.R.string.cancel,
// new DialogInterface.OnClickListener() {
// public void onClick(DialogInterface dialog, int which) {
// result.cancel();
// }
// });
// dlg.setOnCancelListener(new DialogInterface.OnCancelListener() {
// public void onCancel(DialogInterface dialog) {
// result.cancel();
// }
// });
// dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
// // DO NOTHING
// public boolean onKey(DialogInterface dialog, int keyCode,
// KeyEvent event) {
// if (keyCode == KeyEvent.KEYCODE_BACK) {
// result.cancel();
// return false;
// } else
// return true;
// }
// });
// dlg.create();
// dlg.show();
// return true;
// }
//
// @Override
// public boolean onJsPrompt(WebView view, String url, String message,
// String defaultValue, JsPromptResult result) {
// final JsPromptResult res = result;
// AlertDialog.Builder dlg = new AlertDialog.Builder(getApplicationContext());
// dlg.setMessage(message);
// final EditText input = new EditText(getApplicationContext());
// if (defaultValue != null) {
// input.setText(defaultValue);
// }
// dlg.setView(input);
// dlg.setCancelable(false);
// dlg.setPositiveButton(android.R.string.ok,
// new DialogInterface.OnClickListener() {
// public void onClick(DialogInterface dialog,
// int which) {
// String usertext = input.getText().toString();
// res.confirm(usertext);
// }
// });
// dlg.setNegativeButton(android.R.string.cancel,
// new DialogInterface.OnClickListener() {
// public void onClick(DialogInterface dialog,
// int which) {
// res.cancel();
// }
// });
// dlg.create();
// dlg.show();
// return true;
// }
//
// @Override
// public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
// // TODO Auto-generated method stub
// return super.onConsoleMessage(consoleMessage);
// }
// });
// web_view.addJavascriptInterface(new Object(){
// public void callFromJS(String msg){
// showToast(msg);
// }
// }, "itouzi");
//

// private void initPrivateKeyAndX509Certificate()
// throws Exception {
// KeyStore keyStore;
// // 创建一个证书库，并将证书导入证书库
// KeyStore keyStore2 = KeyStore.getInstance("PKCS12", "BC");
// keyStore.load(this.getResources().openRawResource(R.raw.client),
// CERTFILE_PASSWORD.toCharArray());
// Enumeration<?> localEnumeration;
// localEnumeration = keyStore.aliases();
// while (localEnumeration.hasMoreElements()) {
// String str3 = (String) localEnumeration.nextElement();
// mPrivateKey = (PrivateKey) keyStore.getKey(str3,
// CERTFILE_PASSWORD.toCharArray());
// if (mPrivateKey == null) {
// continue;
// } else {
// Certificate[] arrayOfCertificate = keyStore2.getCertificateChain(str3);
// mX509Certificates = new X509Certificate[arrayOfCertificate.length];
// for (int j = 0; j < mX509Certificates.length; j++) {
// mX509Certificates[j] = ((X509Certificate) arrayOfCertificate[j]);
// }
// }
// }
// }
//
//
// public class BasicWebViewClientEx extends WebViewClient {
//
//
// private X509Certificate[] certificatesChain;
// private PrivateKey clientCertPrivateKey;
//
// public BasicWebViewClientEx(AbstractActivity activity) {
// mActivity = activity;
// certificatesChain = getX509Certificates();//此处就是上文中的mX509Certificates
// clientCertPrivateKey = getPrivateKey();//次处就是上文中的mPrivateKey
// }
//
// public void onReceivedClientCertRequest(WebView view,
// ClientCertRequestHandler handler, String host_and_port) {
// //注意该方法是调用的隐藏函数接口。这儿是整个验证的技术难点：就是如何调用隐藏类的接口。
// //方法：去下载一个android4.2版本全编译后的class.jar 然后导入到工程中
// if((null != clientCertPrivateKey) && ((null!=certificatesChain) &&
// (certificatesChain.length !=0))){
// handler.proceed(this.clientCertPrivateKey, this.certificatesChain);
// }else{
// handler.cancel();
// }
// }
//
//
//
// @Override
// public void onReceivedSslError(final WebView view, SslErrorHandler handler,
// SslError error) {
// handler.proceed();
// }

// }
