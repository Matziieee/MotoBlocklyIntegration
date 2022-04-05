package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenSetTilesColor extends ThenBlock {
    int color;

    public ThenSetTilesColor(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        if(color == -1){
            api.setTilesRandomColor(false);
        }else{
            api.setAllTilesColor(color);
        }
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.color = json.getJSONObject("fields").getInt("col");
    }
}
