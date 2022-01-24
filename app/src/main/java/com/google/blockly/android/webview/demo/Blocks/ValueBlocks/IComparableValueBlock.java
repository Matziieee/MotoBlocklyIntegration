package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class IComparableValueBlock<T> extends AbstractValueBlock<T> implements IComparable<T> {
    public IComparableValueBlock(JSONObject json) throws JSONException {
        super(json);
    }
}
