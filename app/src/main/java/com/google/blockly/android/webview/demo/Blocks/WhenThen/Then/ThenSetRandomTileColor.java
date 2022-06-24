package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenSetRandomTileColor extends ThenBlock {
    private int color;
    public ThenSetRandomTileColor(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        api.setRandomTileColor(color);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.color = json.getJSONObject("fields").getInt("col");
    }
}
