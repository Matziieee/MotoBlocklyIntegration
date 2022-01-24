package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.FunctionBlock;

import java.util.HashMap;

public class BlocklyGameState {
    private HashMap<String, Object> variables;
    private HashMap<String, FunctionBlock> functions;

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
}
