package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class SetTileColourBlock implements IExecutableBlock{
    private int colour, tile;
    private boolean isSetFromVar = false;
    private String varId;
    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        this.colour = inputs
                .getJSONObject("colour")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getInt("NUM");
        JSONObject fields = inputs
                .getJSONObject("num_tile")
                .getJSONObject("block")
                .getJSONObject("fields");
        if(fields.has("VAR")){
            this.isSetFromVar = true;
            this.varId = fields.getJSONObject("VAR").getString("id");
        }else{
            this.tile = fields.getInt("NUM");
        }

    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        if(this.isSetFromVar){
            api.setTileColour((Integer) state.getVariables().get(this.varId), this.colour);
        }
        api.setTileColour(tile, colour);
    }
}
