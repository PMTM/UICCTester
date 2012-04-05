package cz.tm.uicc;

import eu.mighty.javatools.HexTools;

public class UICCConstants {
	static int BUFFER = 8192;
	static String S_ISD_AID = "A0 00 00 00 03 00 00 00";
	static String S_ISD_CMD = "80 CA 9F 7F 00";
	static String S_CRS_AID = "A0 00 00 01 51 43 52 53 00";
	static String S_CRS_CMD_ALL = "80 F2 40 00 02 4f 00 00";
	static String S_RAPL1_AID = "A0 00 00 00 87 10 03 FF 49 94 20 89 FF DA 01 01";
	static String S_RAPL2_AID = "A0 00 00 00 87 10 03 FF 49 94 20 89 FF DA 02 02";
	static String S_CRS_RAPL1_CMD = "80 F2 40 00 12 4f 10 A0 00 00 00 87 10 03 FF 49 94 20 89 FF DA 01 01";
	static String S_CRS_RAPL2_CMD = "80 F2 40 00 12 4f 10 A0 00 00 00 87 10 03 FF 49 94 20 89 FF DA 02 02 00";
	static String S_RAPL_CMD = "80 56 00 00 09 ff ff 00 01 00 00 00 00 1a";
	static String S_CRS_CLENA_CMD = "80 F0 04 80 03 80 01 40";
	static String S_CRS_CLDIS_CMD = "80 F0 04 00 03 80 01 40";

	static final byte[] ISD_AID = HexTools.hs2ba(S_ISD_AID);
	static final byte[] ISD_CMD = HexTools.hs2ba(S_ISD_CMD);
	static final byte[] CRS_AID = HexTools.hs2ba(S_CRS_AID);
	static final byte[] CRS_CMD_ALL = HexTools.hs2ba(S_CRS_CMD_ALL);
	static final byte[] RAPL1_AID = HexTools.hs2ba(S_RAPL1_AID);
	static final byte[] RAPL2_AID = HexTools.hs2ba(S_RAPL2_AID);
	static final byte[] CRS_RAPL1_CMD = HexTools.hs2ba(S_CRS_RAPL1_CMD);
	static final byte[] CRS_RAPL2_CMD = HexTools.hs2ba(S_CRS_RAPL2_CMD);
	static final byte[] CRS_CMD = CRS_RAPL1_CMD;
	static final byte[] RAPL_CMD = HexTools.hs2ba(S_RAPL_CMD);
	static final byte[] CRS_CLENA_CMD = HexTools.hs2ba(S_CRS_CLENA_CMD);
	static final byte[] CRS_CLDIS_CMD = HexTools.hs2ba(S_CRS_CLDIS_CMD);

}
