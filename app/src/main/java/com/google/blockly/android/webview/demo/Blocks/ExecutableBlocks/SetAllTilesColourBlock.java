package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.IComparableValueBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class SetAllTilesColourBlock extends AbstractExecutableBlock{
    IComparableValueBlock<Integer> colourBlock;
    public SetAllTilesColourBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        if(json.has("inputs")){
            BlocklyValueParser valueParser = new BlocklyValueParser();
            JSONObject col = json.getJSONObject("inputs").has("colour")
                    ? json.getJSONObject("inputs").getJSONObject("colour")
                    : json.getJSONObject("inputs").getJSONObject("Colour");
            this.colourBlock = valueParser.parseJson(col.getJSONObject("block"));
        }
        else{
            this.colourBlock = new ColourBlock(json.getJSONObject("fields"));
        }
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.setAllTilesToColour(this.colourBlock.getValue(api, state));
    }
}
