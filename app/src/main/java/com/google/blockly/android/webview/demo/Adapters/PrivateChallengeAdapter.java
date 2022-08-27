package com.google.blockly.android.webview.demo.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.Online.PrivateChallenge;

import java.util.List;

//Todo basically a copy of GameObjectAdapter, should do something smarter here...
public class PrivateChallengeAdapter extends BaseAdapter {

    private Context c;
    List<PrivateChallenge> challenges;

    public PrivateChallengeAdapter(@NonNull Context context, @NonNull List<PrivateChallenge> objects){
        c = context;
        challenges = objects;
    }

    public void clear(){
        challenges.clear();
    }

    public void add(PrivateChallenge obj){
        challenges.add(obj);
    }

    @Override
    public int getCount() {
        return challenges.size();
    }

    @Override
    public PrivateChallenge getItem(int i) {
        return challenges.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = null;
        LayoutInflater inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            v = inflater.inflate(R.layout.game_item, parent, false);
        }else{
            v = convertView;
        }

        PrivateChallenge obj = getItem(position);
        TextView name, by, type, published;
        name = v.findViewById(R.id.gameItem_actualName);
        by = v.findViewById(R.id.gameItem_by);
        type = v.findViewById(R.id.gameItem_type);
        published = v.findViewById(R.id.gameItem_published);

        name.setText(obj.getGameName());

        String gBy = "By: " + obj.getCreator();
        by.setText(gBy);

        type.setVisibility(View.GONE);
        published.setVisibility(View.GONE);

        return v;
    }
}
