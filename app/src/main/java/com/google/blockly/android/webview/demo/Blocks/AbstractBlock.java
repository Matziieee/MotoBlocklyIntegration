package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractBlock {

    protected String id;
    //Empty constructor for init without json
    protected AbstractBlock(){}
    public AbstractBlock(JSONObject json) throws JSONException {
        this.id = json.getString("id");
        this.parseFromJson(json);
    };

    public String getId() {
        return id;
    }

    protected abstract void parseFromJson(JSONObject json) throws JSONException;
}
