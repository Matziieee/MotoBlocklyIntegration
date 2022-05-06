package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenPlaySound extends ThenBlock {
    private String sound;

    public ThenPlaySound(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.playSound(this.sound);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.sound = json.getJSONObject("fields").getString("sound");
    }
}
