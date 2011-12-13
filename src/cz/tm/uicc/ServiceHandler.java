package cz.tm.uicc;

import java.io.IOException;
import java.util.ArrayList;

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
import android.os.Message;
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
		new SEService(_a, mCB);
		logItems.clear();
		log("plain","testId: "+testID);
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

	public void log(String type,String txt) {
		logItems.add("["+DateTimeUtil.getDTString()+"]/"+type+": "+txt);
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
		String oldReaderName = readerName;
		readerName =  newReaderName;
		return oldReaderName;
	}

	public class SESvcCB implements CallBack {
		public void serviceConnected(SEService service) {
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
						for (Reader xReader : readers) {
							if (xReader.getName().equals(readerName)) {
								reader = xReader;
								LoggerTool.logIt("Selected Reader:" + xReader.getName() + "\n");

								boolean isPresent = xReader.isSecureElementPresent();
								String s = isPresent ? "present" : "absent";
								LoggerTool.logIt("SecureElement : " + s);
								res = xReader.getName() + "/" + s;
							}
						}
					}
				} else {
					res = "uicc is not ready";
				}

				Message pMsg = new Message();
				String str = "connected:" + res;
				pMsg.obj = str;
				((UICCTester) _a).mMsgHandle.sendMessage(pMsg);
			} catch (Exception e) {
				LoggerTool.logIt("SE Service connected", "exception", e);
			}
		}
	}

}