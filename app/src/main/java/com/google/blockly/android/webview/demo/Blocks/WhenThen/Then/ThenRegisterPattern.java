package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenRegisterPattern extends ThenBlock {
    public ThenRegisterPattern(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {

    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {

    }
}
