package com.google.blockly.android.webview.demo.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.LayoutInflater;
import android.view.View;

import com.example.blocklywebview.R;

public class PracticeGamesActivity extends BlocklyActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View sidebar = LayoutInflater.from(this).inflate(R.layout.practice_sidebar, null);
        NavigationView nv = findViewById(R.id.sideBar);
        nv.addHeaderView(sidebar);
    }

    @Override
    void onBlocklyLoaded() {

    }

    @Override
    protected void onLevelSelected() {
        //Do something?
    }

    @Override
    protected int getLevelsDropdownId() {
        return R.id.practice_levelSelectDropdown;
    }


}
