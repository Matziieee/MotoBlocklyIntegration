package com.google.blockly.android.webview.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.blocklywebview.R;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

public class Main2Activity extends AppCompatActivity implements OnAntEventListener {

    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    TextView statusTextView;
    Button pairingButton, playGameBtn, createGameBtn;
    boolean isPairing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        connection.startMotoConnection(Main2Activity.this);
        connection.saveRfFrequency(66);
        connection.setDeviceId(43);
        connection.registerListener(Main2Activity.this);

        statusTextView = findViewById(R.id.statusTextView);
        pairingButton = findViewById(R.id.pairingButton);
        playGameBtn = findViewById(R.id.playGameBtn);
        createGameBtn = findViewById(R.id.createGameBtn);

        playGameBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, BlocklyGameActivity.class);
            startActivity(i);
        });
        createGameBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, BlocklyActivity.class);
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
}