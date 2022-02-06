package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.AbstractValueBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class StopTimerBlock extends AbstractExecutableBlock{

    AbstractValueBlock<String> name;

    public StopTimerBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyValueParser parser = new BlocklyValueParser();
        this.name = parser.parseJson(json.getJSONObject("inputs").getJSONObject("name").getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.stopTimer(this.name.getValue(api, state));
    }
}
