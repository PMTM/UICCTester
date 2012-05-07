function seReady(info) {
	msg("debug", "UICC reports: " + info);
	if (info.substr(0, 10) == "connected:") {
  		m=dtest.getMCCMNC_SIM();
  		msg("info","MCC MNC/SIM = "+m);
  		m=dtest.getMCCMNC_AIR();
  		msg("info","MCC MNC/AIR = "+m);
  		m=dtest.getReadersNames();
  		msg("info",m);
	}
}
