package com.google.blockly.android.webview.demo.BlocklyTools;

import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.ConfigBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.OnEventBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.FunctionBlock;
import com.google.blockly.android.webview.demo.Blocks.GameBlock;

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
                case "gameblock":
                    gameDef.setGameBlock(new GameBlock(obj));
                    break;
                case "procedures_defnoreturn":
                    FunctionBlock fb = new FunctionBlock(obj);
                    gameDef.getFunctions().put(obj.getJSONObject("fields").getString("NAME"), fb);
                    break;

                default: Log.e("ERROR", "Unknown outer block found; " + type); break;
            }
        }
        JSONArray variables = json.getJSONArray("variables");
        for (int i = 0; i < variables.length(); i++) {
            JSONObject var = variables.getJSONObject(i);
            gameDef.getVariables().put(var.getString("id"), null);
        }
        return gameDef;
    }

}
