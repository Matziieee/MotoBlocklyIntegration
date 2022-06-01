package com.google.blockly.android.webview.demo.Blocks.Image;

import com.google.blockly.android.webview.IMotoImageGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class TilesOn extends ExecutableNodeBlock{

    //rand -1, red 1, blue 2, green 3, indigo 4
    int color;

    public TilesOn(JSONObject jsonObject) throws JSONException {
        super(jsonObject);
    }

    @Override
    public void execute(IMotoImageGameAPI api) {
        api.setAllColor(color);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        color = getColor(json.getString("type"));
    }

    private int getColor (String type){
        switch (type){
            case "rand":
                return -1;
            case "red":
                return 1;
            case "blue":
                return 2;
            case "green":
                return 3;
            case "indigo":
            default:
                return 4;
        }
    }
}
