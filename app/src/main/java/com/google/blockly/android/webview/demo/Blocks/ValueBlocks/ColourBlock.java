package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ColourBlock extends AbstractValueBlock<Integer>{

    private int colour;
    private boolean isRandom;

    public ColourBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        if(json.getString("type").equals("randomcolour")){
            this.isRandom = true;
        }
        else{
            this.colour = json.getJSONObject("fields").getInt("colour");
        }
    }

    @Override
    public Integer getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        if(this.isRandom){
            return api.getRandom().nextInt(5)+1; //from 1 - 5
        }
        return this.colour;
    }
}
