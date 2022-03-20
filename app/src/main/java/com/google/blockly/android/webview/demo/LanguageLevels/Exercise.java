package com.google.blockly.android.webview.demo.LanguageLevels;

import org.json.JSONObject;

public class Exercise {
    private int id;
    private String description;
    private JSONObject game;

    public Exercise (int id, String description, JSONObject game){
        this.id = id;
        this.description = description;
        this.game = game;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public JSONObject getGame() {
        return game;
    }

    @Override
    public String toString(){
        return "Exercise: " + id;
    }
}
