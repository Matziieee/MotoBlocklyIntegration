package com.google.blockly.android.webview.demo.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.blocklywebview.R;
import com.google.android.material.navigation.NavigationView;
import com.google.blockly.android.webview.demo.BlocklyGame;
import com.google.blockly.android.webview.demo.BlocklyImageGame;
import com.google.blockly.android.webview.demo.BlocklyRuleGame;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;
import com.google.blockly.android.webview.demo.BlocklyTools.FirestoreGameManagerService;
import com.google.blockly.android.webview.demo.BlocklyTools.IGameManagerService;
import com.google.blockly.android.webview.demo.Online.GameListItem;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.livelife.motolibrary.Game;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class CreateGamesActivity extends BlocklyActivity{


    private Spinner gameDropdown, typeDropdown;
    private ArrayAdapter<GameListItem> gameNamesAndIndexes;
    private final BlocklyGamesStore store = BlocklyGamesStore.getInstance();
    private EditText nameInput;
    private boolean isGameDropdownInit = false, isTypeDropdownInit = false;
    private int currentSelectedGame = 0;
    private String gameType = "Rule-Based";
    private ArrayAdapter<String> langTypeDropdown;
    private IGameManagerService gameManager;
    private AlertDialog scoreWindow;
    private Handler scoreHandler;
    private boolean isSaving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View sidebar = LayoutInflater.from(this).inflate(R.layout.create_sidebar, null);
        NavigationView nv = findViewById(R.id.sideBar);
        nv.addHeaderView(sidebar);
        langTypeDropdown = new ArrayAdapter<>(this, R.layout.spinner_item);
        langTypeDropdown.add("Rule-Based");
        langTypeDropdown.add("Advanced");
        langTypeDropdown.add("Image");
        gameManager = new FirestoreGameManagerService(this);
        scoreHandler = new Handler();
    }

    @Override
    void onBlocklyLoaded() {
        clearAndCreateStandardBlocks(s -> {});
        gameNamesAndIndexes = new ArrayAdapter<>(this, R.layout.spinner_item);
        try {
            populateGameNamesAndIndexes();
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private String getLangType(int i){
        switch(i){
            case 0: return "Rule-Based";
            case 1: return "Advanced";
            case 2: return "Image";
        }
        return "Error";
    }
    private int getLangTypeIndex(String type){
        switch (type) {
            case "Rule-Based": return 0;
            case "Advanced": return 1;
            case "Image": return 2;
        }
        return -1;
    }

    private void initTypeSelectDropdown(){
        this.typeDropdown.setAdapter(this.langTypeDropdown);
        this.isTypeDropdownInit = true;
        this.typeDropdown.setSelection(getLangTypeIndex(this.gameType));
        this.typeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(isTypeDropdownInit){
                    isTypeDropdownInit = false;
                    return;
                }
                gameType = getLangType(i);
                try {
                    populateGameNamesAndIndexes();
                    currentSelectedGame = 0;
                    gameDropdown.setSelection(0);
                } catch (JSONException e) {
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
                    JSONObject game = new JSONObject(gameNamesAndIndexes.getItem(i).getGameObject().getGame());
                    loadGame(game);
                    nameInput.setText(gameNamesAndIndexes.getItem(i).getName());
                } catch (JSONException e) {
                    Log.e("ERROR", e.toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    protected void populateGameNamesAndIndexes() throws JSONException {
        this.gameNamesAndIndexes.clear();
        gameManager.getMyGames().addOnSuccessListener(queryDocumentSnapshots -> {
            this.gameNamesAndIndexes.add(new GameListItem("NONE", -1));
            List<GameObject> games = queryDocumentSnapshots.toObjects(GameObject.class);

            for (int i = 0; i < games.size(); i++) {
                String name = games.get(i).getName();
                if(this.gameType.equals(games.get(i).getType())){
                    this.gameNamesAndIndexes.add(new GameListItem(name, i, games.get(i)));
                }
            }
            this.gameNamesAndIndexes.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e("Firestore ERROR", "Could not fetch games: " + e.getMessage());
        });

    }

    public void handleSaveClick(View view){
        if(this.isSaving){
            return;
        }
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            webView.evaluateJavascript("getSavedState()", (s1) ->{
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    jsonObject.put("savedState", s1);
                    String name = nameInput.getText().toString();
                    GameObject game;
                    if(gameNamesAndIndexes.getItem(currentSelectedGame).getIndex() == -1){
                        game = new GameObject(name, jsonObject.toString(), this.gameType);
                    }else{
                        game = this.gameNamesAndIndexes.getItem(currentSelectedGame).getGameObject();
                        if(!game.getName().equals(name)){
                            //Save as a new game
                            game.setId(UUID.randomUUID().toString());
                            game.setName(name);
                        }
                        game.setGame(jsonObject.toString());
                    }
                    this.isSaving = true;
                    this.gameManager.saveGame(game).addOnSuccessListener(unused -> {
                        this.isSaving = false;
                        try {
                            populateGameNamesAndIndexes();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
                catch (JSONException e) {
                    Log.e("ERROR", e.toString());
                }
            });
        });
    }

    public void handleDeleteClick(View view){
        int selected = ((GameListItem) gameDropdown.getSelectedItem()).getIndex();
        if(selected == -1){
            clearAndCreateStandardBlocks(s -> {nameInput.setText("");});
            return;
        }
        selected = currentSelectedGame;
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{});
        gameManager.deleteGame(gameNamesAndIndexes.getItem(selected).getGameObject().getId()).addOnSuccessListener(unused -> {
            try {
                populateGameNamesAndIndexes();
                gameDropdown.setSelection(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    protected void clearAndCreateStandardBlocks(Consumer<String> callback){
        if(this.gameType.equals("Rule-Based")){
            this.setToolbox("mads", s ->
                    webView.evaluateJavascript("var workspace = Blockly.Workspace.getAll()[0];" +
                    " workspace.clear(); " +
                    " var config = workspace.newBlock('v2config');" +
                    " config.initSvg();" +
                    " config.render();",(s1)->{
                callback.accept(s1);
            }));

        }else if(this.gameType.equals("Image")){
            this.setToolbox("image", s ->
                    webView.evaluateJavascript("var workspace = Blockly.Workspace.getAll()[0];" +
                            " workspace.clear(); " +
                            " var config = workspace.newBlock('imgConfig');" +
                            " config.initSvg();" +
                            " config.render();",(s1)->{
                        callback.accept(s1);
                    }));
        }
        else{
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
    public void onGameStarted(View view) {
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.score_layout, null);
        builder.setView(popupView);
        scoreWindow = builder.show();
        scoreWindow.setOnDismissListener(dialogInterface -> scoreHandler.removeCallbacksAndMessages(null));
        Runnable r = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    renderScoreView();
                });
                scoreHandler.postDelayed(this, 100);
            }
        };
        scoreHandler.postDelayed(r, 100);
    }

    private void renderScoreView(){
        int size;
        if(this.activeGame instanceof BlocklyRuleGame){
            size = ((BlocklyRuleGame)this.activeGame).gameDef.getConfigBlock().getPlayers();
        }else if(this.activeGame instanceof BlocklyImageGame){
            size = ((BlocklyImageGame)this.activeGame).getConfig().getPlayers();
        }
        else{
            size = ((BlocklyGame)this.activeGame).gameDefinition.getGameBlock().getPlayers();
        }
        TextView t1 = scoreWindow.findViewById(R.id.p1_score_text);
        TextView t2 = scoreWindow.findViewById(R.id.p2_score_text);
        TextView t3 = scoreWindow.findViewById(R.id.p3_score_text);
        TextView t4 = scoreWindow.findViewById(R.id.p4_score_text);
        switch (size){
            case 1:
                t1.setText("Player 1\nScore: " + this.activeGame.getPlayerScore()[0]);
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                t4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                t1.setText("Player 1\nScore: " + this.activeGame.getPlayerScore()[0]);
                t2.setText("Player 2\nScore: " + this.activeGame.getPlayerScore()[1]);
                t3.setVisibility(View.INVISIBLE);
                t4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                t1.setText("Player 1\nScore: " + this.activeGame.getPlayerScore()[0]);
                t2.setText("Player 2\nScore: " + this.activeGame.getPlayerScore()[1]);
                t3.setText("Player 3\nScore: " + this.activeGame.getPlayerScore()[2]);
                t4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                t1.setText("Player 1\nScore: " + this.activeGame.getPlayerScore()[0]);
                t2.setText("Player 2\nScore: " + this.activeGame.getPlayerScore()[1]);
                t3.setText("Player 3\nScore: " + this.activeGame.getPlayerScore()[2]);
                t4.setText("Player 4\nScore: " + this.activeGame.getPlayerScore()[3]);
                break;
        }
    }

    @Override
    public void onGameStopped() {
        if(scoreWindow != null && scoreWindow.isShowing()){
            scoreWindow.dismiss();
        }
        scoreHandler.removeCallbacksAndMessages(null);
    }

    @Override
    Game getGame(JSONObject game) throws JSONException {
        if(this.gameType.equals("Rule-Based")){
            return new BlocklyRuleGame(game, () -> runOnUiThread(this::setGameStopped));
        }else if(this.gameType.equals("Image")){
            return new BlocklyImageGame(game, () -> runOnUiThread(this::setGameStopped));
        }
        return new BlocklyGame(game, () -> runOnUiThread(this::setGameStopped));
    }

}
