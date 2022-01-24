package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.AbstractValueBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class SetVariableBlock extends AbstractExecutableBlock {

    private String sets;
    private AbstractValueBlock value;

    public SetVariableBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyValueParser parser = new BlocklyValueParser();
        JSONObject valueBlock = json
                .getJSONObject("inputs")
                .getJSONObject("VALUE")
                .getJSONObject("block");
        value = parser.parseJson(valueBlock);
        this.sets = json
                .getJSONObject("fields")
                .getJSONObject("VAR")
                .getString("id");
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        state.getVariables().put(sets, value.getValue(api,state));
    }
}
