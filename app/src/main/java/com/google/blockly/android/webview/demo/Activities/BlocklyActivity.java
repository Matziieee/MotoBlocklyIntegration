package com.google.blockly.android.webview.demo.Activities;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;
import com.google.blockly.android.webview.demo.LanguageLevels.Level;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class BlocklyActivity extends AppCompatActivity {

    protected WebView webView;
    private DrawerLayout drawerLayout;
    private ImageButton closeSidebarBtn;
    protected ArrayAdapter<Level> levels;
    private Button startStopButton;
    private boolean isGameRunning = false;
    protected int currentLevelIdx = 0;
    private boolean isLevelsInit = true;
    protected boolean isSideBarOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockly);
        this.levels = this.parseLevels();
        this.currentLevelIdx = this.levels.getCount()-1;
        this.webView = this.findViewById(R.id.blockly_webview);
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onBlocklyLoaded();
            }
        });
        this.startStopButton = findViewById(R.id.startGameBtn);
        this.drawerLayout = this.findViewById(R.id.drawerLayout);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    private ArrayAdapter<Level> parseLevels(){
        ArrayAdapter<Level> levels = new ArrayAdapter<>(this, R.layout.spinner_item);
        try {
            JSONObject levelsObj = new JSONObject(this.readFileFromAssets("blockly/games/levelExercises.json"));
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

    abstract void onBlocklyLoaded();

    protected void loadGame(JSONObject game){
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{
            webView.evaluateJavascript("var tmpblocks = " + game.toString() + ";", (s2)->{
                webView.evaluateJavascript("Blockly.serialization.workspaces" +
                        ".load(tmpblocks,Blockly.Workspace.getAll()[0])", (s3)->{
                });
            });
        });
    }


    protected void setCurrentToolboxToLevel(Level l){
        String toolbox = l.getToolbox();
        webView.evaluateJavascript(""+
                        "var workspace = Blockly.Workspace.getAll()[0];" +
                        "workspace.updateToolbox(BLOCKLY_TOOLBOX_XML['" + toolbox +"']);"
                , s -> {
                    onLevelSelected();
                });
    }

    protected void initLevelsDropdown(Spinner levelsDropdown){
        this.isLevelsInit = true;
        levelsDropdown.setAdapter(this.levels);
        levelsDropdown.setSelection(currentLevelIdx);
        levelsDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isLevelsInit){
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

    abstract protected void onLevelSelected();
    protected void openSidebar(){
        this.isSideBarOpen = true;
        this.drawerLayout.openDrawer(GravityCompat.END);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        this.closeSidebarBtn = this.findViewById(R.id.closeSidebarBtn);
        this.closeSidebarBtn.setOnClickListener(v -> closeSidebar());
        initLevelsDropdown(findViewById(this.getLevelsDropdownId()));
    }

    abstract protected int getLevelsDropdownId();

    protected void closeSidebar(){
        this.isSideBarOpen = false;
        this.drawerLayout.closeDrawer(GravityCompat.END);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.closeSidebarBtn = null;
    }

    public void handleSettingsClick(View view){
        openSidebar();
    }

    public void handlePlayClick(View view){
        if(!isGameRunning){
            startStopButton.setText("Stop Game");
        }else{
            startStopButton.setText("Start Game");
        }
        isGameRunning = !isGameRunning;

        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("Game saved", jsonObject.toString());
            }
            catch (JSONException e) {
                Log.e("ERROR", e.toString());
            }
        });

    }

}
