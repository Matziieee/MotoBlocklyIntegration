package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class VariableGetBlock implements IComparableValueBlock<Object> {

    private String gets;

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
       this.gets = json.getJSONObject("fields").getJSONObject("VAR").getString("id");
    }

    @Override
    public Object getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        return state.getVariables().get(this.gets);
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
