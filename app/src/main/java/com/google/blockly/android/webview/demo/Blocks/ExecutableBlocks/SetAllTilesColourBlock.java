package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class SetAllTilesColourBlock extends AbstractExecutableBlock{
    ColourBlock colourBlock;
    public SetAllTilesColourBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.colourBlock = new ColourBlock(json.getJSONObject("inputs").getJSONObject("Colour").getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.setAllTilesToColour(this.colourBlock.getValue(api, state));
    }
}
