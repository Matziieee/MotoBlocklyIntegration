package com.google.blockly.android.webview.demo.BlocklyTools;

import android.util.Log;

import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.IComparableValueBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.IValueBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.EventTypeBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.IncomingBlockEvent;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.RandomNumberBlock;
import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.VariableGetBlock;

import org.json.JSONException;
import org.json.JSONObject;

public class BlocklyValueParser {

    public IComparableValueBlock parseJson(JSONObject jsonObject) throws JSONException {
        String type = jsonObject.getString("type");
        //Known types: math_number, math_random_int, variables_get, incoming_event, event_press

        switch (type) {
            case "variables_get":
                VariableGetBlock vgb = new VariableGetBlock();
                vgb.parseFromJson(jsonObject);
                return vgb;
            case "incoming_event":
                IncomingBlockEvent ibe = new IncomingBlockEvent();
                ibe.parseFromJson(jsonObject);
                return ibe;
            case "event_press":
                EventTypeBlock etb = new EventTypeBlock();
                etb.parseFromJson(jsonObject);
                return etb;
            case "math_number":
                //return jsonObject.getJSONObject("fields").getInt("NUM");
                NumberBlock nb = new NumberBlock();
                nb.parseFromJson(jsonObject);
                return nb;

            case "math_random_int":
                RandomNumberBlock rnb = new RandomNumberBlock();
                rnb.parseFromJson(jsonObject);
                return rnb;


            default: Log.i("ERROR", "Unknown value block found; " + type); break;
        }
        throw new RuntimeException("Bad value, check logs");
    }
}
