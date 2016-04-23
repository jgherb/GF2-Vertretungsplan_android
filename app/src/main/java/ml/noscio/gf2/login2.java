package ml.noscio.gf2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login2 extends AppCompatActivity {
    public static boolean save_is_required = false;
    String klasse = "";
    public void OpenWebsite(View v) {
        String url = "https://www.noscio.eu";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);


        final Button button5a = (Button) findViewById(R.id.button5a);
        button5a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { klasse = "5a";speichern();

            }
        });
        final Button button5b = (Button) findViewById(R.id.button5b);
        button5b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "5b";speichern();

            }
        });
        final Button button5c = (Button) findViewById(R.id.button5c);
        button5c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "5c";speichern();

            }
        });
        final Button button6a = (Button) findViewById(R.id.button6a);
        button6a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "6a";speichern();

            }
        });
        final Button button6b = (Button) findViewById(R.id.button6b);
        button6b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "6b";speichern();

            }
        });
        final Button button6c = (Button) findViewById(R.id.button6c);
        button6c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "6c";speichern();

            }
        });
        final Button button7a = (Button) findViewById(R.id.button7a);
        button7a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { klasse = "7a";speichern();

            }
        });
        final Button button7b = (Button) findViewById(R.id.button7b);
        button7b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "7b";speichern();

            }
        });
        final Button button7c = (Button) findViewById(R.id.button7c);
        button7c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "7c";speichern();

            }
        });
        final Button button8a = (Button) findViewById(R.id.button8a);
        button8a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "8a";speichern();

            }
        });
        final Button button8b = (Button) findViewById(R.id.button8b);
        button8b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "8b";speichern();

            }
        });
        final Button button8c = (Button) findViewById(R.id.button8c);
        button8c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "8c";speichern();

            }
        });
        final Button button9a = (Button) findViewById(R.id.button9a);
        button9a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "9a";speichern();

            }
        });
        final Button button9b = (Button) findViewById(R.id.button9b);
        button9b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "9b";speichern();

            }
        });
        final Button button9c = (Button) findViewById(R.id.button9c);
        button9c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "9c";speichern();

            }
        });
        final Button button10a = (Button) findViewById(R.id.button10a);
        button10a.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "10a";speichern();

            }
        });
        final Button button10b = (Button) findViewById(R.id.button10b);
        button10b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "10b";speichern();

            }
        });
        final Button button10c = (Button) findViewById(R.id.button10c);
        button10c.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "10c";speichern();

            }
        });
        final Button buttonKs1 = (Button) findViewById(R.id.buttonKs1);
        buttonKs1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "Ks1";speichern();

            }
        });
        final Button buttonKs2 = (Button) findViewById(R.id.buttonKs2);
        buttonKs2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                klasse = "Ks2";speichern();
            }
        });
    }
    void speichern() {
        Log.i("speichern", "reached");
        MainActivity.klasse = klasse;
        if(save_is_required) {
            Log.i("reg_value","saved");
            dbManager.write("klasse",klasse);
            save_is_required = false;
        }
        Intent intent = new Intent(login2.this, MainActivity.class);
        startActivity(intent);
    }
}
