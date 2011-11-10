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
	
	private EditText mEdit;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cfgview);

		mEdit = (EditText) findViewById(R.id.et1);
	}
	public void saveBut(View view) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("logServer",mEdit.getText().toString());
		editor.commit();
		Log.e("UUUU","saveBut:"+mEdit.getText().toString());
	}	
}
