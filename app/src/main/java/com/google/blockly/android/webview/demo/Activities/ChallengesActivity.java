package com.google.blockly.android.webview.demo.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blocklywebview.R;
import com.google.android.material.tabs.TabLayout;
import com.google.blockly.android.webview.demo.BlocklyTools.FirestoreGameManagerService;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.google.blockly.android.webview.demo.Online.Highscore;
import com.google.blockly.android.webview.demo.Online.PrivateChallenge;
import com.google.blockly.android.webview.demo.Online.PublishedGame;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class ChallengesActivity extends AppCompatActivity {

    private FirestoreGameManagerService gameManagerService;
    private ListView gameList, highscores, privateChallengesView;
    private LinearLayout backPlayLayout, privateLayout;
    private ArrayAdapter<GameObject> games;
    private ArrayAdapter<PrivateChallenge> privateChallenges;
    private ArrayAdapter<Highscore> highscoreAdapter;
    private TextView text;
    private GameObject currentGame;
    private Button backBtn;

    private TabLayout tabLayout;
    private Button joinBtn, createBtn;
    private ArrayList<GameObject> initLoadedPublicGames;
    private ArrayAdapter<GameObject> dropDownAdapter;
    private PrivateChallenge currentChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenges);
        gameManagerService =  new FirestoreGameManagerService(this);
        gameList = findViewById(R.id.challengeGameList);
        highscores = findViewById(R.id.challengeHSList);
        text = findViewById(R.id.challengeTextView);
        privateChallengesView = findViewById(R.id.privateChallengesView);
        privateChallenges = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        privateChallengesView.setAdapter(privateChallenges);
        games = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        highscoreAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        dropDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        backPlayLayout = findViewById(R.id.backPlayLayout);
        backBtn = findViewById(R.id.challengeBackBtn);
        gameList.setAdapter(games);
        highscores.setAdapter(highscoreAdapter);
        backBtn.setOnClickListener(v -> {
            onBackBtnPressed();
        });
        initLoadedPublicGames = new ArrayList<>();
        privateChallengesView.setOnItemClickListener((adapterView, view, i, l) -> {
            tabLayout.setVisibility(View.GONE);
            this.gameList.setVisibility(View.GONE);
            this.text.setVisibility(View.GONE);
            privateLayout.setVisibility(View.GONE);
            gameManagerService.getPrivateHighscores(privateChallenges.getItem(i).getKey()).addOnSuccessListener(docs -> {
                updateHighscores(i, docs);
            });
        });
        gameList.setOnItemClickListener((adapterView, view, i, l) -> {
            tabLayout.setVisibility(View.GONE);
            this.gameList.setVisibility(View.GONE);
            this.text.setVisibility(View.GONE);
            gameManagerService.getHighscores(games.getItem(i).getId()).addOnSuccessListener(docs -> {
                updateHighscores(i, docs);
            });

        });
        findViewById(R.id.playChallengeBtn).setOnClickListener(v -> {
            Intent i = new Intent(this, AttemptChallengeActivity.class);
            Bundle bundle = new Bundle();
            if(tabLayout.getSelectedTabPosition() == 0){
                bundle.putSerializable("game", this.currentGame);
                i.putExtras(bundle);
                startActivityForResult(i, 1111);
            }else{
                //async hell :^)
                gameManagerService.getGame(currentChallenge.getGameId()).addOnSuccessListener(doc -> {
                    bundle.putSerializable("game", doc.toObject(PublishedGame.class));
                    bundle.putSerializable("privateChallengeKey", this.currentChallenge.getKey());
                    i.putExtras(bundle);
                    startActivityForResult(i, 1111);
                });
            }
        });

        privateLayout = findViewById(R.id.privateChallengeLayout);
        tabLayout = findViewById(R.id.challengesTabs);
        joinBtn = findViewById(R.id.joinChallengeBtn);
        createBtn = findViewById(R.id.createChallengeBtn);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateGames(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Do Nothing
            }
        });
        joinBtn.setOnClickListener( v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Join existing challenge");
            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setMessage("Enter invite key to join challenge");

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> gameManagerService.joinExistingGame(input.getText().toString())
                    .addOnSuccessListener(g -> {
                        Toast.makeText(this, "Successfully joined challenge", Toast.LENGTH_SHORT).show();
                        updateGames(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));
                    }).addOnFailureListener(g -> {
                        Toast.makeText(this, "Could not join challenge", Toast.LENGTH_SHORT).show();
                    })
            );
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
        createBtn.setOnClickListener(v -> {
            AlertDialog.Builder builder =  new AlertDialog.Builder(this);
            builder.setTitle("Create new Challenge");
            builder.setMessage("Input a key other will use to join, and select which game you want to make a challenge for..");

            View viewInflated = LayoutInflater.from(this).inflate(R.layout.create_challenge_view, findViewById(android.R.id.content), false);
            EditText input = viewInflated.findViewById(R.id.keyInput);
            input.setText(generateRandomKey());
            Spinner gameSpinner = viewInflated.findViewById(R.id.createChallengeSelector);
            gameSpinner.setAdapter(dropDownAdapter);
            dropDownAdapter.clear();
            dropDownAdapter.addAll(this.initLoadedPublicGames);
            dropDownAdapter.notifyDataSetChanged();

            builder.setView(viewInflated);
            builder.setPositiveButton("Create", (dialog, which) -> {
                PrivateChallenge pc =  new PrivateChallenge(
                        input.getText().toString(),
                        gameManagerService.getDeviceId(),
                        ((GameObject)(gameSpinner).getSelectedItem())
                );
                gameManagerService.createPrivateChallenge(pc)
                        .addOnSuccessListener(res -> {
                            Toast.makeText(this, "Successfully created challenge", Toast.LENGTH_SHORT).show();
                            updateGames(tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));
                        })
                        .addOnFailureListener(res -> {
                            Toast.makeText(this, "Could not create challenge", Toast.LENGTH_SHORT).show();
                        });
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();


        });
    }

    private void updateHighscores(int i, QuerySnapshot docs) {
        docs.toObjects(Highscore.class).stream().sorted(Comparator.comparingInt(Highscore::getScore).reversed()).forEach(doc -> highscoreAdapter.add(doc));
        if (highscoreAdapter.getCount() == 0) {
            findViewById(R.id.noHighscoresText).setVisibility(View.VISIBLE);
        } else {
            this.highscores.setVisibility(View.VISIBLE);
        }
        highscoreAdapter.notifyDataSetChanged();
        this.backPlayLayout.setVisibility(View.VISIBLE);
        if(i != -1){
            if(tabLayout.getSelectedTabPosition() == 0){
                this.currentGame = games.getItem(i);
            }else{
                this.currentChallenge = privateChallenges.getItem(i);
            }
        }
    }

    private void updateGames(TabLayout.Tab tab){
        games.clear();
        text.setText("Fetching..");
        int pos = tab.getPosition();
        if(pos == 0){
            //Public
            gameManagerService.getPublishedGames().addOnSuccessListener(docs -> {
                this.initLoadedPublicGames.clear();
                docs.toObjects(PublishedGame.class).forEach(doc -> games.add(doc));
                initLoadedPublicGames.addAll(docs.toObjects(PublishedGame.class));
                games.notifyDataSetChanged();
                text.setText("Click a game to view leaderboard and attempt challenge");
            });
            gameList.setVisibility(View.VISIBLE);
            privateLayout.setVisibility(View.GONE);
            privateChallengesView.setVisibility(View.GONE);
        }
        else{
            //Private
            gameList.setVisibility(View.GONE);
            privateChallengesView.setVisibility(View.VISIBLE);
            privateLayout.setVisibility(View.VISIBLE);
            privateChallenges.clear();

            gameManagerService.getPrivateChallenges().addOnSuccessListener(docs -> {
                docs.toObjects(PrivateChallenge.class).forEach(pc ->{
                    privateChallenges.add(pc);
                });

                if(privateChallenges.getCount() == 0){
                    text.setText("No games found, join a challenge or create a new one!");
                }else{
                    text.setText("Click a game to view leaderboard and attempt challenge");
                }
                privateChallenges.notifyDataSetChanged();
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            //Refresh...
            this.highscoreAdapter.clear();
            if(tabLayout.getSelectedTabPosition() == 0){
                gameManagerService.getHighscores(currentGame.getId()).addOnSuccessListener(docs -> {
                    updateHighscores(-1, docs);
                });
            }else{
                gameManagerService.getHighscores(currentChallenge.getGameId()).addOnSuccessListener(docs -> {
                    updateHighscores(-1, docs);
                });
            }

        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        updateGames(this.tabLayout.getTabAt(tabLayout.getSelectedTabPosition()));
    }

    private String generateRandomKey(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 5;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    private void onBackBtnPressed(){
        this.highscoreAdapter.clear();
        this.highscoreAdapter.notifyDataSetChanged();
        this.backPlayLayout.setVisibility(View.GONE);
        this.highscores.setVisibility(View.GONE);
        findViewById(R.id.noHighscoresText).setVisibility(View.GONE);
        findViewById(R.id.noHighscoresText).setVisibility(View.GONE);

        if(tabLayout.getSelectedTabPosition() == 1){
            privateLayout.setVisibility(View.VISIBLE);
            privateChallengesView.setVisibility(View.VISIBLE);
        }else{
            this.gameList.setVisibility(View.VISIBLE);
        }
        tabLayout.setVisibility(View.VISIBLE);

        this.text.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if(this.backPlayLayout.getVisibility() == View.VISIBLE){
            onBackBtnPressed();
        }
        else{
            super.onBackPressed();
        }
    }
}