package com.google.blockly.android.webview.demo.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyRuleGame;
import com.google.blockly.android.webview.demo.BlocklyTools.FirestoreGameManagerService;
import com.google.blockly.android.webview.demo.GameObject;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONException;

public class AttemptChallengeActivity extends AppCompatActivity implements OnAntEventListener {

    TextView scoreText;
    BlocklyRuleGame game;
    boolean isPlaying = false;
    boolean hasFinishedOnce = false;
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_challenge);
        scoreText = findViewById(R.id.scoreText);
        Button start = findViewById(R.id.challStartBtn);
        Button submit = findViewById(R.id.submitBtn);
        Handler handler = new Handler();
        GameObject gameObj = (GameObject) getIntent().getExtras().get("game");
        MotoConnection.getInstance().registerListener(this);
        try {
            game = new BlocklyRuleGame(gameObj, () -> {
                runOnUiThread(() -> {
                    //Do something..
                    start.setEnabled(true);
                    handler.removeCallbacksAndMessages(null);
                    isPlaying = false;
                    hasFinishedOnce = true;
                    score = game.getPlayerScore()[0];
                    submit.setEnabled(true);
                    submit.setVisibility(View.VISIBLE);
                });
            });
            game.setSelectedGameType(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        start.setOnClickListener(v -> {
            game.startGame();
            start.setEnabled(false);
            submit.setEnabled(false);
            submit.setVisibility(View.INVISIBLE);

            Runnable r  = new Runnable() {
                @Override
                public void run() {
                    runOnUiThread( () ->  {
                        scoreText.setText("Your Score: " + game.getPlayerScore()[0]);
                    });
                    handler.postDelayed(this, 100);
                }
            };
            handler.post(r);
        });
        submit.setOnClickListener(v -> {
            FirestoreGameManagerService manager = new FirestoreGameManagerService(this);
            manager.postHighscore(gameObj.getId(), score).addOnSuccessListener(d -> {
                setResult(Activity.RESULT_OK);
                finish();
            });
        });
    }

    @Override
    public void onBackPressed() {
        if (!isPlaying) {
            setResult(Activity.RESULT_CANCELED);
            finish();
        }

    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        game.addEvent(bytes);
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
        MotoConnection.getInstance().unregisterListener(this);
    }
}