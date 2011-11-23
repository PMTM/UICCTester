function seReady(info) {
	msg("UICC reports: "+info);
	if (info=="connected:UICC/present") {
		msgi("Opening logical channel with CRS AID");
		m=dtest.openLogicalChannel("A0 00 00 01 51 43 52 53 00");
		msg(m);

		msgh("Try w/o Le");
		msgi("Send CL registry query to CRS/with Le=0");
		exp="61 68 4f 10 a0 00 00 00 87 10 03 ff 49 94 20 89 ff da 01 01 9f 70 02 00 01 80 02 00 00 88 01 00 8c 01 81 8a 01 00 8d 42 a0 1f 80 05 04 91 92 93 94 81 01 20 82 02 08 02 83 04 03 a1 a2 a3 84 01 70 85 01 01 86 03 03 03 00 a1 1f 80 05 00 00 00 00 00 81 01 ff 82 02 00 00 83 04 ff ff ff ff 84 01 ff 85 01 ff 86 03 ff ff ff 90 00";
		cmdNoLe="80 F2 40 00 12 4f 10 A0 00 00 00 87 10 03 FF 49 94 20 89 FF DA 01 01";
		cmdWLe="80 F2 40 00 12 4f 10 A0 00 00 00 87 10 03 FF 49 94 20 89 FF DA 01 01 00";
		msgd(cmdNoLe);
		m=dtest.sendCommands(
			cmdNoLe,
			exp);
		sw12=m.substring(m.length-6,m.length-1);
		if (sw12=="90 00") {
			msgi("Command returned ok ~ 0x9000");
			if (m.substring(0,2)=='ko') {
				msge("Unfortunately the result does not match expectation");
				msgd("Expected: "+exp);
			}
			msgd(m.substr(3));
		} else {
			msge("Command error");
			msgd("resp: "+m.substr(3));
		}


		msgi("Close UICC connection/session");
		m=dtest.UICCClose();
		msg(m);
//		msgi("getTLV");
//		m=dtest.getTLV("61 68 4f 10 a0 00 00 00 87 10 03 ff 49 94 20 89 ff da 01 01 9f 70 02 00 01 80 02 00 00 88 01 00 8c 01 81 8a 01 00 8d 42 a0 1f 80 05 04 91 92 93 94 81 01 20 82 02 08 02 83 04 03 a1 a2 a3 84 01 70 85 01 01 86 03 03 03 00 a1 1f 80 05 00 00 00 00 00 81 01 ff 82 02 00 00 83 04 ff ff ff ff 84 01 ff 85 01 ff 86 03 ff ff ff","2");
//		msg("tlv="+m);
	}
}
