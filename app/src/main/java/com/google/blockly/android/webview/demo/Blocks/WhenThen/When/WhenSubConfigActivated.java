package com.google.blockly.android.webview.demo.Blocks.WhenThen.When;

import org.json.JSONException;
import org.json.JSONObject;

public class WhenSubConfigActivated extends WhenBlock {
    private String name;

    public WhenSubConfigActivated(JSONObject block) throws JSONException {
        super(block);
    }

    public String getName() {
        return name;
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.name = json.getJSONObject("fields").getString("rule_name");
    }
}
