package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenTurnPairOff extends ThenBlock {
    boolean shouldSetIdle;
    String pairId;

    public ThenTurnPairOff(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        api.turnPairOff(pairId, shouldSetIdle);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.pairId = json.getJSONObject("fields").getString("pair_name");
        this.shouldSetIdle = json.getJSONObject("fields").getBoolean("set_idle");
    }
}
