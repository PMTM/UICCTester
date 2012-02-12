package eu.mighty.javatools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class RestClient {

	private static final String TAG = "RestClient";

	public static JSONArray downloadJSONArray(String url, Activity a) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			BasicResponseHandler handler = new BasicResponseHandler();
			String result = httpclient.execute(httpget, handler);

			if (result != null) {
				Log.i(TAG, "result=" + result);
				JSONArray json = new JSONArray(result);
				return json;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(a, "ClientProtocolException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		} catch (IOException e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(a, "IOException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		} catch (JSONException e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(a, "JSONException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		}
		return new JSONArray();
	}

	public static String downloadString(String url, Activity a) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		try {
			BasicResponseHandler handler = new BasicResponseHandler();
			String result = httpclient.execute(httpget, handler);
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(a, "ClientProtocolException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		} catch (IOException e) {
			e.printStackTrace();

			Toast msg = Toast.makeText(a, "IOException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		}
		return "";
	}
	
	public static boolean createDirIfNotExists(String path) {
	    boolean ret = true;

	    File file = new File(Environment.getExternalStorageDirectory(), path);
	    if (!file.exists()) {
	        if (!file.mkdirs()) {
	            Log.e("TravellerLog :: ", "Problem creating Image folder");
	            ret = false;
	        }
	    }
	    return ret;
	}
	
	public static void writeToFile(String data) {
		String saveFileName = DateTimeUtil.getDTString(5) + ".log";
		FileOutputStream fos = null;
		String result = "";

		try {
			File root = Environment.getExternalStorageDirectory();

			if (root.canWrite()) {
				createDirIfNotExists("tests");
				fos = new FileOutputStream(root + "/tests/" + saveFileName );
				fos.write(data.getBytes("UTF-8"));
				result = "written to: "+saveFileName;
				fos.close();
			} else {
				result = "file cant write";
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = "file not written";
		}
		LoggerTool.logIt("UUUU",result);
	}

	public static void postString(String url, String data, Activity a) {
		DefaultHttpClient hc = new DefaultHttpClient();
		ResponseHandler<String> res = new BasicResponseHandler();

		HttpPost postMethod = new HttpPost(url);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		nameValuePairs.add(new BasicNameValuePair("json", data));
		nameValuePairs.add(new BasicNameValuePair("devID", DevID.getDevHashMap(a).toString()));

		LoggerTool.logIt(nameValuePairs.toString());
		
		String str ="{\"json\":"+data+",\"devId\":"+ DevID.getDevHashMap(a).toString()+"}";
		try {
			writeToFile(Base64.encodeBytes(str.getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String response = hc.execute(postMethod, res);

			Log.e("postLog", "server said : " + response);
		} catch (UnsupportedEncodingException e) {
			Log.e("postLog", "UnsupportedEncodingException");

			Toast msg = Toast.makeText(a, "UnsupportedEncodingException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		} catch (ClientProtocolException e) {
			Log.e("postLog", "ClientProtocolException");

			Toast msg = Toast.makeText(a, "ClientProtocolException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		} catch (IOException e) {
			Log.e("postLog", "IOException", e);

			Toast msg = Toast.makeText(a, "IOException: " + e.getMessage(), Toast.LENGTH_LONG);
			msg.setGravity(Gravity.CENTER, msg.getXOffset() / 2, msg.getYOffset() / 2);
			msg.show();
		}
	}

}