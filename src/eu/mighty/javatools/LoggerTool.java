package eu.mighty.javatools;

import android.app.Activity;
import android.util.Log;

public class LoggerTool {

	@SuppressWarnings("unused")
	private static String logStr = "";
	
	@SuppressWarnings("unused")
	private static Activity a;

	public static void setActivity(Activity aa) {
		a=aa;
	}
	
	public static void logIt(String pfx, String str) {
		String sa[] = str.split("\n");
		if (!pfx.trim().isEmpty()) {
			logStr += "<h1>" + pfx + "</h1><hr />\n";
		}
		for (int i = 0; i < sa.length; i++) {
			logStr += sa[i] + "<br />\n";
		}
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
