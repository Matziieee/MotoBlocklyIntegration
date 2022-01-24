Blockly.Blocks['ongamestart'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("On Game Start");
    this.appendStatementInput("statements")
        .setCheck(null);
    this.setColour(30);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['ongameend'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("On Game End");
    this.appendStatementInput("statements")
        .setCheck(null);
    this.setColour(30);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['ontilepress'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("On Tile Pressed");
    this.appendStatementInput("statements")
        .setCheck(null);
    this.setColour(30);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['configuration'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Configuration");
    this.appendValueInput("name")
        .setCheck("String")
        .appendField("Game Name");
    this.appendValueInput("numplayers")
        .setCheck("Number")
        .appendField("Number of players");
    this.appendValueInput("numtiles")
        .setCheck("Number")
        .appendField("Number of tiles");
    this.appendValueInput("type")
        .setCheck("GameType")
        .appendField("Game Type");
    this.setColour(30);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['settilecolor'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Set Tile Colour");
    this.appendValueInput("colour")
        .setCheck("Colour")
        .appendField("Colour");
    this.appendValueInput("tile")
        .setCheck("Number")
        .appendField("Tile");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(270);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['settilesidle'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Set All Tiles Idle");
    this.appendValueInput("colour")
        .setCheck("Colour")
        .appendField("Colour");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(270);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['addplayerscore'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Add Player Score");
    this.appendValueInput("player")
        .setCheck("Number")
        .appendField("Player");
    this.appendValueInput("score")
        .setCheck("Number")
        .appendField("Score");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(270);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['colour'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Select Colour")
        .appendField(new Blockly.FieldDropdown([["Off","0"], ["Red","1"], ["Blue","2"], ["Green","3"], ["Indigo","4"], ["Orange","5"]]), "colour");
    this.setOutput(true, "Colour");
    this.setColour(90);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['incomingevent'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Incoming event")
        .appendField(new Blockly.FieldDropdown([["Tile","tile"], ["Type","type"]]), "field");
    this.setOutput(true, ["EventType", "Number"]);
    this.setColour(90);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['eventtype'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Event Type")
        .appendField(new Blockly.FieldDropdown([["Press","press"]]), "type");
    this.setOutput(true, "EventType");
    this.setColour(90);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['gametype'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Game Type")
        .appendField(new Blockly.FieldDropdown([["Time","t"], ["Score","s"]]), "type");
    this.appendDummyInput()
        .appendField("Goal")
        .appendField(new Blockly.FieldNumber(30, 0), "goal");
    this.setOutput(true, "GameType");
    this.setColour(90);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['number'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldNumber(0), "number");
    this.setOutput(true, "Number");
    this.setColour(225);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['randomnumber'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Random Number")
        .appendField("From")
        .appendField(new Blockly.FieldNumber(1, 0), "from")
        .appendField("To")
        .appendField(new Blockly.FieldNumber(4, 1), "to");
    this.setOutput(true, "Number");
    this.setColour(225);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};