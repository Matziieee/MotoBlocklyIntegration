package com.google.blockly.android.webview.demo.BlocklyTools;

import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.ConfigBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.OnEventBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.FunctionBlock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class BlockParser {

    private static final BlockParser instance;

    static {
        instance = new BlockParser();
    }

    private BlockParser(){ }

    public static BlockParser getInstance(){
        return instance;
    }

    public BlocklyGameDefinition getFromTmp() throws JSONException {
        return this.parseJson(new JSONObject(tmp));
    }
    public BlocklyGameDefinition parseJson (JSONObject json) throws JSONException {
        JSONArray array = json.getJSONObject("blocks").getJSONArray("blocks");
        BlocklyGameDefinition gameDef = new BlocklyGameDefinition();
        //Get all the outer blocks
        //Outer Blocks can be: Configuration, OnTilePress and OnGameStart

        for(int i = 0; i < array.length(); i++){
            // Figure out which type it is
            JSONObject obj = array.getJSONObject(i);
            String type = obj.getString("type");
            switch (type) {
                case "configuration":
                    gameDef.setConfig(new ConfigBlock(obj));
                    break;
                case "ongamestart":
                    gameDef.setOnStart(new OnEventBlock(obj));
                    break;
                case "ontilepress":
                    gameDef.setOnTilePress(new OnEventBlock(obj));
                    break;
                case "ongameend":
                    gameDef.setOnGameEnd(new OnEventBlock(obj));
                    break;
                case "procedures_defnoreturn":
                    FunctionBlock fb = new FunctionBlock(obj);
                    gameDef.getFunctions().put(obj.getJSONObject("fields").getString("NAME"), fb);
                    break;

                default: Log.i("ERROR", "Unknown outer block found; " + type); break;
            }
        }
        JSONArray variables = json.getJSONArray("variables");
        for (int i = 0; i < variables.length(); i++) {
            JSONObject var = variables.getJSONObject(i);
            gameDef.getVariables().put(var.getString("id"), null);
        }
        return gameDef;
    }

    private final String tmp = "{\"blocks\":{\"blocks\":[{\"id\":\"y:L_Z6poClYt94J=@UJ,\",\"inputs\":{\"name\":{\"block\":{\"fields\":{\"TEXT\":\"30 second race\"},\"id\":\"FS=VnRS|isrj:0q`#(z$\",\"type\":\"text\"}},\"numplayers\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"Ui-ta;@kD*yh}5Xxc07a\",\"type\":\"number\"}},\"numtiles\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"QGu`DlYVmfyr-C}rTEcb\",\"type\":\"number\"}},\"type\":{\"block\":{\"fields\":{\"goal\":30,\"type\":\"t\"},\"id\":\"1Qd/a|d8@|x@+S}Kpe?8\",\"type\":\"gametype\"}}},\"type\":\"configuration\",\"x\":25,\"y\":32},{\"id\":\"|j,^-#rLnw-edxp5;w)M\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"X}TCPgchWpL0ed?65E@I\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\";S:n@35;n-i|1!jKcR.b\",\"type\":\"colour\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameLoop\"},\"id\":\"ag*k!~f+!WO76kW!5;l2\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"settilesidle\"}}},\"type\":\"ongamestart\",\"x\":24,\"y\":221},{\"fields\":{\"NAME\":\"gameLoop\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"vWTRBMZ6eKdvSeFH=t|3\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"twQ9BY~st5d2WEW+RG1O\"}},\"id\":\"@O=xd^HF.mT3Bh4UE{06\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"from\":1,\"to\":4},\"id\":\"%7[C)cSP1H!1MO)xa1*Z\",\"type\":\"randomnumber\"}}},\"next\":{\"block\":{\"id\":\"iBn9,Bw.!yFtm.owaY3H\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"2\"},\"id\":\"kxD?R?hw+eu?M0?ZyvV)\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"twQ9BY~st5d2WEW+RG1O\"}},\"id\":\"W@iU@2@dRMA^B],lNx]W\",\"type\":\"variables_get\"}}},\"type\":\"settilecolor\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":23,\"y\":374},{\"id\":\"ma?28}zjq?@/8|STY.+:\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"LhU$[rOGlpZ95t[};X@h\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\":LOfkR^rgwy_]!/[#{Fm\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\"i!lxCt4`?^oCH:h+%qe|\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"twQ9BY~st5d2WEW+RG1O\"}},\"id\":\"{PV~K$HE5P?Wb{aK6jIK\",\"type\":\"variables_get\"}}},\"next\":{\"block\":{\"id\":\"s16wcc4XCf,_-TSYy@1n\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"Z3?Fl7LE]O;T*=OZgkAw\",\"type\":\"number\"}},\"score\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"qVg|!!MtPT@=5F9!26G!\",\"type\":\"number\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameLoop\"},\"id\":\"ise4t7vi5S9#cv^4MF0m\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"addplayerscore\"}},\"type\":\"settilecolor\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"R-dPKBZk|sN*9pA_#U;W\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"/-T~$tH-hB?il@}7eb;f\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"twQ9BY~st5d2WEW+RG1O\"}},\"id\":\"`BpA1XHu8#8_N3H`]1$i\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}}},\"type\":\"ontilepress\",\"x\":21,\"y\":553}],\"languageVersion\":0},\"variables\":[{\"id\":\"twQ9BY~st5d2WEW+RG1O\",\"name\":\"tile\"}]}";
}
