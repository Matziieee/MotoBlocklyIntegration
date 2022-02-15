package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerBlock extends AbstractExecutableBlock{
    private String action;
    private int score;
    private NumberBlock player;

    public PlayerBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject fields = json.getJSONObject("fields");
        this.action = fields.getString("action");
        this.score = fields.getInt("num");

        this.player = new NumberBlock(json.getJSONObject("inputs").getJSONObject("player").getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        int numPlayer = this.player.getValue(api, state);
        switch (this.action){
            case "add":
                api.addPlayerScore(numPlayer, this.score);
                break;
            case "sub":
                api.addPlayerScore(numPlayer, -this.score);
        }
    }
}
