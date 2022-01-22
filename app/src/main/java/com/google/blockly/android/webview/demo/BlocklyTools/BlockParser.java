package com.google.blockly.android.webview.demo.BlocklyTools;

import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.FunctionBlock;

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
        //todo OnGameEnd

        for(int i = 0; i < array.length(); i++){
            // Figure out which type it is
            JSONObject obj = array.getJSONObject(i);
            String type = obj.getString("type");
            switch (type) {
                case "configuration":
                    gameDef.getConfig().parseFromJson(obj);
                    break;
                case "ongamestart":
                    gameDef.getOnStart().parseFromJson(obj);
                    break;
                case "on_tile_press":
                    gameDef.getOnTilePress().parseFromJson(obj);
                    break;

                case "procedures_defnoreturn":
                    FunctionBlock fb = new FunctionBlock();
                    fb.parseFromJson(obj);
                    gameDef.getFunctions().put(obj.getJSONObject("fields").getString("NAME"), fb);

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

    private final String tmp = "{\"blocks\":{\"blocks\":[{\"id\":\"Z?xca_pRDbNnb$9bYK59\",\"inputs\":{\"do\":{\"block\":{\"id\":\"r5)\\/2zC]q63;$v:f]aP3\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"]7E)^Y3UTa]pr642?A#T\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"NUM\":0},\"id\":\"fekS}nRvR:_Dp8:aRQAm\",\"type\":\"math_number\"}},\"score\":{\"block\":{\"fields\":{\"NUM\":1},\"id\":\"m0IuX(|SDoIujU!_94{Q\",\"type\":\"math_number\"}}},\"next\":{\"block\":{\"id\":\"_@s88]%FrgAP@\\/IDm-7T\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"NUM\":0},\"id\":\"w4C#XC^EZT7Dx)A6#fRi\",\"type\":\"math_number\"}},\"num_tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\":{(sH)e)B.~11{Pf]]B6\"}},\"id\":\"HsZ(nTjPz5qPIrdB$)Ln\",\"type\":\"variables_get\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameloop\"},\"id\":\"y-Wf#+OEi{*AvpSJkp7v\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"set_tile_colour\"}},\"type\":\"addplayerscore\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"AND\"},\"id\":\"}))|.y+Jm@Ft86Lb:[CC\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"@5tLB$*^`HMZ\\/r$I.KT?\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"NAME\":\"TILE\"},\"id\":\"u-RII$qj[(bTChiaP3n:\",\"type\":\"incoming_event\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\":{(sH)e)B.~11{Pf]]B6\"}},\"id\":\"+R$:rPbSI3~;pSp2Nngt\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}},\"B\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\";6;iYaO0Q2@.1SUy$h`1\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"NAME\":\"TYPE\"},\"id\":\"bv\\/mGb~.([Fck}}!Hg$N\",\"type\":\"incoming_event\"}},\"B\":{\"block\":{\"id\":\"}pw\\/opZ!24zu_bfGrT]O\",\"type\":\"event_press\"}}},\"type\":\"logic_compare\"}}},\"type\":\"logic_operation\"}}},\"type\":\"controls_if\"}}},\"type\":\"on_tile_press\",\"x\":-56,\"y\":568},{\"id\":\"d`_qYxa8;Mtx4\\/%+isvA\",\"inputs\":{\"gameType\":{\"block\":{\"fields\":{\"NAME\":\"T\"},\"id\":\"S%J(rcb!kv(h.TZLx=VC\",\"inputs\":{\"limit\":{\"block\":{\"fields\":{\"NUM\":30},\"id\":\"qAQ{KN:@fL`,qwYIvX2)\",\"type\":\"math_number\"}}},\"type\":\"gametype\"}},\"numPlayers\":{\"block\":{\"fields\":{\"NUM\":1},\"id\":\"=(QMO(aV]OUZN9-]Crs_\",\"type\":\"math_number\"}},\"numTiles\":{\"block\":{\"fields\":{\"NUM\":4},\"id\":\"]fD!qD0\\/K*_sVXGCG-53\",\"type\":\"math_number\"}}},\"type\":\"configuration\",\"x\":-51,\"y\":431},{\"id\":\"o(c;%dm;-ZcL+$hDRqq3\",\"inputs\":{\"NAME\":{\"block\":{\"extraState\":{\"name\":\"gameloop\"},\"id\":\"{-PQi+nCOcM%Xe+.OdhR\",\"type\":\"procedures_callnoreturn\"}}},\"type\":\"ongamestart\",\"x\":-59,\"y\":174},{\"fields\":{\"NAME\":\"gameloop\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"Hh1KNAo(_oMScy19s~Y%\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\":{(sH)e)B.~11{Pf]]B6\"}},\"id\":\"JrITrpVr[I9ND4;++*g4\",\"inputs\":{\"VALUE\":{\"block\":{\"id\":\"01VGt,ht_y`USjid]nIH\",\"inputs\":{\"FROM\":{\"shadow\":{\"fields\":{\"NUM\":1},\"id\":\"Q4Tr^rwAiCHM~UiZEJ[_\",\"type\":\"math_number\"}},\"TO\":{\"shadow\":{\"fields\":{\"NUM\":4},\"id\":\"N_Vn~;QdT_#;_(~Bf6Z}\",\"type\":\"math_number\"}}},\"type\":\"math_random_int\"}}},\"next\":{\"block\":{\"id\":\"^;llyWcXfWj3$#yW{yrJ\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"NUM\":1},\"id\":\"Q^\\/(VM!!%`GF3~ZA60E-\",\"type\":\"math_number\"}},\"num_tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\":{(sH)e)B.~11{Pf]]B6\"}},\"id\":\"K19vBxLZOX20icR2sH1~\",\"type\":\"variables_get\"}}},\"type\":\"set_tile_colour\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":-50,\"y\":253}],\"languageVersion\":0},\"variables\":[{\"id\":\":{(sH)e)B.~11{Pf]]B6\",\"name\":\"tile\"}]}";
}
