package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ConfigBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.OnEventBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.FunctionBlock;
import com.google.blockly.android.webview.demo.Blocks.GameBlock;

import java.util.ArrayList;
import java.util.HashMap;

public class BlocklyGameDefinition {
    private GameBlock gameBlock;


    public GameBlock getGameBlock() {
        return gameBlock;
    }

    public void setGameBlock(GameBlock gameBlock) {
        this.gameBlock = gameBlock;
    }

    public void setBlocklyGameState(BlocklyGameState blocklyGameState) {
        this.blocklyGameState = blocklyGameState;
    }

    private BlocklyGameState blocklyGameState;

    public BlocklyGameDefinition() {
        this.blocklyGameState = new BlocklyGameState(new HashMap<>(), new HashMap<>());
    }

    public BlocklyGameState getBlocklyGameState() {
        return blocklyGameState;
    }

    public HashMap<String, FunctionBlock> getFunctions() {
        return blocklyGameState.getFunctions();
    }

    public HashMap<String, Object> getVariables() {
        return blocklyGameState.getVariables();
    }

    public ArrayList<AbstractExecutableBlock> getOnStart() {
        return this.gameBlock.getOnStart();
    }

    public ArrayList<AbstractExecutableBlock> getOnEnd(){
        return this.gameBlock.getOnEnd();
    }



}
