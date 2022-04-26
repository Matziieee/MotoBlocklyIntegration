
var BLOCKLY_TOOLBOX_XML = BLOCKLY_TOOLBOX_XML || Object.create(null);
/* BEGINNING BLOCKLY_TOOLBOX_XML ASSIGNMENT. DO NOT EDIT. USE BLOCKLY DEVTOOLS. */
BLOCKLY_TOOLBOX_XML['mads'] =
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="When" colour="#5ba58c">'
+   '<block type = "when"></block>'
+ '</category>'
+ '<category name="Then" colour="#008080">'
+   '<block type = "then"></block>'
+ '</category>'
+ '<category name="Sub Rules" colour="#B37A4C">'
+   '<block type = "subConfig"></block>'
+ '</category>'
+'</xml>';

BLOCKLY_TOOLBOX_XML['l1'] =
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="Actions" colour="#5b67a5">'
+   '<block type = "l1_setalltilescolour"></block>'
+   '<block type = "l1_settilecolour"></block>'
+   '<block type = "wait"></block>'
+   '<block type = "l1_gameblock"></block>'
+ '</category>'
+'</xml>';

BLOCKLY_TOOLBOX_XML['l2'] =
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="Colours" colour="#5ba58c">'
+   '<block type="colour">'
+     '<field name="colour">0</field>'
+   '</block>'
+ '</category>'
+ '<category name="Variables" colour="#a55b80" custom="VARIABLE"></category>'
+ '<category name="Actions" colour="#5b67a5">'
+   '<block type = "l2_setalltilescolour"></block>'
+   '<block type = "wait"></block>'
+ '</category>'
+'</xml>';

BLOCKLY_TOOLBOX_XML['l3'] =
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="Variables" colour="#a55b80" custom="VARIABLE"></category>'
+ '<category name="Colours" colour="#5ba58c">'
+   '<block type="colour">'
+     '<field name="colour">0</field>'
+   '</block>'
+ '</category>'
+ '<category name="Numbers" colour="#5b67a5">'
+   '<block type="number">'
+     '<field name="number">0</field>'
+   '</block>'
+   '<block type="randomnumber">'
+     '<field name="from">1</field>'
+     '<field name="to">4</field>'
+   '</block>'
+   '<block type="math_arithmetic">'
+     '<field name="OP">ADD</field>'
+     '<value name="A">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="B">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+ '<category name="Actions" colour="#5b67a5">'
+   '<block type = "l2_setalltilescolour"></block>'
+   '<block type = "wait"></block>'
+   '<block type = "l1_gameblock"></block>'
+ '</category>'
+ '<category name="Logic" colour="#5ba58c">'
+   '<block type="controls_if"></block>'
+   '<block type="logic_compare">'
+     '<field name="OP">EQ</field>'
+   '</block>'
+'<block type="logic_operation">'
+     '<field name="OP">AND</field>'
+   '</block>'
+ '</category>'
+'</xml>';

BLOCKLY_TOOLBOX_XML['l4'] =
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="Variables" colour="#a55b80" custom="VARIABLE"></category>'
+ '<category name="Colours" colour="#5ba58c">'
+   '<block type="colour">'
+     '<field name="colour">0</field>'
+   '</block>'
+ '</category>'
+ '<category name="Numbers" colour="#5b67a5">'
+   '<block type="number">'
+     '<field name="number">0</field>'
+   '</block>'
+   '<block type="randomnumber">'
+     '<field name="from">1</field>'
+     '<field name="to">4</field>'
+   '</block>'
+ '</category>'
+ '<category name="Actions" colour="#5b67a5">'
+   '<block type = "l2_setalltilescolour"></block>'
+   '<block type = "wait"></block>'
+   '<block type = "l1_gameblock"></block>'
+   '<block type = "motosound_speak"></block>'
+   '<block type="l4_tile"></block>'
+ '</category>'
+ '<category name="Logic" colour="#5ba58c">'
+   '<block type="controls_if"></block>'
+   '<block type="logic_compare">'
+     '<field name="OP">EQ</field>'
+   '</block>'
+'<block type="logic_operation">'
+     '<field name="OP">AND</field>'
+   '</block>'
+ '</category>'
+ '<category name="Loops" colour="#a5745b">'
+   '<block type="controls_repeat_ext">'
+     '<value name="TIMES">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="controls_for">'
+     '<field name="VAR" id="F+h3}{Tu$,Iao+,.7@KK">i</field>'
+     '<value name="FROM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="TO">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="BY">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+ '<category name="Text" colour="#5b80a5">'
+   '<block type="text">'
+     '<field name="TEXT"></field>'
+   '</block>'
+ '</category>'
+'</xml>';

BLOCKLY_TOOLBOX_XML['l5'] =
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="Variables" colour="#a55b80" custom="VARIABLE"></category>'
+ '<category name="Functions" colour="#995ba5" custom="PROCEDURE"></category>'
+ '<category name="Colours" colour="#5ba58c">'
+   '<block type="colour">'
+     '<field name="colour">0</field>'
+   '</block>'
+ '</category>'
+ '<category name="Numbers" colour="#5b67a5">'
+   '<block type="number">'
+     '<field name="number">0</field>'
+   '</block>'
+   '<block type="randomnumber">'
+     '<field name="from">1</field>'
+     '<field name="to">4</field>'
+   '</block>'
+ '</category>'
+ '<category name="Actions" colour="#5b67a5">'
+   '<block type = "l2_setalltilescolour"></block>'
+   '<block type = "wait"></block>'
+   '<block type = "l1_gameblock"></block>'
+   '<block type = "motosound_speak"></block>'
+   '<block type="l4_tile"></block>'
+ '</category>'
+ '<category name="Logic" colour="#5ba58c">'
+   '<block type="controls_if"></block>'
+   '<block type="logic_compare">'
+     '<field name="OP">EQ</field>'
+   '</block>'
+'<block type="logic_operation">'
+     '<field name="OP">AND</field>'
+   '</block>'
+ '</category>'
+ '<category name="Loops" colour="#a5745b">'
+   '<block type="controls_repeat_ext">'
+     '<value name="TIMES">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="controls_for">'
+     '<field name="VAR" id="F+h3}{Tu$,Iao+,.7@KK">i</field>'
+     '<value name="FROM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="TO">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="BY">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+ '<category name="Text" colour="#5b80a5">'
+   '<block type="text">'
+     '<field name="TEXT"></field>'
+   '</block>'
+ '</category>'
+'</xml>';

BLOCKLY_TOOLBOX_XML['standard'] =
// From XML string/file, replace ^\s?(\s*)?(<.*>)$ with \+$1'$2'
// Tweak first and last line.
'<xml xmlns="https://developers.google.com/blockly/xml" id="toolbox" style="display: none">'
+ '<category name="Numbers" colour="#5b67a5">'
+   '<block type="number">'
+     '<field name="number">0</field>'
+   '</block>'
+   '<block type="randomnumber">'
+     '<field name="from">1</field>'
+     '<field name="to">4</field>'
+   '</block>'
+   '<block type="math_arithmetic">'
+     '<field name="OP">ADD</field>'
+     '<value name="A">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="B">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+ '<category name="Logic" colour="#5ba58c">'
+   '<block type="controls_if"></block>'
+   '<block type="logic_compare">'
+     '<field name="OP">EQ</field>'
+   '</block>'
+'<block type="logic_operation">'
+     '<field name="OP">AND</field>'
+   '</block>'
+ '</category>'
+ '<category name="Variables" colour="#a55b80" custom="VARIABLE"></category>'
+ '<category name="Functions" colour="#995ba5" custom="PROCEDURE"></category>'
+ '<category name="Text" colour="#5b80a5">'
+   '<block type="text">'
+     '<field name="TEXT"></field>'
+   '</block>'
+ '</category>'

+ '<category name="Colours" colour="#5ba58c">'
+   '<block type="colour">'
+     '<field name="colour">0</field>'
+   '</block>'
+   '<block type="randomcolour"></block>'
+ '</category>'

+ '<category name="Actions" colour="#5ba55b">'
+   '<block type="player"></block>'
+   '<block type="tile"></block>'
+   '<block type = "motosound_speak"></block>'
+   '<block type = "get_player_score"></block>'
+   '<block type = "setgameover"></block>'
+ '</category>'

+ '<category name="Timers" colour="#5ba55b">'
+   '<block type="starttimer"></block>'
+   '<block type="stoptimer"></block>'
+ '</category>'

+ '<category name="Loops" colour="#a5745b">'
+   '<block type="controls_repeat_ext">'
+     '<value name="TIMES">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+   '<block type="controls_for">'
+     '<field name="VAR" id="F+h3}{Tu$,Iao+,.7@KK">i</field>'
+     '<value name="FROM">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="TO">'
+       '<shadow type="math_number">'
+         '<field name="NUM">10</field>'
+       '</shadow>'
+     '</value>'
+     '<value name="BY">'
+       '<shadow type="math_number">'
+         '<field name="NUM">1</field>'
+       '</shadow>'
+     '</value>'
+   '</block>'
+ '</category>'
+'</xml>';
/* END BLOCKLY_TOOLBOX_XML ASSIGNMENT. DO NOT EDIT. */
