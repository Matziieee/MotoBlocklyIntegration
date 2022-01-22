package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;

public class GameTypeBlock implements IBlock{

    private int threshold = -1;
    private String type = "";

    public int getThreshold() {
        return threshold;
    }

    public String getType() {
        return type;
    }

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        JSONObject block = json.getJSONObject("block");
        JSONObject fields = block.getJSONObject("fields");
        JSONObject inputs = block.getJSONObject("inputs");
        this.threshold = inputs
                .getJSONObject("limit")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getInt("NUM");
        String val = fields.getString("NAME");
        if(val.equals("T")){
            this.type = "time";
        }else{
            this.type = "score";
        }
    }
}
