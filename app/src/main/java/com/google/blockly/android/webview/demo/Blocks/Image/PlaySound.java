package com.google.blockly.android.webview.demo.Blocks.Image;

import com.google.blockly.android.webview.IMotoImageGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaySound extends ExecutableNodeBlock{
    public PlaySound(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {

    }

    @Override
    public void execute(IMotoImageGameAPI api) {
        api.playAnimalSound();
    }
}
