package com.google.blockly.android.webview.demo;

import static com.livelife.motolibrary.AntData.LED_COLOR_BLUE;
import static com.livelife.motolibrary.AntData.LED_COLOR_GREEN;
import static com.livelife.motolibrary.AntData.LED_COLOR_OFF;
import static com.livelife.motolibrary.AntData.LED_COLOR_RED;

import android.util.Log;

import com.google.blockly.android.webview.demo.BlocklyTools.BlockParser;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameDefinition;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.EventType;
import com.google.blockly.android.webview.demo.BlocklyTools.MotoEvent;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class BlocklyGame extends Game implements BlocklyMotoAPI {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    BlocklyGameDefinition gameDefinition;
    MotoEvent currentEvent;


    BlocklyGame(JSONObject gameDef) throws JSONException {
        setName("Some game");
        setDescription("Some Description");
        BlockParser parser = BlockParser.getInstance();
        this.gameDefinition = parser.parseJson(gameDef);
        String type = gameDefinition.getConfig().getGameType().getType();

        GameType gt = new GameType(1,
                 type.equals("time") ? GameType.GAME_TYPE_TIME : GameType.GAME_TYPE_SCORE,
                this.gameDefinition.getConfig().getGameType().getThreshold(),
                "Custom Game",
                this.gameDefinition.getConfig().getNumPlayers());
        addGameType(gt);

    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        //Todo remove this line when setAllTilesIdle has been implemented in the language
        connection.setAllTilesIdle(LED_COLOR_OFF);

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

            sound.speak(getPlayerScore()[0]+"");
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();

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
    public void setAllTilesIdle(int colour) {
        this.setAllTilesIdle(colour);
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
