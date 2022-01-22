package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class NumberBlock implements IComparableValueBlock<Integer> {

    private int value;

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        value = json.getJSONObject("fields").getInt("NUM");
    }

    @Override
    public Integer getValue(BlocklyMotoAPI api, BlocklyGameState state) {
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
