package com.google.blockly.android.webview.demo;

import android.os.Handler;

import com.google.blockly.android.webview.IMotoImageGameAPI;
import com.google.blockly.android.webview.demo.Blocks.Image.ExecutableNodeBlock;
import com.google.blockly.android.webview.demo.Blocks.Image.ImageConfig;
import com.google.blockly.android.webview.demo.Blocks.Image.Repeat;
import com.google.blockly.android.webview.demo.Blocks.Image.Waiter;
import com.google.blockly.android.webview.demo.LanguageLevels.GameStopper;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.livelife.motolibrary.AntData;
import com.livelife.motolibrary.Game;
import com.livelife.motolibrary.GameType;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

public class BlocklyImageGame extends Game implements IMotoImageGameAPI {

    ImageConfig config;
    private HashMap<Integer, Integer> tileColorMap;

    public ImageConfig getConfig() {
        return config;
    }
    private final Handler executeHandler;
    private GameStopper stopper;
    private final MotoConnection mConnection;
    private final MotoSound mSound;
    private final Random random;

    public BlocklyImageGame(GameObject game, GameStopper stopper) throws JSONException {
        this(new JSONObject(game.getGame()), stopper);
    }

    public BlocklyImageGame(JSONObject game, GameStopper stopper) throws JSONException {
        // First block is always root block which is the only one we care about :)
        // Extract press and start rules etc based on this guy
        config = new ImageConfig(game.getJSONObject("blocks").getJSONArray("blocks").getJSONObject(0));
        this.stopper = stopper;
        executeHandler = new Handler();
        random = new Random();
        tileColorMap = new HashMap<>();
        mSound = MotoSound.getInstance();
        mConnection = MotoConnection.getInstance();
        mConnection.connectedTiles.forEach(i -> {
            tileColorMap.put(i, 0);
        });
        GameType gt = new GameType(1,
                GameType.GAME_TYPE_TIME,
                45,
                "game",
                getConfig().getPlayers());
        addGameType(gt);
    }

    @Override
    public void onGameStart() {
        super.onGameStart();
        //Turn all tiles off (also removes them from idle state)
        mConnection.setAllTilesColor(0);

        //Run start sequence
        executeSequence(this.getConfig().getStart());
    }

    @Override
    public void onGameUpdate(byte[] message) {
        super.onGameUpdate(message);
        //Handle presses...
        int event = AntData.getCommand(message);
        int tile = AntData.getId(message);
        int color = tileColorMap.get(tile);
        if(event == AntData.EVENT_PRESS){
            // Always execute "any step" rule
            config.getTilePresses().stream()
                    .filter(tp -> tp.getColor() == -1)
                    .forEach(tp -> executeSequence(tp.getThen()));

            // Find all "rules" on the specific color!
            config.getTilePresses().stream()
                    .filter(tp -> tp.getColor() == color)
                    .forEach(tp -> executeSequence(tp.getThen()));
        }
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        executeHandler.removeCallbacksAndMessages(null);
        mConnection.setAllTilesBlink(3, 1);
        stopper.stop();
    }

    private void executeSequence(ExecutableNodeBlock block){
        IMotoImageGameAPI api = this;
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                // Is repeat?
                if(block instanceof Repeat){
                    executeSequence(block.getRoot());
                    return;
                }
                // Is wait?
                if(block instanceof Waiter && block.getNext() != null){
                    executeHandler.postDelayed(() -> executeSequence(block.getNext()), 1000);
                    return;
                }

                block.execute(api);
                if(block.getNext() != null){
                    executeHandler.post(() -> executeSequence(block.getNext()));
                }
            }
        };
        executeHandler.post(runner);
    }

    @Override
    public void setAllColor(int color) {
        if(color == -1){
            mConnection.connectedTiles.forEach(i -> {
                int rand = random.nextInt(4)+1;
                setTileColor(i, rand);
            });
        }else{
            mConnection.connectedTiles.forEach(i -> {
                setTileColor(i, color);
            });
        }
    }

    private void setTileColor(int tile, int color){
        this.tileColorMap.put(tile, color);
        mConnection.setTileColor(color, tile);
    }

    @Override
    public void incrementScore() {
        incrementPlayerScore(1, 0);
    }

    @Override
    public void decrementScore() {
        incrementPlayerScore(-1, 0);
    }

    @Override
    public void playAnimalSound() {
        mSound.playAnimalSound(random.nextInt(10));
    }
}
