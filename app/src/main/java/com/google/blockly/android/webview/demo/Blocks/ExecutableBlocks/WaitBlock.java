package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class WaitBlock extends AbstractExecutableBlock{
    private int seconds;

    public WaitBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.seconds = json.getJSONObject("fields").getInt("time");
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.beginWait(seconds);
    }
}
