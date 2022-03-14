package com.google.blockly.android.webview.demo;

public class GameListItem {
    private String name;
    private int index;

    public GameListItem(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
