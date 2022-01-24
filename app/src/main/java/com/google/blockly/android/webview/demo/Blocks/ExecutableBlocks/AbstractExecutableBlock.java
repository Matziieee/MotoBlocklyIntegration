package com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractExecutableBlock extends AbstractBlock {

    public AbstractExecutableBlock(JSONObject json) throws JSONException {
        super(json);
    }

    public abstract void execute(BlocklyGameState state, BlocklyMotoAPI api);
}
