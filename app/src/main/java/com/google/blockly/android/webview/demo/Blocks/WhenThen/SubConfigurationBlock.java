package com.google.blockly.android.webview.demo.Blocks.WhenThen;

import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SubConfigurationBlock extends AbstractBlock {

    private String name;
    private boolean replaceRules, isActive;

    private ArrayList<WhenBlock> rules;

    public SubConfigurationBlock(JSONObject obj) throws JSONException {
        super(obj);
        this.isActive = false;
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        JSONObject fields = json.getJSONObject("fields");
        this.name = fields.getString("name");
        this.replaceRules = fields.getBoolean("replaceRules");
        WhenThenParser parser = new WhenThenParser();
        if(json.has("inputs")){
            this.rules = parser.parse(
                    json.getJSONObject("inputs").getJSONObject("rules").getJSONObject("block"),
                    new ArrayList<>());
        }else{
            this.rules = new ArrayList<>();
        }
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isReplaceRules() {
        return replaceRules;
    }

    public ArrayList<WhenBlock> getRules() {
        return rules;
    }
}
