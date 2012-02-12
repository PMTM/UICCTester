package eu.mighty.javatools;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UtilConstants {
	public final static String srv = "server";
	public final static String postUrl = "http://"+srv+"/uicc/post.php";
	public final static String testUrl = "http://"+srv+"/uicc/test.php";
	public final static String baseUrl = "file:///android_asset/home.html";
	public final static String aboutUrl = "file:///android_asset/about.html";
	public final static String baseUrl2 = "http://"+srv+"/uicc/base.php";

	public static String getPostUrl(Activity a) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(a.getBaseContext());
		String host = prefs.getString("logServer", "server");

		String link = "http://" + host + "/uicc/post.php";
		return link;
	}
}
