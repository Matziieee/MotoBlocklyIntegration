package com.google.blockly.android.webview.demo.BlocklyTools;

import android.service.quicksettings.Tile;
import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AddPlayerScoreBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.CallFunctionBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.ForILoopBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.IfBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.MotoSoundSpeak;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.PlayerBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.RepeatXTimesLoopBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetAllTilesColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetExpectedNextPressBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetGameOverBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetTileColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetTileColourCountdownBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetTilesIdleBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.SetVariableBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.StartTimerBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.StopTimerBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.TileBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlocklyExecutableBlockParser {

    public ArrayList<AbstractExecutableBlock> parseJson(JSONObject json, ArrayList<AbstractExecutableBlock> result) throws JSONException {
        String type = json.getString("type");
        switch (type){
            case "player":
                PlayerBlock pb = new PlayerBlock(json);
                result.add(pb);
                break;
            case "tile":
                TileBlock tb = new TileBlock(json);
                result.add(tb);
                break;
            case "setalltilescolour":
                SetAllTilesColourBlock satc = new SetAllTilesColourBlock(json);
                result.add(satc);
                break;
            case "settilecolourcountdown":
                SetTileColourCountdownBlock stcc = new SetTileColourCountdownBlock(json);
                result.add(stcc);
                break;
            case "setexpectednextpress":
                SetExpectedNextPressBlock senpb = new SetExpectedNextPressBlock(json);
                result.add(senpb);
                break;
            case "stoptimer":
                StopTimerBlock stopTimerBlock = new StopTimerBlock(json);
                result.add(stopTimerBlock);
                break;
            case "controls_for":
                ForILoopBlock filb = new ForILoopBlock(json);
                result.add(filb);
                break;
            case "starttimer":
                StartTimerBlock stb = new StartTimerBlock(json);
                result.add(stb);
                break;
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
            case "settilecolor":
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
            default: Log.e("ERROR", "Unknown executable block found; " + type); break;
        }
        if(json.has("next")){
            return parseJson(json.getJSONObject("next").getJSONObject("block"),result);
        }
        return result;
    }
}
