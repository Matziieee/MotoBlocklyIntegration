package com.google.blockly.android.webview.demo;

import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.blockly.android.webview.demo.Activities.BlocklyActivity;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.ConfigParser;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.ConfigurationGameDefinition;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenPlayPattern;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenRegisterPattern;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.TilePressEvent;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenColorPress;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenPlayerScore;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenTimePassed;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenType;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlocklyRuleGame extends Game implements MotoConfigGameAPI{

    ConfigurationGameDefinition gameDef;
    private Handler handler;
    private Random random;
    private BlocklyActivity activity;
    private MotoConnection mConnection;
    private HashMap<String, ArrayList<TilePressEvent>> patternMap;

    HashMap<Integer, Integer> tileColorMap;

    private boolean isPlayingPattern = false, isRegisteringPattern = false;
    private String registeringPatternId;
    private int playingPatternIndex, registeringPatternTargetLength;

    //todo fix stop game hack
    public BlocklyRuleGame(JSONObject workspace, BlocklyActivity activity) throws JSONException {
        handler = new Handler();
        ConfigParser parser = new ConfigParser();
        this.gameDef = parser.parse(workspace);
        random = new Random(); //Can add seed here for debugging :)
        GameType gt = new GameType(1,
                GameType.GAME_TYPE_TIME,
                this.gameDef.getConfigBlock().getDuration(),
                "game",
                gameDef.getConfigBlock().getPlayers());
        addGameType(gt);
        patternMap = new HashMap<>();
        tileColorMap = new HashMap<>();
        mConnection = MotoConnection.getInstance();
        mConnection.connectedTiles.forEach(i -> {
            tileColorMap.put(i, 0);
        });
        this.activity = activity;
    }

    @Override
    public void onGameStart() {
        //Always set all to idle the turn off to reset...
        this.setAllTilesColor(0);
        //Initialise all timers and run on start events
        gameDef.getConfigBlock().getWhenBlocks().forEach(wb -> {
            if(wb.getType() == WhenType.GameStart){
                executeThens(wb.getThenBlocks(), null);
            }
            else if(wb.getType() == WhenType.XSecondsPassed){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isPlayingPattern || isRegisteringPattern){
                            handler.postDelayed(this, ((WhenTimePassed)wb).getInterval() * 1000L);
                        }else{
                            executeThens(wb.getThenBlocks(), null);
                            handler.postDelayed(this, ((WhenTimePassed)wb).getInterval() * 1000L);
                        }
                    }
                };
                handler.postDelayed(runnable,((WhenTimePassed)wb).getInterval() * 1000L);
            }
        });
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        if(this.isPlayingPattern){
            return;
        }
        int event = AntData.getCommand(message);
        int tile = AntData.getId(message);
        int color = tileColorMap.get(tile);
        TilePressEvent ev = new TilePressEvent(tile, color);
        System.out.println("RECEIVE UPDATE | tile: " + tile + " | colour " + color);
        if(event == AntData.EVENT_PRESS){
            if(isRegisteringPattern){
                ArrayList<TilePressEvent> list = this.patternMap.get(this.registeringPatternId);
                list.add(ev);
                if(list.size() == this.registeringPatternTargetLength){
                    isRegisteringPattern = false;
                    this.registeringPatternId = "";
                    this.registeringPatternTargetLength = 0;
                }

            }
            this.gameDef.getConfigBlock().getWhenBlocks().forEach(wb -> {
                if(wb.getType() == WhenType.AnyTilePressed){
                    executeThens(wb.getThenBlocks(), ev);
                }
                else if(wb.getType() == WhenType.ColourTilePressed && color == ((WhenColorPress)wb).getCol()){
                    executeThens(wb.getThenBlocks(), ev);
                }
            });
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        this.handler.removeCallbacksAndMessages(null);
        gameDef.getConfigBlock().getWhenBlocks().forEach(wb -> {
            if(wb.getType() == WhenType.GameEnd){
                executeThens(wb.getThenBlocks(), null);
            }
        });
        mConnection.setAllTilesBlink(3, 1);
        this.activity.setGameStopped();
    }


    private void executeThens(List<ThenBlock> thens, @Nullable TilePressEvent ev){
        for (int i = 0; i < thens.size(); i++) {
            if(thens.get(i) instanceof ThenPlayPattern){
                if(isPlayingPattern){
                    handler.removeCallbacksAndMessages("playRunner");
                }
                thens.get(i).execute(this,ev);
                final int index = i;
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        if(isPlayingPattern){
                            Log.i("YO", "run: Waiting");
                            handler.postAtTime(this,"playRunner", SystemClock.uptimeMillis() + 200);
                        }else{
                            Log.i("YO", "run: Executing rest");
                            if(index != thens.size()-1){
                                Log.i("YO", "run: hopefully not here");
                                List<ThenBlock> remaining = thens.subList(index+1, thens.size());
                                executeThens(remaining,ev);
                            }
                        }
                    }
                }, "playRunner",SystemClock.uptimeMillis() + 200);
            }
            else if(thens.get(i) instanceof ThenRegisterPattern){
                if(isRegisteringPattern){
                    this.patternMap.get(this.registeringPatternId).clear();
                    handler.removeCallbacksAndMessages("registerRunner");
                }
                thens.get(i).execute(this, ev);
                final int index = i;
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        if(isRegisteringPattern){
                            handler.postAtTime(this,"registerRunner", SystemClock.uptimeMillis() + 200);
                        }
                        else{

                            if(index != thens.size()-1){
                                List<ThenBlock> remaining = thens.subList(index+1, thens.size());
                                executeThens(remaining,ev);
                            }
                        }
                    }
                }, "registerRunner", SystemClock.uptimeMillis() + 200);
                break;
            }else{
                thens.get(i).execute(this, ev);
            }
        }
    }

    private void checkForPlayerScoreEvent(int player){
        int newScore = this.getPlayerScore()[player];
        MotoSound.getInstance().speak(newScore+"");
        this.gameDef.getConfigBlock().getWhenBlocks().forEach(wb ->{
            if(wb.getType() == WhenType.PlayerScore){
                if( ((WhenPlayerScore)wb).getScore() == newScore){
                    executeThens(wb.getThenBlocks(), null);
                }
            }
        });
    }

    //todo change these..
    @Override
    public void decrementPlayerScore(int player) {
        this.incrementPlayerScore(-1, player);
        checkForPlayerScoreEvent(player);
    }

    @Override
    public void incrementPlayerScore(int player) {
        this.incrementPlayerScore(1, player);
        checkForPlayerScoreEvent(player);
    }

    @Override
    public void setAllTilesColor(int color) {
        this.tileColorMap.keySet().forEach(k -> {
            this.tileColorMap.put(k, color);
        });
        mConnection.setAllTilesColor(color);
    }

    @Override
    public void setTilesRandomColor(boolean isSame) {
        if(isSame){
            setAllTilesColor(random.nextInt(5)+1);
        }else{
            mConnection.connectedTiles.forEach(i -> {
                int color = random.nextInt(5)+1;
                tileColorMap.put(i, color);
                mConnection.setTileColor(color, i);
            });
        }
    }

    @Override
    public void registerPattern(String id, String name, int length){
        this.isRegisteringPattern = true;
        this.patternMap.put(id, new ArrayList<>());
        this.registeringPatternId = id;
        this.registeringPatternTargetLength = length;
    }

    @Override
    public void playPattern(String targetId) {
        Log.i("YO", "playPattern: Im here!");
        playingPatternIndex = 0;
        isPlayingPattern = true;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("YO", "play: Running");
                if(!patternMap.containsKey(targetId) || patternMap.get(targetId).size() == playingPatternIndex){
                    Log.i("YO", "play: Stopping");
                    isPlayingPattern = false;
                    mConnection.setAllTilesColor(0);
                    return;
                }

                mConnection.setAllTilesIdle(0);
                TilePressEvent ev = patternMap.get(targetId).get(playingPatternIndex);
                Log.i("YO", "play: Setting tile " + ev.getTile() + " to " + ev.getColor());
                mConnection.setTileColor(ev.getColor() == 0 ? 1 : ev.getColor(), ev.getTile());
                playingPatternIndex++;
                handler.postDelayed(this, 1000);
            }
        }, 0);
    }

    @Override
    public void setTileRandomColor(int tile) {
        int color = random.nextInt(5)+1;
        this.setTileColor(tile, color);

    }

    @Override
    public void setTileColor(int tile, int color) {
        this.tileColorMap.put(tile, color);
        mConnection.setTileColor(color, tile);
    }

    @Override
    public void setAllTilesExceptRandomColor(int tile, boolean isSame) {
        int color = random.nextInt(5)+1;
        this.mConnection.connectedTiles.forEach(t -> {
            if(t != tile){
                if(isSame){
                    this.setTileColor(tile, color);
                }else{
                    this.setTileRandomColor(t);
                }
            }
        });
    }

    @Override
    public void setAllTilesExceptColor(int tile, int color) {
        this.mConnection.connectedTiles.forEach(t -> {
            if(t != tile){
                this.setTileColor(t, color);
            }
        });
    }

    @Override
    public void playSound(String sound) {
        MotoSound player = MotoSound.getInstance();
        Log.i("HELLO SOUND", sound);
        switch (sound){
            case "start":
                player.playStart();
                break;
            case "end":
                player.playStop();
                break;
            case "s1":
                player.playStep();
                break;
            case "s2":
                player.playStep2();
                break;
            case "p1":
                player.playPress1();
                break;
            case "p2":
                player.playPress2();
                break;
            case "p3":
                player.playPress3();
                break;
            case "p4":
                player.playPress4();
                break;
            case "m":
                player.playMatched();
                break;
            case "e":
                player.playError();
                break;
        }
    }
}
