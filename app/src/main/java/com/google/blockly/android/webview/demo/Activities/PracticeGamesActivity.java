package com.google.blockly.android.webview.demo.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.LanguageLevels.Exercise;

import java.util.ArrayList;
import java.util.HashMap;

public class PracticeGamesActivity extends BlocklyActivity{

    private ArrayAdapter<Exercise> exercises;
    private HashMap<Integer, ArrayList<Exercise>> exerciseMap;
    private int currentExerciseIdx = 0;
    private boolean isExercisesInit = false;
    private boolean shouldLoadGame = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View sidebar = LayoutInflater.from(this).inflate(R.layout.practice_sidebar, null);
        NavigationView nv = findViewById(R.id.sideBar);
        nv.addHeaderView(sidebar);
        this.exercises = new ArrayAdapter<>(this, R.layout.spinner_item);
        this.exerciseMap = this.getExerciseMap();
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
        loadGame(this.levels.getItem(0).getExercises().get(0).getGame());
        this.setCurrentToolboxToLevel(this.levels.getItem(0));
    }

    @Override
    protected void onLevelSelected() {
        if(this.isSideBarOpen){
            this.setExerciseDropdown();
        }
    }

    @Override
    protected void closeSidebar() {
        super.closeSidebar();
        Spinner exerciseDropdown = findViewById(R.id.practice_exerciseSelectDropdown);
        exerciseDropdown.setAdapter(null);
        exerciseDropdown.setOnItemSelectedListener(null);
    }

    @Override
    protected void openSidebar() {
        super.openSidebar();
        this.initExerciseDropdown(findViewById(R.id.practice_exerciseSelectDropdown));
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
                loadGame(exercises.getItem(i).getGame());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });
        this.setExerciseDropdown();
    }

    @Override
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
        }
    }


}
