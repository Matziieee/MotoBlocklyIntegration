package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.TileSetBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SetExpectedNextPressBlock extends AbstractExecutableBlock{
    private ArrayList<AbstractExecutableBlock> onCorrect, onIncorrect;
    private TileSetBlock tiles;

    public SetExpectedNextPressBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        BlocklyExecutableBlockParser exeParser = new BlocklyExecutableBlockParser();
        BlocklyValueParser valParser = new BlocklyValueParser();
        //onIncorrect
        if(inputs.has("onIncorrect")){
            this.onIncorrect = exeParser.parseJson(inputs.getJSONObject("onIncorrect").getJSONObject("block"), new ArrayList<>());
        }
        //onCorrect
        if(inputs.has("onCorrect")){
            this.onCorrect = exeParser.parseJson(inputs.getJSONObject("onCorrect").getJSONObject("block"), new ArrayList<>());
        }
        //TileSet
        this.tiles = new TileSetBlock(inputs.getJSONObject("tiles").getJSONObject("block"));

    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.setExpectedNextPress(this.tiles, this.onCorrect, this.onIncorrect);
    }
}
