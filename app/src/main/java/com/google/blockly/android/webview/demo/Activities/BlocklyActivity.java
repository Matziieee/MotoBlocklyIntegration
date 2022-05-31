package com.google.blockly.android.webview.demo.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.blocklywebview.R;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.Consumer;

public abstract class BlocklyActivity extends AppCompatActivity implements OnAntEventListener {

    protected WebView webView;
    protected DrawerLayout drawerLayout;
    private ImageButton closeSidebarBtn;

    private Button startStopButton;
    private boolean isGameRunning = false;
    protected boolean isSideBarOpen = false;

    abstract void onBlocklyLoaded();
    abstract Game getGame(JSONObject jsonGame) throws JSONException;

    protected Game activeGame;
    protected Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blockly);

        this.webView = this.findViewById(R.id.blockly_webview);

        this.webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onBlocklyLoaded();
            }
        });
        this.startStopButton = findViewById(R.id.startGameBtn);
        this.drawerLayout = this.findViewById(R.id.drawerLayout);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.handler = new Handler(Looper.getMainLooper());
        MotoConnection.getInstance().registerListener(this);
    }

    protected void loadGame(JSONObject game){
        webView.evaluateJavascript("Blockly.Workspace.getAll()[0].clear()",(s)->{
            webView.evaluateJavascript("var tmpblocks = " + game.toString() + "; isLoading = true;", (s2)->{
                try {
                    webView.evaluateJavascript("setSavedState(" + game.getString("savedState") + ")", v -> {
                        webView.evaluateJavascript("Blockly.serialization.workspaces" +
                                ".load(tmpblocks,Blockly.Workspace.getAll()[0]); knownIds = [];", (s3)->{
                        });
                    });
                } catch (JSONException ex) {
                    Log.e("ERROR", ex.toString());
                }
            });
        });
    }

    protected void setToolbox(String toolbox, Consumer<String> callback){
        toolbox = "image";
        webView.evaluateJavascript(""+
                        "var workspace = Blockly.Workspace.getAll()[0];" +
                        "workspace.updateToolbox(BLOCKLY_TOOLBOX_XML['" + toolbox +"']);"
                , s -> {
                    callback.accept(s);
                });
    }

    protected void openSidebar(){
        this.isSideBarOpen = true;
        this.drawerLayout.openDrawer(GravityCompat.END);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
        this.closeSidebarBtn = this.findViewById(R.id.closeSidebarBtn);
        this.closeSidebarBtn.setOnClickListener(v -> closeSidebar());
    }

    protected void closeSidebar(){
        this.isSideBarOpen = false;
        this.drawerLayout.closeDrawer(GravityCompat.END);
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        this.closeSidebarBtn = null;

    }

    public void handleSettingsClick(View view){
        openSidebar();
    }

    public void setGameStopped(){
        this.activeGame = null;
        this.isGameRunning = false;
        startStopButton.setText("Start Game");
        this.onGameStopped();
    }

    public void handlePlayClick(View view){
        if(this.isGameRunning){
            this.activeGame.stopGame();
            setGameStopped();
            return;
        }
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            try {
                JSONObject jsonObject = new JSONObject(s);
                //Parse the game
                this.activeGame = this.getGame(jsonObject);
                Log.i("Game loaded", jsonObject.toString());

                //Start the game
                this.activeGame.setSelectedGameType(0);
                this.activeGame.startGame();
                startStopButton.setText("Stop Game");
                this.isGameRunning = true;
                this.onGameStarted(view);
            }
            catch (JSONException e) {
                //todo show pop-up with error message
                Log.e("ERROR", e.toString());
                this.isGameRunning = false;
                startStopButton.setText("Start Game");
            }
        });

    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        if(this.activeGame != null){
            System.out.println("ADDING EVENT");
            this.activeGame.addEvent(bytes);
        }
    }
    public abstract void onGameStarted(View view);
    public abstract void onGameStopped();

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
