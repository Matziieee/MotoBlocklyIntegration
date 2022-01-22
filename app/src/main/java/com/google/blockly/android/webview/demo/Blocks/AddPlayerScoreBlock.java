package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPlayerScoreBlock implements IExecutableBlock{

    int player, score;


    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        player = inputs.getJSONObject("player").getJSONObject("block").getJSONObject("fields").getInt("NUM");
        score = inputs.getJSONObject("score").getJSONObject("block").getJSONObject("fields").getInt("NUM");
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.addPlayerScore(player, score);
    }
}
