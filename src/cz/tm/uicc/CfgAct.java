package cz.tm.uicc;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class CfgAct extends Activity {
	
	private EditText mUrl;
	private EditText mEmail;
	private EditText mDevKey;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cfgview);

		mUrl = (EditText) findViewById(R.id.etUrl);
		mEmail = (EditText) findViewById(R.id.etEmail);
		mDevKey = (EditText) findViewById(R.id.etDevKey);
	}
	
	public void saveBut(View view) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("logServer",mUrl.getText().toString());
		editor.putString("email",mEmail.getText().toString());
		editor.putString("devkey",mDevKey.getText().toString());
		editor.commit();
		Log.e("UUUU","saveBut:"+mUrl.getText().toString());
	}	
}
