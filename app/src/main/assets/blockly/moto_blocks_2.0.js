Blockly.Blocks['v2config'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("Moto Game");
    this.appendDummyInput()
        .appendField("Number of players")
        .appendField(new Blockly.FieldNumber(1, 1, 10), "players");
    this.appendDummyInput()
            .appendField("Game Duration")
            .appendField(new Blockly.FieldNumber(30, 5, 120), "duration");
    this.appendStatementInput("rules")
        .appendField("Rules")
    this.setColour(30);
 this.setTooltip("");
 this.setHelpUrl("");
  }
};
Blockly.Blocks['when'] = {
  mutationToDom: function(){
    var container = document.createElement('mutation');
    container.setAttribute('count', this.thenCount);
    return container;
  },
  domToMutation: function(xmlElement){
    this.thenCount = parseInt(xmlElement.getAttribute('count'),10);
    this.updateShape_();
  },
  decompose: function(workspace){
      //init the container block in the pop-up
      var topBlock = workspace.newBlock('when_then_container')
      topBlock.initSvg();
      //add then blocks
      var connection = topBlock.getInput('STACK').connection;
      for(var i=0; i < this.thenCount; i++){
        var itemBlock = workspace.newBlock('when_then_item');
        itemBlock.initSvg();
        connection.connect(itemBlock.previousConnection);
        connection = itemBlock.nextConnection;
      }
      return topBlock;
    },
  compose: function(topBlock){
    var itemBlock = topBlock.getInputTargetBlock('STACK');
    var connections = [];
    while (itemBlock){
        connections.push(itemBlock.valueConnection_);
        itemBlock = itemBlock.nextConnection && itemBlock.nextConnection.targetBlock();
    }
    for(var i = 0; i < this.thenCount; i++){
        var connection = this.getInput("THEN"+i).connection.targetConnection;
        if(connection && connections.indexOf(connection) == -1){
        connection.disconnect();
        }
    }
    this.thenCount = connections.length;
    this.updateShape_();
    for(var i = 0; i < this.thenCount; i++){
        Blockly.Mutator.reconnect(connections[i], this, 'THEN'+i);
    }
  },
  saveConnections: function(containerBlock){
    var itemBlock = containerBlock.getInputTargetBlock('STACK');
    var i = 0;
    while (itemBlock) {
      var input = this.getInput('THEN' + i);
      itemBlock.valueConnection_ = input && input.connection.targetConnection;
      i++;
      itemBlock = itemBlock.nextConnection &&
          itemBlock.nextConnection.targetBlock();
    }
  },
  updateShape_: function(){
    for(var i = 0; i < this.thenCount; i++){
        if(!this.getInput('THEN'+i)){
            var input = this.appendValueInput('THEN'+i)
                .setCheck("Then");
            if(i == 0){
                input.appendField("Then");
            }
            else{
                input.appendField("And Then");
            }
        }
    }
    while (this.getInput('THEN' + i)) {
      this.removeInput('THEN' + i);
      i++;
    }

  },
  init: function() {
    this.thenCount = 1;
    var options = [["Game Starts","on_start"],
                   ["Game Ends","on_end"],
                   ["A tile is pressed","on_any_press"],
                   ["A <Colour> Tile is pressed","on_color_press"],
                   ["<X> Seconds have passed","on_x_time_passed"],
                   ["Player score is <X>","on_player_score"],];
    this.appendDummyInput()
            .appendField("When")
            .appendField(new Blockly.FieldDropdown(options, this.validate), "condition");
    this.setColour(120);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.updateShape_();
    this.setMutator(new Blockly.Mutator(['when_then_item']));
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
            this.appendDummyInput("colour")
                    .appendField("Select Colour")
                    .appendField(new Blockly.FieldDropdown([["Red","1"], ["Blue","2"], ["Green","3"], ["Indigo","4"], ["Orange","5"]]), "col");
            this.moveInputBefore("colour", "THEN0");
            break;
        case "on_x_time_passed":
            this.appendDummyInput('time')
                .appendField("Time in seconds")
                .appendField(new Blockly.FieldNumber(1, 1, 10), "num");
                 this.moveInputBefore("time", "THEN0");
            break;
        case "on_player_score":
            this.appendDummyInput('score')
                .appendField("Score")
                .appendField(new Blockly.FieldNumber(1, -100, 100), "num");
                this.moveInputBefore("score", "THEN0");
            break;
        case "on_start":
        case "on_end":
        case "on_any_press": break;
        default: break;
    };
  }
};
Blockly.Blocks['when_then_container'] = {
    init: function(){
        this.setColour(120);
        this.appendDummyInput().appendField('When..');
        this.appendStatementInput('STACK');
        this.setTooltip('');
        this.contextMenu = false;
    }
};
Blockly.Blocks['when_then_item'] = {
    init: function(){
        this.setColour(120);
        this.appendDummyInput().appendField('Then');
        this.setPreviousStatement(true);
        this.setNextStatement(true);
        this.setTooltip('');
        this.contextMenu = false;
    }
};
Blockly.Blocks['then'] = {
  init: function() {
    var options = [["Turn Tiles <Colour>","set_tiles_color"],
                   ["Increment Player <X> Score","increment_player_score"],
                   ["Decrement Player <X> Score","decrement_player_score"]];
    this.appendDummyInput()
            .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
    this.setColour(180);
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
            this.appendDummyInput("colour")
                .appendField("Select Colour")
                .appendField(new Blockly.FieldDropdown([
                    ["Random", "-1"],
                    ["Off","0"],
                    ["Red","1"],
                    ["Blue","2"],
                    ["Green","3"],
                    ["Indigo","4"],
                    ["Orange","5"]
                ]), "col");
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
