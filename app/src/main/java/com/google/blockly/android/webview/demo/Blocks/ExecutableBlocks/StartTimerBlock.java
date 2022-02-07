package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.AbstractValueBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StartTimerBlock extends AbstractExecutableBlock{

    private AbstractValueBlock duration, name;
    private ArrayList<AbstractExecutableBlock> statements;

    public StartTimerBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyValueParser valParser = new BlocklyValueParser();
        BlocklyExecutableBlockParser exeParser = new BlocklyExecutableBlockParser();
        JSONObject inputs = json.getJSONObject("inputs");
        this.name = valParser.parseJson(inputs.getJSONObject("name").getJSONObject("block"));
        this.duration = valParser.parseJson(inputs.getJSONObject("duration").getJSONObject("block"));
        this.statements = exeParser.parseJson(inputs.getJSONObject("onEnd").getJSONObject("block"), new ArrayList<>());
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        api.startTimer(
                (String)this.name.getValue(api, state),
                (int)this.duration.getValue(api, state),
                this.statements
        );
    }
}
