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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import eu.mighty.javatools.LoggerTool;
import eu.mighty.javatools.RestClient;
import eu.mighty.javatools.UtilConstants;

public class UICCTester extends Activity {

	private WebView wv = null;
	private String logStr = "";
	//private ScrollView mScrollView;
	private int selItem;

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

		wv.addJavascriptInterface(new ServiceHandler(this), "dtest");
		
		wv.loadUrl(UtilConstants.baseUrl);

		// mSpin = (Spinner) findViewById(R.id.sel);
		// mScrollView = (ScrollView) findViewById(R.id.ScrollView01);
		// mText = (TextView) findViewById(R.id.textView1);
		// mHelp = (TextView) findViewById(R.id.help);
		//
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, items);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// mSpin.setAdapter(adapter);
		//
		// mSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// Log.e("Spin", "Selected = " + arg2);
		// mHelp.setText(help[arg2]);
		// selItem = arg2;
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// }
		// });

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, CfgAct.class));
			return true;
		case R.id.postLog:
			RestClient.postString(UtilConstants.postUrl, "misc", this);
			return true;
		case R.id.getTest:
			wv.loadUrl(UtilConstants.testUrl);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
//		if (mSEService != null) {
//			mSEService.shutdown();
//		}
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