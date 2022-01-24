package com.google.blockly.android.webview.demo;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameDefinition;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BlocklyGameActivity extends AppCompatActivity implements OnAntEventListener {
    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    BlocklyGame game;
    TableLayout layout;
    ArrayList<BlocklyGame> games = new ArrayList<>();
    Button startButton;

    public BlocklyGameActivity() throws JSONException {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockly_game2);

        connection.registerListener(this);
        connection.setAllTilesToInit();
        //sound.initializeSounds(this);
        layout = findViewById(R.id.table);
        startButton = findViewById(R.id.blocklyStartbtn);
        startButton.setEnabled(false);

        startButton.setOnClickListener(v -> {
            this.game.setSelectedGameType(0);
            this.game.startGame();
        });


        try {
            JSONArray array = BlocklyGamesStore.getInstance().getGames(this);
            for (int i = 0; i < array.length(); i++) {
                try {
                    BlocklyGame game = new BlocklyGame(array.getJSONObject(i));
                    this.games.add(game);
                }catch (JSONException e){
                    this.games.add(null);
                }
            }

            this.games.forEach(g -> {
                TableRow r = new TableRow(this);
                Button b = new Button(this);
                //todo introduce 'Name' to config block
                if(g == null){
                    b.setText("Failed to parse");
                    b.setEnabled(false);
                }else{
                    b.setText("Saved Game");
                    b.setOnClickListener(v -> {
                        this.game = g;
                        this.startButton.setEnabled(true);
                    });
                }
                r.addView(b);
                layout.addView(r);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        this.game.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }
}