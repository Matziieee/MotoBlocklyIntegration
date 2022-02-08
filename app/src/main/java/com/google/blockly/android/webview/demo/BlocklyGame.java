package com.google.blockly.android.webview.demo;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.util.Pair;

import com.google.blockly.android.webview.demo.BlocklyTools.BlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameDefinition;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.EventType;
import com.google.blockly.android.webview.demo.BlocklyTools.MotoEvent;
import com.google.blockly.android.webview.demo.Blocks.ExecutableBlocks.AbstractExecutableBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.TileSetBlock;
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
import java.util.UUID;

public class BlocklyGame extends Game implements BlocklyMotoAPI {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    BlocklyGameDefinition gameDefinition;

    private final Handler timerHandler;
    private HashMap<Object, String> timers = new HashMap<>();
    private boolean isExpectingPress = false;
    private ArrayList<AbstractExecutableBlock> expectedOnCorrect, expectedOnIncorrect;
    private TileSetBlock expectedTileSet;
    private HashMap<Integer, ArrayList<AbstractExecutableBlock>> onCountdownEndExecutables = new HashMap<>();

    BlocklyGame(JSONObject gameDef, Handler handler) throws JSONException {
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
        this.timerHandler = handler;

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
            if(this.isExpectingPress){
                this.handleExpectedPress(tile);

            }
            else{
                //Todo IS THIS INCORRECT??
                this.gameDefinition.getBlocklyGameState().setCurrentEvent(new MotoEvent(tile, EventType.PRESS));
                this.gameDefinition.getOnTilePress().execute(this.gameDefinition.getBlocklyGameState(), this);
            }
        }else if(event == AntData.CMD_COUNTDOWN_TIMEUP){
            if(this.onCountdownEndExecutables.containsKey(tile)){
                ArrayList<AbstractExecutableBlock> toRun = this.onCountdownEndExecutables.get(tile);
                this.onCountdownEndExecutables.remove(tile);
                toRun.forEach(e -> e.execute(gameDefinition.getBlocklyGameState(), this));

            }
        }
    }

    private void handleExpectedPress(int pressed){
        this.isExpectingPress = false;
        boolean isCorrect = this.expectedTileSet.getValue(this, gameDefinition.getBlocklyGameState()).contains(pressed);
        this.expectedTileSet = null;
        ArrayList<AbstractExecutableBlock> correct = this.expectedOnCorrect == null ? null : (ArrayList<AbstractExecutableBlock>) this.expectedOnCorrect.clone();
        ArrayList<AbstractExecutableBlock> incorrect = this.expectedOnIncorrect == null ? null :(ArrayList<AbstractExecutableBlock>) this.expectedOnIncorrect.clone();
        this.expectedOnCorrect = null;
        this.expectedOnIncorrect = null;
        if(isCorrect){
            //Correct
            if(correct != null){
                correct.forEach(e -> e.execute(gameDefinition.getBlocklyGameState(), this));
            }
        }else{
            //InCorrect
            if(incorrect != null){
                incorrect.forEach(e -> e.execute(gameDefinition.getBlocklyGameState(), this));
            }
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
    public void setTileColourCountdown(int tile, int colour, int speed, ArrayList<AbstractExecutableBlock> onEnd) {
        Log.i("COUNTDOWN", "SET COUNTDOWN OF TILE " + tile);
        this.connection.setTileColorCountdown(colour, tile, speed);
        this.onCountdownEndExecutables.put(tile, onEnd);
    }

    @Override
    public void setExpectedNextPress(TileSetBlock expected, ArrayList<AbstractExecutableBlock> onCorrect, ArrayList<AbstractExecutableBlock> onIncorrect) {
        this.isExpectingPress = true;
        this.expectedOnCorrect = onCorrect;
        this.expectedOnIncorrect = onIncorrect;
        this.expectedTileSet = expected;
    }

    @Override
    public void setTileColour(int tile, int colour) {
        Log.i("COLOUR", "SET COLOUR OF TILE " + tile);
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
}
