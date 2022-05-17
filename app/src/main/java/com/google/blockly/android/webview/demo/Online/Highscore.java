package com.google.blockly.android.webview.demo.Online;

public class Highscore {
    String userId, gameId, privateChallengeId;
    int score;
    boolean isPrivate;

    public Highscore(){}
    public Highscore(String userId, String gameId, int score) {
        this.userId = userId;
        this.gameId = gameId;
        this.score = score;
        this.isPrivate = false;
    }

    public Highscore(String userId, String gameId, String privateChallengeId, int score, boolean isPrivate) {
        this.userId = userId;
        this.gameId = gameId;
        this.privateChallengeId = privateChallengeId;
        this.score = score;
        this.isPrivate = isPrivate;
    }

    public String getPrivateChallengeId() {
        return privateChallengeId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivateChallengeId(String privateChallengeId) {
        this.privateChallengeId = privateChallengeId;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getUserId() {
        return userId;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "User: " + userId + "\n" + "Score: " + score;
    }
}
