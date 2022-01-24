package com.google.blockly.android.webview.demo.BlocklyTools;

import java.util.Random;

public interface BlocklyMotoAPI {

    void setTileColour(int tile, int colour);
    void addPlayerScore(int player, int score);
    void setAllTilesIdle(int colour);
    MotoEvent getCurrentEvent();
    Random getRandom();
}
