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
import com.google.blockly.android.webview.demo.Online.Highscore;
import com.google.blockly.android.webview.demo.Online.PrivateChallenge;

import java.util.List;

public class HighscoreAdapter extends BaseAdapter {

    private Context c;
    List<Highscore> scores;

    public HighscoreAdapter(@NonNull Context context, @NonNull List<Highscore> objects){
        c = context;
        scores = objects;
    }

    public void clear(){
        scores.clear();
    }

    public void add(Highscore obj){
        scores.add(obj);
    }

    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Highscore getItem(int i) {
        return scores.get(i);
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
            v = inflater.inflate(R.layout.highscore_item, parent, false);
        }else{
            v = convertView;
        }

        Highscore obj = getItem(position);
        TextView user, score;
        user = v.findViewById(R.id.hs_user);
        //score = v.findViewById(R.id.hs_scoreText);

        user.setText(obj.getUserId());
        //score.setText(obj.getScore());

        return v;
    }
}
