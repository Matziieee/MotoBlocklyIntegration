package com.google.blockly.android.webview.demo.Blocks.WhenThen;

import com.google.blockly.android.webview.demo.Blocks.ValueBlocks.NumberBlock;

import org.json.JSONObject;

import java.util.ArrayList;

public class ConfigurationGameDefinition {

    private ConfigurationBlock configBlock;

    public ConfigurationGameDefinition(){}

    public ConfigurationBlock getConfigBlock() {
        return configBlock;
    }

    public void setConfigBlock(ConfigurationBlock configBlock) {
        this.configBlock = configBlock;
    }
}
