package com.google.blockly.android.webview.demo;

import android.os.Handler;

import com.google.blockly.android.webview.demo.Activities.BlocklyActivity;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.ConfigParser;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.ConfigurationGameDefinition;
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

import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Random;

public class BlocklyConfigGame extends Game implements MotoConfigGameAPI{

    ConfigurationGameDefinition gameDef;
    private Handler handler;
    private Random random;
    private BlocklyActivity activity;
    private MotoConnection mConnection;

    HashMap<Integer, Integer> tileColorMap;

    //todo fix stop game hack
    public BlocklyConfigGame(JSONObject workspace, BlocklyActivity activity) throws JSONException {
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
        gameDef.getConfigBlock().getWhenBlocks().forEach(whenBlock -> {
            if(whenBlock.getType() == WhenType.GameStart){
                whenBlock.getThenBlocks().forEach(tb -> tb.execute(this, null));
            }
            else if(whenBlock.getType() == WhenType.XSecondsPassed){
                MotoConfigGameAPI api = this;
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        whenBlock.getThenBlocks().forEach(tb -> tb.execute(api, null));
                        handler.postDelayed(this, ((WhenTimePassed)whenBlock).getInterval() * 1000L);
                    }
                };
                handler.postDelayed(runnable,((WhenTimePassed)whenBlock).getInterval() * 1000L);
            }
        });
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        int event = AntData.getCommand(message);
        int tile = AntData.getId(message);
        int color = tileColorMap.get(tile);
        TilePressEvent ev = new TilePressEvent(tile, color);
        System.out.println("RECEIVE UPDATE | tile: " + tile + " | colour " + color);
        if(event == AntData.EVENT_PRESS){
            this.gameDef.getConfigBlock().getWhenBlocks().forEach(wb -> {
                if(wb.getType() == WhenType.AnyTilePressed){
                    wb.getThenBlocks().forEach(tb -> tb.execute(this, ev));
                }
                else if(wb.getType() == WhenType.ColourTilePressed && color == ((WhenColorPress)wb).getCol()){
                    wb.getThenBlocks().forEach(tb -> tb.execute(this, ev));
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
                wb.getThenBlocks().forEach(tb -> tb.execute(this, null));
            }
        });
        mConnection.setAllTilesBlink(3, 1);
        this.activity.setGameStopped();
    }

    private void checkForPlayerScoreEvent(int player){
        int newScore = this.getPlayerScore()[player-1];
        MotoSound.getInstance().speak(newScore+"");
        this.gameDef.getConfigBlock().getWhenBlocks().forEach(wb ->{
            if(wb.getType() == WhenType.PlayerScore){
                System.out.println("HERE I AM!!!!!!!!!!!!!!!!!!!!!");
                if( ((WhenPlayerScore)wb).getScore() == newScore){
                    wb.getThenBlocks().forEach(tb -> {tb.execute(this, null);});
                }
            }
        });
    }

    //todo change these..
    @Override
    public void decrementPlayerScore(int player) {
        this.incrementPlayerScore(-1, player-1);
        checkForPlayerScoreEvent(player);
    }

    @Override
    public void incrementPlayerScore(int player) {
        this.incrementPlayerScore(1, player-1);
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
}
