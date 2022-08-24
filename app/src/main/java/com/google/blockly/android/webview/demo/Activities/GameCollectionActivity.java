package com.google.blockly.android.webview.demo.Activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.blocklywebview.R;
import com.google.android.material.tabs.TabLayout;
import com.google.blockly.android.webview.demo.Adapters.GameObjectAdapter;
import com.google.blockly.android.webview.demo.BlocklyTools.FirestoreGameManagerService;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.google.blockly.android.webview.demo.Online.PublishedGame;

import java.util.ArrayList;
import java.util.UUID;

public class GameCollectionActivity extends AppCompatActivity {

    private TextView statusText;
    private ListView myGamesListView, allGamesListView;
    private TabLayout tabLayout;
    private FirestoreGameManagerService gameManagerService;
    private GameObjectAdapter myGamesAdapter;
    private GameObjectAdapter publishedGameAdapter;
    private int currentTab = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_collection);
        statusText = findViewById(R.id.gameCollectionStatusText);
        myGamesListView = findViewById(R.id.myGamesList);
        allGamesListView = findViewById(R.id.allGamesList);
        tabLayout = findViewById(R.id.gameCollectionTabs);
        findViewById(R.id.gamesBackBtn).setOnClickListener(v -> this.finish());
        this.gameManagerService = new FirestoreGameManagerService(this);
        myGamesAdapter = new GameObjectAdapter(this, new ArrayList<>());
        publishedGameAdapter = new GameObjectAdapter(this, new ArrayList<>());
        myGamesListView.setAdapter(myGamesAdapter);
        allGamesListView.setAdapter(publishedGameAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                currentTab = tab.getPosition();
                if(currentTab == 0){
                    allGamesListView.setVisibility(View.GONE);
                    myGamesListView.setVisibility(View.VISIBLE);
                }else{
                    allGamesListView.setVisibility(View.VISIBLE);
                    myGamesListView.setVisibility(View.GONE);
                    currentTab = 1;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Do nothing
            }
        });
        myGamesListView.setOnItemClickListener((adapterView, view, i, l) -> {
            if(!myGamesAdapter.getItem(i).isPublished()){
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Do you want to publish this game?")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton("Publish Game", (dialog, v1) -> {
                            gameManagerService.publishGame(myGamesAdapter.getItem(i)).addOnSuccessListener(v -> {
                                Toast.makeText(this, "Successfully published game", Toast.LENGTH_SHORT).show();
                                updateGames();
                            }).addOnFailureListener(v -> {
                                Toast.makeText(this, "Could not publish game", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .show();
            }else{
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Do you want to make this game private?")
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton("Make Game Private", (dialog, v1) -> {
                            gameManagerService.makeGamePrivate(myGamesAdapter.getItem(i)).addOnSuccessListener(v -> {
                                Toast.makeText(this, "Successfully made game private", Toast.LENGTH_SHORT).show();
                                updateGames();
                            }).addOnFailureListener(v -> {
                                Toast.makeText(this, "Could not make game private", Toast.LENGTH_SHORT).show();
                            });
                        })
                        .show();
            }
        });
        allGamesListView.setOnItemClickListener((adapterView, view, i, l) -> {
            GameObject game = publishedGameAdapter.getItem(i);
            if(game.getUserId().equals(gameManagerService.getDeviceId())){
                return;
            }
            new AlertDialog.Builder(view.getContext())
                    .setTitle("Save this game?")
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton("Save Game", (dialogInterface, i1) -> {
                        game.setPublished(false);
                        game.setOriginalId(game.getUserId());
                        game.setUserId(gameManagerService.getDeviceId());
                        game.setId(UUID.randomUUID().toString());
                        gameManagerService.saveGame(game);
                    })
                    .show();
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        updateGames();
    }

    private void updateGames(){
        myGamesAdapter.clear();
        publishedGameAdapter.clear();
        gameManagerService.getMyGames().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.toObjects(GameObject.class).forEach(doc -> {
                myGamesAdapter.add(doc);
            });
            statusText.setVisibility(View.GONE);
            myGamesAdapter.notifyDataSetChanged();
        });
        gameManagerService.getPublishedGames().addOnSuccessListener( queryDocumentSnapshots -> {
            queryDocumentSnapshots.toObjects(PublishedGame.class).forEach(doc -> {
                publishedGameAdapter.add(doc);
            });
            statusText.setVisibility(View.GONE);
            publishedGameAdapter.notifyDataSetChanged();
        });
    }
}