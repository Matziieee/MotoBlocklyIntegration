package com.google.blockly.android.webview.demo.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyGame;
import com.google.blockly.android.webview.demo.BlocklyImageGame;
import com.google.blockly.android.webview.demo.BlocklyRuleGame;
import com.google.blockly.android.webview.demo.BlocklyTools.FirestoreGameManagerService;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONException;

public class AttemptChallengeActivity extends AppCompatActivity implements OnAntEventListener {

    Game game;
    boolean isPlaying = false;
    boolean isPrivateChallenge = false;
    String privateChallengeKey;
    boolean hasFinishedOnce = false;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempt_challenge);
        Button start = findViewById(R.id.challStartBtn);
        Button submit = findViewById(R.id.submitBtn);
        Handler handler = new Handler();

        Runnable gameStopper = new Runnable() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    start.setEnabled(true);
                    handler.removeCallbacksAndMessages(null);
                    isPlaying = false;
                    hasFinishedOnce = true;
                    score = game.getPlayerScore()[0];
                    submit.setEnabled(true);
                    submit.setVisibility(View.VISIBLE);
                });

            }
        };

        if(getIntent().getExtras().containsKey("privateChallengeKey")){
            this.isPrivateChallenge = true;
            this.privateChallengeKey = (String) getIntent().getExtras().get("privateChallengeKey");
        }
        GameObject gameObj = (GameObject) getIntent().getExtras().get("game");
        MotoConnection.getInstance().registerListener(this);
        try {
            if(gameObj.getType().equals("Rule-Based")){
                game = new BlocklyRuleGame(gameObj, gameStopper::run);
            }else if(gameObj.getType().equals("Image")){
                game = new BlocklyImageGame(gameObj, gameStopper::run);
            }else{
                game = new BlocklyGame(gameObj, gameStopper::run);
            }

            game.setSelectedGameType(0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        renderScoreView();
        start.setOnClickListener(v -> {
            game.startGame();
            start.setEnabled(false);
            submit.setEnabled(false);
            submit.setVisibility(View.INVISIBLE);

            Runnable r  = new Runnable() {
                @Override
                public void run() {
                    runOnUiThread( () ->  {
                        renderScoreView();
                    });
                    handler.postDelayed(this, 100);
                }
            };
            handler.post(r);
        });
        submit.setOnClickListener(v -> {
            FirestoreGameManagerService manager = new FirestoreGameManagerService(this);
            if(isPrivateChallenge){
                manager.postPrivateHighscore(gameObj.getId(), score, this.privateChallengeKey).addOnSuccessListener(d -> {
                   setResult(Activity.RESULT_OK);
                   finish();
                });
            }
            else{
                manager.postHighscore(gameObj.getId(), score).addOnSuccessListener(d -> {
                    setResult(Activity.RESULT_OK);
                    finish();
                });
            }
        });
    }


    private void renderScoreView(){
        int size;
        if(this.game instanceof BlocklyRuleGame){
            size = ((BlocklyRuleGame)this.game).gameDef.getConfigBlock().getPlayers();
        }
        else if(this.game instanceof BlocklyImageGame) {
            size = ((BlocklyImageGame)this.game).getConfig().getPlayers();
        } else{
            size = ((BlocklyGame)this.game).gameDefinition.getGameBlock().getPlayers();
        }
        TextView t1 = findViewById(R.id.p1_score_text);
        TextView t2 = findViewById(R.id.p2_score_text);
        TextView t3 = findViewById(R.id.p3_score_text);
        TextView t4 = findViewById(R.id.p4_score_text);
        switch (size){
            case 1:
                t1.setText("Player 1\nScore: " + this.game.getPlayerScore()[0]);
                t2.setVisibility(View.INVISIBLE);
                t3.setVisibility(View.INVISIBLE);
                t4.setVisibility(View.INVISIBLE);
                break;
            case 2:
                t1.setText("Player 1\nScore: " + this.game.getPlayerScore()[0]);
                t2.setText("Player 2\nScore: " + this.game.getPlayerScore()[1]);
                t3.setVisibility(View.INVISIBLE);
                t4.setVisibility(View.INVISIBLE);
                break;
            case 3:
                t1.setText("Player 1\nScore: " + this.game.getPlayerScore()[0]);
                t2.setText("Player 2\nScore: " + this.game.getPlayerScore()[1]);
                t3.setText("Player 3\nScore: " + this.game.getPlayerScore()[2]);
                t4.setVisibility(View.INVISIBLE);
                break;
            case 4:
                t1.setText("Player 1\nScore: " + this.game.getPlayerScore()[0]);
                t2.setText("Player 2\nScore: " + this.game.getPlayerScore()[1]);
                t3.setText("Player 3\nScore: " + this.game.getPlayerScore()[2]);
                t4.setText("Player 4\nScore: " + this.game.getPlayerScore()[3]);
                break;
        }
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