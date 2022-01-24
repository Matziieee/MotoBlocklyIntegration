package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RepeatXTimesLoopBlock extends AbstractExecutableBlock{

    int timesToRepeat = 0;
    ArrayList<AbstractExecutableBlock> statements = new ArrayList<>();


    public RepeatXTimesLoopBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        JSONObject statementBlock = json
                .getJSONObject("inputs")
                .getJSONObject("DO")
                .getJSONObject("block");

        statements = parser.parseJson(statementBlock, new ArrayList<>());
        //todo handle number blocks as well
        this.timesToRepeat = json
                .getJSONObject("inputs")
                .getJSONObject("TIMES")
                .getJSONObject("shadow")
                .getJSONObject("fields")
                .getInt("NUM");
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        int repeated = 0;
        while(repeated < this.timesToRepeat){
            for (AbstractExecutableBlock e : statements) {
                e.execute(state, api);
            }
            repeated++;
        }
    }
}
