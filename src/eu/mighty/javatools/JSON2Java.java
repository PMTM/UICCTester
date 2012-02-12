package eu.mighty.javatools;

//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URL;
//import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 *formatting a JSON response into Map and List
 */
public class JSON2Java {

	// This is how you use it.
	@SuppressWarnings("unchecked")
	public static void main(String a[]) throws Exception {
		//JSON2Java j = new JSON2Java();
		String jsonStr = "{\"id\":101,\"person\":{\"fname\":\"foo\", \"lname\":\"bar\"}}";
		Map<String, Object> resp = getMap(jsonStr);
		System.out.println("Response message = "
				+ ((Map<String, Map<String, String>>) resp.get("person"))
						.get("lname"));
	}

	/**
	 * getList provides a List representation of the JSON Object
	 * 
	 * @param jsonResponse
	 *            The JSON array string
	 * @return List of JSONObject.
	 **/
	protected List<Object> getList(String jsonResponse) throws Exception {
		List<Object> listResponse = new ArrayList<Object>();
		if (jsonResponse.startsWith("[")) {
			JSONArray jsonArray = new JSONArray(jsonResponse);
			toJavaList(jsonArray, listResponse);
		} else {
			throw new Exception("MalFormed JSON Array Response.");
		}

		return listResponse;
	}

	/**
	 * getMap provides a Map representation of the JSON Object
	 * 
	 * @param jsonResponse
	 *            The JSON object string
	 * @return Map of JSONObject.
	 **/
	public static Map<String, Object> getMap(String jsonResponse) throws Exception {
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		if (jsonResponse.startsWith("{")) {
			JSONObject jsonObj = new JSONObject(jsonResponse);
			toJavaMap(jsonObj, mapResponse);
		} else {
			throw new Exception("MalFormed JSON Array Response.");
		}
		return mapResponse;
	}

	/**
	 * toJavaMap converts the JSONObject into a Java Map
	 * 
	 * @param o
	 *            JSONObject to be converted to Java Map
	 * @param b
	 *            Java Map to hold converted JSONObject response.
	 **/
	@SuppressWarnings("unchecked")
	private static void toJavaMap(JSONObject o, Map<String, Object> b) {
		Iterator<String> ji = (Iterator<String>) o.keys();
		try {
			while (ji.hasNext()) {
				String key = (String) ji.next();
				Object val = o.get(key);
				if (val.getClass() == JSONObject.class) {
					Map<String, Object> sub = new HashMap<String, Object>();
					toJavaMap((JSONObject) val, sub);
					b.put(key, sub);
				} else if (val.getClass() == JSONArray.class) {
					List<Object> l = new ArrayList<Object>();
					JSONArray arr = (JSONArray) val;
					for (int a = 0; a < arr.length(); a++) {
						Map<String, Object> sub = new HashMap<String, Object>();
						Object element = arr.get(a);
						if (element instanceof JSONObject) {
							toJavaMap((JSONObject) element, sub);
							l.add(sub);
						} else {
							l.add(element);
						}
					}
					b.put(key, l);
				} else {
					b.put(key, val);
				}
			}
		} catch (JSONException e) {
			Log.e("JSON2Java", "JSON error: " + e);
		}
	}

	/**
	 * toJavaList converts JSON's array response into Java's List
	 * 
	 * @param ar
	 *            JSONArray to be converted to Java List
	 * @param ll
	 *            Java List to hold the converted JSONArray response
	 **/
	private static void toJavaList(JSONArray ar, List<Object> ll) {
		int i = 0;
		try {
			while (i < ar.length()) {
				Object val = ar.get(i);
				if (val.getClass() == JSONObject.class) {
					Map<String, Object> sub = new HashMap<String, Object>();
					toJavaMap((JSONObject) val, sub);
					ll.add(sub);
				} else if (val.getClass() == JSONArray.class) {
					List<Object> l = new ArrayList<Object>();
					JSONArray arr = (JSONArray) val;
					for (int a = 0; a < arr.length(); a++) {
						Map<String, Object> sub = new HashMap<String, Object>();
						Object element = arr.get(a);
						if (element instanceof JSONObject) {
							toJavaMap((JSONObject) element, sub);
							ll.add(sub);
						} else {
							ll.add(element);
						}
					}
					l.add(l);
				} else {
					ll.add(val);
				}
				i++;
			}
		} catch (JSONException e) {
			Log.e("JSON2Java", "JSON error: " + e);
		}
	}
	
	public static String jsonFromArray(ArrayList<String> al) {
		JSONArray ja = new JSONArray();
		for(String str:al) {
			ja.put(str);
		};
		return ja.toString();
	}
}