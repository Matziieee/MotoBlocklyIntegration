package com.google.blockly.android.webview.demo;

public class PublishedGame extends GameObject {

    @Override
    public String toString() {
        return "Game Name: " + name + "\n" +
                "Posted by: " + userId;
    }
}
