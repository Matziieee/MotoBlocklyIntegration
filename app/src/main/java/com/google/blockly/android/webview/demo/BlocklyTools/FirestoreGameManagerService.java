package com.google.blockly.android.webview.demo.BlocklyTools;

import android.content.Context;
import android.provider.Settings.Secure;

import com.google.android.gms.tasks.Task;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.google.blockly.android.webview.demo.Online.Highscore;
import com.google.blockly.android.webview.demo.Online.PrivateChallenge;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class FirestoreGameManagerService implements IGameManagerService{

    private final String deviceId;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirestoreGameManagerService(Context context) {
        this.deviceId = Secure.getString(context.getContentResolver(),
                Secure.ANDROID_ID);
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public Task<Void> saveGame(GameObject obj) {
        HashMap<String, Object> data = obj.getHashMap();
        data.put("userId", deviceId);
        if(obj.getOriginalId() == null){
            data.put("originalId", deviceId);
        }
        return db.collection("games").document(obj.getId()).set(data);
    }

    @Override
    public Task<Void> deleteGame(String id) {
        return db.collection("games").document(id).delete();
    }

    @Override
    public Task<QuerySnapshot> getMyGames() {
        return db.collection("games").whereEqualTo("userId", deviceId).get();
    }

    @Override
    public Task<QuerySnapshot> getPublishedGames() {
        return db.collection("games").whereEqualTo("published", true).get();
    }

    @Override
    public Task<Void> publishGame(GameObject originalGame) {
        originalGame.setPublished(true);
        return db.collection("games").document(originalGame.getId()).update("published", true);
    }

    @Override
    public Task<Void> makeGamePrivate(GameObject item) {
        item.setPublished(false);
        return db.collection("games").document(item.getId()).update("published", false);
    }

    @Override
    public Task<QuerySnapshot> getHighscores(String id) {
        return db.collection("highscores").whereEqualTo("gameId",id).whereEqualTo("private", false).get();
    }

    @Override
    public Task<DocumentReference> postHighscore(String gameId, int score) {
        Highscore hs = new Highscore(this.deviceId, gameId, score);
        return db.collection("highscores").add(hs);
    }

    @Override
    public Task<QuerySnapshot> getPrivateChallenges() {
        //Get all challenges where my id is included
        return db.collection("private_challenges").whereArrayContains("players", deviceId).get();
    }

    @Override
    public Task<Void> createPrivateChallenge(PrivateChallenge challenge) {
        if(challenge.getPlayers().isEmpty()){
            challenge.getPlayers().add(deviceId);
        }
        return db.collection("private_challenges").document(challenge.getKey()).set(challenge);
    }

    @Override
    public Task<Void> joinExistingGame(String key) {
        return db.collection("private_challenges").document(key).update("players", FieldValue.arrayUnion(deviceId));
    }

    @Override
    public Task<DocumentSnapshot> getGame(String id) {
        return db.collection("games").document(id).get();
    }

    @Override
    public Task<QuerySnapshot> getPrivateHighscores(String challengeId) {
        return db.collection("highscores").whereEqualTo("privateChallengeId",challengeId).whereEqualTo("private", true).get();
    }

    @Override
    public Task<DocumentReference> postPrivateHighscore(String gameId, int score, String challengeId) {
        Highscore hs = new Highscore(deviceId, gameId, challengeId, score, true);
        return db.collection("highscores").add(hs);
    }
}
