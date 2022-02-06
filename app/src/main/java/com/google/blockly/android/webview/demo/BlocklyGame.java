package com.google.blockly.android.webview.demo;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.blockly.android.webview.demo.BlocklyTools.BlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameDefinition;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.EventType;
import com.google.blockly.android.webview.demo.BlocklyTools.MotoEvent;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class BlocklyGame extends Game implements BlocklyMotoAPI {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    BlocklyGameDefinition gameDefinition;
    MotoEvent currentEvent;

    private final Handler timerHandler;

    BlocklyGame(JSONObject gameDef) throws JSONException {
        setName(gameDef.getString("name"));
        BlockParser parser = BlockParser.getInstance();
        this.gameDefinition = parser.parseJson(gameDef.getJSONObject("game"));
        String type = gameDefinition.getConfig().getGameType().getType();

        GameType gt = new GameType(1,
                 type.equals("time") ? GameType.GAME_TYPE_TIME : GameType.GAME_TYPE_SCORE,
                this.gameDefinition.getConfig().getGameType().getThreshold(),
                "Custom Game",
                this.gameDefinition.getConfig().getNumPlayers());
        addGameType(gt);

        this.timerHandler = new Handler();
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        gameDefinition.getOnStart().execute(gameDefinition.getBlocklyGameState(), this);
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int tile= AntData.getId(message);
        //todo when more event types are supported remove this check
        //for now only Event press is supported, so we only register those..
        if (event == AntData.EVENT_PRESS)
        {
            this.currentEvent = new MotoEvent(tile, EventType.PRESS);
            this.gameDefinition.getOnTilePress().execute(this.gameDefinition.getBlocklyGameState(), this);
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        gameDefinition.getOnGameEnd().execute(gameDefinition.getBlocklyGameState(), this);
        //todo should this be removed?
        connection.setAllTilesBlink(4,LED_COLOR_RED);
    }

    @Override
    public void setTileColour(int tile, int colour) {
        connection.setTileColor(colour, tile);
    }

    @Override
    public void addPlayerScore(int player, int score) {
        this.incrementPlayerScore(score,player);
    }

    @Override
    public void setAllTilesToIdle(int colour) {
        connection.setAllTilesIdle(colour);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void startTimer(String name, int duration, ArrayList<AbstractExecutableBlock> onEnd) {
        BlocklyMotoAPI api = this;
        this.timerHandler.postDelayed(() -> {
            for (AbstractExecutableBlock e : onEnd) {
                e.execute(gameDefinition.getBlocklyGameState(), api);
            }
        }, name, duration * 1000L);
    }

    @Override
    public void stopTimer(String name) {
        this.timerHandler.removeCallbacksAndMessages(name);
    }

    @Override
    public void setAllTilesToColour(int colour) {
        this.connection.setAllTilesColor(colour);
    }

    @Override
    public void speak(String toSay) {
        this.sound.speak(toSay);
    }

    @Override
    public void setGameOver() {
        this.stopGame();
    }

    @Override
    public int getScoreOfPlayer(int player) {
        return this.getPlayerScore()[player];
    }

    @Override
    public MotoEvent getCurrentEvent() {
        return this.currentEvent;
    }

    @Override
    public Random getRandom() {
        //this can be replaced with a global random in case a random seed is used
        return new Random();
    }
}
