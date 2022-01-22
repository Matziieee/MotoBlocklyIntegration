package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IfBlock implements IExecutableBlock{

    private LogicOpBlock condition = new LogicOpBlock();
    private ArrayList<IExecutableBlock> doBlockStatements = new ArrayList<>();

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        this.doBlockStatements = parser.
                parseJson(inputs.getJSONObject("DO0").getJSONObject("block"), new ArrayList<>());
        //todo handle ELSE and IF ELSE
        this.condition.parseFromJson(inputs.getJSONObject("IF0").getJSONObject("block"));
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        if (this.condition.getValue(api, state)){
            for (IExecutableBlock e: this.doBlockStatements) {
                e.execute(state, api);
            }
        }
    }
}
