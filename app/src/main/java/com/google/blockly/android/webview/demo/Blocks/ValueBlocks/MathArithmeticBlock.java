package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyValueParser;

import org.json.JSONException;
import org.json.JSONObject;

public class MathArithmeticBlock extends IComparableValueBlock<Integer>{

    IComparableValueBlock<Integer> left,right;
    boolean isLeftDefault, isRightDefault;
    int leftDefault, rightDefault;
    String op;

    public MathArithmeticBlock(JSONObject json) throws JSONException {
        super(json);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        BlocklyValueParser valueParser = new BlocklyValueParser();

        this.op = json.getJSONObject("fields").getString("OP");

        JSONObject inputs = json.getJSONObject("inputs");
        //LEFT
        JSONObject A = inputs.getJSONObject("A");
        if(A.has("block")){
            this.isLeftDefault = false;
            left = valueParser.parseJson(A.getJSONObject("block"));
        }else{
            this.isLeftDefault = true;
            this.leftDefault = A.getJSONObject("shadow").getJSONObject("fields").getInt("NUM");
        }

        //RIGHT
        JSONObject B = inputs.getJSONObject("B");
        if(B.has("block")){
            this.isRightDefault = false;
            right = valueParser.parseJson(B.getJSONObject("block"));
        }else{
            this.isRightDefault = true;
            this.rightDefault = B.getJSONObject("shadow").getJSONObject("fields").getInt("NUM");
        }
    }


    @Override
    public Integer getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        int leftVal, rightVal;
        leftVal = this.isLeftDefault ? this.leftDefault : left.getValue(api, state);
        rightVal = this.isRightDefault ? this.rightDefault : right.getValue(api, state);
        switch ( this.op){
            case "ADD": return leftVal + rightVal;
            case "MULT": return leftVal * rightVal;
            case "SUB": return leftVal - rightVal;
            case "DIV": return leftVal / rightVal;
            default: throw new RuntimeException("unknown operator " + op);
        }
    }

    @Override
    public boolean compare(Integer left, Integer right, char com) {
        switch (com){
            case '>':
                return left > right;
            case '<':
                return left < right;
            case '=':
                return left.equals(right);
        }
        return false;
    }
}
