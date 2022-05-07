package com.google.blockly.android.webview.demo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blocklywebview.R;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

public class HomeActivity extends AppCompatActivity implements OnAntEventListener {
    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    TextView statusTextView;
    Button pairingButton, practiceBtn, createGameBtn, gameCollectionBtn;
    boolean isPairing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        connection.startMotoConnection(HomeActivity.this);
        connection.saveRfFrequency(66);
        connection.setDeviceId(43);
        connection.registerListener(HomeActivity.this);

        statusTextView = findViewById(R.id.statusTextView);
        pairingButton = findViewById(R.id.pairingButton);
        practiceBtn = findViewById(R.id.practiceBtn);
        createGameBtn = findViewById(R.id.createGameBtn);
        gameCollectionBtn = findViewById(R.id.gameCollectionBtn);
        findViewById(R.id.challengesBtn).setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, ChallengesActivity.class);
            startActivity(i);
        });

        practiceBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, PracticeGamesActivity.class);
            startActivity(i);
        });

        createGameBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, CreateGamesActivity.class);
            startActivity(i);
        });

        gameCollectionBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, GameCollectionActivity.class);
            startActivity(i);
        });

        pairingButton.setOnClickListener(view -> {
            if(!isPairing){
                connection.pairTilesStart();
                pairingButton.setText("Stop pairing!");
            }else{
                connection.pairTilesStop();
                pairingButton.setText("Start pairing!");
            }
            isPairing = !isPairing;
        });
        sound.initializeSounds(this);
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {

    }

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(final int i) {
        runOnUiThread(() -> statusTextView.setText(i +" connected tiles"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        connection.registerListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }

    /* private void addStandardGamesIfMissing() throws JSONException, IOException {

        JSONArray arr = new JSONArray(standardGames);
        JSONArray games = BlocklyGamesStore.getInstance().getGamesJsonArray(this);
        for (int i = 0; i < arr.length(); i++) {
            String name = arr.getJSONObject(i).getString("name");
            boolean exists = false;
            for (int j = 0; j < games.length(); j++) {
                if(games.getJSONObject(j).getString("name").equals(name)){
                    exists = true;
                }
            }
            if(!exists){
                BlocklyGamesStore.getInstance().saveGame(
                        this,
                        arr.getJSONObject(i).getJSONObject("game"),
                        name,
                        arr.getJSONObject(i).getBoolean("isRuleBased"));
            }
        }

    }*/
}