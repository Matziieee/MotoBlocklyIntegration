package com.google.blockly.android.webview.demo.BlocklyTools;

import com.google.android.gms.tasks.Task;
import com.google.blockly.android.webview.demo.Online.GameObject;
import com.google.blockly.android.webview.demo.Online.PrivateChallenge;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface IGameManagerService {

    Task<Void> saveGame(GameObject obj);
    Task<Void> deleteGame(String id);
    Task<QuerySnapshot> getMyGames();
    Task<QuerySnapshot> getPublishedGames();
    Task<Void> publishGame(GameObject originalGame);
    Task<Void> makeGamePrivate(GameObject item);
    Task<QuerySnapshot> getHighscores(String id);
    Task<DocumentReference> postHighscore(String gameId, int score);
    Task<QuerySnapshot> getPrivateChallenges();
    Task<Void> createPrivateChallenge(PrivateChallenge challenge);
    Task<Void> joinExistingGame(String key);
    Task<DocumentSnapshot> getGame(String id);
    Task<QuerySnapshot> getPrivateHighscores(String challengeId);
    Task<DocumentReference> postPrivateHighscore(String gameId, int score, String challengeId);
}
