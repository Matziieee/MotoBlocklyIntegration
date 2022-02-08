package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;

import org.json.JSONException;
import org.json.JSONObject;

public class LogicOpBlock extends AbstractValueBlock<Boolean> {

    private LogicOpBlock logicLeft, logicRight;
    private IComparableValueBlock valLeft, valRight;
    private char operator, comparison;
    private boolean isValueComparison;

    public LogicOpBlock(JSONObject json) throws JSONException {
        super(json);
    }
    //todo handle hardcoded True or False values.

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        String type = json.getString("type");
        JSONObject inputs = json.getJSONObject("inputs");
        JSONObject innerA = inputs.getJSONObject("A").getJSONObject("block");
        JSONObject innerB = inputs.getJSONObject("B").getJSONObject("block");
        if(type.equals("logic_operation")){

            this.logicLeft = new LogicOpBlock(innerA);
            this.logicRight = new LogicOpBlock(innerB);
            String op = json.getJSONObject("fields").getString("OP");
            //todo support more operators
            if(op.equals("AND")){
                this.operator = '&';
            }
            else{
                this.operator = '|';
            }
        } else if(type.equals("logic_compare")) {
            this.isValueComparison = true;
            BlocklyValueParser parser = new BlocklyValueParser();
            valLeft = parser.parseJson(innerA);
            valRight = parser.parseJson(innerB);
        }else{
            throw new RuntimeException("bad");
        }

    }

    @Override
    public Boolean getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        if(isValueComparison){
           return this.valLeft.compare(this.valLeft.getValue(api, state), this.valRight.getValue(api, state), '=');
        }
        return this.applyOperator(logicLeft.getValue(api, state), logicRight.getValue(api, state), this.operator);
    }


    private boolean applyOperator(boolean left, boolean right, char op){
        switch (op){
            case '&':
                return left && right;
            case '|':
                return left || right;
        }
        throw new RuntimeException("No such operator " + op);
    }
}
