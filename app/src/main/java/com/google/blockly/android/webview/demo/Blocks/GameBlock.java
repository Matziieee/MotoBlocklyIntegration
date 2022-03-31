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
        if(!json.has("inputs")){
            this.players = 1;
            this.gameType = GameTypeBlock.fromArguments(1000, "time");
            this.onStart = new ArrayList<>();
            this.onEnd = new ArrayList<>();
            return;

        }
        JSONObject inputs = json.getJSONObject("inputs");

        if(inputs.has("fields")){
            this.players = json.getJSONObject("fields")
                    .getInt("num");
        }else{
            this.players = 1;
        }

        if(inputs.has("gameType")){
            this.gameType = new GameTypeBlock(inputs.getJSONObject("gameType"));
        }else{
            this.gameType = GameTypeBlock.fromArguments(1000, "time");
        }

        if(inputs.has("start")){
            this.onStart = parser.parseJson(inputs.getJSONObject("start").getJSONObject("block"), new ArrayList<>());
        }else{
            this.onStart = new ArrayList<>();
        }
        if(inputs.has("end")){
            this.onEnd = parser.parseJson(inputs.getJSONObject("end").getJSONObject("block"), new ArrayList<>());
        }else{
            this.onEnd = new ArrayList<>();
        }
    }
}
