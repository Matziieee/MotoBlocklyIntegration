package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenDefineRandomPair extends ThenBlock {
    private String name;
    private boolean withSound;
    public ThenDefineRandomPair(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        api.defineRandomPair(this.id, name, withSound);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.name = json.getJSONObject("fields").getString("name");
        this.withSound = json.getJSONObject("fields").getBoolean("with_sound");
    }
}
