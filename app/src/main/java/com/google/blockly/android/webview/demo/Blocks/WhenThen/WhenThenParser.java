package com.google.blockly.android.webview.demo.Blocks.WhenThen;

import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenDecrementPlayerScore;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenIncrementPlayerScore;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenPlayPattern;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenRegisterPattern;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenSetTileColor;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenSetTilesColor;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenSetTilesColorExcept;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenAnyPress;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenColorPress;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenGameEnd;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenGameStart;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenPlayerScore;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenTimePassed;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WhenThenParser {

    public ArrayList<WhenBlock> parse(JSONObject block, ArrayList<WhenBlock> blocks) throws JSONException {
        //Get dropdown value, return relevant block.
        String type = block.getJSONObject("fields").getString("condition");
        WhenBlock wb;
        switch (type){
            case "on_start": wb = new WhenGameStart(block); break;
            case "on_end": wb = new WhenGameEnd(block); break;
            case "on_any_press": wb = new WhenAnyPress(block); break;
            case "on_color_press": wb = new WhenColorPress(block); break;
            case "on_x_time_passed": wb = new WhenTimePassed(block); break;
            case "on_player_score": wb = new WhenPlayerScore(block); break;
            default: throw new RuntimeException("Unsupported block type found; " + type);
        }
        blocks.add(wb);

        if(block.has("inputs") && block.getJSONObject("inputs").has("THEN0")){
            wb.getThenBlocks().addAll(this.parseThenBlocks(block.getJSONObject("inputs")));
        }
        if(block.has("next")){
            return parse(block.getJSONObject("next").getJSONObject("block"),blocks);
        }
        return blocks;
    }

    private ArrayList<ThenBlock> parseThenBlocks(JSONObject inputs) throws JSONException {
        ArrayList<ThenBlock> result = new ArrayList<>();
        int i = 0;
        while(inputs.has("THEN" + i)){
            result.add(parseThen(inputs.getJSONObject("THEN"+i).getJSONObject("block")));
            i++;
        }
        return result;
    }

    private ThenBlock parseThen(JSONObject block) throws JSONException {
        String type = block.getJSONObject("fields").getString("action");
        switch (type){
            case "set_tiles_color": return new ThenSetTilesColor(block);
            case "increment_player_score": return new ThenIncrementPlayerScore(block);
            case "decrement_player_score": return new ThenDecrementPlayerScore(block);
            case "register_pattern": return new ThenRegisterPattern(block);
            case "play_pattern": return new ThenPlayPattern(block);
            case "set_tile_color": return new ThenSetTileColor(block);
            case "set_tiles_color_except": return new ThenSetTilesColorExcept(block);
            default: throw new RuntimeException("Unsupported block type found; " + type);
        }
    }
}
