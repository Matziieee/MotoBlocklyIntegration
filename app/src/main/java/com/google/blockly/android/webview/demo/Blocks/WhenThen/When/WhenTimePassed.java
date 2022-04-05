package com.google.blockly.android.webview.demo.Blocks.WhenThen.When;

import org.json.JSONException;
import org.json.JSONObject;

public class WhenTimePassed extends WhenBlock{

    private int interval;

    public WhenTimePassed(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.interval = json.getJSONObject("fields").getInt("num");
    }

    public int getInterval() {
        return interval;
    }
}
