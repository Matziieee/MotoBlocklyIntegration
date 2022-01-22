package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;

public interface IBlock {

    void parseFromJson(JSONObject json) throws JSONException;
}
