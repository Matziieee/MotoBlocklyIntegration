package com.google.blockly.android.webview.demo.Blocks.WhenThen.When;

import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenType;
import com.google.blockly.android.webview.demo.MotoConfigGameAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public abstract class WhenBlock extends AbstractBlock {

    protected WhenType type;
    protected ArrayList<ThenBlock> thenBlocks;

    public WhenBlock (JSONObject jsonObject) throws JSONException {
        super(jsonObject);
        this.thenBlocks = new ArrayList<>();
        switch (jsonObject.getJSONObject("fields").getString("condition")){
            case "on_start": this.type = WhenType.GameStart; break;
            case "on_end": this.type = WhenType.GameEnd; break;
            case "on_any_press": this.type = WhenType.AnyTilePressed; break;
            case "on_color_press": this.type = WhenType.ColourTilePressed; break;
            case "on_x_time_passed": this.type = WhenType.XSecondsPassed; break;
            case "on_player_score": this.type = WhenType.PlayerScore; break;
            case "pair_pressed": this.type = WhenType.PairPressed; break;
            case "on_sub_config": this.type = WhenType.SubRuleActive; break;
            default: throw new RuntimeException("Unsupported block type found; " + type);
        }
    }

    public WhenType getType() {
        return type;
    }

    public ArrayList<ThenBlock> getThenBlocks() {
        return thenBlocks;
    }
}
