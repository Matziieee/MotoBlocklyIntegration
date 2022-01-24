package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class SetTileColourBlock extends AbstractExecutableBlock {
    private ColourBlock colour;
    private NumberBlock tile;

    public SetTileColourBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        this.colour = new ColourBlock(inputs
                .getJSONObject("colour")
                .getJSONObject("block"));
        this.tile = new NumberBlock(inputs
                .getJSONObject("tile")
                .getJSONObject("block"));

    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.setTileColour(
                tile.getValue(api, state),
                colour.getValue(api, state));
    }
}
