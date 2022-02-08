package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.AbstractValueBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.ColourBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SetTileColourCountdownBlock extends AbstractExecutableBlock{
    private String speed;
    private NumberBlock numberBlock;
    private ColourBlock colourBlock;
    private ArrayList<AbstractExecutableBlock> onEnd;

    public SetTileColourCountdownBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        this.speed = json.getJSONObject("fields").getString("speed");
        JSONObject inputs = json.getJSONObject("inputs");
        this.numberBlock = new NumberBlock(inputs.getJSONObject("tile").getJSONObject("block"));
        this.colourBlock = new ColourBlock(inputs.getJSONObject("colour").getJSONObject("block"));
        if(inputs.has("onCountdownEnd")){
            this.onEnd = parser.parseJson(inputs.getJSONObject("onCountdownEnd").getJSONObject("block"), new ArrayList<>());
        }else{
            this.onEnd = new ArrayList<>();
        }
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        //Speed: S = slow, M = med, F = Fast
        int speed;
        switch (this.speed){
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
        api.setTileColourCountdown(
                this.numberBlock.getValue(api, state),
                this.colourBlock.getValue(api, state),
                speed,
                this.onEnd);
    }
}
