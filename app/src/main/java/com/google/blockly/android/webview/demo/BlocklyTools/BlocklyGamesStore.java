package com.google.blockly.android.webview.demo.BlocklyTools;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class BlocklyGamesStore {

    private static final BlocklyGamesStore store;

    private HashMap<String, JSONObject> games = new HashMap<>();

    static {
        store = new BlocklyGamesStore();
    }

    private BlocklyGamesStore(){
    }


    public static BlocklyGamesStore getInstance(){
        return store;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveGame(Context context, JSONObject game) throws IOException, JSONException {
        JSONArray games = this.getGames(context);
        games.put(game);
        FileOutputStream fo = context.openFileOutput("games.json", Context.MODE_PRIVATE);
        fo.write(games.toString().getBytes());


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public JSONArray getGames(Context context) throws IOException, JSONException {
        this.createFilesDirIfDoesNotExist(context);
        FileInputStream fi = context.openFileInput("games.json");
        InputStreamReader inputStreamReader = new InputStreamReader(fi, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
            stringBuilder.append(line).append('\n');
            line = reader.readLine();
        }
        fi.close();
        JSONArray obj = new JSONArray(stringBuilder.toString());

        return obj;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createFilesDirIfDoesNotExist(Context context) throws IOException {
        if(Arrays.stream(Objects.requireNonNull(context.getFilesDir().listFiles())).noneMatch((file -> file.getName().equals("games.json")))){
            String fileName = "games.json";
            String content = "[]";
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(content.getBytes());
        }
    }
}
