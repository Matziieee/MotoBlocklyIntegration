package com.google.blockly.android.webview.demo;

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
    MotoSound sound = MotoSound.getInstance();
    BlocklyGame game;
    JSONObject selectedJsonGame;
    private Spinner spinner;
    private ArrayAdapter<Pair<String, Integer>> gameNamesAndIndexes;
    Button startButton;
    Handler handler;

    public BlocklyGameActivity() throws JSONException {
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blockly_game2);

        handler = new Handler(Looper.getMainLooper());
        connection.registerListener(this);
        connection.setAllTilesToInit();
        sound.initializeSounds(this);
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
                throw new RuntimeException("Could not parse game");
            }
        });
        initDropdown();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateGamNamesAndIndexes() throws IOException, JSONException {
        this.gameNamesAndIndexes.clear();
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


    /*@RequiresApi(api = Build.VERSION_CODES.N)
    private void addStandardGamesIfMissing() throws JSONException, IOException {
        String standardGames = "[{\"name\":\"Race\",\"game\":{\"blocks\":{\"blocks\":[{\"id\":\"mXdCjEpT%rR@O7evQyWB\",\"inputs\":{\"name\":{\"block\":{\"fields\":{\"TEXT\":\"Race\"},\"id\":\"@.UZAY-X`u}Tz%nM6$pE\",\"type\":\"text\"}},\"numplayers\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"o%`5{L:#Ta=X8aAp}pTH\",\"type\":\"number\"}},\"numtiles\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"aJe8cC@gveMq@YiR{ST`\",\"type\":\"number\"}},\"type\":{\"block\":{\"fields\":{\"goal\":30,\"type\":\"t\"},\"id\":\"bd!ArGQVhnz#;!3;HlS:\",\"type\":\"gametype\"}}},\"type\":\"configuration\",\"x\":39,\"y\":39},{\"id\":\"B@!Aw5g_\\/1u|4!xhuj_`\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"6Hv3daLgptab=J@aX~~4\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"Ry_1*ub2Y2N)Fs6xOFvm\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\":D]@CY1zph]e{;~$baGo\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"1MX%MGm{d=u~V5H.+k1W\"}},\"id\":\"7]!*TchsDrFAgmrMe%g[\",\"type\":\"variables_get\"}}},\"next\":{\"block\":{\"id\":\"~I!.D|#!|y#jl9ejtLEn\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"BlSp)$,FTt%aD\\/~2)2U#\",\"type\":\"number\"}},\"score\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"H0_.k\\/i$Z#3[wL)pSPMQ\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"{dj?LwfaX@U+.dP^BN,c\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"wF~JlaDjBC#7Vn7F~16}\",\"type\":\"get_player_score\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameLoop\"},\"id\":\"j2LDHN.E!K|^PYn+q8Sz\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"motosound_speak\"}},\"type\":\"addplayerscore\"}},\"type\":\"settilecolor\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\",7Lb0l3GzVb@MOA|nUV5\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"NW*~gOuJN~@2cOc:J2u~\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"1MX%MGm{d=u~V5H.+k1W\"}},\"id\":\"@sDbw!`)|[^pR;P@l{Yg\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}}},\"type\":\"ontilepress\",\"x\":26,\"y\":245},{\"fields\":{\"NAME\":\"gameLoop\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"mG=).N#$|xYRu|hIf5`u\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"1MX%MGm{d=u~V5H.+k1W\"}},\"id\":\"^~MgmU;gv4`Tkbwci+\\/7\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"from\":1,\"to\":4},\"id\":\"_c(z|9zk:lbJA~9BW9eW\",\"type\":\"randomnumber\"}}},\"next\":{\"block\":{\"id\":\"1Qt0B0F{n@oGp(sDKrLV\",\"inputs\":{\"colour\":{\"block\":{\"id\":\"xi;[[C9r.T86p:OO*P,_\",\"type\":\"randomcolour\"}},\"tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"1MX%MGm{d=u~V5H.+k1W\"}},\"id\":\"LFnbhrM3r~{Qi~05Hs;s\",\"type\":\"variables_get\"}}},\"type\":\"settilecolor\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":35,\"y\":576},{\"id\":\"_+g,:nHXL2C*lcf[fgyO\",\"inputs\":{\"statements\":{\"block\":{\"id\":\":.:Bx+VbGG2{v[fL$1Mn\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\"@,]t+Exx9G9Ws4tK|pn\\/\",\"type\":\"colour\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameLoop\"},\"id\":\"as!VLRU`}U).3j9Z$qg-\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"settilesidle\"}}},\"type\":\"ongamestart\",\"x\":43,\"y\":758},{\"id\":\"5cR|!HZbUPlvl7!Wf*e]\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"}$?RK}hu.IlO7|Mk=oAA\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"TEXT\":\"Your final score is\"},\"id\":\"Y?ih)`zi{BoVy)vL+LQu\",\"type\":\"text\"}}},\"next\":{\"block\":{\"id\":\"QcQQh}raHq}uj(q**biD\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"y2AA!AmI[O73F3m#;VA8\",\"type\":\"get_player_score\"}}},\"type\":\"motosound_speak\"}},\"type\":\"motosound_speak\"}}},\"type\":\"ongameend\",\"x\":43,\"y\":933}],\"languageVersion\":0},\"variables\":[{\"id\":\"1MX%MGm{d=u~V5H.+k1W\",\"name\":\"tile\"}]}},{\"name\":\"Odd one out\",\"game\":{\"blocks\":{\"blocks\":[{\"id\":\"E\\/[gh3s.bN0]BJAr.5m6\",\"inputs\":{\"name\":{\"block\":{\"fields\":{\"TEXT\":\"Odd one out\"},\"id\":\"w_7(F^M+XlCEUIb+0ZCI\",\"type\":\"text\"}},\"numplayers\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"vRX%5{-RtGoXc9*UGGaa\",\"type\":\"number\"}},\"numtiles\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"v[sH3YrYoKlB~_m)#sUI\",\"type\":\"number\"}},\"type\":{\"block\":{\"fields\":{\"goal\":30,\"type\":\"t\"},\"id\":\"hEtSe^jRP1w:Uf;-:;w:\",\"type\":\"gametype\"}}},\"type\":\"configuration\",\"x\":7,\"y\":65},{\"id\":\"Ma3}asr_KH_RJ$:5~85O\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"@JpN9vjbeFd1L$)oJ2#0\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\"}C~eRr=k_CE`1ic~9kA8\",\"type\":\"colour\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameLoop\"},\"id\":\"9(I6(KUC4_C?:v-\\/i2R0\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"settilesidle\"}}},\"type\":\"ongamestart\",\"x\":13,\"y\":258},{\"id\":\"fm3%`=Z}7m`ef?sjRo$Y\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"=`)\\/*CA]UtEDRJOs%C4V\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"TEXT\":\"Your score was\"},\"id\":\"fAIu(7WR.|uRiXm%La*W\",\"type\":\"text\"}}},\"next\":{\"block\":{\"id\":\"E)*-rUB`Uq+HVH%7+X5R\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"?Er^ICaGLC[{Vcz;7bV$\",\"type\":\"get_player_score\"}}},\"type\":\"motosound_speak\"}},\"type\":\"motosound_speak\"}}},\"type\":\"ongameend\",\"x\":18,\"y\":411},{\"id\":\"ruej[(+knIeo$UM#Ii*:\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"mgi@raU-91#4z1iViWIx\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"-PT]wi]jBO#OGEg_`}9m\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"*A5=A^:.j`40VMW\\/jvlv\",\"type\":\"number\"}},\"score\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"OaCYw|BXV[m02P$%c!cd\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"xx^A6Wkp~ib70-F2wTt*\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"kC08eCgY{R|pR,^[^^x7\",\"type\":\"get_player_score\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameLoop\"},\"id\":\"O:M|Nl;_](a4bA[bRe9B\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"motosound_speak\"}},\"type\":\"addplayerscore\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"b}l9Rg^`6+G1?=sP+=2*\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"L1K5qD+if;~d]iapvU|-\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"lI^aBA=C[-AV~*[(V{ID\"}},\"id\":\"5TC~^ygZqaqm7Bu]CLFb\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}}},\"type\":\"ontilepress\",\"x\":12,\"y\":527},{\"fields\":{\"NAME\":\"gameLoop\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"$w.usF_exY)V$;il=209\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"lI^aBA=C[-AV~*[(V{ID\"}},\"id\":\"j9SbZ1*l=N(nKq0:#|}@\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"from\":1,\"to\":4},\"id\":\"K[h]zYUkFSl\\/vfjq+08T\",\"type\":\"randomnumber\"}}},\"next\":{\"block\":{\"id\":\"8%+%WUJ]s=IC,.3y@?jR\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"#X\\/o\\/tK5@wKw6kYae7s6\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"Zy7o[mZ98\\/ks|EdePP;g\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\")F=!`ak@QfN2cI{t;I51\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\";-)Hl5}p3T8_yEDQ\\/b`.\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"Ws9CFWR-TOzbj90|?;nc\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"!mH}3jxE?:W[pgsrHnoI\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"DMwp.3!g+tAL7uno$$f[\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"Q.#xbG=i8t0qDncx},W5\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"qZ1|d[6_x#m.Esezs6nV\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"D@M:J;O#uEJ4!:gNrktT\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"eiAsNopwJ[=fV;YH4[+M\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"2T`fGN?B[N)\\/Bz}X;oZS\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"2\"},\"id\":\"B!^:L6+q[gLUE[1[GKow\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"lI^aBA=C[-AV~*[(V{ID\"}},\"id\":\"3!_tsI?6mVr3e?\\/-SI|S\",\"type\":\"variables_get\"}}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":6,\"y\":772}],\"languageVersion\":0},\"variables\":[{\"id\":\"lI^aBA=C[-AV~*[(V{ID\",\"name\":\"tile\"}]}},{\"name\":\"Pairs\",\"game\":{\"blocks\":{\"blocks\":[{\"id\":\"M9@^:r-LDEiV7Tc!XX{C\",\"inputs\":{\"name\":{\"block\":{\"fields\":{\"TEXT\":\"pairs\"},\"id\":\"gu;$1UdH^^kd\\/89^\\/opG\",\"type\":\"text\"}},\"numplayers\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"Im(1U2:JtU$0s@HFKy)0\",\"type\":\"number\"}},\"numtiles\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"9{y)-K%;?+ZT(@Q(#*$`\",\"type\":\"number\"}},\"type\":{\"block\":{\"fields\":{\"goal\":30,\"type\":\"t\"},\"id\":\"eQULX3;7dKJP_SR}S5x4\",\"type\":\"gametype\"}}},\"type\":\"configuration\",\"x\":26,\"y\":64},{\"fields\":{\"NAME\":\"gameloop\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"p}\\/Z^bGz`Rw7+9[_R[HB\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"*gAT%X:4!,heCB?$$NUG\"}},\"id\":\"UJ`[S*2D1Q7cAmt25.UQ\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"v8Ne$k`3ImMbq=9;WQ1l\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"K!_d.sTk_dDw!B{YQ%1l\"}},\"id\":\"+eE2}-{\\/uj57+1YZTEUM\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"gWe|%3GFVD+?]K^Nz`JC\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Xo1gPgo6!;X+\\/N$_3feB\"}},\"id\":\"j|S@E8pNLxdH9ZF2WNHN\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"kHB\\/\\/R}-*[Pp@ivjM[w$\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"_m{\\/`m!?gw)cK1#{$]mw\"}},\"id\":\"`OCbFVv!]9Vcir$}@Gg3\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"from\":1,\"to\":3},\"id\":\"1}Gifi0lXHxXH5MhUpz;\",\"type\":\"randomnumber\"}}},\"next\":{\"block\":{\"id\":\"^+YV~i(kyn0^MjJp*CYB\",\"inputs\":{\"DO0\":{\"block\":{\"extraState\":{\"name\":\"conf1\"},\"id\":\"Q#uv9M9U,U!#sM|!gpK?\",\"type\":\"procedures_callnoreturn\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"6,(Soyp1QZ22j]PFIq*R\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"_m{\\/`m!?gw)cK1#{$]mw\"}},\"id\":\"3h5qmsg}x\\/W%Gq*8HJDK\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"G%-ms#fFeN_DVP81O`8q\",\"type\":\"number\"}}},\"type\":\"logic_compare\"}}},\"next\":{\"block\":{\"id\":\";S5W?RP=QsaAX;UfGd_]\",\"inputs\":{\"DO0\":{\"block\":{\"extraState\":{\"name\":\"conf2\"},\"id\":\"q!j;0jTyD\\/ulQiigS@,g\",\"type\":\"procedures_callnoreturn\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"z|;kzjGON$A6%%5^39Nz\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"_m{\\/`m!?gw)cK1#{$]mw\"}},\"id\":\"@(HJ!SFYP]4G%9p0@3E4\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"08u;H*47Rlg~2bDHxC1C\",\"type\":\"number\"}}},\"type\":\"logic_compare\"}}},\"next\":{\"block\":{\"id\":\"tE{_wP@u:26eeP(;7`S-\",\"inputs\":{\"DO0\":{\"block\":{\"extraState\":{\"name\":\"conf3\"},\"id\":\"_$75lg4a)8pS`:=LQ20\\/\",\"type\":\"procedures_callnoreturn\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"!l?,|JbcmozQ9^N}l?0%\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"_m{\\/`m!?gw)cK1#{$]mw\"}},\"id\":\"dc#;I:gYLE8Y\\/FQwyx#\\/\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"P0%%%r|aX7og3dbvt]Em\",\"type\":\"number\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}},\"type\":\"controls_if\"}},\"type\":\"controls_if\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":25,\"y\":289},{\"fields\":{\"NAME\":\"conf1\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"Q70t8^GrFb6F\\/qW[~Qb@\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"x).Gg,.rRGrB|Jeqb7|-\"}},\"id\":\"4,+^o\\/W9;@q]4W;DSfMU\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"Qlx3tMvXH,-4^Dcwit@X\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\")ebFF0Za$XZ;53sU7`!s\"}},\"id\":\"Pjhl27_H*pXf)yT;g+TN\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"g|C6Tx~bg-Lzi4[VD:mU\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"iokz4Ojd,,,V*PJlSVv)\"}},\"id\":\"]hGY]JGwKp:y6=VWr\\/U5\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"56*.rSso%tL=hXSH~~GJ\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Z!]lF%ieLP5-Vtg2^7ne\"}},\"id\":\"Xwt:PN|`Th1koBIfWRA~\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"#3Nfp6.6x_s)z|Lu`Vsq\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"6I5r?`6}gWHXrg.CQi\\/\\/\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"NN|)aU:-t(F1Ze+1tnc[\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"3wfMbKUax_p2)U`$z=h%\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"Vk0Opp_[kcfOt\\/x875$5\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"hx{s60bL(s|si#6U6%rX\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"C@DclN*88O+CA]1f|K[b\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"u^LRfH;P$eGqt_!pia#k\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"3\"},\"id\":\"_bFZnQ8_0w]46dst^,+0\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"N4,6pQFK;?z]nBX8Sw+X\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"~+r]NG#;F2,a351$zI=Q\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"2\"},\"id\":\"\\/)N{sd~DDC[yOFR$frok\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"%!(J(hjRw8e^RJ{L)Fhj\",\"type\":\"number\"}}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":26,\"y\":689},{\"fields\":{\"NAME\":\"conf2\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"U8H=t{NzmJ\\/*c^pa2Y}n\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"x).Gg,.rRGrB|Jeqb7|-\"}},\"id\":\"bo|nc14dv2A\\/4Kp~._U`\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"]wOWrIro[!:wUzGkj+`P\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\")ebFF0Za$XZ;53sU7`!s\"}},\"id\":\"n-KcdMPF;%IBIA:lh,b:\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"K?sK+m(.+;sS7QD7^Me2\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"iokz4Ojd,,,V*PJlSVv)\"}},\"id\":\")Wve)MzWj}bo+JLoT6;|\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"B2Da+pzMmRb3@n14f6y}\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Z!]lF%ieLP5-Vtg2^7ne\"}},\"id\":\"CrPtYkg_4*s$Qptbo|PD\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"v[U\\/o@n$s5QfY(G2tS3d\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"fQ:hej~Mr-19WT50xc\\/^\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"5\"},\"id\":\"^olZEx;^Jw@u8vM%mgo;\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":1},\"id\":\":1x-vz@H6c9,Rt19ydk{\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"-ctxtlLi_fG1$jBCW#19\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"-|u?J7I=r$[67]Zu1sa%\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"xbH^xe{)bH@TooeET)Ve\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\":6Zk#D=6iI2nf.nYY(;J\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"3\"},\"id\":\"Rz{Ub4{^(-WpppcVEAYE\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"Vm;kb|]%+y.nNT$V97vA\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"|:;q[Hn=?I-SpkJO`]YF\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"5\"},\"id\":\"Xq!QI5L;2W~w(oQqezFX\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"[161!hcic1p5)SKR0.*4\",\"type\":\"number\"}}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":32,\"y\":1186},{\"fields\":{\"NAME\":\"conf3\"},\"icons\":{\"comment\":{\"height\":80,\"pinned\":false,\"text\":\"Describe this function...\",\"width\":160}},\"id\":\"16`QQ@[PkYe~2,MYKb@b\",\"inputs\":{\"STACK\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"x).Gg,.rRGrB|Jeqb7|-\"}},\"id\":\"qm@`0T~IeKShzTARGwd$\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"D2eB-11l8v$^aFJxwlgZ\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\")ebFF0Za$XZ;53sU7`!s\"}},\"id\":\"hw1Kubfm9CL[(`P~VE!1\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":3},\"id\":\"(r7^_UrM`?g!K[~R+TM%\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"iokz4Ojd,,,V*PJlSVv)\"}},\"id\":\"Q9)r|4{~NT.dIoZ~!Xc~\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"z]47]7qA!:ow!-e7vrcH\",\"type\":\"number\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Z!]lF%ieLP5-Vtg2^7ne\"}},\"id\":\"4N%0bSAa,S5a]7jgbm+S\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"!WFqA*%es4PSXKoLhlD+\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"v]JiT,;G+#dwV#ku3[I3\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"5\"},\"id\":\"Bx1*8=YYb*RZ_I,s2uV@\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"GS(Lg.nBu+FR.~7CP6VQ\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"9?\\/m[344A}i=YQ,wl-k*\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"2\"},\"id\":\"+6vezK#{T:)CYN#)1+Pz\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":2},\"id\":\"=7RkrL`p3;V02AKv}Tz^\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"CBcq$x}:qY%BAgw-GD\\/z\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"2\"},\"id\":\"C]02tMD$BN+3FajVTyLh\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":3},\"id\":\";6`~)u8^hKK|%KN]i@3f\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"i8WSv$EL,)Z91]XML`gw\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"1\"},\"id\":\"X\\/x`Mp5nv*+vR%j:S#;r\",\"type\":\"colour\"}},\"tile\":{\"block\":{\"fields\":{\"number\":4},\"id\":\"x)ga\\/1ofq%h0q+x6~j~T\",\"type\":\"number\"}}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"settilecolor\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}},\"type\":\"variables_set\"}}},\"type\":\"procedures_defnoreturn\",\"x\":26,\"y\":1703},{\"id\":\"E}+R[cKfsgqwbj3mdZ+%\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"J{]VX+q^e2TnJg%l)Z{B\",\"inputs\":{\"colour\":{\"block\":{\"fields\":{\"colour\":\"0\"},\"id\":\"*J;)fxz2W`oym;EI!Z7R\",\"type\":\"colour\"}}},\"next\":{\"block\":{\"extraState\":{\"name\":\"gameloop\"},\"id\":\"7n^~C1iUcL(`x2JF_Z);\",\"type\":\"procedures_callnoreturn\"}},\"type\":\"settilesidle\"}}},\"type\":\"ongamestart\",\"x\":35,\"y\":2230},{\"id\":\"4yH!RK:rbPSZe4Nc,x!p\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"21A4ITO3$Gso]wVd%TbD\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"TEXT\":\"Your score was\"},\"id\":\"XG\\/*e7X36@j}X]a`bW`h\",\"type\":\"text\"}}},\"next\":{\"block\":{\"id\":\"[O=+fhdK]WD#UG\\/B7?P*\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"v3a`K4TUf?XV|e]R19~$\",\"type\":\"get_player_score\"}}},\"type\":\"motosound_speak\"}},\"type\":\"motosound_speak\"}}},\"type\":\"ongameend\",\"x\":34,\"y\":2398},{\"id\":\"?90]|{$sVN2FWz%lT.2M\",\"inputs\":{\"statements\":{\"block\":{\"id\":\"N;Fjw^MB|Jv-_|VO6~l)\",\"inputs\":{\"DO0\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"K!_d.sTk_dDw!B{YQ%1l\"}},\"id\":\"58I+c*:kTTHDJ.#MzUY}\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"HC=KOa4IqH[CuxLfYo*N\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"Kml;^~!YDK5qQ4{,7#2,\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"xJ8C?^pVd$UP`a8hrTFs\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"um:I)v=]yAckY\\/{=}{rc\",\"type\":\"number\"}},\"score\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"U;,{gB_P:tE+PW[dQ`_8\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"WWrSHdEWQ;VSE!q^+Apu\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"YG@?)!in;%?[]LtwrZcy\",\"type\":\"get_player_score\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"*gAT%X:4!,heCB?$$NUG\"}},\"id\":\"4091W@LHVsiO5`g`LQKc\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"a=s:f$F-u^MwCK!A~w%=\",\"type\":\"number\"}}},\"type\":\"variables_set\"}},\"type\":\"motosound_speak\"}},\"type\":\"addplayerscore\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\":5q2k6yU@nPe(J3JoR}(\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Xo1gPgo6!;X+\\/N$_3feB\"}},\"id\":\"`N7sQV+XRzSX7hydZ#bC\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"_\\/`bf;#)VBR1SC)YGj|j\",\"type\":\"number\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}},\"type\":\"variables_set\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"l!=5JMr[)2f(O`_li+kk\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"t*!7`LjCM()#Ab{jW$DF\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"x).Gg,.rRGrB|Jeqb7|-\"}},\"id\":\"9%kV!nE)B8_kKxTD,F1W\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"next\":{\"block\":{\"id\":\"V?[\\/JGhKB\\/B2ef5BCdhi\",\"inputs\":{\"DO0\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Xo1gPgo6!;X+\\/N$_3feB\"}},\"id\":\"{#\\/hT?{$dGa{%9G(.n;,\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"uhvfq)0b15M;B%W==lxn\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"3i(4gfiN(bYhq|8rNp)N\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"M9F{1]mgDLV]MKmBB\\/lB\",\"inputs\":{\"player\":{\"block\":{\"fields\":{\"number\":0},\"id\":\"Ng7qoli*f@R8lXmC=IpI\",\"type\":\"number\"}},\"score\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"bGtNqdr[UY=:`%dF1);%\",\"type\":\"number\"}}},\"next\":{\"block\":{\"id\":\"[MgMsd6+c4ZAx*WN*ok}\",\"inputs\":{\"text\":{\"block\":{\"fields\":{\"player\":0},\"id\":\"}|piwS6=sp02yZ:Q7oG2\",\"type\":\"get_player_score\"}}},\"next\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"*gAT%X:4!,heCB?$$NUG\"}},\"id\":\"`xGdk~Rn\\/dm9Ws#E;[v_\",\"inputs\":{\"VALUE\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"f|6xz$\\/6iZZ5pOh)@7y9\",\"type\":\"number\"}}},\"type\":\"variables_set\"}},\"type\":\"motosound_speak\"}},\"type\":\"addplayerscore\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"L6-[N6=^S#AiPB)PMW?%\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"K!_d.sTk_dDw!B{YQ%1l\"}},\"id\":\"Li!_C,OPa)6N]SP,laFQ\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"rFk7V#Zn|#0.\\/70AVVn?\",\"type\":\"number\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}},\"type\":\"variables_set\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"q2idDcsmwF}It=@PS;P=\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"q#fBO=gZaL[*IKsVGtKF\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\")ebFF0Za$XZ;53sU7`!s\"}},\"id\":\"vf$bfNNWRP$R9z#?s%B8\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"next\":{\"block\":{\"id\":\"pSUysZK#Rc6sO2pVp{c^\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"aCC#:B3G3PMA|kP4Vbsi\",\"type\":\"setgameover\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"~Mk6!ii~B7S~=)E}?qWJ\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"boDCsQ^aT|zhH(gm)XK=\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"iokz4Ojd,,,V*PJlSVv)\"}},\"id\":\"Kq{ZdRlz}vYHL,xERjnB\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"next\":{\"block\":{\"id\":\"{iK[oFLt%,m4lMT~L]Vs\",\"inputs\":{\"DO0\":{\"block\":{\"id\":\"00\\/gHWufgH0nv}mdccC[\",\"type\":\"setgameover\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"^=s986uJsslNKEHuJheL\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"field\":\"tile\"},\"id\":\"UT,y=(nu,W9\\/~CPbQ!=H\",\"type\":\"incomingevent\"}},\"B\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"Z!]lF%ieLP5-Vtg2^7ne\"}},\"id\":\"K}7HEE#=E`+vInRzj6zo\",\"type\":\"variables_get\"}}},\"type\":\"logic_compare\"}}},\"next\":{\"block\":{\"id\":\")\\/jeHvn[t$4T$+4CoxAW\",\"inputs\":{\"DO0\":{\"block\":{\"extraState\":{\"name\":\"gameloop\"},\"id\":\"@L]i:JOSd!UcS,[P9@;Q\",\"type\":\"procedures_callnoreturn\"}},\"IF0\":{\"block\":{\"fields\":{\"OP\":\"EQ\"},\"id\":\"*H#G+um`]jAg=9yM@g%W\",\"inputs\":{\"A\":{\"block\":{\"fields\":{\"VAR\":{\"id\":\"*gAT%X:4!,heCB?$$NUG\"}},\"id\":\"+;HCYwb(n*kXNKA$k!AC\",\"type\":\"variables_get\"}},\"B\":{\"block\":{\"fields\":{\"number\":1},\"id\":\"z:L8p}%qou,y4}~Rco9A\",\"type\":\"number\"}}},\"type\":\"logic_compare\"}}},\"type\":\"controls_if\"}},\"type\":\"controls_if\"}},\"type\":\"controls_if\"}},\"type\":\"controls_if\"}},\"type\":\"controls_if\"}}},\"type\":\"ontilepress\",\"x\":36,\"y\":2531}],\"languageVersion\":0},\"variables\":[{\"id\":\"iokz4Ojd,,,V*PJlSVv)\",\"name\":\"wrong1\"},{\"id\":\"Z!]lF%ieLP5-Vtg2^7ne\",\"name\":\"wrong2\"},{\"id\":\"x).Gg,.rRGrB|Jeqb7|-\",\"name\":\"first\"},{\"id\":\")ebFF0Za$XZ;53sU7`!s\",\"name\":\"second\"},{\"id\":\"K!_d.sTk_dDw!B{YQ%1l\",\"name\":\"1clicked\"},{\"id\":\"Xo1gPgo6!;X+\\/N$_3feB\",\"name\":\"2clicked\"},{\"id\":\"_m{\\/`m!?gw)cK1#{$]mw\",\"name\":\"randomizer\"},{\"id\":\"*gAT%X:4!,heCB?$$NUG\",\"name\":\"loop\"}]}}]";
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

    }*/
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
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }
}