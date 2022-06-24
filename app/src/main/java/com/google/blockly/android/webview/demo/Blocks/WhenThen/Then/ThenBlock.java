package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;
import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ThenBlock extends AbstractBlock {

    public ThenBlock(JSONObject json) throws JSONException {
        super(json);
    }

    public abstract void execute(MotoConfigGameAPI api);
}
