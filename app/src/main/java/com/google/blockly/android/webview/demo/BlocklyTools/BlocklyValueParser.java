package com.google.blockly.android.webview.demo.BlocklyTools;

import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.GetPlayerScoreBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.IComparableValueBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.EventTypeBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.IncomingEventBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.TextBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.VariableGetBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class BlocklyValueParser {

    public IComparableValueBlock parseJson(JSONObject jsonObject) throws JSONException {
        String type = jsonObject.getString("type");
        //Known types: number variables_get, incomingevent, eventtype

        switch (type) {
            case "get_player_score":
                return new GetPlayerScoreBlock(jsonObject);
            case "text":
                return new TextBlock(jsonObject);
            case "variables_get":
                return new VariableGetBlock(jsonObject);
            case "incomingevent":
                return new IncomingEventBlock(jsonObject);
            case "eventtype":
                return new EventTypeBlock(jsonObject);
            case "randomnumber":
            case "number":
                return new NumberBlock(jsonObject);
            default: Log.i("ERROR", "Unknown value block found; " + type); break;
        }
        throw new RuntimeException("Bad value, check logs");
    }
}
