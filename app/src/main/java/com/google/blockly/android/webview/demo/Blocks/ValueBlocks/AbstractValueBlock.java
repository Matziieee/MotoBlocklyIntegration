package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractValueBlock<T> extends AbstractBlock {
    public AbstractValueBlock(JSONObject json) throws JSONException {
        super(json);
    }

    public abstract T getValue(BlocklyMotoAPI api, BlocklyGameState state);
}
