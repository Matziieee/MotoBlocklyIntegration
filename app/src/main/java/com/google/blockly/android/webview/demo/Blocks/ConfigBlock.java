package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;


public class ConfigBlock implements IBlock {

    private int numPlayers = -1;
    private int numTiles = -1;
    private GameTypeBlock gameType = new GameTypeBlock();

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getNumTiles() {
        return numTiles;
    }

    public GameTypeBlock getGameType() {
        return gameType;
    }

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        this.numPlayers = inputs
                .getJSONObject("numPlayers")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getInt("NUM");
        this.numTiles = inputs
                .getJSONObject("numTiles")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getInt("NUM");
        this.gameType.parseFromJson(inputs.getJSONObject("gameType"));
    }
}
