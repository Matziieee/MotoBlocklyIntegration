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

Blockly.Blocks['motosound_speak'] = {
  init: function() {
    this.appendValueInput("text")
        .setCheck(["String", "Number"])
        .appendField("Motosound Speak");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(270);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['randomcolour'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Random Colour");
    this.setOutput(true, "Colour");
    this.setColour(90);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['setgameover'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Set Game Over");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(270);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['get_player_score'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Get Player")
        .appendField(new Blockly.FieldNumber(0, 0), "player")
        .appendField("Score");
    this.setInputsInline(false);
    this.setOutput(true, "Number");
    this.setColour(90);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['starttimer'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Start Timer");
    this.appendValueInput("name")
        .setCheck("String")
        .appendField("Name");
    this.appendValueInput("duration")
        .setCheck("Number")
        .appendField("Duration");
    this.appendStatementInput("onEnd")
        .setCheck(null)
        .appendField("On Timer End");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['stoptimer'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Stop Timer");
    this.appendValueInput("name")
        .setCheck("String")
        .appendField("Name");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['settilecolourcountdown'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Set Tile Colour Countdown");
    this.appendValueInput("tile")
        .setCheck("Number")
        .appendField("Tile");
    this.appendValueInput("colour")
        .setCheck("Colour")
        .appendField("Colour");
    this.appendDummyInput()
        .appendField("Speed")
        .appendField(new Blockly.FieldDropdown([["slow","S"], ["medium","M"], ["fast","F"]]), "speed");
    this.appendStatementInput("onCountdownEnd")
            .setCheck(null)
            .appendField("On Countdown End");
    this.setInputsInline(false);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['setexpectednextpress'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Set Expected Next Press");
    this.appendValueInput("tiles")
        .setCheck("TileSet")
        .appendField("TileSet");
    this.appendStatementInput("onCorrect")
        .setCheck(null)
        .appendField("On Correct Press");
    this.appendStatementInput("onIncorrect")
        .setCheck(null)
        .appendField("On Incorrect Press");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['tileset'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Define TileSet");
    this.appendValueInput("tile")
        .setCheck("Number")
        .appendField("Tile");
    this.appendValueInput("tileset")
        .setCheck("TileSet")
        .appendField("TileSet (Optional)");
    this.setOutput(true, "TileSet");
    this.setColour(105);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['gameblock'] = {
  init: function() {
    this.setDeletable(false);
    this.appendDummyInput()
        .appendField("Game");
    this.appendDummyInput()
        .appendField("Players")
        .appendField(new Blockly.FieldNumber(1, 1, 4), "num");
    this.appendValueInput("gameType")
        .setCheck("GameType")
        .appendField("GameType");
    this.appendStatementInput("start")
        .setCheck(null)
        .appendField("On Game Start");
    this.appendStatementInput("end")
        .setCheck(null)
        .appendField("On Game End");
    this.setColour(20);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['tile'] = {
  init: function() {
  this.appendValueInput("tiles")
          .setCheck("Number")
          .appendField("Tile");
    var options = [["Set Colour","colour"], ["Set On Press","on_press"]];
    this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown(options, this.validate), "action");

    this.setColour(120);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
 this.setTooltip("");
 this.setHelpUrl("");
  },

  validate: function(value){
    this.getSourceBlock().updateConnections(value);
  },
  updateConnections: function(value){
    this.removeInput('Colour', true);
    this.removeInput('speed', true);
    this.removeInput('onend', true);
    this.removeInput('OP', true);
    if (value == 'colour'){
        this.appendValueInput("Colour")
                .setCheck("Colour")
                .appendField("Colour");
    }else if(value == 'on_press'){
        this.appendStatementInput("OP");
    }
  }
};

Blockly.Blocks['player'] = {
  init: function() {
  this.appendValueInput("player")
            .setCheck("Number")
            .appendField("Player");
    var options = [["Add","add"], ["Subtract","sub"]];
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
    this.setColour(120);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
 this.setTooltip("");
 this.setHelpUrl("");
  },

  validate: function(value) {
    this.getSourceBlock().updateConnections(value);
  },

  updateConnections: function(value) {
    this.removeInput('ADD', true);
    this.removeInput('SUB', true);
    if (value == 'add'){
        this.appendDummyInput('ADD')
                .appendField(new Blockly.FieldNumber(1, 1, 10), "num")
                .appendField("Score");
    }else if(value == 'sub'){
        this.appendDummyInput('SUB')
                .appendField(new Blockly.FieldNumber(1, 1, 10), "num")
                .appendField("Score");
    }
  }
};

Blockly.Blocks['l1_setalltilescolour'] = {
  init: function() {
    this.appendDummyInput('colour')
             .appendField("Set All Tiles Colour")
             .appendField(new Blockly.FieldDropdown([["Red","R"], ["Green","G"], ["Blue","B"]]), "colour");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['l1_settilecolour'] = {
  init: function() {
    this.appendDummyInput('colour')
             .appendField("Set Tile")
             .appendField(new Blockly.FieldDropdown([["1","1"], ["2","2"], ["3","3"], ["4","4"]]), "tile")
             .appendField("Colour")
             .appendField(new Blockly.FieldDropdown([["Red","R"], ["Green","G"], ["Blue","B"]]), "colour");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['wait'] = {
  init: function() {
    this.appendDummyInput()
            .appendField("Wait for")
            .appendField(new Blockly.FieldNumber(1, 1), "time")
            .appendField("seconds");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['l1_gameblock'] = {
  init: function() {
    this.setDeletable(false);
    this.appendDummyInput()
        .appendField("Game");
   this.appendStatementInput("start")
        .setCheck(null)
        .appendField("On game start")
    this.setColour(20);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};

Blockly.Blocks['l2_setalltilescolour'] = {
  init: function() {
    this.appendValueInput('colour')
        .setCheck("Colour")
        .appendField("Set All Tiles Colour");
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(285);
    this.setTooltip("");
    this.setHelpUrl("");
  }
};

Blockly.Blocks['l4_tile'] = {
  init: function() {
        this.appendValueInput('tile')
               .setCheck("Number")
               .appendField("Set Tile")
        this.appendValueInput("Colour")
               .setCheck("Colour")
               .appendField("Colour");
        this.setColour(120);
        this.setPreviousStatement(true, null);
        this.setNextStatement(true, null);
        this.setTooltip("");
        this.setHelpUrl("");
  }
};
