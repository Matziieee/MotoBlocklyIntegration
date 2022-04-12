package com.google.blockly.android.webview;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.livelife.motolibrary.MotoConnection;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This fragments contains and manages the web view that hosts Blockly.
 */
public class BlocklyWebViewFragment extends Fragment {
    protected @Nullable WebView mWebView = null;

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        mWebView = new WebView(inflater.getContext());
        mWebView.setWebChromeClient(new WebChromeClient());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JsMethods(), "javaMethods");
        mWebView.loadUrl("file:///android_asset/blockly/webview.html");
        return mWebView;
    }
}
class JsMethods {
    @JavascriptInterface
    public String getConnectedTiles(){
        ArrayList<Integer> test = new ArrayList<>();
        test.add(1); test.add(2);
        //MotoConnection.getInstance().connectedTiles
        return MotoConnection.getInstance().connectedTiles.stream().map(Object::toString).collect(Collectors.joining(","));
        //return test.stream().map(Object::toString).collect(Collectors.joining(","));
    }
}
