package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenTurnPairOn extends ThenBlock {
    private String pairName;
    private int color;

    public ThenTurnPairOn(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        api.turnPairOn(pairName, color);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.pairName = json.getJSONObject("fields").getString("pair_name");
        this.color = json.getJSONObject("fields").getInt("col");
    }
}
