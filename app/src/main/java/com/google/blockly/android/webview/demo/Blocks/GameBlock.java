package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameBlock extends AbstractBlock{

    private int players;
    private GameTypeBlock gameType;
    private ArrayList<AbstractExecutableBlock> onStart, onEnd;

    public GameBlock(JSONObject json) throws JSONException {
        super(json);
    }

    public int getPlayers() {
        return players;
    }

    public GameTypeBlock getGameType() {
        return gameType;
    }

    public ArrayList<AbstractExecutableBlock> getOnStart() {
        return onStart;
    }

    public ArrayList<AbstractExecutableBlock> getOnEnd() {
        return onEnd;
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        JSONObject inputs = json.getJSONObject("inputs");

        this.players = json.getJSONObject("fields")
                .getInt("num");

        this.gameType = new GameTypeBlock(inputs.getJSONObject("gameType"));

        this.onStart = parser.parseJson(inputs.getJSONObject("start").getJSONObject("block"), new ArrayList<>());
        this.onEnd = parser.parseJson(inputs.getJSONObject("end").getJSONObject("block"), new ArrayList<>());

    }
}
