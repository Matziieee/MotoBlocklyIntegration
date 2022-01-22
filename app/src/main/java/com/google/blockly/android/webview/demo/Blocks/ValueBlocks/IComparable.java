package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

public interface IComparable<T> {
    boolean compare(T left, T right, char com);
}
