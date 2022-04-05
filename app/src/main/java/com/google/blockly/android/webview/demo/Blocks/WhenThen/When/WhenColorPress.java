package com.google.blockly.android.webview.demo.Blocks.WhenThen.When;

import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class WhenColorPress extends WhenBlock {
    private int col;

    public WhenColorPress(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.col = json.getJSONObject("fields").getInt("col");
    }

    public int getCol() {
        return col;
    }
}
