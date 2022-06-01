package com.google.blockly.android.webview.demo.Blocks.Image;

import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class TilePress extends AbstractBlock {

    int color; // -1 all, 1 red, 2 blue, 3 green, 4 indigo

    private ExecutableNodeBlock then;

    public TilePress(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        String type = json.getString("type");
        switch (type){
            case "stepRed":
                color = 1;
                break;
            case "stepBlue":
                color = 2;
                break;
            case "stepGreen":
                color = 3;
                break;
            case "stepIndigo":
                color = 4;
                break;
            case "step":
                color = -1;
            default: break;
        }
    }

    public ExecutableNodeBlock getThen() {
        return then;
    }

    public void setThen(ExecutableNodeBlock then) {
        this.then = then;
    }

    public int getColor() {
        return color;
    }
}
