#!/bin/bash

echo -n >out/tests.html

function genHdr() {
(
cat frags/00header-a.frag
echo "<head>"
cat frags/01beg.frag
cat frags/02js.frag
) >> "$1"
}

function addHdrMain() {
# add header features for main file
(
cat <<DATA
</head>
<body>
<div>
<div class="pm">
<h1>Android Test Framework</h1>
</div>
<hr />
<ul class='pm'>
DATA
) >> "$1"
}

function mkTest() {
  FILE="$1"
  DESC="$2"
  echo -n >out/${FILE}.html
  genHdr out/${FILE}.html
(
cat <<DATA
<script rel="script" type="text/javascript" language="javascript" src="${FILE}.js"></script>
<script rel="script" type="text/javascript" language="javascript">
function go() {
  m=dtest.setReaderName(SEID);
  msg("info",m);
  m=dtest.UICCInit("${FILE}/${DESC}");
}
</script>
</head>
<body style='margin:0;padding:0' onLoad='go()'>
<div>
<div class="pm">
<h1>Android Test Framework</h1>
</div>
<hr />
<div id='io'>Waiting for UICC response<br /></div>
</div>
</body>
</html>
DATA
) >>"out/${FILE}.html"
if [ -f "tsts/${FILE}.js" ]; then
  cp "tsts/${FILE}.js" "out/${FILE}.js"
else
  echo -n >"out/${FILE}.js"
fi
}

genHdr out/tests.html
addHdrMain out/tests.html

grep -v "^[;#]" tests.txt | while IFS=":" read FILE DESC; do
  echo file=$FILE desc=$DESC

  echo "<li class='arrow li-red' onclick='document.location.href=\"${FILE}.html\"'>$DESC</li>" >>out/tests.html
  echo "<li class='desc'>" >>out/tests.html
  if [ -f "tsts/${FILE}.txt" ]; then
	cat "tsts/${FILE}.txt" >>out/tests.html
  else
	echo "No description" >>out/tests.html
  fi
  echo "</li>" >>out/tests.html
  mkTest "$FILE" "$DESC"
done

(
cat <<DATA
<li class='arrow li-red' onclick='document.location.href="../about.html"'>About</li>
<li class='arrow li-red' onclick='document.location.href="../home.html"'>Home</li>
</ul>
<br />
</div>
</body>
</html>
DATA
) >>out/tests.html

cp -rf out/. ../assets/tst-mw/
