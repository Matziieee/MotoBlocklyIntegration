package com.google.blockly.android.webview.demo.Blocks.WhenThen.Then;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

public class ThenPlaySound extends ThenBlock {
    private String sound;
    private boolean isSpeak, isPlayerScore;
    private String text;
    private int player;

    public ThenPlaySound(JSONObject block) throws JSONException {
        super(block);
    }

    @Override
    public void execute(MotoConfigGameAPI api, @Nullable TilePressEvent tilePressEvent) {
        if(isSpeak){
            api.speak(text);
        } else if(isPlayerScore) {
            api.sayPlayerScore(player);
        }
        else{
            api.playSound(this.sound);
        }
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.isSpeak = json.getJSONObject("fields").has("text");
        this.isPlayerScore = json.getJSONObject("fields").has("player");
        if(isSpeak){
            this.text = json.getJSONObject("fields").getString("text");
        } else if(isPlayerScore) {
            this.player = json.getJSONObject("fields").getInt("player");
        }
        else{
            this.sound = json.getJSONObject("fields").getString("sound");
        }
    }
}
