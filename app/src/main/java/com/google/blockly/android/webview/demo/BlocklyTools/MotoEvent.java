package com.google.blockly.android.webview.demo.BlocklyTools;

public class MotoEvent {
    private int tile = -1;
    private EventType type = EventType.NONE;

    public MotoEvent(int tile, EventType type) {
        this.tile = tile;
        this.type = type;
    }

    public int getTile() {
        return tile;
    }

    public EventType getType() {
        return type;
    }
}
