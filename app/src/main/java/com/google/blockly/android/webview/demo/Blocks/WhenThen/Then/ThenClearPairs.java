package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenClearPairs extends ThenBlock {

    public ThenClearPairs(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        api.clearPairs();
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {

    }
}
