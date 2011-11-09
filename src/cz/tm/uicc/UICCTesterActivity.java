package cz.tm.uicc;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.simalliance.openmobileapi.Channel; //import org.simalliance.openmobileapi.R;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.Session; //import org.simalliance.openmobileapi.OpenMobileApiSampleActivity.SESvcCB;
import org.simalliance.openmobileapi.SEService.CallBack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class UICCTesterActivity extends Activity {

	private TextView mText;
	private TextView mHelp;
	private Spinner mSpin;
	private String logStr = "";
	private ScrollView mScrollView;
	private int selItem;
	@SuppressWarnings("unused")
	private SEService mSESvc = null;
	private CallBack mCB = null;

	private SEService mSEService = null;
	private Boolean mSEServiceReady = false;

	private Channel logicalChannel;
	private Channel basicChannel;
	private byte[] response;
	private byte[] cmd;
	private Session session;

	private static final byte[] ISD_AID = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x00, 0x03, 0x00, 0x00, 0x00 };
	private static final byte[] ISD_CMD = new byte[] { (byte) 0x80, (byte) 0xCA, (byte) 0x9F, 0x7F, 0x00 };

	private static final byte[] CRS_AID = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x01, 0x51, 0x43, 0x52, 0x53, 0x00 };
	// private static final byte[] CRS_AID = new byte[] { (byte) 0xA0, 0x00,
	// 0x00, 0x01, 0x51, 0x02 };

	@SuppressWarnings("unused")
	private static final byte[] CRS_CMD_ALL = new byte[] { (byte) 0x80, (byte) 0xF2, (byte) 0x40, 0x00, 0x02, 0x4f, 0x00, 0x00 };

	private static final byte[] RAPL1_AID = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x00, (byte) 0x87, 0x10, 0x03, (byte) 0xFF, 0x49, (byte) 0x94, 0x20, (byte) 0x89, (byte) 0xFF,
			(byte) 0xDA, 0x01, 0x01 };
	private static final byte[] RAPL2_AID = new byte[] { (byte) 0xA0, 0x00, 0x00, 0x00, (byte) 0x87, 0x10, 0x03, (byte) 0xFF, 0x49, (byte) 0x94, 0x20, (byte) 0x89, (byte) 0xFF,
			(byte) 0xDA, 0x02, 0x02 };

	private static final byte[] CRS_RAPL1_CMD = new byte[] { (byte) 0x80, (byte) 0xF2, (byte) 0x40, 0x00, 0x12, 0x4f, 0x10, (byte) 0xA0, 0x00, 0x00, 0x00, (byte) 0x87, 0x10, 0x03,
			(byte) 0xFF, 0x49, (byte) 0x94, 0x20, (byte) 0x89, (byte) 0xFF, (byte) 0xDA, 0x01, 0x01 };
	private static final byte[] CRS_RAPL2_CMD = new byte[] { (byte) 0x80, (byte) 0xF2, (byte) 0x40, 0x00, 0x12, 0x4f, 0x10, (byte) 0xA0, 0x00, 0x00, 0x00, (byte) 0x87, 0x10, 0x03,
			(byte) 0xFF, 0x49, (byte) 0x94, 0x20, (byte) 0x89, (byte) 0xFF, (byte) 0xDA, 0x02, 0x02, 0x00 };

	private static byte[] CRS_CMD = CRS_RAPL1_CMD;

	private static final byte[] RAPL_CMD = new byte[] { (byte) 0x80, (byte) 0x56, (byte) 0x00, 0x00, 0x09, (byte) 0xff, (byte) 0xff, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x1a };

	private static final byte[] CRS_CLENA_CMD = new byte[] { (byte) 0x80, (byte) 0xF0, (byte) 0x04, (byte) 0x80, 0x03, (byte) 0x80, (byte) 0x01, 0x40 };
	private static final byte[] CRS_CLDIS_CMD = new byte[] { (byte) 0x80, (byte) 0xF0, (byte) 0x04, (byte) 0x00, 0x03, (byte) 0x80, (byte) 0x01, 0x40 };

	String[] items = new String[] { "Connect SE service", "B:ISD AID select", "B:CRS App AID", "B:No AID select", "L:No AID select", "L:ISD AID select", "L:CRS App AID",
			"L:Query RAPL1 AID in CRS", "L:Query RAPL2 AID in CRS", "L:Query RAPL1 AID", "L:Query RAPL2 AID",

			"L:CRS Disable CL", "L:CRS Enable CL",

			"Post Log", "Clean Log", };

	String[] help = new String[] { "Connect to the SE service and set callback on connect", "Select ISD AID through basic channel", "Select CRS AID through basic channel",
			"Open basic channel without specifying AID", "Open logical channel without specifying AID", "Select ISD AID through logical channel",
			"Select CRS AID through logical channel", "Query CRS for RAPL1 info by AID through logical channel", "Query CRS for RAPL2 info by AID through logical channel",
			"Query RAPL1 info through logical channel", "Query RAPL2 info through logical channel",

			"Disable CL interface through CRS", "Enable CL interface through CRS",

			"Post Log to the web for better reading and copy and paste", "Clean Log", };

	private static String bytesToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bytes) {
			sb.append(String.format("%02x ", b & 0xFF));
		}
		return sb.toString();
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.main);

		mSpin = (Spinner) findViewById(R.id.sel);
		mScrollView = (ScrollView) findViewById(R.id.ScrollView01);
		mText = (TextView) findViewById(R.id.textView1);
		mHelp = (TextView) findViewById(R.id.help);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpin.setAdapter(adapter);

		mSpin.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// items[0] = "One";
				// selectedItem = items[position];
				Log.e("Spin", "Selected = " + arg2);
				mHelp.setText(help[arg2]);
				selItem = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.one:
			startActivity(new Intent (this, CfgAct.class));
			return true;
		case R.id.two:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class SESvcCB implements CallBack {
		public void serviceConnected(SEService service) {
			try {
				mSEService = service;
				mSEServiceReady = true;
				logIt("SE Service connected", "ok");
			} catch (Exception e) {
				logIt("SE Service connected", "exception", e);
			}
		}
	}

	void ops_crsAppAID_B(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;

				try {
					session = reader.openSession();
					logIt("OpenBasicChannel(AID=" + bytesToString(CRS_AID) + ")");
					basicChannel = session.openBasicChannel(CRS_AID);
					logIt("basicChannel.transmit()");
					logIt(" Command: " + bytesToString(CRS_CMD));
					response = basicChannel.transmit(CRS_CMD);
					logIt(" Response: " + bytesToString(response));

				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void ops_isdAID_B(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					Session session = reader.openSession();
					logIt("OpenBasicChannel(AID=" + bytesToString(ISD_AID) + ")");
					basicChannel = session.openBasicChannel(ISD_AID);
					logIt("basicChannel.transmit()");
					logIt(" Command: " + bytesToString(ISD_CMD));
					response = basicChannel.transmit(ISD_CMD);
					logIt(" Response: " + bytesToString(response));
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void ops_noAID_B(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;

				try {
					session = reader.openSession();
					basicChannel = session.openBasicChannel(null);
					logIt("basicChannel.transmit()");
					cmd = new byte[] { (byte) 0x80, (byte) 0xCA, (byte) 0x9F, 0x7F, 0x00 };
					logIt(" Command: " + bytesToString(cmd));
					response = basicChannel.transmit(cmd);
					logIt(" Response: " + bytesToString(response));
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void ops_crsDisClIf(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					Session session = reader.openSession();
					logIt("OpenLogicalChannel\nAID=" + bytesToString(CRS_AID));
					logicalChannel = session.openLogicalChannel(CRS_AID);
					logIt("logicalChannel.transmit");
					logIt("CMD=" + bytesToString(CRS_CLDIS_CMD));
					response = logicalChannel.transmit(CRS_CLDIS_CMD);
					logIt("RC=" + bytesToString(response));
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
		logIt("ops_crsDisClIf(SEService)");
	}

	void ops_crsEnaClIf(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					Session session = reader.openSession();
					logIt("OpenLogicalChannel\nAID=" + bytesToString(CRS_AID));
					logicalChannel = session.openLogicalChannel(CRS_AID);
					logIt("logicalChannel.transmit");
					logIt("CMD=" + bytesToString(CRS_CLENA_CMD));
					response = logicalChannel.transmit(CRS_CLENA_CMD);
					logIt("RC=" + bytesToString(response));
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
		logIt("ops_crsDisClIf(SEService)");
	}

	void ops_crsAppRAPL1_L(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					Session session = reader.openSession();
					logIt("OpenLogicalChannel\nAID=" + bytesToString(CRS_AID));
					logicalChannel = session.openLogicalChannel(CRS_AID);
					logIt("logicalChannel.transmit");
					logIt("CMD=" + bytesToString(CRS_RAPL1_CMD));
					response = logicalChannel.transmit(CRS_RAPL1_CMD);
					logIt("RC=" + bytesToString(response));
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
		logIt("ops_CRSRAPL1_L(SEService)");
	}

	void ops_crsAppRAPL2_L(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					Session session = reader.openSession();
					logIt("OpenLogicalChannel(AID=" + bytesToString(CRS_AID) + ")");
					logicalChannel = session.openLogicalChannel(CRS_AID);
					logIt("logicalChannel.transmit()");
					logIt(" Command: " + bytesToString(CRS_RAPL2_CMD));
					response = logicalChannel.transmit(CRS_RAPL2_CMD);
					logIt(" Response: " + bytesToString(response));
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
		logIt("ops_CRSRAPL2_L(SEService)");
	}

	void ops_isdAID_L(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader : " + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					session = reader.openSession();
					logIt("OpenLogicalChannel(AID=" + bytesToString(ISD_AID) + ")");
					logicalChannel = session.openLogicalChannel(ISD_AID);
					if (!logicalChannel.isClosed()) {
						logIt("logicalChannel.transmit()");
						logIt(" Command: " + bytesToString(ISD_CMD));
						response = logicalChannel.transmit(ISD_CMD);
						logIt(" Response: " + bytesToString(response));
					} else {
						logIt("logical channel is closed");
					}
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void ops_rapl1AID_L(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader : " + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					session = reader.openSession();
					logIt("OpenLogicalChannel(AID=" + bytesToString(RAPL1_AID) + ")");
					logicalChannel = session.openLogicalChannel(RAPL1_AID);
					if (!logicalChannel.isClosed()) {
						logIt("logicalChannel.transmit()");
						logIt(" Command: " + bytesToString(RAPL_CMD));
						response = logicalChannel.transmit(RAPL_CMD);
						logIt(" Response: " + bytesToString(response));
					} else {
						logIt("logical channel is closed");
					}
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void ops_rapl2AID_L(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader : " + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					session = reader.openSession();
					logIt("OpenLogicalChannel(AID=" + bytesToString(RAPL2_AID) + ")");
					logicalChannel = session.openLogicalChannel(RAPL2_AID);
					if (!logicalChannel.isClosed()) {
						logIt("logicalChannel.transmit()");
						logIt(" Command: " + bytesToString(RAPL_CMD));
						response = logicalChannel.transmit(RAPL_CMD);
						logIt(" Response: " + bytesToString(response));
					} else {
						logIt("logical channel is closed");
					}
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void ops_noAID_L(SEService service) {

		Reader[] readers = service.getReaders();

		if (readers.length == 0) {
			logIt("No reader available \n");
			service.shutdown();
			return;
		}

		for (Reader reader : readers) {
			if (reader.getName().equals("UICC")) {
				logIt("Selected Reader:" + reader.getName() + "\n");

				boolean isPresent = reader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				logIt("SecureElement : " + s);

				if (!isPresent)
					continue;
				try {
					Session session = reader.openSession();
					logIt("OpenLogicalChannel(noAID)");
					logicalChannel = session.openLogicalChannel(null);
					logIt("logicalChannel.transmit()");
					cmd = new byte[] { (byte) 0x80, (byte) 0xCA, (byte) 0x9F, 0x7F, 0x00 };
					logIt(" Command: " + bytesToString(cmd));
					response = logicalChannel.transmit(cmd);
					logIt(" Response: " + bytesToString(response));

					if (logicalChannel != null) {
						logIt("logicalChannel.close()");
						logicalChannel.close();
					}
				} catch (Exception e) {
					logIt("", "", e);
				} finally {
					uiccClose();
				}
			}
		}
	}

	void uiccClose() {
		logIt("session.closeChannels()");
		session.closeChannels();
		logIt("session.close()");
		session.close();
	}

	@Override
	protected void onDestroy() {
		if (mSEService != null) {
			mSEService.shutdown();
		}
		super.onDestroy();
	}

	public void e_cleanLog() {
		logStr = "";
		mText.setText(logStr);
	}

	public void e_postLog() {
		String host = "server";
		String link = "http://" + host + "/u/moravekp/simpost.php";

		DefaultHttpClient hc = new DefaultHttpClient();
		ResponseHandler<String> res = new BasicResponseHandler();

		HttpPost postMethod = new HttpPost(link);

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

		nameValuePairs.add(new BasicNameValuePair("log", logStr));

		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String response = hc.execute(postMethod, res);

			Log.e("postLog", "server said : " + response);
		} catch (UnsupportedEncodingException e) {
			Log.e("postLog", "UnsupportedEncodingException");
		} catch (ClientProtocolException e) {
			Log.e("postLog", "ClientProtocolException");
		} catch (IOException e) {
			Log.e("postLog", "IOException", e);
		}
	}

	public void restartSESvc(View view) {
		if (mSEServiceReady) {
			logIt("restartSESvc", "shutdown");
			mSEService.shutdown();
			mSEServiceReady = false;
		} else {
			logIt("No SE service connected");
		}
		logIt("restartSESvc", "connect");
		mCB = (CallBack) new SESvcCB();
		mSESvc = new SEService(this, mCB);
	}

	public void goBut(View view) {
		logIt("invoke", "goBut");
		if (selItem == 0 || mSEServiceReady) {
			switch (selItem) {
			case 0:
				mCB = (CallBack) new SESvcCB();
				mSESvc = new SEService(this, mCB);
				break;

			case 1:
				ops_isdAID_B(mSEService);
				break;
			case 2:
				ops_crsAppAID_B(mSEService);
				break;
			case 3:
				ops_noAID_B(mSEService);
				break;

			case 4:
				ops_isdAID_L(mSEService);
				break;
			case 5:
				ops_isdAID_L(mSEService);
				break;
			case 6:
				ops_noAID_L(mSEService);
				break;

			case 7:
				ops_crsAppRAPL1_L(mSEService);
				break;
			case 8:
				ops_crsAppRAPL2_L(mSEService);
				break;
			case 9:
				ops_rapl1AID_L(mSEService);
				break;
			case 10:
				ops_rapl2AID_L(mSEService);
				break;

			case 11:
				ops_crsDisClIf(mSEService);
				break;
			case 12:
				ops_crsEnaClIf(mSEService);
				break;

			case 13:
				e_cleanLog();
				break;
			case 14:
				e_postLog();
				break;
			}
		}
	}

	private void logIt(String pfx, String str) {
		String sa[] = str.split("\n");
		if (!pfx.trim().isEmpty()) {
			logStr += "<h1>" + pfx + "</h1><hr />\n";
		}
		for (int i = 0; i < sa.length; i++) {
			logStr += sa[i] + "<br />\n";
		}
		mText.setText(Html.fromHtml(logStr));
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

	private void logIt(String str) {
		logIt("", str);
	}

	private void logIt(String pfx, String str, Exception e) {
		logIt(pfx, str);
		String msg = e.toString();
		logIt("Exception", msg + "\n");
	}

}