package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;

public class GameTypeBlock extends AbstractBlock {

    private int threshold = -1;
    private String type = "";

    public GameTypeBlock(JSONObject json) throws JSONException {
        super(json);
    }

    public int getThreshold() {
        return threshold;
    }

    public String getType() {
        return type;
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject block = json.getJSONObject("block");
        JSONObject fields = block.getJSONObject("fields");
        this.threshold = fields.getInt("goal");
        String val = fields.getString("type");
        if(val.equals("t")){
            this.type = "time";
        }else{
            this.type = "score";
        }
    }
}
