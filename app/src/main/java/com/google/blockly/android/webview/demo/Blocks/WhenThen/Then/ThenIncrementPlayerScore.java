package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenIncrementPlayerScore extends ThenBlock {
    int player;

    public ThenIncrementPlayerScore(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.incrementPlayerScore(player);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.player = json.getJSONObject("fields").getInt("num");
    }
}
