package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenSetTileColor extends ThenBlock {
    int tile, color;

    public ThenSetTileColor(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        if(tile == -1){
            return;
        }
        if(color == -1){
            api.setTileRandomColor(tile);
        }else{
            api.setTileColor(tile, color);
        }
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.color = json.getJSONObject("fields").getInt("col");
        this.tile = json.getJSONObject("fields").getInt("num");
    }
}
