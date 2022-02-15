package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.TileSetBlock;

import java.util.ArrayList;
import java.util.Random;

public interface BlocklyMotoAPI {

    void setTileColourCountdown(int tile, int colour, int speed, ArrayList<AbstractExecutableBlock> onEnd);
    void setTileColour(int tile, int colour);
    void addPlayerScore(int player, int score);
    void setAllTilesToIdle(int colour);
    void startTimer(String name, int duration, ArrayList<AbstractExecutableBlock> onEnd);
    void stopTimer(String name);
    void setAllTilesToColour(int colour);
    void speak(String toSay);
    void setGameOver();
    int getScoreOfPlayer(int player);
    Random getRandom();

    void setTilePressBehavior(int tile, ArrayList<AbstractExecutableBlock> onPressed);
}
