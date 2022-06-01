package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

public abstract class AbstractBlock {

    protected String id;
    //Empty constructor for init without json
    protected AbstractBlock(){}
    public AbstractBlock(JSONObject json) throws JSONException {
        if(json.has("id")){
            this.id = json.getString("id");
        }else{
            this.id = UUID.randomUUID().toString();
        }

        this.parseFromJson(json);
    }

    public String getId() {
        return id;
    }

    protected abstract void parseFromJson(JSONObject json) throws JSONException;
}
