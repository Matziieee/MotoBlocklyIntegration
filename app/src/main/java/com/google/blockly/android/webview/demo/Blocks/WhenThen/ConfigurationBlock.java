package com.google.blockly.android.webview.demo.Blocks.WhenThen;

import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenBlock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConfigurationBlock extends AbstractBlock {

    private ArrayList<WhenBlock> whenBlocks;
    private int players, duration;

    public ConfigurationBlock(JSONObject obj) throws JSONException {
        super(obj);
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        this.duration = json.getJSONObject("fields").getInt("duration");
        this.players = json.getJSONObject("fields").getInt("players");
        WhenThenParser parser = new WhenThenParser();
        if(json.has("inputs")){
            this.whenBlocks = parser.parse(
                    json.getJSONObject("inputs").getJSONObject("rules").getJSONObject("block"),
                    new ArrayList<>());
        }else{
            this.whenBlocks = new ArrayList<>();
        }

    }

    public ArrayList<WhenBlock> getWhenBlocks() {
        return whenBlocks;
    }

    public int getPlayers() {
        return players;
    }

    public int getDuration() {
        return duration;
    }
}
