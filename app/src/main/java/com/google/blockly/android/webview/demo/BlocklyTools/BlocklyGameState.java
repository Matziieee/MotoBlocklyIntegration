package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.FunctionBlock;

import java.util.HashMap;

public class BlocklyGameState {
    private HashMap<String, Object> variables;
    private HashMap<String, FunctionBlock> functions;
    private MotoEvent currentEvent;

    public void setCurrentEvent(MotoEvent currentEvent) {
        this.currentEvent = currentEvent;
    }

    public MotoEvent getCurrentEvent() {
        return currentEvent;
    }

    public BlocklyGameState(HashMap<String, Object> variables, HashMap<String, FunctionBlock> functions) {
        this.variables = variables;
        this.functions = functions;
    }

    public HashMap<String, Object> getVariables() {
        return variables;
    }

    public HashMap<String, FunctionBlock> getFunctions() {
        return functions;
    }

    public BlocklyGameState copy() {
        //Not sure if this actually does anything
        HashMap<String, Object> c_variables = new HashMap<>();
        variables.forEach((k,v) -> {
            //Values are either strings or integers
            if(v instanceof String){
                c_variables.put(k, v.toString());
            }else if(v instanceof Integer){
                int val = (int) v;
                c_variables.put(k, val);
            }else {
                c_variables.put(k, null);
            }
        });
        BlocklyGameState copyState = new BlocklyGameState(c_variables, this.functions);
        MotoEvent ev = null;
        if(this.currentEvent != null){
            ev = this.currentEvent.copy();
        }
        copyState.setCurrentEvent(ev);
        return copyState;
    }
}
