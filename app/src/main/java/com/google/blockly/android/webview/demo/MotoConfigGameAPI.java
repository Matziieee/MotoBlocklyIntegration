package com.google.blockly.android.webview.demo;

public interface MotoConfigGameAPI {
    void decrementPlayerScore(int player);
    void incrementPlayerScore(int player);
    void setAllTilesColor(int color);
    void setTilesRandomColor(boolean isSame);
    //All "THEN" methods go here..
}
