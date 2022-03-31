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
    private ArrayList<AbstractExecutableBlock> doBlockStatements;
    private ArrayList<AbstractExecutableBlock> elseBlockStatements;
    private boolean hasElse;

    public IfBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject inputs = json.getJSONObject("inputs");
        BlocklyExecutableBlockParser parser = new BlocklyExecutableBlockParser();
        if(inputs.has("DO0")){
            this.doBlockStatements = parser.
                    parseJson(inputs.getJSONObject("DO0").getJSONObject("block"), new ArrayList<>());
        }
        else{
            this.doBlockStatements = new ArrayList<>();
        }
        //todo handle IF ELSE - Removed from blockly js code so no longer possible for user to add
        this.condition = new LogicOpBlock(inputs.getJSONObject("IF0").getJSONObject("block"));
        if(json.has("extraState")){
            this.hasElse = json.getJSONObject("extraState").getBoolean("hasElse");
            if(inputs.has("ELSE")){
                this.elseBlockStatements = parser.parseJson(inputs.getJSONObject("ELSE").getJSONObject("block"), new ArrayList<>());
            }else{
                this.elseBlockStatements = new ArrayList<>();
            }

        }else{
            this.hasElse = false;
        }
    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {
        if (this.condition.getValue(api, state)){
            for (AbstractExecutableBlock e: this.doBlockStatements) {
                e.execute(state, api);
            }
        }else if(this.hasElse){
            for (AbstractExecutableBlock e: this.elseBlockStatements){
                e.execute(state, api);
            }
        }
    }
}
