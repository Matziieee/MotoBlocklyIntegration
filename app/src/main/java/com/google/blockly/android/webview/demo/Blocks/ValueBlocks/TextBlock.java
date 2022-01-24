package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class TextBlock extends IComparableValueBlock<String>{

    private String value;
    public TextBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.value = json.getJSONObject("fields").getString("TEXT");
    }

    @Override
    public String getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        return this.value;
    }

    @Override
    public boolean compare(String left, String right, char com) {
        if(com != '='){
            throw new RuntimeException("Operator " + com + " not supported for text");
        }
        return left.equals(right);
    }
}
