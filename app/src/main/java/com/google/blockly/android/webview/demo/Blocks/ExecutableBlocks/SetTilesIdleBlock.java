package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class SetTilesIdleBlock extends AbstractExecutableBlock{

    ColourBlock cb;

    public SetTilesIdleBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.cb = new ColourBlock(json
                .getJSONObject("inputs")
                .getJSONObject("colour")
                .getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.setAllTilesIdle(this.cb.getValue(api, state));
    }
}
