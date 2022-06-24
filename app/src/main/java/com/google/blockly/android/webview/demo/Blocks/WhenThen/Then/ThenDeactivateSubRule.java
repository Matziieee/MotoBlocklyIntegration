package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenDeactivateSubRule extends ThenBlock {
    private String name;

    public ThenDeactivateSubRule(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api) {
        api.deactivateSubrule(name);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.name = json.getJSONObject("fields").getString("rule_name");
    }
}
