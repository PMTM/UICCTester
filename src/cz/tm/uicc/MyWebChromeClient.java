package cz.tm.uicc;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

public class MyWebChromeClient extends WebChromeClient {
	Activity _a;
	
	public MyWebChromeClient(Activity anActivity) {
		Log.i("UXXU", "MyWebChromeClient constructor");
		_a = anActivity;
	}
	/**
	 * called on JS alert function instead of original one
	 * 
	 * can be used for customized design
	 */
	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		Log.d(getClass().getSimpleName(), "JavaScriptAlert: " + message);

		Context context = _a.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();

		return true;
	}

	/**
	 * intercepts console messages including details, log level and content
	 */
	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

		Context context = _a.getApplicationContext();
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, consoleMessage.message(), duration);
		toast.show();

		return false;
	}

	/**
	 * certain progress bar handling, to be reengineered, imported from web
	 */
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		// setStatus(view.getUrl());
		//_a.setProgress(newProgress * 100);

		Log.d(getClass().getSimpleName(), String.format("onProgressChanged url : %s, progress: %s", view.getUrl(), newProgress));

		if (newProgress == 100) {
			Log.d(getClass().getSimpleName(), "Probably finished loading url in webview: " + view.getUrl());
		}
	}

}
