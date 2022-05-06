package com.google.blockly.android.webview.demo.BlocklyTools;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class BlocklyGamesStore {
    private static final BlocklyGamesStore store;

    static {
        store = new BlocklyGamesStore();
    }

    private BlocklyGamesStore(){
    }

    public static BlocklyGamesStore getInstance(){
        return store;
    }


    public void deleteGame(Context context, int index) throws IOException, JSONException {
        JSONArray games = this.getGames(context);
        games.remove(index);
        this.writeGames(context, games);
    }

    private void writeGames(Context context, JSONArray games) throws IOException {
        FileOutputStream fo = context.openFileOutput("games.json", Context.MODE_PRIVATE);
        fo.write(games.toString().getBytes());
    }

    public void saveGame(Context context, JSONObject game, String name, boolean isRuleBased) throws IOException, JSONException {
        JSONArray games = this.getGames(context);
        this.findAndRemoveExisting(games, name);
        JSONObject object = new JSONObject();
        object.put("name", name);
        object.put("isRuleBased", isRuleBased);
        object.put("game", game);
        games.put(object);
        this.writeGames(context, games);


    }

    private void findAndRemoveExisting(JSONArray games, String name) throws JSONException {
        int toRemove = -1;
        for (int i = 0; i < games.length(); i++) {
            JSONObject obj = games.getJSONObject(i);
            if(obj.getString("name").equals(name)){
                toRemove = i;
                break;
            }
        }
        games.remove(toRemove);
    }

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

    private void createFilesDirIfDoesNotExist(Context context) throws IOException {
        if(Arrays.stream(Objects.requireNonNull(context.getFilesDir().listFiles())).noneMatch((file -> file.getName().equals("games.json")))){
            String fileName = "games.json";
            String content = "[]";
            FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(content.getBytes());
        }
    }
}
