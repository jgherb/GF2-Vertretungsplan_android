package ml.noscio.gf2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class settings extends AppCompatActivity {
    String klasse = dbManager.read("klasse");
    public void OpenPrivacy() {
        String url = "https://www.noscio.eu/privacy";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        // Recuperation du TabHost
        TabHost mytabhost = (TabHost) findViewById(R.id.TabHost01);
// Before adding tabs, it is imperative to call the method setup()
        assert mytabhost != null;
        mytabhost.setup();

// Adding tabs
        // tab1 settings
        TabHost.TabSpec spec = mytabhost.newTabSpec("tab_klasse");
        // text and image of tab
        spec.setIndicator("Klasse", getResources().getDrawable(android.R.drawable.ic_menu_add));
        // specify layout of tab
        spec.setContent(R.id.onglet1);
        // adding tab in TabHost
        mytabhost.addTab(spec);

// otherwise :
        //mytabhost.addTab(mytabhost.newTabSpec("tab_fach").setIndicator("Fächer", getResources().getDrawable(android.R.drawable.ic_menu_edit)).setContent(R.id.Onglet2));

        mytabhost.addTab(mytabhost.newTabSpec("tab_allgemein").setIndicator("Sonstiges", getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.Onglet3));

        TextView IDview = (TextView) findViewById(R.id.textViewAndroidID);
        assert IDview != null;
        IDview.setText("Device ID: " + MainActivity.android_id);

        alleAus();
        Resources res = getResources();
        int id = res.getIdentifier("button" + klasse, "id", this.getApplicationContext().getPackageName());
        Button aktuelleKlasseButton = (Button) findViewById(id);
        assert aktuelleKlasseButton != null;
        aktuelleKlasseButton.setBackgroundColor(Color.parseColor("#0C76B3"));

        final Button button2 = (Button) findViewById(R.id.button2);
        assert button2 != null;
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                OpenPrivacy();
            }
        });

        final Switch beta_switch = (Switch) findViewById(R.id.switch1);
        assert beta_switch != null;
        String beta_str = dbManager.read("beta");
        if(beta_str.equals("true")) {
            beta_switch.setChecked(true);
        }
        beta_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    dbManager.write("beta","true");
                }
                else {
                    dbManager.write("beta","false");
                }
            }
        });

/*
final CheckBox Box_E = (CheckBox) findViewById(R.id.checkE);
assert Box_E != null;
Box_E.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("E", Box_E.isChecked());
Log.i("click","E");
}
});

Box_E.setChecked(getState("E"));
final CheckBox Box_F = (CheckBox) findViewById(R.id.checkF);
assert Box_F != null;
Box_F.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("F", Box_F.isChecked());
}
});
Box_F.setChecked(getState("F"));


final CheckBox Box_Ph = (CheckBox) findViewById(R.id.checkPh);
assert Box_Ph != null;
Box_Ph.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Ph", Box_Ph.isChecked());
}
});
Box_Ph.setChecked(getState("Ph"));

final CheckBox Box_Ch = (CheckBox) findViewById(R.id.checkCh);
assert Box_Ch != null;
Box_Ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Ch", Box_Ch.isChecked());
}
});
Box_Ch.setChecked(getState("Ch"));

final CheckBox Box_Ethik = (CheckBox) findViewById(R.id.checkEthik);
assert Box_Ethik != null;
Box_Ethik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Ethik", Box_Ethik.isChecked());
}
});
Box_Ethik.setChecked(getState("Ethik"));

final CheckBox Box_Mu = (CheckBox) findViewById(R.id.checkMusik);
assert Box_Mu != null;
Box_Ethik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Mu", Box_Mu.isChecked());
}
});
Box_Ethik.setChecked(getState("Mu"));

final CheckBox Box_Inf = (CheckBox) findViewById(R.id.checkInf);
assert Box_Inf != null;
Box_Inf.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Inf", Box_Inf.isChecked());
}
});
Box_Inf.setChecked(getState("Inf"));

Box_F.setChecked(getState("F"));
final CheckBox Box_L = (CheckBox) findViewById(R.id.checkL);
assert Box_L != null;
Box_L.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("L", Box_L.isChecked());
}
});
Box_L.setChecked(getState("L"));
final CheckBox Box_Spa = (CheckBox) findViewById(R.id.checkSpa);
assert Box_Spa != null;
Box_Spa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Spa", Box_Spa.isChecked());
}
});
Box_Spa.setChecked(getState("Spa"));
final CheckBox Box_NwT = (CheckBox) findViewById(R.id.checkNwT);
assert Box_NwT != null;
Box_NwT.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("NwT", Box_NwT.isChecked());
}
});
Box_NwT.setChecked(getState("NwT"));
final CheckBox Box_G = (CheckBox) findViewById(R.id.checkG);
assert Box_G != null;
Box_G.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("G", Box_G.isChecked());
}
});
Box_G.setChecked(getState("G"));
final CheckBox Box_Gk = (CheckBox) findViewById(R.id.checkGk);
assert Box_Gk != null;
Box_Gk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Gk", Box_Gk.isChecked());
}
});
Box_Gk.setChecked(getState("Gk"));
final CheckBox Box_Geo = (CheckBox) findViewById(R.id.checkGeo);
assert Box_Geo != null;
Box_Geo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
@Override
public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
setFach("Geo", Box_Geo.isChecked());
}
});
Box_Geo.setChecked(getState("Geo"));
*/

        final Button button5a = (Button) findViewById(R.id.button5a);
        assert button5a != null;
        button5a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "5a";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button5b = (Button) findViewById(R.id.button5b);
        assert button5b != null;
        button5b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "5b";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button5c = (Button) findViewById(R.id.button5c);
        assert button5c != null;
        button5c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "5c";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button6a = (Button) findViewById(R.id.button6a);
        assert button6a != null;
        button6a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "6a";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button6b = (Button) findViewById(R.id.button6b);
        assert button6b != null;
        button6b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "6b";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button6c = (Button) findViewById(R.id.button6c);
        assert button6c != null;
        button6c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "6c";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button7a = (Button) findViewById(R.id.button7a);
        assert button7a != null;
        button7a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "7a";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button7b = (Button) findViewById(R.id.button7b);
        assert button7b != null;
        button7b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "7b";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button7c = (Button) findViewById(R.id.button7c);
        assert button7c != null;
        button7c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "7c";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button8a = (Button) findViewById(R.id.button8a);
        assert button8a != null;
        button8a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "8a";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button8b = (Button) findViewById(R.id.button8b);
        assert button8b != null;
        button8b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "8b";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button8c = (Button) findViewById(R.id.button8c);
        assert button8c != null;
        button8c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "8c";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button9a = (Button) findViewById(R.id.button9a);
        assert button9a != null;
        button9a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "9a";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button9b = (Button) findViewById(R.id.button9b);
        assert button9b != null;
        button9b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "9b";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button9c = (Button) findViewById(R.id.button9c);
        assert button9c != null;
        button9c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "9c";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button10a = (Button) findViewById(R.id.button10a);
        assert button10a != null;
        button10a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "10a";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button10b = (Button) findViewById(R.id.button10b);
        assert button10b != null;
        button10b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "10b";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button button10c = (Button) findViewById(R.id.button10c);
        assert button10c != null;
        button10c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "10c";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button buttonKs1 = (Button) findViewById(R.id.buttonKs1);
        assert buttonKs1 != null;
        buttonKs1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "Ks1";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();

            }
        });
        final Button buttonKs2 = (Button) findViewById(R.id.buttonKs2);
        assert buttonKs2 != null;
        buttonKs2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "Ks2";
                alleAus();
                v.setBackgroundColor(Color.parseColor("#0C76B3"));
                speichern();
            }
        });

        final CheckBox Box_Push = (CheckBox) findViewById(R.id.check_push);
        assert Box_Push != null;
        Box_Push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setPush(Box_Push.isChecked());
            }
        });
        Box_Push.setChecked(getPush());

    }
    boolean getPush() {
        if(MainActivity.Push.equals(("1"))) {
            return true;
        }
        return false;
    }
    private boolean getState(String e) {
        if (MainActivity.Fächer.contains(e + ";")) {
            return true;
        }
        return false;
    }

    private void setPush(boolean checked) {
        if (checked) {
            MainActivity.Push = "1";
        } else {
            MainActivity.Push = "0";
        }
        speichern();
    }

    private void setFach(String e, boolean b) {
        Log.i("setFach", "reached");
        Log.i("setFach_result", MainActivity.Fächer);
        if (!b) {
            if (!MainActivity.Fächer.contains(e + ";")) {
                MainActivity.Fächer = MainActivity.Fächer + e + ";";
            }
        } else {
            if (MainActivity.Fächer.contains(e + ";")) {
                MainActivity.Fächer = MainActivity.Fächer.replace(e + ";", "");
            }
        }
        speichern();
    }

    void alleAus() {
        Button button5a = (Button) findViewById(R.id.button5a);
        assert button5a != null;
        button5a.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button5b = (Button) findViewById(R.id.button5b);
        assert button5b != null;
        button5b.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button5c = (Button) findViewById(R.id.button5c);
        assert button5c != null;
        button5c.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button6a = (Button) findViewById(R.id.button6a);
        assert button6a != null;
        button6a.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button6b = (Button) findViewById(R.id.button6b);
        assert button6b != null;
        button6b.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button6c = (Button) findViewById(R.id.button6c);
        assert button6c != null;
        button6c.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button7a = (Button) findViewById(R.id.button7a);
        assert button7a != null;
        button7a.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button7b = (Button) findViewById(R.id.button7b);
        assert button7b != null;
        button7b.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button7c = (Button) findViewById(R.id.button7c);
        assert button7c != null;
        button7c.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button8a = (Button) findViewById(R.id.button8a);
        assert button8a != null;
        button8a.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button8b = (Button) findViewById(R.id.button8b);
        assert button8b != null;
        button8b.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button8c = (Button) findViewById(R.id.button8c);
        assert button8c != null;
        button8c.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button9a = (Button) findViewById(R.id.button9a);
        assert button9a != null;
        button9a.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button9b = (Button) findViewById(R.id.button9b);
        assert button9b != null;
        button9b.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button9c = (Button) findViewById(R.id.button9c);
        assert button9c != null;
        button9c.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button10a = (Button) findViewById(R.id.button10a);
        assert button10a != null;
        button10a.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button10b = (Button) findViewById(R.id.button10b);
        assert button10b != null;
        button10b.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button button10c = (Button) findViewById(R.id.button10c);
        assert button10c != null;
        button10c.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button buttonKs1 = (Button) findViewById(R.id.buttonKs1);
        assert buttonKs1 != null;
        buttonKs1.setBackgroundColor(Color.parseColor("#CCCCCC"));

        Button buttonKs2 = (Button) findViewById(R.id.buttonKs2);
        assert buttonKs2 != null;
        buttonKs2.setBackgroundColor(Color.parseColor("#CCCCCC"));

    }

    void speichern() {
        Log.i("speichern", "reached");
        MainActivity.klasse = klasse;
        MainActivity.reload_forced = true;
        dbManager.write("klasse", klasse);
        dbManager.write("subjects", MainActivity.Fächer);
        dbManager.write("push", MainActivity.Push);

        String stringUrl = SecurityValues.url_reg_token + RegistrationIntentService._token +
                SecurityValues.url_reg_id + MainActivity.android_id + SecurityValues.url_reg_klasse +
                MainActivity.klasse + SecurityValues.url_reg_subjects + MainActivity.Fächer +
                SecurityValues.url_reg_push + MainActivity.Push;
        Log.i("url", stringUrl);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute(stringUrl);
        } else {
            Toast.makeText(getApplicationContext(), "No network connection available.", Toast.LENGTH_LONG).show();
        }
        //Intent intent = new Intent(settings.this, MainActivity.class);
        //startActivity(intent);
    }

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.i("response", result);
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("DEBUG", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return readIt(is, len);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }
}