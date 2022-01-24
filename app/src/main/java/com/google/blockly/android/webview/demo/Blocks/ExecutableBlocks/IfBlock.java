package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.LogicOpBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IfBlock extends AbstractExecutableBlock {

    private LogicOpBlock condition;
    private ArrayList<AbstractExecutableBlock> doBlockStatements = new ArrayList<>();

    public IfBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        this.doBlockStatements = parser.
                parseJson(inputs.getJSONObject("DO0").getJSONObject("block"), new ArrayList<>());
        //todo handle ELSE and IF ELSE
        this.condition = new LogicOpBlock(inputs.getJSONObject("IF0").getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        if (this.condition.getValue(api, state)){
            for (AbstractExecutableBlock e: this.doBlockStatements) {
                e.execute(state, api);
            }
        }
    }
}