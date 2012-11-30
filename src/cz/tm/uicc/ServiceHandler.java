package cz.tm.uicc;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.Session;
import org.simalliance.openmobileapi.SEService.CallBack;

import eu.mighty.javatools.DateTimeUtil;
import eu.mighty.javatools.HexTools;
import eu.mighty.javatools.LoggerTool;
import eu.mighty.javatools.TLV;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Log;

public final class ServiceHandler {
	Activity _a;

	private Channel channel;
	private Session session;
	private Reader reader = null;
	private String readerName = "UICC";
	public ArrayList<String> logItems = new ArrayList<String>();

	// private SEService mSESvc = null;
	private CallBack mCB = null;

	private SEService mSEService = null;
	private Boolean mSEServiceReady = false;

	private static final String TAG = "dtest-js";

	public ServiceHandler(Activity anActivity) {
		Log.i(TAG, "Service Handler initialized (JSInterface)");
		_a = anActivity;
	}

	public String UICCInit(String testID) {
		Log.i(TAG, "JS: UICCInit");
		mCB = (CallBack) new SESvcCB();
		try {
			new SEService(_a, mCB);
		} catch (Exception e) {
			Log.e("UIIU", "Exception in creation", e);
		}
		;
		logItems.clear();
		log("plain", "testId: " + testID);
		return "called";
	}

	public String cutBa(String baStr, String stStr, String lenStr) {
		String res = "";
		byte[] ba = HexTools.hs2ba(baStr);
		byte[] bo = new byte[Integer.parseInt(lenStr)];
		int sta = Integer.parseInt(stStr);
		int len = Integer.parseInt(lenStr);
		for (int i = 0; i < len; i++) {
			bo[i] = ba[sta + i];
		}
		res = HexTools.ba2hs(bo);
		return res;
	}

	public void log(String type, String txt) {
		logItems.add("[" + DateTimeUtil.getDTString() + "]/" + type + ": "
				+ txt);
	}

	public String getTLV(String baStr, String ofsStr) {
		byte[] ba = HexTools.hs2ba(baStr);
		int ofs = Integer.parseInt(ofsStr);

		TLV[] tlv = TLV.parseData(ba, ofs, ba.length - ofs);
		return tlv[0].toString();
	}

	public String openBasicChannel(String AID) {
		Log.i(TAG, "JS: openBasicChannel(AID=" + AID + ")");

		String res = "";
		try {
			session = reader.openSession();
			LoggerTool.logIt("OpenBasicChannel(AID=" + AID + ")");
			channel = session.openBasicChannel(HexTools.hs2ba(AID));
			res = "openBasicChannel(AID=" + AID + ")";
		} catch (IOException e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String openLogicalChannel(String AID) {
		Log.i(TAG, "JS: openLogicalChannel(AID=" + AID + ")");

		String res = "";
		try {
			session = reader.openSession();
			LoggerTool.logIt("OpenLogicalChannel(AID=" + AID + ")");
			channel = session.openLogicalChannel(HexTools.hs2ba(AID));
			res = "openLogicalChannel(AID=" + AID + ")";
		} catch (IOException e) {
			e.printStackTrace();
			res = e.getMessage();
		}
		return res;
	}

	public String getSelectResponse() {
		String res = "call not available";
		byte[] ba = channel.getSelectResponse();
		res=HexTools.ba2hs(ba);
		channel.getSelectResponse();
		return res;
	}
	
	public void loadRelativeURL(String rUrl) {
		Log.i(TAG, "mWBridge/JS: loadRelativeURL('" + rUrl + "')");

		String FILES_DIR = _a.getFilesDir().getAbsolutePath();
		((UICCTester)_a).wv.loadUrl("file://" + FILES_DIR + rUrl);
	}
	
	public String getMCCMNC_AIR() {
		TelephonyManager tel = (TelephonyManager) _a.getSystemService(Context.TELEPHONY_SERVICE);
	    String networkOperator = tel.getNetworkOperator();

	    if (networkOperator != null) {
	        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
	        int mnc = Integer.parseInt(networkOperator.substring(3));
	        return String.format("%03d%02d",mcc,mnc);
	    } else {
	    	return "00000";
	    }
	}

	public String getMCCMNC_SIM() {
		TelephonyManager tel = (TelephonyManager) _a.getSystemService(Context.TELEPHONY_SERVICE);
	    String networkOperator = tel.getSimOperator();

	    if (networkOperator != null) {
	        int mcc = Integer.parseInt(networkOperator.substring(0, 3));
	        int mnc = Integer.parseInt(networkOperator.substring(3));
	        return String.format("%03d%02d",mcc,mnc);
	    } else {
	    	return "00000";
	    }
	}

	public String sendCommands(String cmd, String exp) {
		String res = "";

		byte[] cmdBa = HexTools.hs2ba(cmd);
		byte[] expBa = HexTools.hs2ba(exp);

		LoggerTool.logIt("sendCommad=" + cmd);

		byte[] resBa;

		try {
			resBa = channel.transmit(cmdBa);
			boolean match = false;
			if (expBa.length == resBa.length) {
				boolean tmpMatch = true;
				if ((expBa.length == 2) && (resBa.length >= 2)) {
					for (int j = resBa.length - 2; j < resBa.length; j++) {
						tmpMatch = tmpMatch && (expBa[j] == resBa[j]);
					}
				} else {
					for (int j = 0; j < expBa.length; j++) {
						tmpMatch = tmpMatch && (expBa[j] == resBa[j]);
					}
				}
				match = tmpMatch;
			}
			if (match) {
				LoggerTool.logIt("Recieved expected response");
				res = "ok:" + HexTools.ba2hs(resBa);
			} else {
				LoggerTool.logIt("Error, unexpected response");
				LoggerTool.logIt(" Expected: " + exp);
				res = "ko:" + HexTools.ba2hs(resBa);
			}
			LoggerTool.logIt(" Response: " + HexTools.ba2hs(resBa));
		} catch (IOException e) {
			e.printStackTrace();
			res = "ko:" + e.getMessage();
		}

		return res;
	}

	public String getVersion() {
		String ts = "unknown version";
		try {
			ApplicationInfo ai = _a.getPackageManager().getApplicationInfo(
					_a.getPackageName(), 0);
			ZipFile zf = new ZipFile(ai.sourceDir);
			ZipEntry ze = zf.getEntry("classes.dex");
			long time = ze.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			ts = "ver: " + sdf.format(new java.util.Date(time));
		} catch (Exception e) {
			ts = "exception getting version";
		}
		;
		return ts;
	}

	public String UICCClose() {
		if (session != null) {
			LoggerTool.logIt("session.closeChannels()");
			session.closeChannels();
			LoggerTool.logIt("session.close()");
			session.close();
			return "closed";
		} else {
			return "session not open";
		}
	}

	public String setReaderName(String newReaderName) {
		// String oldReaderName = readerName;
		readerName = newReaderName;
		return "SE requested = '" + newReaderName + "'";
	}
	
	public String getReadersNames() {
		Reader[] readers = mSEService.getReaders();
		boolean isPresent = false;

		String res="";
		if (readers.length == 0) {
			res = "no readers";
		} else {
			res = "readers=";
			for (Reader xReader : readers) {
				isPresent = xReader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				String rn = xReader.getName();
				res += "<br />\\'" + rn + "\\' ("
						+ HexTools.ba2hs(rn.getBytes()) + ")/" + s
						+ ",";
			}
		}
		return res;
	}

	public class SESvcCB implements CallBack {
		public void serviceConnected(SEService service) {
			boolean seFound = false;
			boolean isPresent = false;
			try {
				mSEService = service;
				mSEServiceReady = true;

				LoggerTool.logIt("SE Service connected", "ok");

				String res = "";
				if (mSEServiceReady) {

					Reader[] readers = mSEService.getReaders();

					if (readers.length == 0) {
						LoggerTool.logIt("No reader available \n");
						mSEService.shutdown();
						res = "no readers";
					} else {
						seFound = false;
						reader = null;
						res = "readers=";
						for (Reader xReader : readers) {
							boolean isPresentTmp = xReader.isSecureElementPresent();
							String s = isPresentTmp ? "present" : "absent";
							String rn = xReader.getName();
							LoggerTool
									.logIt("SecureElement (" + rn + "): " + s);
							res += "<br />\\'" + rn + "\\' ("
									+ HexTools.ba2hs(rn.getBytes()) + ")/" + s
									+ ",";

							if (xReader.getName().equals(readerName)) {
								reader = xReader;
								LoggerTool.logIt("Selected Reader:"
										+ xReader.getName() + "\n");
								seFound = true;
								isPresent = xReader.isSecureElementPresent();
							}
						}
					}
				} else {
					res = "uicc is not ready";
				}

				Message pMsg = new Message();
				String str = "";
				if (seFound) {
					if (!isPresent) {
						str = "found but not connected:" + res;
					} else {
						str = "connected:" + res;
					}
				} else {
					str = "not-found:" + res;
				}
				pMsg.obj = str;
				((UICCTester) _a).mMsgHandle.sendMessage(pMsg);
			} catch (Exception e) {
				LoggerTool.logIt("SE Service connected", "exception", e);
			}
		}
	}

}