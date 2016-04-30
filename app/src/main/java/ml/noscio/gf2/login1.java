package ml.noscio.gf2;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class login1 extends AppCompatActivity {

    public void OpenWebsite(View v) {
        String url = "https://www.noscio.eu";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i("LOGIN", "reached");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);
        final Button button = (Button) findViewById(R.id.button);
        final EditText editText = (EditText)findViewById(R.id.editText);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                assert editText != null;
                String password = editText.getText().toString();
                String stringUrl = SecurityValues.auth_url+password;
                ConnectivityManager connMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    Toast.makeText(getApplicationContext(),"No network connection available.",Toast.LENGTH_LONG).show();
                }

            }
        });
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
            if(result.contains("true")) {
                dbManager.write("reg","yes");
                dbManager.write("klasse","5a");
                dbManager.write("subjects","");
                dbManager.write("push","");
                Intent intent = new Intent(login1.this, login2.class);
                startActivity(intent);
            }
            else {
                Log.i("result",result);
                Toast.makeText(getApplicationContext(), "Passwort falsch!", Toast.LENGTH_LONG).show();
            }
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
            String contentAsString = readIt(is, len);
            return contentAsString;

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
    public void onPrivacyViewClick(View v) {
        String url = "https://www.noscio.eu/privacy";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}