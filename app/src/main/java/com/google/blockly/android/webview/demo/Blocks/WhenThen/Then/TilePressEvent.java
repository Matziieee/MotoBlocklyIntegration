package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

public class TilePressEvent {
    private int tile, color;

    public TilePressEvent(int tile, int color) {
        this.tile = tile;
        this.color = color;
    }

    public int getTile() {
        return tile;
    }

    public int getColor() {
        return color;
    }
}
