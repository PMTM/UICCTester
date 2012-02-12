package eu.mighty.javatools;

import android.util.Log;
import android.widget.TextView;

/**
 * Logger class
 * 
 * @author moravekp
 *
 */
public class Xog {
	
	public static TextView ltv;

	public static TextView getLtv() {
		return ltv;
	}

	public static void setLtv(TextView ltv) {
		Xog.ltv = ltv;
	}

	public static void u(String tag, String text) {
		if (ltv != null) {
			ltv.setText(ltv.getText() + tag + ":" + text + "\n");
		}
		Log.d(tag, text);
	}

	public static void d(String tag, String text) {
		if (ltv != null) {
			ltv.setText(ltv.getText() + tag + ":" + text + "\n");
		}
		Log.d(tag, text);
	}

	public static void e(String tag, String text) {
		if (ltv != null) {
			ltv.setText(ltv.getText() + tag + ":" + text + "\n");
		}
		Log.e(tag, text);
	}

	public static void i(String tag, String text) {
		if (ltv != null) {
			ltv.setText(ltv.getText() + tag + ":" + text + "\n");
		}
		Log.i(tag, text);
	}
}