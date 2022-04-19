package com.google.blockly.android.webview.demo.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.blocklywebview.R;
import com.google.blockly.android.webview.demo.BlocklyTools.BlocklyGamesStore;
import com.livelife.motolibrary.MotoConnection;
import com.livelife.motolibrary.MotoSound;
import com.livelife.motolibrary.OnAntEventListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements OnAntEventListener {
    private final String standardGames = "[{\"name\": \"spil2\", \"isRuleBased\": true, \"game\": {\"blocks\":{\"blocks\":[{\"fields\":{\"duration\":30,\"players\":1},\"id\":\"Vbd(KAxZ,[g).g#`q#qc\",\"inputs\":{\"rules\":{\"block\":{\"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"1\\\">\\u003C/mutation>\",\"fields\":{\"condition\":\"on_start\"},\"id\":\"Zrf-j6.FM/Wfkcge`494\",\"inputs\":{\"THEN0\":{\"block\":{\"fields\":{\"action\":\"set_tiles_color\",\"col\":\"-1\"},\"id\":\"0-CI)jkoqUFN}?TyP1ue\",\"type\":\"then\"}}},\"next\":{\"block\":{\"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"1\\\">\\u003C/mutation>\",\"fields\":{\"condition\":\"on_x_time_passed\",\"num\":2},\"id\":\"6Mi]:vM=/p:E.t$}=/?q\",\"inputs\":{\"THEN0\":{\"block\":{\"fields\":{\"action\":\"set_tiles_color\",\"col\":\"-1\"},\"id\":\"Fsa]!M]T*LK:8GC#=+|P\",\"type\":\"then\"}}},\"next\":{\"block\":{\"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"3\\\">\\u003C/mutation>\",\"fields\":{\"col\":\"1\",\"condition\":\"on_color_press\"},\"id\":\"C(:c|?S|IhaVSqI6kxN;\",\"inputs\":{\"THEN0\":{\"block\":{\"fields\":{\"action\":\"increment_player_score\",\"num\":\"0\"},\"id\":\"!RJqa*5,vNC}4x^z{D#}\",\"type\":\"then\"}},\"THEN1\":{\"block\":{\"fields\":{\"action\":\"set_tiles_color\",\"col\":\"-1\"},\"id\":\"]YOSqHy::o;[Q?/f+*o^\",\"type\":\"then\"}},\"THEN2\":{\"block\":{\"fields\":{\"action\":\"play_sound\",\"sound\":\"m\"},\"id\":\"weH0c-t)Ccb!L~,W@H?d\",\"type\":\"then\"}}},\"type\":\"when\"}},\"type\":\"when\"}},\"type\":\"when\"}}},\"type\":\"v2config\",\"x\":0,\"y\":0}],\"languageVersion\":0}}},{\"name\": \"spil1\",\"isRuleBased\": true,\"game\": {   \"blocks\":{      \"blocks\":[         {            \"fields\":{               \"duration\":120,               \"players\":1            },            \"id\":\"j)~6:WNn@lXK!IzN6N:=\",            \"inputs\":{               \"rules\":{                  \"block\":{                     \"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"2\\\">\\u003C/mutation>\",                     \"fields\":{                        \"condition\":\"on_start\"                     },                     \"id\":\"mYYlL=?RMsO7$*nYFPjF\",                     \"inputs\":{                        \"THEN0\":{                           \"block\":{                              \"fields\":{                                 \"action\":\"set_tile_color\",                                 \"col\":\"3\",                                 \"num\":\"-1\"                              },                              \"id\":\"5$+Njljg$]4vuGLhe]7_\",                              \"type\":\"then\"                           }                        },                        \"THEN1\":{                           \"block\":{                              \"fields\":{                                 \"action\":\"set_tile_color\",                                 \"col\":\"1\",                                 \"num\":\"-1\"                              },                              \"id\":\"jnEa-sXEwtY^]xcA:x0u\",                              \"type\":\"then\"                           }                        }                     },                     \"next\":{                        \"block\":{                           \"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"5\\\">\\u003C/mutation>\",                           \"fields\":{                              \"col\":\"1\",                              \"condition\":\"on_color_press\"                           },                           \"id\":\"rZ#]?/6TN{!N(x;@cLnm\",                           \"inputs\":{                              \"THEN0\":{                                 \"block\":{                                    \"fields\":{                                       \"action\":\"set_tiles_color\",                                       \"col\":\"4\"                                    },                                    \"id\":\"A^M@MctH;8T`wUiwq7FJ\",                                    \"type\":\"then\"                                 }                              },                              \"THEN1\":{                                 \"block\":{                                    \"fields\":{                                       \"action\":\"register_pattern\",                                       \"name\":\"p1\",                                       \"num\":4                                    },                                    \"id\":\"Di=pU_]V31{lI]kG}h_S\",                                    \"type\":\"then\"                                 }                              },                              \"THEN2\":{                                 \"block\":{                                    \"fields\":{                                       \"action\":\"set_tiles_color\",                                       \"col\":\"0\"                                    },                                    \"id\":\"FB.loYj-y8Z7bqZ_g7*i\",                                    \"type\":\"then\"                                 }                              },                              \"THEN3\":{                                 \"block\":{                                    \"fields\":{                                       \"action\":\"set_tile_color\",                                       \"col\":\"3\",                                       \"num\":\"-1\"                                    },                                    \"id\":\"){Z((lp~Re+m`Z#n4Wqu\",                                    \"type\":\"then\"                                 }                              },                              \"THEN4\":{                                 \"block\":{                                    \"fields\":{                                       \"action\":\"set_tile_color\",                                       \"col\":\"1\",                                       \"num\":\"-1\"                                    },                                    \"id\":\"Rd=r$#;AE+g,z7!h.qj.\",                                    \"type\":\"then\"                                 }                              }                           },                           \"next\":{                              \"block\":{                                 \"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"3\\\">\\u003C/mutation>\",                                 \"fields\":{                                    \"col\":\"1\",                                    \"condition\":\"on_color_press\"                                 },                                 \"id\":\"r4m}DQZpg-nm?)gzIZgV\",                                 \"inputs\":{                                    \"THEN0\":{                                       \"block\":{                                          \"fields\":{                                             \"action\":\"play_pattern\",                                             \"pattern_name\":\"Di=pU_]V31{lI]kG}h_S\"                                          },                                          \"id\":\"C:he1}:my$Xki;DPg0jB\",                                          \"type\":\"then\"                                       }                                    },                                    \"THEN1\":{                                       \"block\":{                                          \"fields\":{                                             \"action\":\"set_tile_color\",                                             \"col\":\"3\",                                             \"num\":\"-1\"                                          },                                          \"id\":\"88%0(Kt^V{V/bIdDv4]6\",                                          \"type\":\"then\"                                       }                                    },                                    \"THEN2\":{                                       \"block\":{                                          \"fields\":{                                             \"action\":\"set_tile_color\",                                             \"col\":\"1\",                                             \"num\":\"-1\"                                          },                                          \"id\":\"RE9Fi5q?;Pc2faVJ`R/e\",                                          \"type\":\"then\"                                       }                                    }                                 },                                 \"next\":{                                    \"block\":{                                       \"extraState\":\"\\u003Cmutation xmlns=\\\"http://www.w3.org/1999/xhtml\\\" count=\\\"1\\\">\\u003C/mutation>\",                                       \"fields\":{                                          \"condition\":\"on_any_press\"                                       },                                       \"id\":\"!#HYDQ*n@!9[q`LG`,=6\",                                       \"inputs\":{                                          \"THEN0\":{                                             \"block\":{                                                \"fields\":{                                                   \"action\":\"play_sound\",                                                   \"sound\":\"p1\"                                                },                                                \"id\":\"f!:%L~2UD)wfn)5sj%t;\",                                                \"type\":\"then\"                                             }                                          }                                       },                                       \"type\":\"when\"                                    }                                 },                                 \"type\":\"when\"                              }                           },                           \"type\":\"when\"                        }                     },                     \"type\":\"when\"                  }               }            },            \"type\":\"v2config\",            \"x\":0,            \"y\":0         }      ],      \"languageVersion\":0   }}}]";
    MotoConnection connection = MotoConnection.getInstance();
    MotoSound sound = MotoSound.getInstance();
    TextView statusTextView;
    Button pairingButton, practiceBtn, createGameBtn;
    boolean isPairing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        connection.startMotoConnection(HomeActivity.this);
        connection.saveRfFrequency(66);
        connection.setDeviceId(43);
        connection.registerListener(HomeActivity.this);

        statusTextView = findViewById(R.id.statusTextView);
        pairingButton = findViewById(R.id.pairingButton);
        practiceBtn = findViewById(R.id.practiceBtn);
        createGameBtn = findViewById(R.id.createGameBtn);

        practiceBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, PracticeGamesActivity.class);
            startActivity(i);
        });

        createGameBtn.setOnClickListener(v -> {
            connection.unregisterListener(this);
            Intent i = new Intent(this, CreateGamesActivity.class);
            startActivity(i);
        });

        pairingButton.setOnClickListener(view -> {
            if(!isPairing){
                connection.pairTilesStart();
                pairingButton.setText("Stop pairing!");
            }else{
                connection.pairTilesStop();
                pairingButton.setText("Start pairing!");
            }
            isPairing = !isPairing;
        });
        sound.initializeSounds(this);
        try {
            addStandardGamesIfMissing();
        } catch (JSONException | IOException e) {
            Log.e("ERROR", "onCreate: Failed to add standard games " + e);
        }
    }

    @Override
    public void onMessageReceived(byte[] bytes, long l) {

    }

    @Override
    public void onAntServiceConnected() {
        connection.setAllTilesToInit();
    }

    @Override
    public void onNumbersOfTilesConnected(final int i) {
        runOnUiThread(() -> statusTextView.setText(i +" connected tiles"));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        connection.registerListener(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        connection.stopMotoConnection();
        connection.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addStandardGamesIfMissing() throws JSONException, IOException {

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
                        name,
                        arr.getJSONObject(i).getBoolean("isRuleBased"));
            }
        }

    }
}