package com.google.blockly.android.webview.demo.LanguageLevels;

import org.json.JSONArray;
import org.json.JSONObject;

public class Level {
    private String name, toolbox;
    private int level;
    private JSONObject defaultWorkspace;
    private JSONArray exercises;

    public Level(String name, String toolbox, int level, JSONObject defaultWorkspace, JSONArray exercises) {
        this.name = name;
        this.toolbox = toolbox;
        this.level = level;
        this.defaultWorkspace = defaultWorkspace;
        this.exercises = exercises;
    }

    public String getName() {
        return name;
    }

    public String getToolbox() {
        return toolbox;
    }

    public int getLevel() {
        return level;
    }

    public JSONObject getDefaultWorkspace() {
        return defaultWorkspace;
    }

    public JSONArray getExercises() {
        return exercises;
    }

    @Override
    public String toString() {
        return level + ": " + name;
    }
}
