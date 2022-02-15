package com.google.blockly.android.webview.demo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * The primary activity of the demo application. The activity embeds the
 * {@link com.google.blockly.android.webview.BlocklyWebViewFragment}.
 */
public class BlocklyActivity extends AppCompatActivity {

    private WebView webView;
    private Spinner spinner;
    private ArrayAdapter<Pair<String, Integer>> gameNamesAndIndexes;
    private BlocklyGamesStore store = BlocklyGamesStore.getInstance();
    private EditText nameInput;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.webView = this.findViewById(R.id.blockly_webview);
        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                initDropdown();
            }
        });
        this.nameInput = this.findViewById(R.id.gameNameInput);
        this.spinner = this.findViewById(R.id.gameSelectDropdown);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDropdown(){
        Context context = this;
        this.gameNamesAndIndexes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        this.spinner.setAdapter(this.gameNamesAndIndexes);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Load clicked game
                try {
                    if(gameNamesAndIndexes.getItem(i).second == -1){
                        webView.evaluateJavascript("" +
                                "var workspace = Blockly.Workspace.getAll()[0];" +
                                " var config = workspace.newBlock('gameblock');" +
                                " config.initSvg();" +
                                " config.render();" +
                                " var type = workspace.newBlock('gametype');" +
                                " type.initSvg();" +
                                " type.render();" +
                                " var parentConnection = config.getInput('gameType').connection;" +
                                " var childConnection = type.outputConnection;" +
                                " parentConnection.connect(childConnection);", (s)->{
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
        try {
            this.populateGamNamesAndIndexes();

        } catch (IOException | JSONException e) {
            Log.e("ERROR", e.toString());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onStart() {
        super.onStart();

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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateGamNamesAndIndexes() throws IOException, JSONException {
        this.gameNamesAndIndexes.clear();
        JSONArray games = store.getGames(this);
        //Always add "NONE" as first
        this.gameNamesAndIndexes.add(new Pair<>("NONE", -1));
        for (int i = 0; i < games.length(); i++) {
            String name = games.getJSONObject(i).getString("name");
            this.gameNamesAndIndexes.add(new Pair<>(name, i));
        }
        this.gameNamesAndIndexes.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void handleSaveClick(View view){
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                BlocklyGamesStore.getInstance().saveGame(this, jsonObject, nameInput.getText().toString());
                populateGamNamesAndIndexes();
            }
            catch (JSONException | IOException e) {
                Log.e("ERROR", e.toString());
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void handleDeleteClick(View view) throws IOException, JSONException {
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
        });
        int selected = ((Pair<String,Integer>)spinner.getSelectedItem()).second;
        if(selected == -1){
            return;
        }
        store.deleteGame(this, selected);
        populateGamNamesAndIndexes();
        spinner.setSelection(0);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void handleCreateClick(View view){
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{
            nameInput.setText("New Game");
            spinner.setSelection(0);
        });

    }

}
