package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenDefineRandomSequence extends ThenBlock {
    private int length;
    private String name;

    public ThenDefineRandomSequence(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.defineRandomSequence(this.id, this.name, this.length);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.length = json.getJSONObject("fields").getInt("num");
        this.name = json.getJSONObject("fields").getString("name");
    }
}
