package com.google.blockly.android.webview.demo;

public interface MotoConfigGameAPI {
    //All "THEN" methods go here..
    void decrementPlayerScore(int player);
    void incrementPlayerScore(int player);
    void setAllTilesColor(int color);
    void setTilesRandomColor(boolean isSame);
    void registerPattern(String id, String name, int length);
    void playPattern(String targetId);
    void setTileRandomColor(int tile);
    void setTileColor(int tile, int color);
    void setAllTilesExceptRandomColor(int tile, boolean isSame);
    void setAllTilesExceptColor(int tile, int color);
    void playSound(String sound);
}
