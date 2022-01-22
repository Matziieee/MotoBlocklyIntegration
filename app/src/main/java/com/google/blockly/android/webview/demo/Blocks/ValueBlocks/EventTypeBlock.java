package com.google.blockly.android.webview.demo.Blocks.ValueBlocks;

import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameState;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyMotoAPI;
import com.google.blockly.android.webview.demo.BlocklyTools.EventType;

import org.json.JSONException;
import org.json.JSONObject;

public class EventTypeBlock implements IComparableValueBlock<EventType> {

    private EventType type;

    @Override
    public void parseFromJson(JSONObject json) throws JSONException {
        switch (json.getString("type")){
            case "event_press": type = EventType.PRESS;
            //todo add more...
        }
    }

    @Override
    public EventType getValue(BlocklyMotoAPI api, BlocklyGameState state) {
        return this.type;
    }

    @Override
    public boolean compare(EventType left, EventType right, char com) {
        //todo Only equals is supported!
        switch (com){
            case '=':
                return left == right;
        }

        return false;
    }
}
