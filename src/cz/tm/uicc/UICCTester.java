package cz.tm.uicc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import eu.mighty.javatools.JSON2Java;
import eu.mighty.javatools.LoggerTool;
import eu.mighty.javatools.RestClient;
import eu.mighty.javatools.UtilConstants;

//android:theme="@android:style/Theme.Light.NoTitleBar.Fullscreen">

public class UICCTester extends Activity {

	private WebView wv = null;
	private String logStr = "";
	private int selItem;
	private ServiceHandler mServiceHandler = null;

	private static final String TAG = "UIIU";

	public Handler mMsgHandle = new Handler() {
		public void handleMessage(Message msg) {
			if (wv != null) {
				wv.loadUrl("javascript:seReady('" + (String) msg.obj + "')");
			}
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.webview);
		wv = (WebView) findViewById(R.id.wv);
		WebSettings webSettings = wv.getSettings();
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(false);
		webSettings.setUseWebViewBackgroundForOverscrollBackground(true);

		wv.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		wv.clearCache(true);
		wv.setWebChromeClient(new MyWebChromeClient(this));
		wv.setWebViewClient(new MyWebViewClient(this));

		mServiceHandler = new ServiceHandler(this);
		wv.addJavascriptInterface(mServiceHandler, "dtest");

		wv.loadUrl(UtilConstants.baseUrl);

		ActivityInfo ac_i;
		ApplicationInfo ap_i;

		// Toast.makeText(getApplicationContext(),
		// "componet name: " + this.getComponentName(), Toast.LENGTH_LONG)
		// .show();
		try {
			ap_i = getPackageManager().getApplicationInfo(
					this.getPackageName(), PackageManager.GET_META_DATA);

			ac_i = getPackageManager().getActivityInfo(this.getComponentName(),
					PackageManager.GET_META_DATA);

			Bundle bundle = ap_i.metaData;

			if (bundle != null) {
				String value = (String) bundle.get("zoo");

				Log.d(TAG, "value =" + value);

				// Toast.makeText(getApplicationContext(), "from meta: " +
				// value,
				// Toast.LENGTH_LONG).show();
				// } else {
				// Toast.makeText(getApplicationContext(), "no bundle",
				// Toast.LENGTH_LONG).show();

			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (wv.canGoBack() == true) {
					wv.goBack();
				} else {
					finish();
				}
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			// startActivity(new Intent(this, CfgPrefs.class));
			startActivity(new Intent(this, CfgAct.class));
			return true;
		case R.id.postLog:
			RestClient.postString(UtilConstants.postUrl,
					JSON2Java.jsonFromArray(mServiceHandler.logItems), this);
			return true;
		case R.id.getTest:
			wv.loadUrl(UtilConstants.baseUrl);
			return true;
		case R.id.help:
			wv.loadUrl(UtilConstants.aboutUrl);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		// if (mSEService != null) {
		// mSEService.shutdown();
		// }
		super.onDestroy();
	}

	public void e_cleanLog() {
		logStr = "";
		// mText.setText(logStr);
	}

	public void e_postLog() {
		String link = UtilConstants.getPostUrl(this);

		LoggerTool.logIt("UUUU", link);

		DefaultHttpClient hc = new DefaultHttpClient();
		ResponseHandler<String> res = new BasicResponseHandler();

		HttpPost postMethod = new HttpPost(link);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		nameValuePairs.add(new BasicNameValuePair("log", logStr));

		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String response = hc.execute(postMethod, res);

			Log.e("postLog", "server said : " + response);
		} catch (UnsupportedEncodingException e) {
			Log.e("postLog", "UnsupportedEncodingException");
		} catch (ClientProtocolException e) {
			Log.e("postLog", "ClientProtocolException");
		} catch (IOException e) {
			Log.e("postLog", "IOException", e);
		}
	}

	public void goBut(View view) {
		LoggerTool.logIt("UUUU", "goBut:" + selItem);
	}

}