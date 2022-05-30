package com.google.blockly.android.webview.demo;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.Nullable;

import com.google.blockly.android.webview.demo.Blocks.WhenThen.ConfigurationGameDefinition;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.RuleGameParser;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.SubConfigurationBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenPlayPattern;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenRegisterPattern;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.ThenWaitForSequence;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.Then.TilePressEvent;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenBlock;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenColorPress;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenPairPressed;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenPlayerScore;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenSubConfigActivated;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenTimePassed;
import com.google.blockly.android.webview.demo.Blocks.WhenThen.When.WhenType;
import com.google.blockly.android.webview.demo.LanguageLevels.GameStopper;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BlocklyRuleGame extends Game implements MotoConfigGameAPI{

    ConfigurationGameDefinition gameDef;
    GameStopper gameStopper;
    private Handler handler;
    private Random random;
    private MotoConnection mConnection;
    private HashMap<String, ArrayList<TilePressEvent>> patternMap;
    private HashMap<String, Pair<Integer, Integer>> pairMap;
    private HashMap<Integer, Integer> pairSounds;
    private HashMap<String, String> pairIdNameMap;
    private int lastPress = -1;

    HashMap<Integer, Integer> tileColorMap;

    private boolean isPlayingPattern = false, isRegisteringPattern = false;
    private String registeringPatternId;
    private int playingPatternIndex, registeringPatternTargetLength;
    private boolean isWaitingForPattern;
    private String waitingPatternId;
    private String waitingPatternOnCorrectId, waitingPatternOnIncorrectId;
    private ArrayList<Integer> waitingPatternPresses;
    private boolean shouldPausePlay = false;

    //todo fix stop game hack
    public BlocklyRuleGame(GameObject game, GameStopper stopper) throws JSONException {
        this(new JSONObject(game.getGame()), stopper);
    }
    public BlocklyRuleGame(JSONObject workspace, GameStopper stopper) throws JSONException {
        handler = new Handler();
        RuleGameParser parser = new RuleGameParser();
        this.gameDef = parser.parse(workspace);
        random = new Random(); //Can add seed here for debugging :)
        GameType gt = new GameType(1,
                GameType.GAME_TYPE_TIME,
                this.gameDef.getConfigBlock().getDuration(),
                "game",
                gameDef.getConfigBlock().getPlayers());
        addGameType(gt);
        patternMap = new HashMap<>();
        tileColorMap = new HashMap<>();
        mConnection = MotoConnection.getInstance();
        mConnection.connectedTiles.forEach(i -> {
            tileColorMap.put(i, 0);
        });
        this.gameStopper = stopper;
        this.pairMap = new HashMap<>();
        this.pairIdNameMap = new HashMap<>();
        this.pairSounds = new HashMap<>();
    }

    @Override
    public void onGameStart() {
        //Always set all to idle the turn off to reset...
        mConnection.setAllTilesColor(0);
        //Initialise all timers and run on start events
        gameDef.getRules().forEach(wb -> {
            if(wb.getType() == WhenType.GameStart){
                executeThens(wb.getThenBlocks(), null);
            }
            else if(wb.getType() == WhenType.XSecondsPassed){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if(isPlayingPattern || isRegisteringPattern){
                            handler.postDelayed(this, ((WhenTimePassed)wb).getInterval() * 1000L);
                        }else{
                            executeThens(wb.getThenBlocks(), null);
                            handler.postDelayed(this, ((WhenTimePassed)wb).getInterval() * 1000L);
                        }
                    }
                };
                handler.postDelayed(runnable,((WhenTimePassed)wb).getInterval() * 1000L);
            }
        });
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        if(this.isPlayingPattern){
            return;
        }
        int event = AntData.getCommand(message);
        int tile = AntData.getId(message);
        int color = tileColorMap.get(tile);
        TilePressEvent ev = new TilePressEvent(tile, color);
        System.out.println("RECEIVE UPDATE | tile: " + tile + " | colour " + color);
        if(event == AntData.EVENT_PRESS){
            if(isRegisteringPattern){
                ArrayList<TilePressEvent> list = this.patternMap.get(this.registeringPatternId);
                list.add(ev);
                if(list.size() == this.registeringPatternTargetLength){
                    isRegisteringPattern = false;
                    this.registeringPatternId = "";
                    this.registeringPatternTargetLength = 0;
                }

            }
            else if(isWaitingForPattern){
                this.waitingPatternPresses.add(tile);
                //Validate that newest index is equal to correct..
                int correct = this.patternMap.get(this.waitingPatternId).get(this.waitingPatternPresses.size()-1).getTile();
                if(tile != correct){
                    activateSubrule(this.waitingPatternOnIncorrectId);
                    stopWaitForSequence();
                }
                if(this.waitingPatternId != null && this.waitingPatternPresses.size() == this.patternMap.get(this.waitingPatternId).size()){
                    activateSubrule(this.waitingPatternOnCorrectId);
                    stopWaitForSequence();
                }
            }

            //todo, is this a good idea to do here?
            if(pairSounds.containsKey(tile)){
                playSound("an"+pairSounds.get(tile));
            }

            this.gameDef.getRules().forEach(wb -> {
                if(wb.getType() == WhenType.AnyTilePressed){
                    executeThens(wb.getThenBlocks(), ev);
                }
                else if(wb.getType() == WhenType.ColourTilePressed && color == ((WhenColorPress)wb).getCol()){
                    executeThens(wb.getThenBlocks(), ev);
                }
            });

            List<WhenBlock> whenPairPressed = this.gameDef.getRules().stream().filter(r -> r.getType() == WhenType.PairPressed).collect(Collectors.toList());
            final boolean[] shouldResetLast = {false};
            if(!whenPairPressed.isEmpty()){

                List<String> fulfilledPairs = this.pairMap.keySet().stream().filter(k ->  {
                    Pair<Integer, Integer> p = this.pairMap.get(k);
                    return lastPress != tile && (p.first == lastPress || p.second == lastPress) && (p.first == tile || p.second == tile);
                }).collect(Collectors.toList());
                if(!fulfilledPairs.isEmpty()){
                    whenPairPressed.forEach(wb -> {
                        String pairId = pairIdNameMap.get(((WhenPairPressed)wb).getPairId());
                        if(fulfilledPairs.contains(pairId)){
                            shouldResetLast[0] = true;
                            executeThens(wb.getThenBlocks(), ev);
                        }
                    });
                }
            }
            if(shouldResetLast[0]){
                this.lastPress = -1;
            }else{
                lastPress = tile;
            }
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        this.handler.removeCallbacksAndMessages(null);
        gameDef.getRules().forEach(wb -> {
            if(wb.getType() == WhenType.GameEnd){
                executeThens(wb.getThenBlocks(), null);
            }
        });
        mConnection.setAllTilesBlink(3, 1);
        this.gameStopper.stop();
    }


    private void executeThens(List<ThenBlock> thens, @Nullable TilePressEvent ev){
        for (int i = 0; i < thens.size(); i++) {
            if(thens.get(i) instanceof ThenPlayPattern){
                if(isPlayingPattern){
                    handler.removeCallbacksAndMessages("playRunner");
                }
                thens.get(i).execute(this,ev);
                final int index = i;
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        if(isPlayingPattern){
                            Log.i("YO", "run: Waiting");
                            handler.postAtTime(this,"playRunner", SystemClock.uptimeMillis() + 200);
                        }else{
                            Log.i("YO", "run: Executing rest");
                            if(index != thens.size()-1){
                                Log.i("YO", "run: hopefully not here");
                                List<ThenBlock> remaining = thens.subList(index+1, thens.size());
                                executeThens(remaining,ev);
                            }
                        }
                    }
                }, "playRunner",SystemClock.uptimeMillis() + 200);
            }
            else if(thens.get(i) instanceof ThenRegisterPattern){
                if(isRegisteringPattern){
                    this.patternMap.get(this.registeringPatternId).clear();
                    handler.removeCallbacksAndMessages("registerRunner");
                }
                thens.get(i).execute(this, ev);
                final int index = i;
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        if(isRegisteringPattern){
                            handler.postAtTime(this,"registerRunner", SystemClock.uptimeMillis() + 200);
                        }
                        else{
                            if(index != thens.size()-1){
                                List<ThenBlock> remaining = thens.subList(index+1, thens.size());
                                executeThens(remaining,ev);
                            }
                        }
                    }
                }, "registerRunner", SystemClock.uptimeMillis() + 200);
                break;
            }
            else if(thens.get(i) instanceof ThenWaitForSequence){
                if(isWaitingForPattern){
                    handler.removeCallbacksAndMessages("waitSequenceRunner");
                }
                thens.get(i).execute(this, ev);
                final int index = i;
                handler.postAtTime(new Runnable() {
                    @Override
                    public void run() {
                        if(isWaitingForPattern){
                            handler.postAtTime(this,"waitSequenceRunner", SystemClock.uptimeMillis() + 200);
                        }
                        else{
                            if(index != thens.size()-1){
                                List<ThenBlock> remaining = thens.subList(index+1, thens.size());
                                executeThens(remaining,ev);
                            }
                        }
                    }
                }, "waitSequenceRunner", SystemClock.uptimeMillis() + 200);
                break;

            }
            else{
                thens.get(i).execute(this, ev);
            }
        }
    }

    private void checkForPlayerScoreEvent(int player){
        int newScore = this.getPlayerScore()[player];
        this.gameDef.getRules().forEach(wb ->{
            if(wb.getType() == WhenType.PlayerScore){
                if( ((WhenPlayerScore)wb).getScore() == newScore){
                    executeThens(wb.getThenBlocks(), null);
                }
            }
        });
    }

    //todo change these..
    @Override
    public void decrementPlayerScore(int player) {
        this.incrementPlayerScore(-1, player);
        checkForPlayerScoreEvent(player);
    }

    @Override
    public void incrementPlayerScore(int player) {
        this.incrementPlayerScore(1, player);
        checkForPlayerScoreEvent(player);
    }

    @Override
    public void setAllTilesColor(int color) {
        mConnection.connectedTiles.forEach(i -> {
            Log.i("SETTING COLOR", "TILE: " + i + " COLOR: " + color);
            setTileColor(i, color);
        });
    }

    @Override
    public void setTilesRandomColor(boolean isSame) {
        if(isSame){
            setAllTilesColor(random.nextInt(5)+1);
        }else{
            mConnection.connectedTiles.forEach(i -> {
                int color = random.nextInt(5)+1;
                setTileColor(i, color);
            });
        }
    }

    @Override
    public void registerPattern(String id, String name, int length){
        this.isRegisteringPattern = true;
        this.patternMap.put(id, new ArrayList<>());
        this.registeringPatternId = id;
        this.registeringPatternTargetLength = length;
    }

    @Override
    public void playPattern(String targetId) {
        playingPatternIndex = 0;
        isPlayingPattern = true;
        this.shouldPausePlay = false;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!patternMap.containsKey(targetId) || patternMap.get(targetId).size() == playingPatternIndex){
                    isPlayingPattern = false;
                    setAllTilesColor(0);
                    return;
                }
                setAllTilesColor(0);
                mConnection.setAllTilesIdle(0);

                if(shouldPausePlay){
                    shouldPausePlay = false;
                    handler.postDelayed(this, 500);
                }else{
                    shouldPausePlay = true;
                    TilePressEvent ev = patternMap.get(targetId).get(playingPatternIndex);
                    setTileColor(ev.getTile(), ev.getColor() == 0 ? 1 : ev.getColor());
                    playingPatternIndex++;
                    handler.postDelayed(this, 1000);
                }
            }
        }, 0);
    }

    @Override
    public void setTileRandomColor(int tile) {
        int color = random.nextInt(5)+1;
        this.setTileColor(tile, color);

    }

    private void setTileIdleColor(int tile, int color){
        this.tileColorMap.put(tile, color);
        mConnection.setTileIdle(color, tile);
        if (tileColorMap.values().stream().allMatch(v -> v == 0)){
            this.gameDef.getRules().stream()
                    .filter( wb -> wb.getType() == WhenType.AllTilesOff)
                    .forEach(wb -> {
                        executeThens(wb.getThenBlocks(),null);
                    });
        }
    }

    @Override
    public void setTileColor(int tile, int color) {
        this.tileColorMap.put(tile, color);
        mConnection.setTileColor(color, tile);
        if (tileColorMap.values().stream().allMatch(v -> v == 0)){
            this.gameDef.getRules().stream()
                    .filter( wb -> wb.getType() == WhenType.AllTilesOff)
                    .forEach(wb -> {
                        executeThens(wb.getThenBlocks(),null);
                    });
        }
    }

    @Override
    public void setAllTilesExceptRandomColor(int tile, boolean isSame) {
        int color = random.nextInt(5)+1;
        this.mConnection.connectedTiles.forEach(t -> {
            if(t != tile){
                if(isSame){
                    this.setTileColor(tile, color);
                }else{
                    this.setTileRandomColor(t);
                }
            }
        });
    }

    @Override
    public void setAllTilesExceptColor(int tile, int color) {
        this.mConnection.connectedTiles.forEach(t -> {
            if(t != tile){
                this.setTileColor(t, color);
            }
        });
    }

    @Override
    public void playSound(String sound) {
        MotoSound player = MotoSound.getInstance();
        switch (sound){
            case "an0":
                player.playAnimalSound(0);
                break;
            case "an1":
                player.playAnimalSound(1);
                break;
            case "an2":
                player.playAnimalSound(2);
                break;
            case "an3":
                player.playAnimalSound(3);
                break;
            case "an4":
                player.playAnimalSound(4);
                break;
            case "an5":
                player.playAnimalSound(5);
                break;
            case "an6":
                player.playAnimalSound(6);
                break;
            case "an7":
                player.playAnimalSound(7);
                break;
            case "an8":
                player.playAnimalSound(8);
                break;
            case "an9":
                player.playAnimalSound(9);
                break;
            case "start":
                player.playStart();
                break;
            case "end":
                player.playStop();
                break;
            case "s1":
                player.playStep();
                break;
            case "s2":
                player.playStep2();
                break;
            case "p1":
                player.playPress1();
                break;
            case "p2":
                player.playPress2();
                break;
            case "p3":
                player.playPress3();
                break;
            case "p4":
                player.playPress4();
                break;
            case "m":
                player.playMatched();
                break;
            case "e":
                player.playError();
                break;
        }
    }

    private int getRandomUnusedTile(List<Integer> used, @Nullable Integer reserved){
        List<Integer> available = mConnection
                .connectedTiles.stream().filter(t -> {
                    return !used.contains(t);
                }).collect(Collectors.toList());
        //Panic.. game will surely break here..
        //but we just return a random int from connectedTiles..
        if(available.isEmpty()){
            return this.random.nextInt(mConnection.connectedTiles.size())+1;
        }

        while(true){
            int res = available.get(this.random.nextInt(available.size()));
            if(reserved == null){
                return res;
            }
            else if(res != reserved){
                return res;
            }
        }
    }

    private int getUnusedAnimalSound(List<Integer> used){
        int sound = 0;
        while (used.contains(sound)){
            sound = random.nextInt(10);
        }
        return sound;
    }

    @Override
    public void defineRandomPair(String id, String name, boolean withSound) {
        //Find used tiles..
        List<Integer> used = this.mConnection.connectedTiles.stream().filter(t ->
                pairMap.keySet().stream().anyMatch(k -> {
                    Pair<Integer, Integer> p = pairMap.get(k);
                    return !k.equals(name) && (p.first.equals(t) || p.second.equals(t));
                })
            ).distinct().collect(Collectors.toList());

        int tile1 = this.getRandomUnusedTile(used, null);
        used.add(tile1);
        int tile2 = this.getRandomUnusedTile(used, tile1);
        if(withSound){
            int sound = getUnusedAnimalSound(this.pairSounds.values().stream().distinct().collect(Collectors.toList()));
            this.pairSounds.put(tile1, sound);
            this.pairSounds.put(tile2, sound);
        }
        this.pairMap.put(name, new Pair<>(tile1, tile2));
        this.pairIdNameMap.put(id, name);
    }

    @Override
    public void defineRandomSequence(String id, String name, int length) {
        ArrayList<TilePressEvent> sequence = new ArrayList<>();
        while (sequence.size() != length){
            // New random tile
            int tile = this.random.nextInt(mConnection.connectedTiles.size())+1;
            // Random color
            int color = random.nextInt(5)+1;
            sequence.add(new TilePressEvent(tile, color));
        }
        this.patternMap.put(id, sequence);
    }

    @Override
    public void waitForSequence(String patternId, String correctName, String incorrectName) {
        this.isWaitingForPattern = true;
        this.waitingPatternId = patternId;
        this.waitingPatternOnCorrectId = correctName;
        this.waitingPatternOnIncorrectId = incorrectName;
        this.waitingPatternPresses = new ArrayList<>();
    }
    private void stopWaitForSequence(){
        this.isWaitingForPattern = false;
        this.waitingPatternId = null;
        this.waitingPatternOnCorrectId = null;
        this.waitingPatternOnIncorrectId = null;
    }

    @Override
    public void turnPairOn(String id, int color) {
        //Find actual pair name
        String pairName = this.pairIdNameMap.get(id);
        Pair<Integer, Integer> tiles = this.pairMap.get(pairName);
        setTileColor(tiles.first, color);
        setTileColor(tiles.second, color);

    }

    @Override
    public void activateSubrule(String name) {
        SubConfigurationBlock subrule = this.gameDef.getSubRules().stream().filter(sr -> {
            return sr.getId().equals(name);
        }).findFirst().get();
        subrule.setActive(true);
        if(subrule.isReplaceRules()){
            this.gameDef.getSubRules().forEach(r -> {
                if(!r.getId().equals(subrule.getId())){
                    r.setActive(false);
                }
            });
        }

        this.gameDef.getRules().stream().filter(r -> r.getType() == WhenType.SubRuleActive).forEach(wb -> {
            if(((WhenSubConfigActivated)wb).getName().equals(name)){
                executeThens(wb.getThenBlocks(),null);
            }
        });
    }

    @Override
    public void deactivateSubrule(String name) {
        this.gameDef.getSubRules().stream().filter(sr -> {
            return sr.getId().equals(name);
        }).findFirst().get().setActive(false);
    }

    @Override
    public void setRandomTileColor(int color) {
        int index = this.random.nextInt(mConnection.connectedTiles.size());
        int tile = mConnection.connectedTiles.get(index);
        setTileColor(tile, color);
    }

    @Override
    public void setRandomTileColorAndRestColor(int color, int color2) {
        int index = this.random.nextInt(mConnection.connectedTiles.size());
        int tile = mConnection.connectedTiles.get(index);
        mConnection.connectedTiles.forEach(t -> {
            if(t == tile){
                setTileColor(t, color);
            }else{
                setTileColor(t, color2);
            }
        });
    }

    @Override
    public void turnPairOff(String pairId, boolean shouldSetIdle) {
        String pairName = this.pairIdNameMap.get(pairId);
        Pair<Integer, Integer> tiles = this.pairMap.get(pairName);
        if(shouldSetIdle){
            setTileIdleColor(tiles.first, 0);
            setTileIdleColor(tiles.second, 0);
        }else{
            setTileColor(tiles.first, 0);
            setTileColor(tiles.second, 0);
        }
    }

    @Override
    public void clearPairs() {
        this.pairMap = new HashMap<>();
        this.pairIdNameMap = new HashMap<>();
        this.pairSounds = new HashMap<>();
    }

    @Override
    public void speak(String text) {
        MotoSound.getInstance().speak(text);
    }

    @Override
    public void sayPlayerScore(int player) {
        int[] scores = this.getPlayerScore();
        if(scores.length > player){
            MotoSound.getInstance().speak(scores[player]+"");
        }
    }
}
