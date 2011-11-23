function log(txt,sep) {
	dtest.log(txt);
	e=document.getElementById('io');
	if (e)
		e.innerHTML+=txt+sep+"\n";
}
function msg(txt) {
	log(txt,"<hr />");
}
function msgi(txt) {
	log("<span style='color: green'>"+txt+"</span>","<br />");
}
function msge(txt) {
	log("<span style='color: red'>"+txt+"</span>","<br />");
}
function msgd(txt) {
	log("<div style='background-color: yellow;font-style:italic'>"+txt+"</div>","");
}
function msgh(txt) {
	log("<h2>"+txt+"</h2>","");
}
