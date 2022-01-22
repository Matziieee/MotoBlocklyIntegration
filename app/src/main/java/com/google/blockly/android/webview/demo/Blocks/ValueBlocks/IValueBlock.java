package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.IBlock;

public interface IValueBlock<T> extends IBlock {
    T getValue(BlocklyMotoAPI api, BlocklyGameState state);
}
