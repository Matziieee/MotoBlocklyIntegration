package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class AddPlayerScoreBlock extends AbstractExecutableBlock {

    NumberBlock player, score;

    public AddPlayerScoreBlock(JSONObject json) throws JSONException {
        super(json);
    }


    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        player = new NumberBlock(inputs.
                getJSONObject("player")
                .getJSONObject("block"));
        score = new NumberBlock(inputs
                .getJSONObject("score")
                .getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.addPlayerScore(
                player.getValue(api, state),
                score.getValue(api, state));
    }
}
