package com.google.blockly.android.webview.demo.Blocks.Image;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ExecutableNodeBlock extends Executable{
    private ExecutableNodeBlock previous, next;

    public ExecutableNodeBlock(JSONObject json) throws JSONException {
        super(json);
    }

    public ExecutableNodeBlock getPrevious() {
        return previous;
    }

    public void setPrevious(ExecutableNodeBlock previous) {
        this.previous = previous;
    }

    public ExecutableNodeBlock getNext() {
        return next;
    }

    public void setNext(ExecutableNodeBlock next) {
        this.next = next;
        this.next.setPrevious(this);
    }

    public ExecutableNodeBlock getRoot(){
        if(this.previous == null){
            return this;
        }
        return previous.getRoot();
    }
}
