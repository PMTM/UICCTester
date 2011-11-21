package cz.tm.uicc;

import java.io.IOException;

import org.simalliance.openmobileapi.Channel;
import org.simalliance.openmobileapi.Reader;
import org.simalliance.openmobileapi.SEService;
import org.simalliance.openmobileapi.Session;
import org.simalliance.openmobileapi.SEService.CallBack;

import android.app.Activity;
import android.util.Log;

public final class ServiceHandler {
	Activity _a;

	private Channel channel;
//	private byte[] response;
//	private byte[] cmd;
	private Session session;
	private Reader reader = null;

//	private SEService mSESvc = null;
	private CallBack mCB = null;

	private SEService mSEService = null;
//	private Boolean mSEServiceReady = false;

	private static final String TAG = "dtest-js";

	public ServiceHandler(Activity anActivity) {
		Log.i(TAG, "Service Handler initialized (JSInterface)");
		_a = anActivity;
	}

	public void MySEService() {
		Log.i(TAG, "JS: SEService");

		mCB = (CallBack) new SESvcCB();
		new SEService(_a, mCB);
	}

	public void UICCInit() {
		Log.i(TAG, "JS: UICCInit");

		Reader[] readers = mSEService.getReaders();

		if (readers.length == 0) {
			LoggerTool.logIt("No reader available \n");
			mSEService.shutdown();
			return;
		}

		for (Reader xReader : readers) {
			if (xReader.getName().equals("UICC")) {
				reader= xReader;
				LoggerTool.logIt("Selected Reader:" + xReader.getName() + "\n");

				boolean isPresent = xReader.isSecureElementPresent();
				String s = isPresent ? "present" : "absent";
				LoggerTool.logIt("SecureElement : " + s);
			}
		}
	}

	public void openBasicChannel(String AID) {
		Log.i(TAG, "JS: openBasicChannel(AID=" + AID + ")");
		
		try {
			session = reader.openSession();
			LoggerTool.logIt("OpenBasicChannel(AID=" + AID +")");
			channel = session.openBasicChannel(HexTools.hs2ba(AID));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void openLogicalChannel(String AID) {
		Log.i(TAG, "JS: openLogicalChannel(AID=" + AID + ")");

		try {
			session = reader.openSession();
			LoggerTool.logIt("OpenLogicalChannel(AID=" + AID +")");
			channel = session.openLogicalChannel(HexTools.hs2ba(AID));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void sendCommands(String AID) {
		Log.i(TAG, "JS: openLogicalChannel(AID=" + AID + ")");
		int sel = 1;
		switch (sel) {
		case 0:
//			String[] cmd0 = { UICCConstants.S_RAPL_CMD, };
//			String[] exp0 = { "90", };
			// ops_script(mSEService, UICCConstants.S_RAPL1_AID, cmd0, exp0);
			break;
		case 1:
			// String[] cmd1 = { UICCConstants.S_CRS_RAPL1_CMD, };
			// String[] exp1 = {
			// "61 68 4f 10 a0 00 00 00 87 10 03 ff 49 94 20 89 ff da 01 01 9f 70 02 00 01 80 02 00 00 88 01 00 8c 01 81 8a 01 00 8d 42 a0 1f 80 05 04 91 92 93 94 81 01 20 82 02 08 02 83 04 03 a1 a2 a3 84 01 70 85 01 01 86 03 03 03 00 a1 1f 80 05 00 00 00 00 00 81 01 ff 82 02 00 00 83 04 ff ff ff ff 84 01 ff 85 01 ff 86 03 ff ff ff 90 00",
			// };
			// ops_script(mSEService, UICCConstants.S_CRS_AID, cmd1, exp1);
			break;
		}
	}

	// Reader[] readers = service.getReaders();
	//
	// if (readers.length == 0) {
	// LoggerTool.logIt("No reader available \n");
	// service.shutdown();
	// return;
	// }
	//
	// for (Reader reader : readers) {
	// if (reader.getName().equals("UICC")) {
	// LoggerTool.logIt("Selected Reader:" + reader.getName() + "\n");
	//
	// boolean isPresent = reader.isSecureElementPresent();
	// String s = isPresent ? "present" : "absent";
	// LoggerTool.logIt("SecureElement : " + s);
	//
	// if (!isPresent)
	// continue;
	//
	// try {
	// session = reader.openSession();
	// LoggerTool.logIt("OpenBasicChannel(AID=" + HexTools.ba2hs(CRS_AID) +
	// ")");
	// basicChannel = session.openBasicChannel(CRS_AID);
	// LoggerTool.logIt("basicChannel.transmit()");
	// LoggerTool.logIt(" Command: " + HexTools.ba2hs(CRS_CMD));
	// response = basicChannel.transmit(CRS_CMD);
	// LoggerTool.logIt(" Response: " + HexTools.ba2hs(response));
	//
	// } catch (Exception e) {
	// LoggerTool.logIt("", "", e);
	// } finally {
	// uiccClose();
	// }
	// }
	// }

//	void ops_script(SEService service, String anAID, String[] aScript, String[] anExpect) {
//
//		Reader[] readers = service.getReaders();
//
//		if (readers.length == 0) {
//			LoggerTool.logIt("No reader available \n");
//			service.shutdown();
//			return;
//		}
//
//		Reader selReader = null;
//		for (Reader reader : readers) {
//			if (reader.getName().equals("UICC")) {
//				selReader = reader;
//			}
//		}
//		boolean isPresent = selReader.isSecureElementPresent();
//		String s = isPresent ? "present" : "absent";
//		LoggerTool.logIt("SecureElement : " + s);
//
//		if (isPresent) {
//			try {
//				Session session = selReader.openSession();
//				byte[] ba = null;
//				byte[] ex = null;
//
//				// Select particular AID
//				ba = HexTools.hs2ba(anAID);
//				LoggerTool.logIt("OpenLogicalChannel, AID=" + HexTools.ba2hs(ba));
//				Channel xChannel = session.openLogicalChannel(ba);
//				// xChannel = session.openBasicChannel(ba);
//
//				// Send commands and expect responses
//				for (int i = 0; i < aScript.length; i++) {
//					ba = HexTools.hs2ba(aScript[i]);
//					ex = HexTools.hs2ba(anExpect[i]);
//					LoggerTool.logIt("xChannel.transmit=" + HexTools.ba2hs(ba));
//					response = xChannel.transmit(ba);
//					boolean match = false;
//					if (ex.length == response.length) {
//						boolean tmpMatch = true;
//						for (int j = 0; j < ex.length; j++) {
//							tmpMatch = tmpMatch && (ex[j] == response[j]);
//						}
//						match = tmpMatch;
//					}
//					if (match) {
//						LoggerTool.logIt("Recieved expected response");
//					} else {
//						LoggerTool.logIt("Error, unexpected response");
//						LoggerTool.logIt(" Expected: " + HexTools.ba2hs(ex));
//					}
//					LoggerTool.logIt(" Response: " + HexTools.ba2hs(response));
//				}
//
//				if (logicalChannel != null) {
//					LoggerTool.logIt("logicalChannel.close()");
//					logicalChannel.close();
//				}
//			} catch (Exception e) {
//				LoggerTool.logIt("", "", e);
//			} finally {
//				uiccClose();
//			}
//		}
//	}

	void uiccClose() {
		if (session != null) {
			LoggerTool.logIt("session.closeChannels()");
			session.closeChannels();
			LoggerTool.logIt("session.close()");
			session.close();
		}
	}

	public class SESvcCB implements CallBack {
		public void serviceConnected(SEService service) {
			try {
				mSEService = service;
				//mSEServiceReady = true;
				LoggerTool.logIt("SE Service connected", "ok");
			} catch (Exception e) {
				LoggerTool.logIt("SE Service connected", "exception", e);
			}
		}
	}

}
