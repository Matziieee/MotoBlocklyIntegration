package com.google.blockly.android.webview.demo.Online;

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
    String originalId; //Original User ;)
    boolean published = false;
    String type;

    public GameObject(){}

    @Override
    public String toString() {
        return "Game Name: " + name + "\n" +
                "Posted by: " + userId + "\n" +
                "isPublished: " + published + "\n" +
                "Type: " + type;
    }

    public GameObject(String name, String gameJsonString, String type) {
        this.name = name;
        this.game = gameJsonString;
        this.type = type;
    }

    public GameObject(JSONObject json) throws JSONException {
        this.id = json.getString("id");
        this.name = json.getString("name");
        game = json.getString("game");
        userId = json.getString("userId");
        originalId = json.getString("originalId");
        published = json.getBoolean("published");
        type = json.getString("type");
    }


    public HashMap<String, Object> getHashMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("game", game);
        result.put("type", type);
        result.put("published", published);
        result.put("originalId", originalId);
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
        jObj.put("type", type);
        jObj.put("published", published);
        return jObj;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
