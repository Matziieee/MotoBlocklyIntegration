package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ConfigBlock;
import com.google.blockly.android.webview.demo.Blocks.FunctionBlock;
import com.google.blockly.android.webview.demo.Blocks.OnGameStartBlock;
import com.google.blockly.android.webview.demo.Blocks.OnTilePressBlock;

import java.util.HashMap;

public class BlocklyGameDefinition {

    private ConfigBlock config;
    private OnGameStartBlock onStart;
    private OnTilePressBlock onTilePress;
    private BlocklyGameState blocklyGameState;

    public BlocklyGameDefinition() {
        this.blocklyGameState = new BlocklyGameState(new HashMap<>(), new HashMap<>());
        this.config = new ConfigBlock();
        this.onStart = new OnGameStartBlock();
        this.onTilePress = new OnTilePressBlock();


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

    public OnGameStartBlock getOnStart() {
        return onStart;
    }

    public void setOnStart(OnGameStartBlock onStart) {
        this.onStart = onStart;
    }

    public OnTilePressBlock getOnTilePress() {
        return onTilePress;
    }

    public void setOnTilePress(OnTilePressBlock onTilePress) {
        this.onTilePress = onTilePress;
    }
}
