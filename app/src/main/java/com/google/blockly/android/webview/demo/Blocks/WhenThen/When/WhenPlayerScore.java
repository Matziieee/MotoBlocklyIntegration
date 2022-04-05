package com.google.blockly.android.webview.demo.Blocks.WhenThen.When;

import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class WhenPlayerScore extends WhenBlock {

    int score;

    public WhenPlayerScore(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.score = json.getJSONObject("fields").getInt("num");
    }

    public int getScore() {
        return score;
    }
}
