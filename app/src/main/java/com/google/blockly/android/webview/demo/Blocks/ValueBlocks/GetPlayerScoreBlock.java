package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class GetPlayerScoreBlock extends IComparableValueBlock<Integer>{

    int player;

    public GetPlayerScoreBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.player = json.getJSONObject("fields").getInt("player");

    }

    @Override
    public Integer getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        return api.getScoreOfPlayer(this.player);
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
