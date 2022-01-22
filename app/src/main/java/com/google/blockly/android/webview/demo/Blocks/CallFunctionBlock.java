package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class CallFunctionBlock implements IExecutableBlock{

    private String calls;

    //Todo, consider adding support method calls with params
    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        this.calls = json.getJSONObject("extraState").getString("name");
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        state.getFunctions().get(calls).execute(state, api);
    }
}
