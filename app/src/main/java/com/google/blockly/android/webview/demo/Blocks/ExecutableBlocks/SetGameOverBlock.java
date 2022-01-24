package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class SetGameOverBlock extends AbstractExecutableBlock{
    public SetGameOverBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        //Nothing to parse in this
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.setGameOver();
    }
}
