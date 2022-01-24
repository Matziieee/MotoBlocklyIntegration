package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.EventType;
import com.google.blockly.android.webview.demo.BlocklyTools.MotoEvent;

import org.json.JSONException;
import org.json.JSONObject;

public class IncomingEventBlock extends IComparableValueBlock<Object> {

    private String val;

    public IncomingEventBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.val = json.getJSONObject("fields").getString("field");
    }

    @Override
    public Object getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        MotoEvent event = api.getCurrentEvent();
        if(val.equals("type")){
            return event.getType();
        }
        return event.getTile();
    }

    @Override
    public boolean compare(Object left, Object right, char com) {
        if(!left.getClass().equals(right.getClass())){
            return false;
        }
        //todo Only equals supported!
        switch (com){
            case '=':
                return left == right;
        }

        return false;
    }
}
