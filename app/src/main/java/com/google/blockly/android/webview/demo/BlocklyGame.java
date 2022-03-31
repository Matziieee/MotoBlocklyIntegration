package com.google.blockly.android.webview.demo;

import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;

import com.google.blockly.android.webview.demo.BlocklyTools.BlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameDefinition;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
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

    public MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    BlocklyGameDefinition gameDefinition;

    private final Handler timerHandler;
    private HashMap<Object, String> timers = new HashMap<>();
    private HashMap<Integer, ArrayList<AbstractExecutableBlock>> onCountdownEndExecutables = new HashMap<>();
    private HashMap<Integer, Pair<BlocklyGameState, ArrayList<AbstractExecutableBlock>>> onTilePressExecutables = new HashMap<>();
    private boolean isScoreGame;
    private int scoreThreshold;

    public BlocklyGame(JSONObject gameDef, Handler handler) throws JSONException {
        BlockParser parser = BlockParser.getInstance();
        this.gameDefinition = parser.parseJson(gameDef);
        String type = gameDefinition.getGameBlock().getGameType().getType();

        GameType gt = new GameType(1,
                type.equals("time") ? GameType.GAME_TYPE_TIME : GameType.GAME_TYPE_SCORE,
                this.gameDefinition.getGameBlock().getGameType().getThreshold(),
                "Custom Game",
                this.gameDefinition.getGameBlock().getPlayers());
        addGameType(gt);
        this.timerHandler = handler;
        if(gt.getType() == 2){
            this.isScoreGame = true;
            this.scoreThreshold = this.gameDefinition.getGameBlock().getGameType().getThreshold();
        }
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        connection.setAllTilesColor(LED_COLOR_OFF);
        gameDefinition.getOnStart().forEach(e -> e.execute(gameDefinition.getBlocklyGameState(), this));
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
           if(this.onTilePressExecutables.containsKey(tile)){
               Pair<BlocklyGameState, ArrayList<AbstractExecutableBlock>> values = this.onTilePressExecutables.get(tile);
                values.second.forEach(e -> e.execute(values.first, this));
           }
        }else if(event == AntData.CMD_COUNTDOWN_TIMEUP){
            if(this.onCountdownEndExecutables.containsKey(tile)){
                ArrayList<AbstractExecutableBlock> toRun = this.onCountdownEndExecutables.get(tile);
                this.onCountdownEndExecutables.remove(tile);
                toRun.forEach(e -> e.execute(gameDefinition.getBlocklyGameState(), this));
            }
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        gameDefinition.getOnEnd().forEach(e -> e.execute(gameDefinition.getBlocklyGameState(), this));
        //todo should this be removed?
        connection.setAllTilesIdle(LED_COLOR_OFF);
        connection.setAllTilesBlink(4,LED_COLOR_RED);
    }

    @Override
    public void setTileColourCountdown(int tile, int colour, int speed, ArrayList<AbstractExecutableBlock> onEnd) {
        Log.i("COUNTDOWN", "SET COUNTDOWN OF TILE " + tile);
        this.connection.setTileColorCountdown(colour, tile, speed);
        this.onCountdownEndExecutables.put(tile, onEnd);
    }

    @Override
    public void setTileColour(int tile, int colour) {
        connection.setTileColor(colour, tile);
    }

    @Override
    public void addPlayerScore(int player, int score) {
        this.incrementPlayerScore(score,player);
        if(this.isScoreGame && this.getScoreOfPlayer(player) >= this.scoreThreshold){
            this.setGameOver();
        }
    }

    @Override
    public void setAllTilesToIdle(int colour) {
        connection.setAllTilesIdle(colour);
    }

    @Override
    public void startTimer(String name, int duration, ArrayList<AbstractExecutableBlock> onEnd) {
        BlocklyMotoAPI api = this;
        Object token = new Object();
        this.timers.put(token, name);
        long time = SystemClock.uptimeMillis() + (duration*1000L);
        BlocklyGameState copiedState = this.gameDefinition.getBlocklyGameState().copy();
        this.timerHandler.postAtTime(new Runnable() {
            @Override
            public void run() {
                for (AbstractExecutableBlock e : onEnd) {
                    e.execute(copiedState, api);
                }
            }
        }, token , time);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void stopTimer(String name) {
        this.timers.forEach((k,v) -> {
            if(v.equals(name)){
                this.timerHandler.removeCallbacksAndMessages(k);
            }
        });
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
    public Random getRandom() {
        //this can be replaced with a global random in case a random seed is used
        return new Random();
    }

    @Override
    public void setTilePressBehavior(int tile, ArrayList<AbstractExecutableBlock> onPressed) {
        this.onTilePressExecutables.put(tile,
                new Pair<>(this.gameDefinition.getBlocklyGameState().copy(), onPressed));
    }

    @Override
    public void beginWait(int seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
