package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenWaitForSequence extends ThenBlock {

    private String correctName, incorrectName, patternId;
    public ThenWaitForSequence(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        api.waitForSequence(patternId, correctName, incorrectName);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.patternId = json.getJSONObject("fields").getString("seq_name");
        this.correctName = json.getJSONObject("fields").getString("correct_name");
        this.incorrectName = json.getJSONObject("fields").getString("incorrect_name");
    }
}
