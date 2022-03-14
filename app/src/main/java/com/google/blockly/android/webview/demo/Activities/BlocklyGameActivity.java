package com.google.blockly.android.webview.demo.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyGame;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGameDefinition;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BlocklyGameActivity extends AppCompatActivity implements OnAntEventListener {
    MotoConnection connection = MotoConnection.getInstance();
    BlocklyGamesStore store = BlocklyGamesStore.getInstance();

    BlocklyGame game;
    JSONObject selectedJsonGame;
    private Spinner spinner;
    private ArrayAdapter<Pair<String, Integer>> gameNamesAndIndexes;
    Button startButton;
    Handler handler;

    public BlocklyGameActivity() throws JSONException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        handler = new Handler(Looper.getMainLooper());
        connection.registerListener(this);
        connection.setAllTilesToInit();
        startButton = findViewById(R.id.blocklyStartbtn);
        startButton.setEnabled(false);
        this.spinner = this.findViewById(R.id.playGameDropdown);

        startButton.setOnClickListener(v -> {
            try {
                handler.removeCallbacksAndMessages(null);
                this.game = new BlocklyGame(this.selectedJsonGame, handler);
                this.game.setSelectedGameType(0);
                this.game.startGame();
            } catch (JSONException e) {
                Log.e("PARSE ERROR", e.getMessage(), e);
                //todo popup??
            }
        });
        initDropdown();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateGamNamesAndIndexes() throws IOException, JSONException {
        this.gameNamesAndIndexes.clear();
        this.addStandardGamesIfMissing();
        JSONArray games = store.getGames(this);
        //Always add "NONE" as first
        this.gameNamesAndIndexes.add(new Pair<>("NONE", -1));
        for (int i = 0; i < games.length(); i++) {
            String name = games.getJSONObject(i).getString("name");
            this.gameNamesAndIndexes.add(new Pair<>(name, i));
        }
        this.gameNamesAndIndexes.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDropdown(){
        Context context = this;
        this.gameNamesAndIndexes = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        this.spinner.setAdapter(this.gameNamesAndIndexes);
        this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Load clicked game
                try {
                    if(gameNamesAndIndexes.getItem(i).second == -1){
                        startButton.setEnabled(false);
                        return;
                    }
                    selectedJsonGame = store.getGames(context).getJSONObject(i-1);
                    startButton.setEnabled(true);
                } catch (IOException | JSONException e) {
                    Log.e("ERROR", e.toString());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
        try {
            this.populateGamNamesAndIndexes();

        } catch (IOException | JSONException e) {
            Log.e("ERROR", e.toString());
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addStandardGamesIfMissing() throws JSONException, IOException {
        String standardGames = "[{\"name\":\"race\",\"game\":{\"blocks\":{\"blocks\":[{\"fields\":{\"num\":1},\"id\":\"seUS3C-$NcE*DCT\\/M$2$\",\"inputs\":{\"end\":{\"block\":{\"id\":\"N{ftw|[u)--@($FL9(?f\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"TEXT\":\"score\"},\"id\":\"8[d|@{Flw%Bd0[KDanZ?\",\"type\":\"text\"}}},\"next\":{\"block\":{\"id\":\"uOYHeDRRrt%]m!pdj$gy\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"hy%`9Viv7|?)eaRe_s8S\",\"type\":\"get_player_score\"}}},\"type\":\"motosound_speak\"}},\"type\":\"motosound_speak\"}},\"gameType\":{\"block\":{\"fields\":{\"goal\":10,\"type\":\"s\"},\"id\":\"7Hf)6il)1KWN(eHcW\\/fS\",\"type\":\"gametype\"}},\"start\":{\"block\":{\"extraState\":{\"name\":\"game\"},\"id\":\"Zm^b)u;+$`g_p2)hC1zy\",\"type\":\"procedures_callnoreturn\"}}},\"type\":\"gameblock\",\"x\":1,\"y\":35},{\"fields\":{\"NAME\":\"game\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"vq$qXAOxLvX[Vo~0kKN{\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Xcwo=i;Kb?49v#oQvtN1\"}},\"id\":\"1%nK8GvbeWc#|sXB6+,g\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"from\":1,\"to\":4},\"id\":\"CC[]_Vv.cy3JE9M]@Jo%\",\"type\":\"randomnumber\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"4t5I#%gjR*mMSlr}CDp:\",\"inputs\":{\"BY\":{\"shadow\":{\"fields\":{\"NUM\":1},\"id\":\"06=koNGo$8MjgI*7IB%H\",\"type\":\"math_number\"}},\"DO\":{\"block\":{\"extraState\":{\"hasElse\":true},\"id\":\"Kk9Si)D)o%o#.w1Rol.%\",\"inputs\":{\"DO0\":{\"block\":{\"fields\":{\"action\":\"colour\"},\"id\":\"`n!KIK$e8{-0;XaYfa=S\",\"inputs\":{\"Colour\":{\"block\":{\"id\":\"^nthjl!gai1k^Y]s]6AC\",\"type\":\"randomcolour\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"Zb!D9,8{5m-}$mCn:DiI\",\"type\":\"variables_get\"}}},\"next\":{\"block\":{\"fields\":{\"action\":\"on_press\"},\"id\":\")(MSQwgixl2#o.-orJYm\",\"inputs\":{\"OP\":{\"block\":{\"fields\":{\"action\":\"add\",\"num\":1},\"id\":\"(\\/~_{jqBJ.X-NcE:^i:@\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"3CKyDj1bu*M_$FPFk%7l\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"A[#B|AyTeJ1.eVka|{AA\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"Fg|jdmZ={zb0|lyM.HOO\",\"type\":\"get_player_score\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"game\"},\"id\":\";j_-p9.tpqbhGn`BR;x,\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"motosound_speak\"}},\"type\":\"player\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"mF?D@*++1FA1ANOxyTyz\",\"type\":\"variables_get\"}}},\"type\":\"tile\"}},\"type\":\"tile\"}},\"ELSE\":{\"block\":{\"fields\":{\"action\":\"colour\"},\"id\":\"wq7D~`$^Z6!ARnRWI!Vd\",\"inputs\":{\"Colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\"$b4`jpTy8nDZg$gJ8_`P\",\"type\":\"colour\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"`H7ya2`bV8y%)tUAjEmL\",\"type\":\"variables_get\"}}},\"next\":{\"block\":{\"fields\":{\"action\":\"on_press\"},\"id\":\"ult!zL-^!XR!@O(D*Dds\",\"inputs\":{\"OP\":{\"block\":{\"fields\":{\"action\":\"sub\",\"num\":1},\"id\":\"q$7pzqofxTE72XS,7Zbk\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"mCmK:F+fk[:{VRpjcATP\",\"type\":\"number\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"game\"},\"id\":\"EvZG~Bm+DrQh~O4tXXjF\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"player\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"Xp@-!~R4PqNin5}Z9x`2\",\"type\":\"variables_get\"}}},\"type\":\"tile\"}},\"type\":\"tile\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"9HbpXmY)0)rnN.GfR{ol\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Xcwo=i;Kb?49v#oQvtN1\"}},\"id\":\"[uj[YnX]{4Jw;|+wus*E\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"mja+{5`~{47P!WV}H#(Y\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}},\"FROM\":{\"shadow\":{\"fields\":{\"NUM\":1},\"id\":\"u,gEiuwqrAp8p4=Qj31c\",\"type\":\"math_number\"}},\"TO\":{\"shadow\":{\"fields\":{\"NUM\":4},\"id\":\"{;QKq;[B6Z~~5ym+g0\\/|\",\"type\":\"math_number\"}}},\"type\":\"controls_for\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":5,\"y\":319}],\"languageVersion\":0},\"variables\":[{\"id\":\"Xcwo=i;Kb?49v#oQvtN1\",\"name\":\"tile\"},{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\",\"name\":\"i\"}]}},{\"name\":\"countdown\",\"game\":{\"blocks\":{\"blocks\":[{\"fields\":{\"num\":1},\"id\":\"@_^?fjS,~e4q*iwvH7rH\",\"inputs\":{\"end\":{\"block\":{\"id\":\"#G{a2-E6Y}?=7*j[Q?^t\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"TEXT\":\"Game Over\"},\"id\":\"amuHPn4w|e-Gn~@%px3B\",\"type\":\"text\"}}},\"type\":\"motosound_speak\"}},\"gameType\":{\"block\":{\"fields\":{\"goal\":30,\"type\":\"t\"},\"id\":\"+I67HDw-Oq2y*!{z81Fv\",\"type\":\"gametype\"}},\"start\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"`#j]S{XgS,\\/L)^HV)q8:\",\"inputs\":{\"BY\":{\"shadow\":{\"fields\":{\"NUM\":1},\"id\":\"3K8`-1l*s3!`%j4h(u.~\",\"type\":\"math_number\"}},\"DO\":{\"block\":{\"fields\":{\"action\":\"countdown\",\"speed\":\"M\"},\"id\":\"bEA0cGVm54rGSATgnq(J\",\"inputs\":{\"Colour\":{\"block\":{\"id\":\"NBmZuTwYg(|.o_Ai{p_p\",\"type\":\"randomcolour\"}},\"onend\":{\"block\":{\"id\":\"WB)ZOQjXXE(L_^M7w[]?\",\"type\":\"setgameover\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"23^eFtQO`NC0BJCyu_TH\",\"type\":\"variables_get\"}}},\"next\":{\"block\":{\"fields\":{\"action\":\"on_press\"},\"id\":\"{j|,8R=Yl*_?7X+j|`c*\",\"inputs\":{\"OP\":{\"block\":{\"fields\":{\"action\":\"countdown\",\"speed\":\"M\"},\"id\":\"XcP(p}7t[N}`[Q)W[i~b\",\"inputs\":{\"Colour\":{\"block\":{\"id\":\"r{t;]WUyL|m^?C;!7U1(\",\"type\":\"randomcolour\"}},\"onend\":{\"block\":{\"id\":\"|aHM9%IO02EXAzI]}Au}\",\"type\":\"setgameover\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"sZxWMtv_*)|HoM^uMU~[\",\"type\":\"variables_get\"}}},\"type\":\"tile\"}},\"tiles\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\"}},\"id\":\"os,p+g2C[hwfofgno5Ne\",\"type\":\"variables_get\"}}},\"type\":\"tile\"}},\"type\":\"tile\"}},\"FROM\":{\"shadow\":{\"fields\":{\"NUM\":1},\"id\":\"J?R(ed}|em^!E^bbu@2H\",\"type\":\"math_number\"}},\"TO\":{\"shadow\":{\"fields\":{\"NUM\":4},\"id\":\"Po1J:ET3[^pJW4s;CWj0\",\"type\":\"math_number\"}}},\"type\":\"controls_for\"}}},\"type\":\"gameblock\",\"x\":54,\"y\":126}],\"languageVersion\":0},\"variables\":[{\"id\":\"F+h3}{Tu$,Iao+,.7@KK\",\"name\":\"i\"}]}}]";
        JSONArray arr = new JSONArray(standardGames);
        JSONArray games = BlocklyGamesStore.getInstance().getGames(this);
        for (int i = 0; i < arr.length(); i++) {
            String name = arr.getJSONObject(i).getString("name");
            boolean exists = false;
            for (int j = 0; j < games.length(); j++) {
                if(games.getJSONObject(j).getString("name").equals(name)){
                    exists = true;
                }
            }
            if(!exists){
                BlocklyGamesStore.getInstance().saveGame(
                        this,
                        arr.getJSONObject(i).getJSONObject("game"),
                        name);
            }
        }

    }
    @Override
    public void onMessageReceived(byte[] bytes, long l) {
        this.game.addEvent(bytes);
    }

    @Override
    public void onAntServiceConnected() {

    }

    @Override
    public void onNumbersOfTilesConnected(int i) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.unregisterListener(this);
    }
}