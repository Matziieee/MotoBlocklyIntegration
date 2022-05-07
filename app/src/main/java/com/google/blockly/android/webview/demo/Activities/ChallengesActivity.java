package com.google.blockly.android.webview.demo.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.FirestoreGameManagerService;
import com.google.blockly.android.webview.demo.GameObject;
import com.google.blockly.android.webview.demo.Highscore;

public class ChallengesActivity extends AppCompatActivity {

    private FirestoreGameManagerService gameManagerService;
    private ListView gameList, highscores;
    private LinearLayout linearLayout;
    private ArrayAdapter<GameObject> games;
    private ArrayAdapter<Highscore> highscoreAdapter;
    private TextView text;
    private GameObject currentGame;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        gameManagerService =  new FirestoreGameManagerService(this);
        gameList = findViewById(R.id.challengeGameList);
        highscores = findViewById(R.id.challengeHSList);
        text = findViewById(R.id.challengeTextView);
        games = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        highscoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        linearLayout = findViewById(R.id.backPlayLayout);
        button = findViewById(R.id.challengeBackBtn);
        gameList.setAdapter(games);
        highscores.setAdapter(highscoreAdapter);
        button.setOnClickListener(v -> {
            onBackBtnPressed();
        });
        gameList.setOnItemClickListener((adapterView, view, i, l) -> {
            gameManagerService.getHighscores(games.getItem(i).getId()).addOnSuccessListener(docs -> {
                docs.toObjects(Highscore.class).forEach(doc -> highscoreAdapter.add(doc));
                if(highscoreAdapter.getCount() == 0){
                    findViewById(R.id.noHighscoresText).setVisibility(View.VISIBLE);
                }else{
                    this.highscores.setVisibility(View.VISIBLE);
                }
                highscoreAdapter.notifyDataSetChanged();
                this.linearLayout.setVisibility(View.VISIBLE);

                this.gameList.setVisibility(View.GONE);
                this.text.setVisibility(View.GONE);
                this.currentGame = games.getItem(i);
            });
        });
        findViewById(R.id.playChallengeBtn).setOnClickListener(v -> {
            Intent i = new Intent(this, AttemptChallengeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("game", this.currentGame);
            i.putExtras(bundle);
            startActivityForResult(i, 1111);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            //Refresh...
            this.highscoreAdapter.clear();
            gameManagerService.getHighscores(currentGame.getId()).addOnSuccessListener(docs -> {
                docs.toObjects(Highscore.class).stream().sorted((h1, h2) -> {
                    if(h1.getScore() > h2.getScore()){
                        return 1;
                    }
                    return 0;
                }).forEach(doc -> highscoreAdapter.add(doc));
                if(highscoreAdapter.getCount() == 0){
                    findViewById(R.id.noHighscoresText).setVisibility(View.VISIBLE);
                }else{
                    this.highscores.setVisibility(View.VISIBLE);
                    findViewById(R.id.noHighscoresText).setVisibility(View.INVISIBLE);
                }
                highscoreAdapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        gameManagerService.getPublishedGames().addOnSuccessListener(docs -> {
            docs.toObjects(GameObject.class).forEach(doc -> games.add(doc));
            games.notifyDataSetChanged();
        });
    }

    private void onBackBtnPressed(){
        this.highscoreAdapter.clear();
        this.highscoreAdapter.notifyDataSetChanged();
        this.linearLayout.setVisibility(View.GONE);
        this.highscores.setVisibility(View.GONE);
        findViewById(R.id.noHighscoresText).setVisibility(View.GONE);
        findViewById(R.id.noHighscoresText).setVisibility(View.GONE);
        this.gameList.setVisibility(View.VISIBLE);
        this.text.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(this.linearLayout.getVisibility() == View.VISIBLE){
            onBackBtnPressed();
        }
        else{
            super.onBackPressed();
        }
    }
}