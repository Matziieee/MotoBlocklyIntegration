package com.google.blockly.android.webview.demo;

public class GameListItem {
    private String name;
    private int index;
    private GameObject gameObject;

    public GameListItem(String name, int index) {
        this.name = name;
        this.index = index;
        this.gameObject = null;
    }

    public GameListItem(String name, int index, GameObject gameObject) {
        this.name = name;
        this.index = index;
        this.gameObject = gameObject;
    }

    public GameObject getGameObject() {
        return gameObject;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
