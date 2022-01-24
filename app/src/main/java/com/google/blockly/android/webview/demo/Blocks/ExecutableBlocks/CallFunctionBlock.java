package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class CallFunctionBlock extends AbstractExecutableBlock {

    private String calls;

    public CallFunctionBlock(JSONObject json) throws JSONException {
        super(json);
    }

    //Todo, consider adding support method calls with params
    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.calls = json.getJSONObject("extraState").getString("name");
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        state.getFunctions().get(calls).execute(state, api);
    }
}
