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
