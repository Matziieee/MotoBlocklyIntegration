package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FunctionBlock implements IExecutableBlock {

    ArrayList<IExecutableBlock> statements = new ArrayList<>();

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        JSONObject statementBlock = json
                .getJSONObject("inputs")
                .getJSONObject("STACK")
                .getJSONObject("block");

        statements = parser.parseJson(statementBlock, new ArrayList<>());
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        for (IExecutableBlock e : statements) {
            e.execute(state, api);
        }
    }
}
