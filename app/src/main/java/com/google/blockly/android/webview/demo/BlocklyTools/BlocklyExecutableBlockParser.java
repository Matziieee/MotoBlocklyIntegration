package com.google.blockly.android.webview.demo.BlocklyTools;

import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AddPlayerScoreBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.CallFunctionBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.IfBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.MotoSoundSpeak;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.RepeatXTimesLoopBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetGameOverBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetTileColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetTilesIdleBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetVariableBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlocklyExecutableBlockParser {

    public ArrayList<AbstractExecutableBlock> parseJson(JSONObject json, ArrayList<AbstractExecutableBlock> result) throws JSONException {
        //Given: "block : {..}
        //Four types:  procedures_callnoreturn, variables_set, set_tile_colour, addplayerscore
        String type = json.getString("type");
        switch (type){
            case "setgameover":
                SetGameOverBlock sgob = new SetGameOverBlock(json);
                result.add(sgob);
                break;
            case "motosound_speak":
                MotoSoundSpeak mss = new MotoSoundSpeak(json);
                result.add(mss);
                break;
            case "controls_repeat_ext":
                RepeatXTimesLoopBlock rxtb = new RepeatXTimesLoopBlock(json);
                result.add(rxtb);
                break;
            case "settilesidle":
                SetTilesIdleBlock stib = new SetTilesIdleBlock(json);
                result.add(stib);
                break;
            case "procedures_callnoreturn":
                CallFunctionBlock cfb = new CallFunctionBlock(json);
                result.add(cfb);
                break;
            case "variables_set":
                SetVariableBlock svb = new SetVariableBlock(json);
                result.add(svb);
                break;
            case "set_tile_colour":
                SetTileColourBlock stcb = new SetTileColourBlock(json);
                result.add(stcb);
                break;
            case "addplayerscore":
                AddPlayerScoreBlock apsb = new AddPlayerScoreBlock(json);
                result.add(apsb);
                break;
            case "controls_if":
                IfBlock ifBlock = new IfBlock(json);
                result.add(ifBlock);
                break;
            default: Log.i("ERROR", "Unknown executable block found; " + type); break;
        }
        if(json.has("next")){
            return parseJson(json.getJSONObject("next").getJSONObject("block"),result);
        }
        return result;
    }
}
