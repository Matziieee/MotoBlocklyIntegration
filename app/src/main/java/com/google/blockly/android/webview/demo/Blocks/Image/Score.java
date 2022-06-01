package com.google.blockly.android.webview.demo.Blocks.Image;

import com.google.blockly.android.webview.IMotoImageGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class Score extends ExecutableNodeBlock{
    boolean isIncrement;
    public Score(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        String type = json.getString("type");
        isIncrement = type.equals("plusScore");
    }

    @Override
    public void execute(IMotoImageGameAPI api) {
        if(this.isIncrement){
            api.incrementScore();
        }
        else{
            api.decrementScore();
        }
    }
}
