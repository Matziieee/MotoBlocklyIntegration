package com.google.blockly.android.webview.demo.Blocks.WhenThen;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RuleGameParser {

    public ConfigurationGameDefinition parse(JSONObject workspace) throws JSONException {
        JSONArray array = workspace.getJSONObject("blocks").getJSONArray("blocks");
        ConfigurationGameDefinition gameDefinition = new ConfigurationGameDefinition();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String type = obj.getString("type");
            switch (type){
                case "subConfig":
                    gameDefinition.getSubRules().add(new SubConfigurationBlock(obj));
                    break;
                case "v2config":
                    gameDefinition.setConfigBlock(new ConfigurationBlock(obj));
                    break;
                default:
                    throw new RuntimeException("Unknown outer block found.. " + type);
            }
        }
        return gameDefinition;
    }
}
