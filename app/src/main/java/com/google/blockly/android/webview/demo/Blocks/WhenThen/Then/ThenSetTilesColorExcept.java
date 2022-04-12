package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import android.support.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenSetTilesColorExcept extends ThenBlock {
    int tile, color;
    public ThenSetTilesColorExcept(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        if(tile == -1){
            return;
        }
        if(color == -1){
            api.setAllTilesExceptRandomColor(tile, false);
        }else{
            api.setAllTilesExceptColor(tile, color);
        }
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.color = json.getJSONObject("fields").getInt("col");
        this.tile = json.getJSONObject("fields").getInt("num");
    }
}
