package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenDefineRandomPair extends ThenBlock {
    private String name;
    public ThenDefineRandomPair(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.defineRandomPair(this.id, name);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.name = json.getJSONObject("fields").getString("name");
    }
}
