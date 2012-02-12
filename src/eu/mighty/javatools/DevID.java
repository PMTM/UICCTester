package eu.mighty.javatools;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;

public class DevID {
	
	public static JSONObject getDevHashMap(Activity a) {
		JSONObject obj = new JSONObject();

		TelephonyManager tm = (TelephonyManager)a.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			obj.put("IMEI",tm.getDeviceId());
			obj.put("BOARD",Build.BOARD); 
			obj.put("CPU_ABI",Build.CPU_ABI); 
			obj.put("DEVICE",Build.DEVICE); 
			obj.put("DISPLAY",Build.DISPLAY); 
			obj.put("HOST",Build.HOST);

			obj.put("ID",Build.ID); 
			obj.put("MANUFACTURER",Build.MANUFACTURER); 
			obj.put("MODEL",Build.MODEL); 
			obj.put("PRODUCT",Build.PRODUCT); 
			obj.put("TAGS",Build.TAGS);

			obj.put("TYPE",Build.TYPE); 
			obj.put("USER",Build.USER);
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(a.getBaseContext());
			String email = prefs.getString("email", "no-email");
			
			obj.put("EMAIL",email);
			
		} catch (JSONException e) {
			e.printStackTrace();
			LoggerTool.logIt(e.toString());
		};
		return obj;
	}
}
