package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;

import java.util.ArrayList;
import java.util.Random;

public interface BlocklyMotoAPI {

    void setTileColour(int tile, int colour);
    void addPlayerScore(int player, int score);
    void setAllTilesToIdle(int colour);
    void startTimer(String name, int duration, ArrayList<AbstractExecutableBlock> onEnd);
    void setAllTilesToColour(int colour);
    void speak(String toSay);
    void setGameOver();
    int getScoreOfPlayer(int player);
    MotoEvent getCurrentEvent();
    Random getRandom();
}
