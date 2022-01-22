package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class RandomNumberBlock implements IComparableValueBlock<Integer> {
    private int lower, upper;

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        //lower bound
        JSONObject inputs = json.getJSONObject("inputs");
        this.lower = inputs
                .getJSONObject("FROM")
                .getJSONObject("shadow")
                .getJSONObject("fields")
                .getInt("NUM");
        //upper bound
        this.upper = inputs
                .getJSONObject("TO")
                .getJSONObject("shadow")
                .getJSONObject("fields")
                .getInt("NUM");

    }

    @Override
    public Integer getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        return api.getRandom().nextInt(upper)+lower;
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
