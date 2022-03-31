package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ColourBlock extends IComparableValueBlock<Integer>{

    private int colour;
    private boolean isRandom;

    public ColourBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        if(json.has("colour")){
            String col = json.getString("colour");
            switch (col){
                case "R": this.colour = 1; break;
                case "G": this.colour = 3; break;
                case "B": this.colour = 2; break;
            }
            return;
        }
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

    @Override
    public boolean compare(Integer left, Integer right, char com) {
        switch (com){
            case '>':
                return left > right;
            case '<':
                return left < right;
            case '=':
                return left.equals(right);
            case '!':
                return !left.equals(right);
        }
        return false;
    }
}
