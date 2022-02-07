package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class NumberBlock extends IComparableValueBlock<Integer> {

    private int value;
    private String varName;
    private boolean isSetFromVariable;
    private boolean isSetFromRandom;
    private int lower, upper;
    private boolean isSetFromIncomingEvent;

    public NumberBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        if(json.has("fields")){
            JSONObject fields = json.getJSONObject("fields");
            if(fields.has("VAR")){
                this.isSetFromVariable = true;
                this.varName = fields.getJSONObject("VAR").getString("id");
            }
            else if(fields.has("from")){
                this.isSetFromRandom = true;
                this.lower = fields.getInt("from");
                this.upper = fields.getInt("to");
            }
            else {
                if(fields.has("field") && fields.getString("field").equals("tile")){
                    this.isSetFromIncomingEvent = true;
                    return;
                }
                value = json.getJSONObject("fields").getInt("number");
            }
        }else{
            this.isSetFromRandom = true;
            JSONObject fields = json
                    .getJSONObject("inputs")
                    .getJSONObject("VALUE")
                    .getJSONObject("block")
                    .getJSONObject("fields");
            this.lower = fields.getInt("from");
            this.upper = fields.getInt("to");
        }
    }

    @Override
    public Integer getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        if(this.isSetFromVariable){
            return (Integer) state.getVariables().get(varName);
        }
        else if(this.isSetFromRandom){
            return api.getRandom().nextInt(upper)+lower;
        }
        else if(this.isSetFromIncomingEvent){
            return state.getCurrentEvent().getTile();
        }
        return value;
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
        }
        return false;
    }
}
