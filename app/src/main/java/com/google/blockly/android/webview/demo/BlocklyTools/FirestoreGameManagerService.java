package com.google.blockly.android.webview.demo.BlocklyTools;

import android.content.Context;
import android.provider.Settings.Secure;

import com.google.android.gms.tasks.Task;
import com.google.blockly.android.webview.demo.GameObject;
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
}
