function seReady(info) {
	msg("debug", "UICC reports: " + info);
	if (info.substr(0, 10) == "connected:") {
		msg("info", "Opening logical channel with MCREL AID");
		m = dtest.openLogicalChannel(MCREL);
		msg("debug", m);
		msg("info", "mCREL");

		m = dtest.getSelectResponse();
		msg("debug", "getSelectResponse(): " + m);

		exp = "90 00";
		// verify wallet pin
		// "C0 20 01 01 1E 1E 31 32 33 34 00"
		// "00" x 25
		// get auth token - first part
		// "C0 16 00 00 03 38 01 01" (resp 63 10)
		// "00 C0 00 00 00" - get response
		// get auth token - next
		// "C0 16 00 01 03 38 01 01"
		// 6310: "00 C0 00 00 00" - get reponse Le=(00=256)
		// 61xx: "00 C0 00 00 xx" - get reponse Le=xx

		// select 2PAY.SYS.DDF01
		// cmdWLe="80 D4 04 00 00 00";
		cmdx = "C0 20 00 01 1E" + " 31 32 33 34 FF" + " FF FF FF FF FF"
				+ " FF FF FF FF FF" + " FF FF FF FF FF" + " FF FF FF FF FF"
				+ " FF FF FF FF FF" + " 00";

		msg("debug", cmdx);
		m = dtest.sendCommands(cmdx, exp);
		sw12 = m.substring(m.length - 6, m.length - 1);

		if (sw12 == "90 00") {
			msg("debug", "resp: " + m.substr(3));
		} else {
			msg("debug", "resp: " + m.substr(3));
		}

		cmdx = "00 16 00 00 03 38 01 01 00";

		msg("debug", cmdx);
		m = dtest.sendCommands(cmdx, exp);
		sw12 = m.substring(m.length - 6, m.length - 1);

		if (sw12 == "63 10") {
			msg("debug", "resp: " + m.substr(3));
		} else {
			msg("debug", "resp: " + m.substr(3));
		}

		while (sw12 == "63 10") {
			cmdx = "00 C0 00 00";

			msg("debug", cmdx);
			m = dtest.sendCommands(cmdx, exp);
			sw12 = m.substring(m.length - 6, m.length - 1);

			if (sw12 == "90 00") {
				msg("debug", "resp: " + m.substr(3));
			} else {
				msg("debug", "resp: " + m.substr(3));
			}

			cmdx = "00 16 00 01 03 38 01 01 00";

			msg("debug", cmdx);
			m = dtest.sendCommands(cmdx, exp);
			sw12 = m.substring(m.length - 6, m.length - 1);

			if (sw12 == "90 00") {
				msg("debug", "resp: " + m.substr(3));
			} else {
				msg("debug", "resp: " + m.substr(3));
			}

		}

//		cmdx = "00 C0 00 00 " + sw12.substr(3);
//
//		msg("debug", cmdx);
//		m = dtest.sendCommands(cmdx, exp);
//		sw12 = m.substring(m.length - 6, m.length - 1);
//
//		if (sw12 == "90 00") {
//			msg("debug", "resp: " + m.substr(3));
//		} else {
//			msg("debug", "resp: " + m.substr(3));
//		}

		msg("info", "Close UICC connection/session");
		m = dtest.UICCClose();
		msg("plain", m);
	}
}
