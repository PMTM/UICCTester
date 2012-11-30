function eCmd(cmdx,infotxt) {
	exp = "90 00";
	msg("info", infotxt);
	msg("debug", cmdx);
	m = dtest.sendCommands(cmdx, exp);
	msg("info", "response");
	msg("debug", m.substr(3));

	msg("info", "*** get last command");
	msg("debug", "00 22 00 00 00");
	m = dtest.sendCommands("00 22 00 00 00", exp);
	msg("info", "*** response");
	msg("debug", m.substr(3));
}

function seReady(info) {
	msg("debug", "UICC reports: " + info);
	
	if (info.substr(0, 10) == "connected:") {
		msg("info", "Opening logical channel with CRS AID");
		m = dtest.openLogicalChannel("A0 00 00 00 87 10 03 FF 49 94 20 89 FF 81 01 01");
		msg("debug", m);
		msg("info", "select test applet no.1 AID = A0 00 00 00 87 10 03 FF 49 94 20 89 FF 81 01 01");

		m = dtest.getSelectResponse();
		msg("debug", "getSelectResponse(): " + m);

		exp = "90 00";
		eCmd("00 15 00 00 00","null data just sw12");
		eCmd("00 15 00 00 00 00","null data just sw12");
		eCmd("00 17 00 00 00", "get debug 1.");
		eCmd("00 17 00 00 00", "get debug 2.");
		eCmd("00 20 00 00 00", "get CLA");
		eCmd("00 17 00 00 00 05", "explicit Le");

		msg("info", "*** get last command");
		msg("debug", "00 22 00 00 00");
		m = dtest.sendCommands("00 22 00 00 00", exp);
		msg("info", "*** response");
		msg("debug", m.substr(3));

		msg("info", "Close UICC connection/session");
		m = dtest.UICCClose();
		msg("plain", m);
	}
}
