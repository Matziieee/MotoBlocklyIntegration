package com.google.blockly.android.webview.demo.Blocks.WhenThen;

import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenBlock;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ConfigurationGameDefinition {

    private ConfigurationBlock configBlock;
    private ArrayList<SubConfigurationBlock> subRules;

    public ConfigurationGameDefinition(){
        this.subRules = new ArrayList<SubConfigurationBlock>();
    }

    public ArrayList<SubConfigurationBlock> getSubRules() {
        return subRules;
    }

    public ConfigurationBlock getConfigBlock() {
        return configBlock;
    }

    public ArrayList<WhenBlock> getRules(){
       // Find out if an active sub rule is overriding existing
        long count = this.subRules.stream().filter(sr -> {
            return sr.isActive() && sr.isReplaceRules();
        }).count();
        if(count > 0){
            return this.subRules.stream().filter(sr -> {
                return sr.isActive() && sr.isReplaceRules();
            }).findFirst().get().getRules();
        }
        //No active subrule is overriding
        ArrayList<WhenBlock> result = (ArrayList<WhenBlock>) this.configBlock.getWhenBlocks().clone();
        this.subRules.forEach(sr -> {
            if(sr.isActive()){
                result.addAll(sr.getRules());
            }
        });
        return result;
    }
    public void setConfigBlock(ConfigurationBlock configBlock) {
        this.configBlock = configBlock;
    }
}
