package com.google.blockly.android.webview.demo.Blocks.Image;

import com.google.blockly.android.webview.demo.Blocks.AbstractBlock;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ImageConfig extends AbstractBlock {
    private int players = 1;
    private TilesOn start; //We only accept Tiles On blocks here..
    private ArrayList<TilePress> tilePresses;

    public ImageConfig(JSONObject game) throws JSONException {
        super(game);
    }

    public int getPlayers() {
        return players;
    }

    public void setPlayers(int players) {
        this.players = players;
    }

    public TilesOn getStart() {
        return start;
    }

    public void setStart(TilesOn start) {
        this.start = start;
    }

    public ArrayList<TilePress> getTilePresses() {
        return tilePresses;
    }

    public void setTilePresses(ArrayList<TilePress> tilePresses) {
        this.tilePresses = tilePresses;
    }

    @Override
    protected void parseFromJson(JSONObject json) throws JSONException {
        tilePresses = new ArrayList<>();
        if(!json.has("inputs")){
            start = null;
        }
        if(json.getJSONObject("inputs").has("onStart")){
            JSONObject firstBlock = json.getJSONObject("inputs").getJSONObject("onStart").getJSONObject("block");
            TilesOn first = new TilesOn(firstBlock);
            JSONObject next = getNextIfPresent(firstBlock);
            start = first;
            if(next != null){
                start.setNext(parseNext(next));
            }
        }
        if(json.getJSONObject("inputs").has("pressRules")){
            tilePresses = parseTilePressRules(json.getJSONObject("inputs").getJSONObject("pressRules").getJSONObject("block"), new ArrayList<>());
        }
    }

    private ArrayList<TilePress> parseTilePressRules(JSONObject press, ArrayList<TilePress> list) throws JSONException {
        TilePress pressBlock = new TilePress(press);
        //Does it have any executables?
        JSONObject nextNext = getNextIfPresent(press);
        if(nextNext != null){
            //Recursion!
            pressBlock.setThen(parseNext(nextNext));
        }
        list.add(pressBlock);

        if(press.has("next")){
            return parseTilePressRules(press.getJSONObject("next").getJSONObject("block"), list);
        }
        return list;

    }
    private JSONObject getNextIfPresent(JSONObject curr) throws JSONException {
        if(curr.has("inputs")){
            return curr.getJSONObject("inputs").getJSONObject("next").getJSONObject("block");
        }
        return null;
    }
    private ExecutableNodeBlock parseNext(JSONObject next) throws JSONException {
        String type = next.getString("type");
        ExecutableNodeBlock nextBlock;
        if(type.equals("waiter")){
            //new Waiter block
            nextBlock = new Waiter(next);
        }else if(type.equals("repeat")){
            //new Repeat block - No blocks can follow this so always return
            return new Repeat(next);
        }
        else if(type.equals("plusScore")){
            return new Score(next);
        }
        else if(type.equals("minusScore")){
            return new Score(next);
        }
        else if(type.equals("animalSound")){
            return new PlaySound(next);
        }
        else{
            //Tiles on
            nextBlock = new TilesOn(next);
        }
        JSONObject nextNext = getNextIfPresent(next);
        if(nextNext != null){
            //Recursion!
            nextBlock.setNext(parseNext(nextNext));
        }
        return nextBlock;
    }
}
