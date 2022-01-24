package com.google.blockly.android.webview.demo;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.webView = this.findViewById(R.id.blockly_webview);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void handleSaveClick(View view){
        Log.i("A", "ello");
        webView.evaluateJavascript("Blockly.serialization.workspaces.save(Blockly.Workspace.getAll()[0])", (s) -> {
            Log.i("B", "ello2");

            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("C", jsonObject.toString());
                BlocklyGamesStore.getInstance().saveGame(this, jsonObject);
            }
            catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        });
    }

}
