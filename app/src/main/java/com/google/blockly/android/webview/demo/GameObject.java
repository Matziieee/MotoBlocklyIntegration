package com.google.blockly.android.webview.demo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class GameObject implements Serializable {
    String id = UUID.randomUUID().toString();
    protected String name;
    String game;
    protected String userId;
    String originalId;
    boolean ruleBased, published = false;

    public GameObject(){}

    @Override
    public String toString() {
        return "Game Name: " + name + "\n" +
                "Posted by: " + userId + "\n" +
                "isPublished: " + published;
    }

    public GameObject(String name, String gameJsonString, boolean isRuleBased) {
        this.name = name;
        this.game = gameJsonString;
        this.ruleBased = isRuleBased;
    }

    public GameObject(JSONObject json) throws JSONException {
        this.id = json.getString("id");
        this.name = json.getString("name");
        game = json.getString("game");
        userId = json.getString("userId");
        originalId = json.getString("originalId");
        ruleBased = json.getBoolean("ruleBased");
        published = json.getBoolean("published");
    }


    public HashMap<String, Object> getHashMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("game", game);
        result.put("ruleBased", ruleBased);
        return result;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean published) {
        this.published = published;
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

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject jObj = new JSONObject();
        jObj.put("id", id);
        jObj.put("name", name);
        jObj.put("userId", userId);
        jObj.put("originalId", originalId);
        jObj.put("ruleBased", ruleBased);
        jObj.put("published", published);
        return jObj;
    }
}
