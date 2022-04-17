package com.google.blockly.android.webview.demo.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyGame;
import com.google.blockly.android.webview.demo.LanguageLevels.Exercise;
import com.google.blockly.android.webview.demo.LanguageLevels.Level;
import com.livelife.motolibrary.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class PracticeGamesActivity extends BlocklyActivity{

    private ArrayAdapter<Exercise> exercises;
    private HashMap<Integer, ArrayList<Exercise>> exerciseMap;
    private int currentExerciseIdx = 0;
    private boolean isExercisesInit = false;
    private boolean isLevelsInit = true;
    private ArrayAdapter<Level> levels;
    private int currentLevelIdx = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View sidebar = LayoutInflater.from(this).inflate(R.layout.practice_sidebar, null);
        NavigationView nv = findViewById(R.id.sideBar);
        nv.addHeaderView(sidebar);
        this.levels = this.parseLevels("blockly/games/levelExercises.json");
        this.currentLevelIdx = this.levels.getCount()-1;
        this.exercises = new ArrayAdapter<>(this, R.layout.spinner_item);
        this.exerciseMap = this.getExerciseMap();
    }


    private ArrayAdapter<Level> parseLevels(String path){
        ArrayAdapter<Level> levels = new ArrayAdapter<>(this, R.layout.spinner_item);
        try {
            JSONObject levelsObj = new JSONObject(this.readFileFromAssets(path));
            JSONArray levelsArray = levelsObj.getJSONArray("levels");
            for(int i = 0; i < levelsArray.length(); i++){
                JSONObject lObj = levelsArray.getJSONObject(i);
                String name = lObj.getString("name");
                String toolbox = lObj.getString("toolbox");
                int level = lObj.getInt("level");
                JSONArray exercises = lObj.getJSONArray("exercises");
                levels.add(new Level(name, toolbox, level, exercises));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return levels;
    }

    private String readFileFromAssets(String fileName){
        String json = "";
        try {
            InputStream stream = getAssets().open(fileName);
            int size = stream.available();
            byte[] byteArray = new byte[size];
            stream.read(byteArray);
            stream.close();
            json = new String(byteArray, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    private void setCurrentToolboxToLevel(Level l){
        String toolbox = l.getToolbox();
        this.setToolbox(toolbox, s -> onLevelSelected());
    }

    protected void initLevelsDropdown(Spinner levelsDropdown){
        this.isLevelsInit = true;
        levelsDropdown.setAdapter(this.levels);
        levelsDropdown.setSelection(currentLevelIdx);
        levelsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isLevelsInit || i == currentLevelIdx){
                    isLevelsInit = false;
                    return;
                }
                currentLevelIdx = i;
                setCurrentToolboxToLevel(levels.getItem(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        levels.notifyDataSetChanged();
    }

    private HashMap<Integer, ArrayList<Exercise>> getExerciseMap() {
        HashMap<Integer, ArrayList<Exercise>> map = new HashMap<>();
        for (int i = 0; i < this.levels.getCount(); i++) {
            map.put(i, this.levels.getItem(i).getExercises());
        }
        return map;
    }

    @Override
    void onBlocklyLoaded() {
        //Set workspace to level 1, exercise 1
        this.currentLevelIdx = 0;
        this.currentExerciseIdx = 0;
        Exercise e = this.levels.getItem(0).getExercises().get(0);
        loadGame(e.getGame());
        this.setCurrentToolboxToLevel(this.levels.getItem(0));
    }

    @Override
    Game getGame(JSONObject game) throws JSONException {
        return new BlocklyGame(game, this.handler, this);
    }

    private void onLevelSelected() {
        if(this.isSideBarOpen){
            this.currentExerciseIdx = 0;
            this.setExerciseDropdown();
        }
    }

    @Override
    protected void closeSidebar() {
        super.closeSidebar();
        Spinner exerciseDropdown = findViewById(R.id.practice_exerciseSelectDropdown);
        exerciseDropdown.setAdapter(null);
        exerciseDropdown.setOnItemSelectedListener(null);
        Spinner levelsDropdown = findViewById(this.getLevelsDropdownId());
        levelsDropdown.setOnItemSelectedListener(null);
        levelsDropdown.setAdapter(null);
    }

    @Override
    protected void openSidebar() {
        super.openSidebar();
        initLevelsDropdown(findViewById(this.getLevelsDropdownId()));
        this.initExerciseDropdown(findViewById(R.id.practice_exerciseSelectDropdown));
        ((TextView)findViewById(R.id.descriptionTV))
                .setText(this.exercises.getItem(this.currentExerciseIdx).getDescription());

    }

    private void initExerciseDropdown(Spinner exerciseDropdown) {
        this.isExercisesInit = true;
        exerciseDropdown.setAdapter(this.exercises);
        exerciseDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isExercisesInit){
                    isExercisesInit = false;
                    return;
                }
                currentExerciseIdx = i;
                Exercise e = exercises.getItem(i);
                loadGame(exercises.getItem(i).getGame());
                //Set description
                ((TextView)findViewById(R.id.descriptionTV)).setText(e.getDescription());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        this.setExerciseDropdown();
    }

    protected int getLevelsDropdownId() {
        return R.id.practice_levelSelectDropdown;
    }

    private void setExerciseDropdown(){
        this.exercises.clear();
        this.exerciseMap.get(currentLevelIdx).forEach(e -> {
            this.exercises.add(e);
        });
        this.exercises.notifyDataSetChanged();
        ((Spinner)findViewById(R.id.practice_exerciseSelectDropdown)).setSelection(currentExerciseIdx);
        if(!this.isExercisesInit){
            loadGame(exercises.getItem(currentExerciseIdx).getGame());
            ((TextView)findViewById(R.id.descriptionTV))
                    .setText(exercises.getItem(currentExerciseIdx).getDescription());
        }
    }


}
