package com.google.blockly.android.webview.demo.Activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyRuleGame;
import com.google.blockly.android.webview.demo.BlocklyGame;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;
import com.google.blockly.android.webview.demo.GameListItem;
import com.livelife.motolibrary.Game;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.function.Consumer;

public class CreateGamesActivity extends BlocklyActivity{


    private Spinner gameDropdown, typeDropdown;
    private ArrayAdapter<GameListItem> gameNamesAndIndexes;
    private final BlocklyGamesStore store = BlocklyGamesStore.getInstance();
    private EditText nameInput;
    private boolean isGameDropdownInit = false, isTypeDropdownInit = false;
    private int currentSelectedGame = 0;
    private boolean isRuleBased = true;
    private ArrayAdapter<String> langTypeDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View sidebar = LayoutInflater.from(this).inflate(R.layout.create_sidebar, null);
        NavigationView nv = findViewById(R.id.sideBar);
        nv.addHeaderView(sidebar);
        langTypeDropdown = new ArrayAdapter<>(this, R.layout.spinner_item);
        langTypeDropdown.add("Rule-Based");
        langTypeDropdown.add("Advanced");
    }

    @Override
    void onBlocklyLoaded() {
        clearAndCreateStandardBlocks(s -> {});
        gameNamesAndIndexes = new ArrayAdapter<>(this, R.layout.spinner_item);
        try {
            populateGameNamesAndIndexes();
        } catch (IOException | JSONException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private void initTypeSelectDropdown(){
        this.typeDropdown.setAdapter(this.langTypeDropdown);
        this.isTypeDropdownInit = true;
        this.typeDropdown.setSelection(this.isRuleBased ? 0 : 1);
        this.typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isTypeDropdownInit){
                    isTypeDropdownInit = false;
                    return;
                }
                isRuleBased = i == 0;
                try {
                    populateGameNamesAndIndexes();
                    currentSelectedGame = 0;
                    gameDropdown.setSelection(0);
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
                clearAndCreateStandardBlocks(s -> {
                    nameInput.setText("");
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Do nothing
            }
        });
    }

    private void initGameSelectDropdown(){
        Context context = this;
        this.gameDropdown.setAdapter(this.gameNamesAndIndexes);
        this.gameDropdown.setSelection(currentSelectedGame);
        this.isGameDropdownInit = true;
        this.gameDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isGameDropdownInit){
                    isGameDropdownInit = false;
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
                    JSONObject game = findGame(i-1, store.getGames(context));
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

    private JSONObject findGame (int i, JSONArray array) throws JSONException {
        int virtualIdx = 0;
        for (int j = 0; j < array.length(); j++) {
            if(isRuleBased && array.getJSONObject(j).getBoolean("isRuleBased") || (!isRuleBased && !array.getJSONObject(j).getBoolean("isRuleBased"))){
                if(i == virtualIdx){
                    return array.getJSONObject(j);
                }
                virtualIdx++;
            }
        }
        throw new JSONException("No game at index " + i + " found");
    }

    protected void populateGameNamesAndIndexes() throws IOException, JSONException {
        this.gameNamesAndIndexes.clear();
        JSONArray games = store.getGames(this);
        //Always add "NONE" as first
        this.gameNamesAndIndexes.add(new GameListItem("NONE", -1));
        for (int i = 0; i < games.length(); i++) {
            String name = games.getJSONObject(i).getString("name");
            if(this.isRuleBased && games.getJSONObject(i).getBoolean("isRuleBased")){
                this.gameNamesAndIndexes.add(new GameListItem(name, i));
            }
            else if(!this.isRuleBased && !games.getJSONObject(i).getBoolean("isRuleBased")){
                this.gameNamesAndIndexes.add(new GameListItem(name, i));
            }
        }
        this.gameNamesAndIndexes.notifyDataSetChanged();
    }

    public void handleSaveClick(View view){
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                BlocklyGamesStore.getInstance().saveGame(this, jsonObject, nameInput.getText().toString(), this.isRuleBased);
                populateGameNamesAndIndexes();
            }
            catch (JSONException | IOException e) {
                Log.e("ERROR", e.toString());
            }
        });
    }

    public void handleDeleteClick(View view) throws IOException, JSONException {
        int selected = ((GameListItem) gameDropdown.getSelectedItem()).getIndex();
        if(selected == -1){
            clearAndCreateStandardBlocks(s -> {nameInput.setText("");});
            return;
        }
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{});
        store.deleteGame(this, selected);
        populateGameNamesAndIndexes();
        gameDropdown.setSelection(0);
    }

    protected void clearAndCreateStandardBlocks(Consumer<String> callback){
        if(this.isRuleBased){
            this.setToolbox("mads", s ->
                    webView.evaluateJavascript("var workspace = Blockly.Workspace.getAll()[0];" +
                    " workspace.clear(); " +
                    " var config = workspace.newBlock('v2config');" +
                    " config.initSvg();" +
                    " config.render();",(s1)->{
                callback.accept(s1);
            }));

        }else{
            this.setToolbox("standard", s ->
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
                    " parentConnection.connect(childConnection);",(s1)->{
                callback.accept(s1);
            }));
        }

    }

    public void handleCreateClick(View view){
        clearAndCreateStandardBlocks(s -> {
            gameDropdown.setSelection(0);
            gameDropdown.setSelected(true);
        });

    }

    @Override
    protected void openSidebar() {
        super.openSidebar();
        this.nameInput = this.findViewById(R.id.gameNameInput);
        this.gameDropdown = this.findViewById(R.id.gameSelectDropdown);
        this.typeDropdown = this.findViewById(R.id.create_typeSelectDropdown);
        initGameSelectDropdown();
        initTypeSelectDropdown();
    }

    @Override
    Game getGame(JSONObject game) throws JSONException {
        if(this.isRuleBased){
            return new BlocklyRuleGame(game, this);
        }
        return new BlocklyGame(game, this.handler);
    }

}
