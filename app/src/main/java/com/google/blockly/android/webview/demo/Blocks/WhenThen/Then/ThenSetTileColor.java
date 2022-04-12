package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenSetTileColor extends ThenBlock {
    public ThenSetTileColor(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {

    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {

    }
}
