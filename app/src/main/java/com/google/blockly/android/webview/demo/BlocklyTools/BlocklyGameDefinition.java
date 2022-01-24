package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ConfigBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.OnEventBlock;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.FunctionBlock;

import java.util.HashMap;

public class BlocklyGameDefinition {

    private ConfigBlock config;
    private OnEventBlock onStart;
    private OnEventBlock onTilePress;

    public void setBlocklyGameState(BlocklyGameState blocklyGameState) {
        this.blocklyGameState = blocklyGameState;
    }

    private OnEventBlock onGameEnd;
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

    public ConfigBlock getConfig() {
        return config;
    }

    public void setConfig(ConfigBlock config) {
        this.config = config;
    }

    public OnEventBlock getOnStart() {
        return onStart;
    }

    public void setOnStart(OnEventBlock onStart) {
        this.onStart = onStart;
    }

    public OnEventBlock getOnTilePress() {
        return onTilePress;
    }

    public void setOnTilePress(OnEventBlock onTilePress) {
        this.onTilePress = onTilePress;
    }

    public OnEventBlock getOnGameEnd() {
        return onGameEnd;
    }

    public void setOnGameEnd(OnEventBlock onGameEnd) {
        this.onGameEnd = onGameEnd;
    }
}
