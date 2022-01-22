package com.google.blockly.android.webview.demo.Blocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;

public interface IExecutableBlock extends IBlock {
    void execute(BlocklyGameState state, BlocklyMotoAPI api);
}
