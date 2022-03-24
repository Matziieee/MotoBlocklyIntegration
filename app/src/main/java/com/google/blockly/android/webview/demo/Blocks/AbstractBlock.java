package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractBlock {

    //Empty constructor for init without json
    protected AbstractBlock(){}
    public AbstractBlock(JSONObject json) throws JSONException {
        this.parseFromJson(json);
    };

    protected abstract void parseFromJson(JSONObject json) throws JSONException;
}
