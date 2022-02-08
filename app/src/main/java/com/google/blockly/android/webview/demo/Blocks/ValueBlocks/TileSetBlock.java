package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TileSetBlock extends AbstractValueBlock<ArrayList<Integer>> {
    private NumberBlock numberBlock;
    private TileSetBlock next;

    public TileSetBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    public ArrayList<Integer> getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        ArrayList<Integer> tiles = new ArrayList<>();
        tiles.add(numberBlock.getValue(api, state));
        if(next != null){
            tiles.addAll(next.getValue(api, state));
        }
        return tiles;
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        this.numberBlock = new NumberBlock(inputs.getJSONObject("tile").getJSONObject("block"));
        if(inputs.has("tileset")){
            this.next = new TileSetBlock(json.getJSONObject("inputs").getJSONObject("tileset").getJSONObject("block"));
        }
    }

}
