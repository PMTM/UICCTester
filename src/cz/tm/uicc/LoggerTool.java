package cz.tm.uicc;

import android.util.Log;
import android.widget.ScrollView;

public class LoggerTool {
	
	private static ScrollView mScrollView = null;
	private static String logStr = "";
	
	public static void logIt(String pfx, String str) {
		String sa[] = str.split("\n");
		if (!pfx.trim().isEmpty()) {
			logStr += "<h1>" + pfx + "</h1><hr />\n";
		}
		for (int i = 0; i < sa.length; i++) {
			logStr += sa[i] + "<br />\n";
		}
		// mText.setText(Html.fromHtml(logStr));
		mScrollView.post(new Runnable() {
			public void run() {
				mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}

		});
		if (!pfx.trim().isEmpty()) {
			Log.e(pfx, str);
		} else {
			Log.e("Exc", str);
		}
	}

	public static void logIt(String str) {
		logIt("", str);
	}

	public static void logIt(String pfx, String str, Exception e) {
		logIt(pfx, str);
		String msg = e.toString();
		logIt("Exception", msg + "\n");
	}

}
