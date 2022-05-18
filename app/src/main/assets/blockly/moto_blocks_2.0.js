/*
* GLOBAL VARIABLES
*/
var knownIds = [];
var patterns = {};
var subConfigs = {};
var pairs = {};
var playerAmount = 1;
var isLoading = false;

function setSavedState(savedState){
    if(savedState["patterns"]){
        patterns = savedState["patterns"];
    }
    if(savedState["subConfigs"]){
        subConfigs = savedState["subConfigs"];
    }
    if(savedState["pairs"]){
        pairs = savedState["pairs"];
    }
    if(savedState["blockIds"]){
        knownIds = savedState["blockIds"]
    }
}
function getSavedState(){
    let ids = [];
    this.workspace.getAllBlocks().forEach((b) => {
        ids.push(b.id)
    })

    Object.keys(subConfigs).forEach(k => {
        let found = ids.find(s => s === k);
        if(!found){
            delete subConfigs[k];
        }
    })

    Object.keys(pairs).forEach(k => {
        let found = ids.find(s => s === k);
        if(!found){
            delete pairs[k];
        }
    })

    Object.keys(patterns).forEach(k => {
        let found = ids.find(s => s === k);
        if(!found){
            delete patterns[k];
        }
    })

    return {"patterns": patterns, "subConfigs": subConfigs, "pairs": pairs, "blockIds": ids};
}
function getConnectedTiles(){
    let a = javaMethods.getConnectedTiles();
    if(a === ""){
        return [];
    }
    var arr = Array.from(a.split(','),Number);
    console.log("array", arr)
    return arr;
}
function getOptions(collection){
    var keys = Object.keys(collection);
    var options = [];
    knownIds.forEach(s => console.log("known id: ",s))
    for(let i = 0; i < keys.length; i++){
        console.log("looking for ", keys[i])
        let id = knownIds.find(s => s === keys[i]);
        console.log("found", id)
        if(this.workspace.getBlockById(keys[i]) || id){
            options.push([collection[keys[i]], keys[i]]);
        }
     }
     if(options.length === 0){
        options.push(['NONE FOUND', '-1'])
     }
    return options;
}

Blockly.Blocks['v2config'] = {
  setPlayerAmount: function(value){
    playerAmount = value;
    return value;
  },
  init: function() {
    this.appendDummyInput()
        .appendField("Moto Game");
    this.appendDummyInput()
        .appendField("Number of players")
        .appendField(new Blockly.FieldNumber(1, 1, 10, null, this.setPlayerAmount), "players");
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
  onchange: function(e) {
    console.log("changes!!", JSON.stringify(e))
    let state = null;
    if(e['blockId'] && this.id === e['blockId']){
        if(e['element'] && e['element'] === 'field'){
            if(e['name'] && e['name'] === 'condition'){
                state = {'condition': e['newValue']}
            }
        }
    }

    this.updateShape_(state);
  },
  saveExtraState: function(){
    let state = {'thenCount': this.thenCount, 'condition': this.getFieldValue('condition')};
    if(this.getFieldValue('condition') === 'on_color_press'){
        let selected = this.getFieldValue('col');
        state['selected'] = selected;
    }
    console.log("state!", JSON.stringify(state))
    return state;
  },
  loadExtraState: function(state){
    this.thenCount = state['thenCount']
    this.updateShape_(state);
  },
  decompose: function(workspace){
    console.log("decompose")
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
    console.log("compose")
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
    console.log("save connections")
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
  updateShape_: function(state){
    if(state){
        this.updateConnections(state['condition']);
    }
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
                   ["Pair pressed", "pair_pressed"],
                   ["<Sub rule> is made Active", "on_sub_config"],
                   ["All tiles are off", "on_all_tiles_off"]
                   ];
                   //["Player score is <X>","on_player_score"]
    this.appendDummyInput()
            .appendField("When")
            .appendField(new Blockly.FieldDropdown(options), "condition");

    this.setColour('#097B10');
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.updateShape_();
    this.setMutator(new Blockly.Mutator(['when_then_item']));
    this.setTooltip("");
    this.setHelpUrl("");
    console.log("Done initializing!")
  },
  updateConnections: function(value){
    this.removeInput('col', true);
    this.removeInput('score', true);
    this.removeInput('time', true);
    this.removeInput('name', true);
    switch(value){
        case "on_sub_config":
            var options = getOptions(subConfigs);
            this.appendDummyInput("name")
                .appendField("Select Sub Rule")
                .appendField(new Blockly.FieldDropdown(options), "rule_name");
            this.moveInputBefore("name", "THEN0");
            break;
        case "pair_pressed":
            var options = getOptions(pairs);
            this.appendDummyInput("name")
                .appendField("Select Pair")
                .appendField(new Blockly.FieldDropdown(options), "pair_name");
            this.moveInputBefore("name", "THEN0");
            break;
        case "on_color_press":
            this.appendDummyInput("col")
                    .appendField("Select Colour")
                    .appendField(new Blockly.FieldDropdown([["Red","1"], ["Blue","2"], ["Green","3"], ["Indigo","4"], ["Orange","5"]]), "col");
            this.moveInputBefore("col", "THEN0");
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
        case "on_all_tiles_off":
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
    var options = [["Turn Tiles <Color>","set_tiles_color"],
                   ["Increment Player <X> Score","increment_player_score"],
                   ["Decrement Player <X> Score","decrement_player_score"],
                   ["Register Sequence", "register_pattern"],
                   ["Play Sequence", "play_pattern"],
                   ["Turn Tile <X> <Color>", "set_tile_color"],
                   ["Turn Tiles Except <X> <Color>", "set_tiles_color_except"],
                   ["Turn Random Tile <Color>", "set_random_tile_color"],
                   ["Turn Random Tile <Color> And Rest <Color>", "set_random_tile_color_with_rest"],
                   ["Play Sound", "play_sound"],
                   ["Stop Game", "stop_game"],
                   ["Define Random Sequence", "def_ran_seq"],
                   ["Define Random Pair", "def_ran_pair"],
                   ["Turn Pair Off", "turn_pair_off"],
                   ["Clear All Pairs", "clear_pairs"],
                   ["Wait for sequence", "wait_sequence"],
                   ["Turn Pair <Color>", "turn_pair_on"],
                   ["Activate Sub Rule", "activate_subrule"],
                   ["Deactivate Sub Rule", "deactivate_subrule"]
                   ];
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
  registerPair: function(value){
      let bId = this.id ? this.id : this.getSourceBlock().id;
      var keys = Object.keys(pairs);
      for(let i = 0; i < keys.length; i++){
        if(pairs[keys[i]] === value){
            bId = keys[i];
        }
      }
      pairs[bId] = value;
      return value;
  },
  registerPattern: function(value){
    if(this.id){
        patterns[this.id] = value;
    }else{
        patterns[this.getSourceBlock().id] = value;
    }
    return value;
  },
  updateConnections: function(value){
    this.removeInput('colour', true);
    this.removeInput('colour2', true);
    this.removeInput('player', true);
    this.removeInput('tile', true);
    this.removeInput("name", true);
    this.removeInput("correct", true);
    this.removeInput("incorrect", true);
    this.removeInput("len", true);
    this.removeInput("sound", true);
    this.removeInput("checkbox", true);
    delete patterns[this.id]

    switch(value){
        case "set_random_tile_color":
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
        case "set_random_tile_color_with_rest":
            this.appendDummyInput("colour")
                .appendField("Select Colour")
                .appendField(new Blockly.FieldDropdown([
                    ["Red","1"],
                    ["Blue","2"],
                    ["Green","3"],
                    ["Indigo","4"],
                    ["Orange","5"]
                ]), "col");
            this.appendDummyInput("colour2")
                .appendField("And colour of rest")
                .appendField(new Blockly.FieldDropdown([
                    ["Red","1"],
                    ["Blue","2"],
                    ["Green","3"],
                    ["Indigo","4"],
                    ["Orange","5"]
                ]), "col2");
            break;
        case "deactivate_subrule":
        case "activate_subrule":
            var options = getOptions(subConfigs);
            this.appendDummyInput("name")
                .appendField("Select Sub Rule")
                .appendField(new Blockly.FieldDropdown(options), "rule_name");
            break;
        case "def_ran_pair":
            this.appendDummyInput("name")
                .appendField("Name")
                .appendField(new Blockly.FieldTextInput('pair', this.registerPair), "name")
            this.registerPair("pair");
            this.appendDummyInput("checkbox")
                 .appendField('With sound?')
                 .appendField(new Blockly.FieldCheckbox(false), 'with_sound');
            break;
        case "wait_sequence":
            var sequences = getOptions(patterns);
            var options = getOptions(subConfigs);
            this.appendDummyInput("name")
                .appendField("Select Sequence")
                .appendField(new Blockly.FieldDropdown(sequences), "seq_name");
            //On correct
            this.appendDummyInput("correct")
                    .appendField("On Correct")
                    .appendField(new Blockly.FieldDropdown(options), "correct_name");
            //On false
            this.appendDummyInput("incorrect")
                    .appendField("On Incorrect")
                    .appendField(new Blockly.FieldDropdown(options), "incorrect_name");
            break;
        case "turn_pair_off":
            this.appendDummyInput("checkbox")
              .appendField('Set to Idle?')
              .appendField(new Blockly.FieldCheckbox(false), 'set_idle');
            var options = getOptions(pairs);
            this.appendDummyInput("name")
                .appendField(new Blockly.FieldDropdown(options), "pair_name");
            break;
        case "turn_pair_on":
            var options = getOptions(pairs);
            this.appendDummyInput("name")
                .appendField(new Blockly.FieldDropdown(options), "pair_name");
            this.appendDummyInput("colour")
                .appendField("Select Colour")
                .appendField(new Blockly.FieldDropdown([["Red","1"], ["Blue","2"], ["Green","3"], ["Indigo","4"], ["Orange","5"]]), "col");
            break;
        case "play_sound":
            var sounds = [
                ["Start", "start"],
                ["End", "end"],
                ["Step1","s1"],
                ["Step2","s2"],
                ["Press1","p1"],
                ["Press2","p2"],
                ["Press3","p3"],
                ["Press4","p4"],
                ["Matched","m"],
                ["Error","e"]
            ];
            this.appendDummyInput("sound")
               .appendField(new Blockly.FieldDropdown(sounds), "sound");
            break;
        case "play_pattern":
            var options = getOptions(patterns);
             this.appendDummyInput("name")
                       .appendField(new Blockly.FieldDropdown(options), "pattern_name");
             break;
        case "def_ran_seq":
        case "register_pattern":
            this.appendDummyInput("name")
                .appendField("Name")
                .appendField(new Blockly.FieldTextInput('pattern', this.registerPattern), "name")
            this.appendDummyInput("len")
                 .appendField("Length")
                 .appendField(new Blockly.FieldNumber(1, 1, 10), "num");
                 this.registerPattern("pattern");
                 break;
        case "set_tile_color":
        case "set_tiles_color_except":
            let tiles = getConnectedTiles();
            let tile_options = [];
            if(tiles.length === 0){
                tile_options = [["No Tiles connected", "-1"],];
            }else{
                for(let i = 0; i < tiles.length; i++){
                    tile_options.push([""+tiles[i], ""+tiles[i]]);
                }
            }
            this.appendDummyInput("tile")
                .appendField("Select Tile")
                .appendField(new Blockly.FieldDropdown(tile_options), "num");
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
            let players = [];
            for(let i = 0; i < playerAmount; i++){
                players.push([""+(i+1), ""+i]);
            }
            this.appendDummyInput('player')
                .appendField("Player")
                .appendField(new Blockly.FieldDropdown(players), "num");
            break;
        default: break;
    };
  }
};
Blockly.Blocks['subConfig'] = {
    setPlayerAmount: function(value){
      playerAmount = value;
      return value;
    },
    init: function() {
      this.appendDummyInput()
          .appendField("Sub Rule");
      this.appendDummyInput()
              .appendField('Replace Existing Rules?')
              .appendField(new Blockly.FieldCheckbox(false), 'replaceRules');
      this.appendDummyInput("name")
          .appendField("Name")
          .appendField(new Blockly.FieldTextInput('sub rule', this.registerSubConfig), "name")
      this.appendStatementInput("rules")
          .appendField("Rules")
      this.setColour(30);
      this.setTooltip("");
      this.setHelpUrl("");
      this.registerSubConfig('sub rule');
    },
    registerSubConfig: function(value){
        if(this.id){
            subConfigs[this.id] = value;
        }else{
            subConfigs[this.getSourceBlock().id] = value;
        }
        return value;
    }
};

Blockly.Blocks['then_score'] = {
    init: function() {
        var options = [["Increment Player <X> Score","increment_player_score"],
                       ["Decrement Player <X> Score","decrement_player_score"],
                       ];
        this.appendDummyInput()
                .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
        this.setColour('#9fa55b');
        this.setOutput(true, "Then");
        this.setTooltip("");
        this.setHelpUrl("");
    },

    validate: function(value){
        this.getSourceBlock().updateConnections(value);
    },
    updateConnections: function(value){
        this.removeInput('player', true);
        let players = [];
        for(let i = 0; i < playerAmount; i++){
            players.push([""+(i+1), ""+i]);
        }
        this.appendDummyInput('player')
            .appendField("Player")
            .appendField(new Blockly.FieldDropdown(players), "num");
    }
};

Blockly.Blocks['then_seq'] = {
    init: function() {
        var options = [["Register Sequence", "register_pattern"],
                       ["Play Sequence", "play_pattern"],
                       ["Define Random Sequence", "def_ran_seq"],
                       ["Wait for sequence", "wait_sequence"],
                       ];
        this.appendDummyInput()
                .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
        this.setColour('#5ba55b');
        this.setOutput(true, "Then");
        this.setTooltip("");
        this.setHelpUrl("");
    },

    validate: function(value){
        this.getSourceBlock().updateConnections(value);
    },
    registerPattern: function(value){
        if(this.id){
            patterns[this.id] = value;
        }else{
            patterns[this.getSourceBlock().id] = value;
        }
        return value;
    },
    updateConnections: function(value){
        this.removeInput("name", true);
        this.removeInput("correct", true);
        this.removeInput("incorrect", true);
        this.removeInput("len", true);
        if(patterns[this.id]){
            delete patterns[this.id]
        }

        switch(value) {
            case "wait_sequence":
                var sequences = getOptions(patterns);
                var options = getOptions(subConfigs);
                this.appendDummyInput("name")
                    .appendField("Select Sequence")
                    .appendField(new Blockly.FieldDropdown(sequences), "seq_name");
                //On correct
                this.appendDummyInput("correct")
                    .appendField("On Correct")
                    .appendField(new Blockly.FieldDropdown(options), "correct_name");
                //On false
                this.appendDummyInput("incorrect")
                    .appendField("On Incorrect")
                    .appendField(new Blockly.FieldDropdown(options), "incorrect_name");
                break;
            case "play_pattern":
                var options = getOptions(patterns);
                this.appendDummyInput("name")
                    .appendField(new Blockly.FieldDropdown(options), "pattern_name");
                break;
            case "def_ran_seq":
            case "register_pattern":
                this.appendDummyInput("name")
                    .appendField("Name")
                    .appendField(new Blockly.FieldTextInput('pattern', this.registerPattern), "name")
                this.appendDummyInput("len")
                    .appendField("Length")
                    .appendField(new Blockly.FieldNumber(1, 1, 10), "num");
                     this.registerPattern("pattern");
                break;
            default: break;
        }
    }
};

Blockly.Blocks['then_tile'] = {
    init: function() {
        var options = [["Turn Tiles <Color>","set_tiles_color"],
                       ["Turn Tile <X> <Color>", "set_tile_color"],
                       ["Turn Tiles Except <X> <Color>", "set_tiles_color_except"],
                       ["Turn Random Tile <Color>", "set_random_tile_color"],
                       ["Turn Random Tile <Color> And Rest <Color>", "set_random_tile_color_with_rest"]
                       ];
        this.appendDummyInput()
                .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
        this.setColour('#5ba58c');
        this.setOutput(true, "Then");
        this.setTooltip("");
        this.setHelpUrl("");
    },

    validate: function(value){
        this.getSourceBlock().updateConnections(value);
    },
    updateConnections: function(value){
        this.removeInput('colour', true);
        this.removeInput('colour2', true);
        this.removeInput('tile', true);
        switch(value) {

            case "set_tile_color":
            case "set_tiles_color_except":
                let tiles = getConnectedTiles();
                let tile_options = [];
                if(tiles.length === 0){
                    tile_options = [["No Tiles connected", "-1"],];
                }else{
                    for(let i = 0; i < tiles.length; i++){
                        tile_options.push([""+tiles[i], ""+tiles[i]]);
                    }
                }
                this.appendDummyInput("tile")
                    .appendField("Select Tile")
                    .appendField(new Blockly.FieldDropdown(tile_options), "num");
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
            case "set_random_tile_color_with_rest":
                this.appendDummyInput("colour")
                    .appendField("Select Colour")
                    .appendField(new Blockly.FieldDropdown([
                        ["Red","1"],
                        ["Blue","2"],
                        ["Green","3"],
                        ["Indigo","4"],
                        ["Orange","5"]
                    ]), "col");
                this.appendDummyInput("colour2")
                    .appendField("And colour of rest")
                    .appendField(new Blockly.FieldDropdown([
                        ["Red","1"],
                        ["Blue","2"],
                        ["Green","3"],
                        ["Indigo","4"],
                        ["Orange","5"]
                    ]), "col2");
                break;
            default: break;
        }
    }
};

Blockly.Blocks['then_pair'] = {
    init: function() {
        var options = [["Define Random Pair", "def_ran_pair"],
                       ["Turn Pair Off", "turn_pair_off"],
                       ["Clear All Pairs", "clear_pairs"],
                       ["Turn Pair <Color>", "turn_pair_on"]
                       ];
        this.appendDummyInput()
                .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
        this.setColour('#5b80a5');
        this.setOutput(true, "Then");
        this.setTooltip("");
        this.setHelpUrl("");
    },
    registerPair: function(value){
          let bId = this.id ? this.id : this.getSourceBlock().id;
          var keys = Object.keys(pairs);
          for(let i = 0; i < keys.length; i++){
            if(pairs[keys[i]] === value){
                bId = keys[i];
            }
          }
          pairs[bId] = value;
          return value;
    },
    validate: function(value){
        this.getSourceBlock().updateConnections(value);
    },
    updateConnections: function(value){
        this.removeInput('colour', true);
        this.removeInput("name", true);
        this.removeInput("checkbox", true);
        if(pairs[this.id]){
                    delete pairs[this.id]
        }
        switch(value){
            case "turn_pair_off":
                this.appendDummyInput("checkbox")
                  .appendField('Set to Idle?')
                  .appendField(new Blockly.FieldCheckbox(false), 'set_idle');
                var options = getOptions(pairs);
                this.appendDummyInput("name")
                    .appendField(new Blockly.FieldDropdown(options), "pair_name");
                break;
            case "turn_pair_on":
                var options = getOptions(pairs);
                this.appendDummyInput("name")
                    .appendField(new Blockly.FieldDropdown(options), "pair_name");
                this.appendDummyInput("colour")
                    .appendField("Select Colour")
                    .appendField(new Blockly.FieldDropdown([["Red","1"], ["Blue","2"], ["Green","3"], ["Indigo","4"], ["Orange","5"]]), "col");
                break;
            case "def_ran_pair":
                this.appendDummyInput("name")
                    .appendField("Name")
                    .appendField(new Blockly.FieldTextInput('pair', this.registerPair), "name")
                this.registerPair("pair");
                this.appendDummyInput("checkbox")
                     .appendField('With sound?')
                     .appendField(new Blockly.FieldCheckbox(false), 'with_sound');
                break;
            default: break;
        }
    }
};

Blockly.Blocks['then_subrule'] = {
    init: function() {
        var options = [["Activate Sub Rule", "activate_subrule"],
                       ["Deactivate Sub Rule", "deactivate_subrule"]
                       ];
        this.appendDummyInput()
                .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
        this.setColour('#5b67a5');
        this.setOutput(true, "Then");
        this.setTooltip("");
        this.setHelpUrl("");
    },

    validate: function(value){
        this.getSourceBlock().updateConnections(value);
    },
    updateConnections: function(value){
        this.removeInput('name', true);
        var options = getOptions(subConfigs);
        this.appendDummyInput("name")
            .appendField("Select Sub Rule")
            .appendField(new Blockly.FieldDropdown(options), "rule_name");
    }
};

Blockly.Blocks['then_gen'] = {
    init: function() {
        var options = [["Play Sound", "play_sound"],
                       ["Stop Game", "stop_game"],
                       ];
        this.appendDummyInput()
                .appendField(new Blockly.FieldDropdown(options, this.validate), "action");
        this.setColour('#745ba5');
        this.setOutput(true, "Then");
        this.setTooltip("");
        this.setHelpUrl("");
    },

    validate: function(value){
        this.getSourceBlock().updateConnections(value);
    },
    updateConnections: function(value){
        this.removeInput('player', true);
        if(value === 'play_sound'){
            var sounds = [["Start", "start"],
                          ["End", "end"],
                          ["Step1","s1"],
                          ["Step2","s2"],
                          ["Press1","p1"],
                          ["Press2","p2"],
                          ["Press3","p3"],
                          ["Press4","p4"],
                          ["Matched","m"],
                          ["Error","e"]
                         ];
            this.appendDummyInput("sound")
               .appendField(new Blockly.FieldDropdown(sounds), "sound");
        }
    }
};

