package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TileBlock extends AbstractExecutableBlock{

    private NumberBlock tileBlock;
    private String action;
    private int speed;
    private ColourBlock colourBlock;
    private ArrayList<AbstractExecutableBlock> onCdEnd, onPressed;



    public TileBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        if(!json.has("fields")){
            this.tileBlock = new NumberBlock(json.getJSONObject("inputs").getJSONObject("tile").getJSONObject("block"));
            this.parseAsSetColour(json);
            this.action = "colour";
            return;
        }
        this.action = json.getJSONObject("fields").getString("action");
        this.tileBlock = new NumberBlock(json.getJSONObject("inputs").getJSONObject("tiles").getJSONObject("block"));
        switch (action){
            case "colour":
                this.parseAsSetColour(json);
                break;
            case "countdown":
                this.parseAsCountdown(json);
                break;
            case "on_press":
                this.parseAsOnPress(json);
                break;
        }
    }

    private void parseAsOnPress(JSONObject json) throws JSONException {
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        if(json.getJSONObject("inputs").has("OP")){
            this.onPressed = parser.parseJson(json.getJSONObject("inputs").getJSONObject("OP").getJSONObject("block"), new ArrayList<>());
        }
        else{
            this.onPressed = new ArrayList<>();
        }
    }

    private void parseAsCountdown(JSONObject json) throws JSONException {
        String spd = json.getJSONObject("fields").getString("speed");
        switch (spd){
            case "S":
                speed = 20;
                break;
            case "M":
                speed = 10;
                break;
            case "F":
            default:
                speed = 5;
        }
        this.colourBlock = new ColourBlock(json.getJSONObject("inputs").getJSONObject("Colour").getJSONObject("block"));
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        if(json.getJSONObject("inputs").has("onend")){
            this.onCdEnd = parser.parseJson(json.getJSONObject("inputs").getJSONObject("onend").getJSONObject("block"), new ArrayList<>());
        }
        else{
            this.onCdEnd = new ArrayList<>();
        }
    }

    private void parseAsSetColour(JSONObject json) throws JSONException {
        this.colourBlock = new ColourBlock(json.getJSONObject("inputs").getJSONObject("Colour").getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        int tile = this.tileBlock.getValue(api, state);
        switch (action){
            case "colour":
                api.setTileColour(tile, this.colourBlock.getValue(api, state));
                break;
            case "countdown":
                api.setTileColourCountdown(tile, this.colourBlock.getValue(api, state), this.speed, this.onCdEnd);
                break;
            case "on_press":
                api.setTilePressBehavior(tile, this.onPressed);
                break;
        }

    }
}
