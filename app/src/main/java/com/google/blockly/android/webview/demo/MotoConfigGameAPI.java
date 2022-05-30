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
    void defineRandomPair(String id, String name, boolean withSound);
    void defineRandomSequence(String id, String name, int length);
    void waitForSequence(String patternId, String correctName, String incorrectName);
    void turnPairOn(String pairName, int color);
    void activateSubrule(String name);
    void deactivateSubrule(String name);
    void setRandomTileColor(int color);
    void setRandomTileColorAndRestColor(int color, int color2);
    void turnPairOff(String pairId, boolean shouldSetIdle);
    void clearPairs();
    void speak(String text);
    void sayPlayerScore(int player);
}
