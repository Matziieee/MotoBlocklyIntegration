package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.android.gms.tasks.Task;
import com.google.blockly.android.webview.demo.GameObject;
import com.google.firebase.firestore.QuerySnapshot;

public interface IGameManagerService {

    Task<Void> saveGame(GameObject obj);
    Task<Void> deleteGame(String id);
    Task<QuerySnapshot> getMyGames();
    Task<QuerySnapshot> getPublishedGames();
    Task<Void> publishGame(GameObject originalGame);
    Task<Void> makeGamePrivate(GameObject item);
}
