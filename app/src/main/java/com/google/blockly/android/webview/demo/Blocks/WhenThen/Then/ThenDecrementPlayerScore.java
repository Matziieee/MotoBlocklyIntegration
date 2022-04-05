package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenDecrementPlayerScore extends ThenBlock {
    int player;
    public ThenDecrementPlayerScore(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.decrementPlayerScore(player);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.player = json.getJSONObject("fields").getInt("num");
    }
}
