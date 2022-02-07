package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyExecutableBlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.AbstractValueBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ForILoopBlock extends AbstractExecutableBlock{

    String variableId;
    AbstractValueBlock from, to, by;
    boolean isByDefault, isFromDefault, isToDefault;
    int fromDefault, toDefault, byDefault;
    ArrayList<AbstractExecutableBlock> statements;

    public ForILoopBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyValueParser valueParser = new BlocklyValueParser();
        BlocklyExecutableBlockParser exeParser = new BlocklyExecutableBlockParser();
        JSONObject inputs = json.getJSONObject("inputs");
        //Var ID
        this.variableId = json.getJSONObject("fields").getJSONObject("VAR").getString("id");

        //FROM
        JSONObject from = inputs.getJSONObject("FROM");
        if(from.has("block")){
            this.isFromDefault = false;
            this.from = valueParser.parseJson(from.getJSONObject("block"));
        }else{
            this.isFromDefault = true;
            this.fromDefault = from.getJSONObject("shadow").getJSONObject("fields").getInt("NUM");
        }
        //TO
        JSONObject to = inputs.getJSONObject("TO");
        if(to.has("block")){
            this.isToDefault = false;
            this.to = valueParser.parseJson(to.getJSONObject("block"));
        }else{
            this.isToDefault = true;
            this.toDefault = to.getJSONObject("shadow").getJSONObject("fields").getInt("NUM");
        }
        //BY
        JSONObject by = inputs.getJSONObject("BY");
        if(by.has("block")){
            this.isByDefault = false;
            this.by = valueParser.parseJson(by.getJSONObject("block"));
        }else{
            this.isByDefault = true;
            this.byDefault = by.getJSONObject("shadow").getJSONObject("fields").getInt("NUM");
        }
        //DO
        this.statements = exeParser.parseJson(inputs.getJSONObject("DO").getJSONObject("block"), new ArrayList<>());

    }

    @Override
    public void execute(BlocklyGameState state, BlocklyMotoAPI api) {

        int byVal, fromVal, toVal, i;
        byVal = this.isByDefault ? this.byDefault : (int) this.by.getValue(api, state);
        fromVal = this.isFromDefault ? this.fromDefault : (int) this.from.getValue(api, state);
        toVal = this.isToDefault ? this.toDefault : (int) this.to.getValue(api, state);
        i = fromVal;
        state.getVariables().put(this.variableId, i);
        while (i <= toVal){
            for (AbstractExecutableBlock e: this.statements) {
                e.execute(state, api);
            }
            i += byVal;
            state.getVariables().put(this.variableId, i);
        }
    }
}
