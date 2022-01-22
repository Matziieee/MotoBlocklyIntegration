Blockly.Blocks['set_tile_colour'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Set Tile Colour");
    this.appendValueInput("colour")
        .setCheck("Number")
        .appendField("colour");
    this.appendValueInput("num_tile")
        .setCheck("Number")
        .appendField("num tile");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(45);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['on_tile_press'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("On Tile Press");
    this.appendStatementInput("do")
        .setCheck(null);
    this.setColour(0);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['configuration'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Configuration:");
    this.appendValueInput("numPlayers")
        .setCheck("Number")
        .appendField("Number of Players");
    this.appendValueInput("numTiles")
        .setCheck("Number")
        .appendField("Number of Tiles");
    this.appendValueInput("gameType")
        .setCheck("GameType")
        .appendField("GameType");
    this.setColour(230);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['ongamestart'] = {
  init: function() {
    this.appendStatementInput("NAME")
        .setCheck(null)
        .appendField("On Game Start");
    this.setColour(230);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['gametype'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Game Type");
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["time","T"], ["score","S"]]), "NAME");
    this.appendValueInput("limit")
        .setCheck("Number")
        .appendField("Limit");
    this.setInputsInline(true);
    this.setOutput(true, null);
    this.setColour(230);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['addplayerscore'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("AddPlayerScore");
    this.appendValueInput("player")
        .setCheck("Number")
        .appendField("Player");
    this.appendValueInput("score")
        .setCheck("Number")
        .appendField("Score");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(0);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['event_press'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Event Press");
    this.setOutput(true, "Event");
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['event_tile'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Event Tile");
    this.setOutput(true, "Number");
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['incoming_event'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Incoming Event");
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["Tile","TILE"], ["Type","TYPE"]]), "NAME");
    this.setInputsInline(true);
    this.setOutput(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};