function msg(type,txt) {

	dtest.log(type,txt);
	htxt=txt;
	switch (type) {
	  case "plain":
		htxt=txt+"<br />\n";
	  break;
	  case "info":
		htxt="<span style='color: green'>"+txt+"</span><br />\n";
	  break;
	  case "error":
		htxt="<span style='color: red'>"+txt+"</span><br />\n";
	  break;
	  case "debug":
		htxt="<div style='background-color: yellow;font-style:italic;border: 1px dotted black'>"+txt+"</div>\n";
	  break;
	  case "h":
		htxt="<h2>"+txt+"</h2>\n";
	  break;
	};
	e=document.getElementById('io');
	if (e)
		e.innerHTML+=htxt;
	else
		alert("no io div");
}
SEID="SIM: UICC";
var MCREL="A0 00 00 00 87 10 03 FF 49 94 20 89 FF 01 01 01";
//2PAY.SYS.DDF01
var PPSE="32 50 41 59 2E 53 59 53 2E 44 44 46 30 31";
//1PAY.SYS.DDF01
var PSE1="31 50 41 59 2E 53 59 53 2E 44 44 46 30 31";
var CRS="A0 00 00 01 51 43 52 53 00";
var VISA="A0 00 00 00 03 10 10";
var CASD="";
var RAPL1="";
var RAPL2="";
/*
This interface provides services to verify a key and to sign data.
The CASD shall publish an implementation of this
interface to the OPEN as a GlobalService, to make this service
available to other Security Domains.

There is only one CASD inside the card. The CASD shall register
this service as a unique Global Service with the service family
identifier ='83' (per section 8.1.3 of GP221[0]).

*/
function errox(id) {
	sw12u=id.substring(id.length-6,id.length-4)+id.substring(id.length-3,id.length-1);
	var sw12x=sw12u.toUpperCase();
	msg("debug","sw12 = "+sw12x+" : "+err[sw12x]);	
}
