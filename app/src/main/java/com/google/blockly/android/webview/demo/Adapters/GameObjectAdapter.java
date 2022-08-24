package com.google.blockly.android.webview.demo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.Online.GameObject;

import java.util.ArrayList;
import java.util.List;

public class GameObjectAdapter extends BaseAdapter {

    private Context c;
    List<GameObject> games;

    public GameObjectAdapter(@NonNull Context context, @NonNull List<GameObject> objects){
        c = context;
        games = objects;
    }

    public void clear(){
        games.clear();
    }

    public void add(GameObject obj){
        games.add(obj);
    }

    @Override
    public int getCount() {
        return games.size();
    }

    @Override
    public GameObject getItem(int i) {
        return games.get(i);
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

        GameObject obj = getItem(position);
        TextView name, by, type, published;
        name = v.findViewById(R.id.gameItem_actualName);
        by = v.findViewById(R.id.gameItem_by);
        type = v.findViewById(R.id.gameItem_type);
        published = v.findViewById(R.id.gameItem_published);

        name.setText(obj.getName());

        String gBy = "By: " + obj.getOriginalId();
        by.setText(gBy);

        String gType = "Type: " + obj.getType();
        type.setText(gType);

        String status = "Status: " + (obj.isPublished() ? "Published" : "Unpublished");
        published.setText(status );

        return v;
    }

}
