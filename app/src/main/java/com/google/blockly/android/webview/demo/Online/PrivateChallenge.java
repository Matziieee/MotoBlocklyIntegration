package com.google.blockly.android.webview.demo.Online;

import java.util.ArrayList;
import java.util.List;

public class PrivateChallenge {

    private String key; //This is the key used by other players used to join your game
    private String creator;
    private String gameId;
    private String gameName;
    private List<String> players;

    public PrivateChallenge(){ }

    public PrivateChallenge(String key, String creator, GameObject game) {
        this.key = key;
        this.creator = creator;
        this.gameId = game.getId();
        this.gameName = game.getName();
        this.players = new ArrayList<>();
    }


    public void setKey(String key) {
        this.key = key;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

    public String getGameName() {
        return gameName;
    }

    public String getCreator() {
        return creator;
    }

    public String getGameId() {
        return gameId;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "Game: " + gameName + "\n"+
                "Creator: " + creator;
    }
}
