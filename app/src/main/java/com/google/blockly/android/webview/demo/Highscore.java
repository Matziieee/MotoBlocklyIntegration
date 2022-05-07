package com.google.blockly.android.webview.demo;

public class Highscore {
    String userId, gameId;
    int score;

    public Highscore(){}
    public Highscore(String userId, String gameId, int score) {
        this.userId = userId;
        this.gameId = gameId;
        this.score = score;
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
