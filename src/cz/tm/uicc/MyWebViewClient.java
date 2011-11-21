package cz.tm.uicc;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MyWebViewClient extends WebViewClient {
	Activity _a;
	
	public MyWebViewClient(Activity anActivity) {
		Log.i("UXXU", "MyWebViewClient constructor");
		_a = anActivity;
	}
	
	/**
	 * called when webview receives an error (404,500, etc.)
	 */
	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		Log.d(getClass().getSimpleName(), "onReceiveError error='" + description + "'");
		Log.d(getClass().getSimpleName(), "onReceiveError url=" + failingUrl);

	}

	/**
	 * debug function to show how resources are loaded into webview
	 */
	@Override
	public void onLoadResource(WebView view, String url) {
		Log.d(getClass().getSimpleName(), "onLoadResource url=" + url);
	}

	/**
	 * function that is able to redirect certain links to others
	 * 
	 * can be used for login using sim when some [placeholder] is used
	 * inside url http://server/client/login/?login_token=[login_token]
	 * could be turned into
	 * http://server/client/login/?login_token=676a6asds6df76sdasas
	 */
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {

		Log.d(getClass().getSimpleName(), "try to load: " + url);
		return false;
	}

	/**
	 * debug function later can be used for time mesurements or capturing
	 * events
	 */
	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
		Log.d(getClass().getSimpleName(), "onPageStarted  Loading: " + url);
		//_a.setAcceptingMWEvents(false);
	}

	/**
	 * after this event is received loadUrl('javascript: cosi()') can only
	 * be used
	 * 
	 * shall include async queue for such event that shall rather be pulled
	 * after onPageFinished, between onPageStarted and
	 * onPageFinished(time-wise) should never be a JS call used, or even
	 * after mWDispatcherReady() shall such function be used. onPageStart
	 * shall flag acceptingMWEvents be set to false
	 */
	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		Log.d(getClass().getSimpleName(), "onPageFinished  Loading: " + url + ", acceptingMWEvents set to true");
		// TODO: might be re-moved from here, js function MWJSReady might be
		// enough
		//_a.setAcceptingMWEvents(true);
	}

}
