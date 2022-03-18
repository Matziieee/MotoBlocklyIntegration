package com.google.blockly.android.webview.demo.LanguageLevels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Level {
    private String name, toolbox;
    private int level;
    private JSONObject defaultWorkspace;
    private ArrayList<Exercise> exercises;

    public Level(String name, String toolbox, int level, JSONObject defaultWorkspace, JSONArray exercises) {
        this.name = name;
        this.toolbox = toolbox;
        this.level = level;
        this.defaultWorkspace = defaultWorkspace;
        this.exercises = new ArrayList<>();
        for (int i = 0; i < exercises.length(); i++){
            try {
                this.exercises.add(new Exercise(
                        exercises.getJSONObject(i).getInt("id"),
                        exercises.getJSONObject(i).getString("description"),
                        exercises.getJSONObject(i).getJSONObject("game")
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

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

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    @Override
    public String toString() {
        return level + ": " + name;
    }
}
