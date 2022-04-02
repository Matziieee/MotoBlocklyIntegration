Blockly.Blocks['when'] = {
  init: function() {
    var options = [["Game Starts","on_start"],
                   ["Game Ends","on_end"],
                   ["A tile is pressed","on_any_press"],
                   ["A <Colour> Tile is pressed","on_color_press"],
                   ["<X> Seconds have passed","on_x_time_passed"],
                   ["Player score is <X>","on_player_score"],];
    this.appendDummyInput()
            .appendField("When")
            .appendField(new Blockly.FieldDropdown(options, this.validate), "condition");
    this.appendValueInput("then")
              .setCheck("Then")
              .appendField("Then");
    this.setColour(120);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
 this.setTooltip("");
 this.setHelpUrl("");
  },

  validate: function(value){
    /*const src = this.getSourceBlock();
    const then = src.getInputTargetBlock("then");
    src.removeInput("then", true);*/

    this.getSourceBlock().updateConnections(value);
  },
  updateConnections: function(value){
    this.removeInput('colour', true);
    this.removeInput('score', true);
    this.removeInput('time', true);

    switch(value){
        case "on_color_press":
            this.appendValueInput("colour")
                 .setCheck("Colour")
                 .appendField("Colour");
            this.moveInputBefore("colour", "then");
            break;
        case "on_x_time_passed":
            this.appendDummyInput('time')
                .appendField("Time in seconds")
                .appendField(new Blockly.FieldNumber(1, 1, 10), "num");

                 this.moveInputBefore("time", "then");
            break;
        case "on_player_score":
            this.appendDummyInput('score')
                .appendField("Score")
                .appendField(new Blockly.FieldNumber(1, 1, 100), "num");
                this.moveInputBefore("score", "then");
            break;
        case "on_start":
        case "on_end":
        case "on_any_press": break;
        default: break;
    };
  }
};

Blockly.Blocks['then'] = {
  init: function() {
    var options = [["Turn Tiles <Colour>","set_tiles_color"],
                   ["Increment Player <X> Score","increment_player_score"],
                   ["Decrement Player <X> Score","decrement_player_score"]];
    this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown(options, this.validate), "condition");
    this.setColour(120);
    this.setOutput(true, "Then");
 this.setTooltip("");
 this.setHelpUrl("");
  },

  validate: function(value){
    this.getSourceBlock().updateConnections(value);
  },
  updateConnections: function(value){
    this.removeInput('colour', true);
    this.removeInput('player', true);

    switch(value){
        case "set_tiles_color":
            this.appendValueInput("colour")
                 .setCheck("Colour")
                 .appendField("Colour");
            break;
        case "decrement_player_score":
        case "increment_player_score":
            this.appendDummyInput('player')
            //todo get this from global config value :)
                .appendField("Player")
                .appendField(new Blockly.FieldNumber(1, 1, 10), "num");
            break;
        default: break;
    };
  }
};
