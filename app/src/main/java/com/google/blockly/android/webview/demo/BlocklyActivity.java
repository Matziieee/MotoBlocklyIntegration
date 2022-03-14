package com.google.blockly.android.webview.demo;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.function.Consumer;

public class BlocklyActivity extends AppCompatActivity {

    private WebView webView;
    private Spinner spinner;
    private Button startStopButton;
    private ImageButton closeSidebarBtn;
    private DrawerLayout drawerLayout;
    private ArrayAdapter<GameListItem> gameNamesAndIndexes;
    private final BlocklyGamesStore store = BlocklyGamesStore.getInstance();
    private EditText nameInput;
    private boolean isDropdownInit = false;
    private int currentSelectedGame = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BlocklyActivity x = this;
        this.webView = this.findViewById(R.id.blockly_webview);
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                try {
                    clearAndCreateStandardBlocks(s -> {});
                    gameNamesAndIndexes = new ArrayAdapter<>(x, R.layout.spinner_item);
                    populateGameNamesAndIndexes();
                } catch (IOException | JSONException e) {
                    Log.e("ERROR", e.toString());
                }
            }
        });
        this.drawerLayout = this.findViewById(R.id.drawerLayout);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

    }

    private void initGameSelectDropdown(){
        Context context = this;
        this.spinner.setAdapter(this.gameNamesAndIndexes);
        this.spinner.setSelection(currentSelectedGame);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isDropdownInit){
                    isDropdownInit = false;
                    return;
                }
                //Load clicked game
                currentSelectedGame = i;
                try {
                    if(gameNamesAndIndexes.getItem(i).getIndex() == -1){
                        clearAndCreateStandardBlocks(s -> {
                            nameInput.setText("");
                        });
                        return;
                    }
                    JSONObject game = store.getGames(context).getJSONObject(i-1);
                    loadGame(game.getJSONObject("game"));
                    nameInput.setText(game.getString("name"));
                } catch (IOException | JSONException e) {
                    Log.e("ERROR", e.toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void loadGame(JSONObject game){
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{
            webView.evaluateJavascript("var tmpblocks = " + game.toString() + ";", (s2)->{
                webView.evaluateJavascript("Blockly.serialization.workspaces" +
                        ".load(tmpblocks,Blockly.Workspace.getAll()[0])", (s3)->{
                });
            });
        });
    }

    private void populateGameNamesAndIndexes() throws IOException, JSONException {
        this.gameNamesAndIndexes.clear();
        JSONArray games = store.getGames(this);
        //Always add "NONE" as first
        this.gameNamesAndIndexes.add(new GameListItem("NONE", -1));
        for (int i = 0; i < games.length(); i++) {
            String name = games.getJSONObject(i).getString("name");
            this.gameNamesAndIndexes.add(new GameListItem(name, i));
        }
        this.gameNamesAndIndexes.notifyDataSetChanged();

    }

    public void handleSaveClick(View view){
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                BlocklyGamesStore.getInstance().saveGame(this, jsonObject, nameInput.getText().toString());
                populateGameNamesAndIndexes();
            }
            catch (JSONException | IOException e) {
                Log.e("ERROR", e.toString());
            }
        });
    }

    public void handleDeleteClick(View view) throws IOException, JSONException {
        int selected = ((GameListItem) spinner.getSelectedItem()).getIndex();
        if(selected == -1){
            clearAndCreateStandardBlocks(s -> {nameInput.setText("");});
            return;
        }
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{});
        store.deleteGame(this, selected);
        populateGameNamesAndIndexes();
        spinner.setSelection(0);
    }

    private void clearAndCreateStandardBlocks(Consumer<String> callback){
        webView.evaluateJavascript("var workspace = Blockly.Workspace.getAll()[0];" +
                " workspace.clear(); " +
                " var config = workspace.newBlock('gameblock');" +
                " config.initSvg();" +
                " config.render();" +
                " var type = workspace.newBlock('gametype');" +
                " type.initSvg();" +
                " type.render();" +
                " var parentConnection = config.getInput('gameType').connection;" +
                " var childConnection = type.outputConnection;" +
                " parentConnection.connect(childConnection);",(s)->{
            callback.accept(s);
        });
    }

    public void handleCreateClick(View view){
        clearAndCreateStandardBlocks(s -> {
            spinner.setSelection(0);
            spinner.setSelected(true);
        });

    }

    private void openSidebar(){
        this.drawerLayout.openDrawer(GravityCompat.END);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        this.closeSidebarBtn = this.findViewById(R.id.closeSidebarBtn);
        this.closeSidebarBtn.setOnClickListener(v -> closeSidebar());

        this.nameInput = this.findViewById(R.id.gameNameInput);
        this.spinner = this.findViewById(R.id.gameSelectDropdown);
        this.isDropdownInit = true;
        initGameSelectDropdown();
    }

    private void closeSidebar(){
        this.drawerLayout.closeDrawer(GravityCompat.END);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.closeSidebarBtn = null;
    }

    public void handleSettingsClick(View view){
        openSidebar();
    }

}
