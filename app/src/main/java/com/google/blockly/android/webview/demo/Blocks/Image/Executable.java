package com.google.blockly.android.webview.demo.Blocks.Image;

import com.google.blockly.android.webview.IMotoImageGameAPI;
import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Executable extends AbstractBlock {

    public Executable (JSONObject json) throws JSONException {
        this.parseFromJson(json);
    }

    public abstract void execute(IMotoImageGameAPI api);
}
