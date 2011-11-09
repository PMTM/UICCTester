package cz.tm.uicc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class CfgAct extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.cfgview);
	}
}
