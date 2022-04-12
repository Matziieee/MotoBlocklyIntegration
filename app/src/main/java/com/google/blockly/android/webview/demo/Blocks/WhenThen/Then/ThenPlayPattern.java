package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenPlayPattern extends ThenBlock {
    String targetId;
    public ThenPlayPattern(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.playPattern(targetId);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.targetId = json.getJSONObject("fields").getString("pattern_name");
    }
}
