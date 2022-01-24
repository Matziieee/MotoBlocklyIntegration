package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OnEventBlock extends AbstractExecutableBlock{

    ArrayList<AbstractExecutableBlock> statements = new ArrayList<>();

    public OnEventBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        JSONObject statementBlock = json
                .getJSONObject("inputs")
                .getJSONObject("statements")
                .getJSONObject("block");

        statements = parser.parseJson(statementBlock, new ArrayList<>());
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        for (AbstractExecutableBlock e : statements) {
            e.execute(state, api);
        }
    }
}
