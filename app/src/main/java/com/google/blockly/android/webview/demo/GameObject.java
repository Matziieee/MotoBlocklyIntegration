package com.google.blockly.android.webview.demo;

import java.util.HashMap;
import java.util.UUID;

public class GameObject {
    String id = UUID.randomUUID().toString();
    String name;
    String game;
    String userId;
    boolean ruleBased;

    public GameObject(){}

    public GameObject(String name, String gameJsonString, boolean isRuleBased) {
        this.name = name;
        this.game = gameJsonString;
        this.ruleBased = isRuleBased;
    }


    public HashMap<String, Object> getHashMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("game", game);
        result.put("ruleBased", ruleBased);
        return result;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setRuleBased(boolean ruleBased) {
        this.ruleBased = ruleBased;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGame() {
        return game;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isRuleBased() {
        return ruleBased;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
