package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;
import com.livelife.motolibrary.Game;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenStopGame extends ThenBlock {
    public ThenStopGame(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        ((Game)api).stopGame();
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {

    }
}
