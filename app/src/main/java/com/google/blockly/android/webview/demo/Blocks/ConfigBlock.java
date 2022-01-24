package com.google.blockly.android.webview.demo.Blocks;

import org.json.JSONException;
import org.json.JSONObject;


public class ConfigBlock extends AbstractBlock {

    private String gameName = "";
    private int numPlayers = -1;
    private int numTiles = -1;
    private GameTypeBlock gameType;

    public ConfigBlock(JSONObject json) throws JSONException {
        super(json);
    }

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
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        this.gameName = inputs.getJSONObject("name")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getString("TEXT");
        this.numPlayers = inputs
                .getJSONObject("numplayers")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getInt("number");
        this.numTiles = inputs
                .getJSONObject("numtiles")
                .getJSONObject("block")
                .getJSONObject("fields")
                .getInt("number");
        this.gameType = new GameTypeBlock(inputs.getJSONObject("type"));
    }
}
