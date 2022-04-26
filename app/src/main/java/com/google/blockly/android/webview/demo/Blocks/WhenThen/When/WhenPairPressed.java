package com.google.blockly.android.webview.demo.Blocks.WhenThen.When;

import org.json.JSONException;
import org.json.JSONObject;

public class WhenPairPressed extends WhenBlock {
    private String pairId;

    public WhenPairPressed(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.pairId = json.getJSONObject("fields").getString("pair_name");
    }

    public String getPairId() {
        return pairId;
    }

    public WhenPairPressed(JSONObject jsonObject, String pairId) throws JSONException {
        super(jsonObject);
        this.pairId = pairId;
    }
}
