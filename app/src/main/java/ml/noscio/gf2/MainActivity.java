package ml.noscio.gf2;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    public static String url = "";
    static Context _context;
    WebView webview;
    String version = "3.0.0";
    public static String klasse = "";
    static boolean DB_loaded = false;
    public static String android_id = "not_given";
    public static String Fächer = "";
    public static String Push = "1";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    IInAppBillingService mService;

    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context = this.getApplicationContext();

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android_id = Settings.Secure.getString(this.getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenWebsite();
            }
        });

        webview = (WebView) findViewById(R.id.webView);

        if (!DB_loaded) {
            android_id = Settings.Secure.getString(this.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.i("DEBUG", "reached Main");

            boolean firststart = false;
            try {
                String reg = dbManager.read("reg");
                Log.i("reg_value",reg);
                if(!reg.equals("yes")) {
                    firststart = true;
                }
                Log.i("DEBUG", "survive try");
            } catch (Exception e) {
                firststart = true;
                Log.i("DEBUG", "catch");
            }
            Log.i("DEBUG", "close");
            if (firststart) {
                Log.i("DEBUG", "first");
                dbManager.deleteTable();
                dbManager.write("beta","false");
                Intent intent = new Intent(MainActivity.this, login1.class);
                startActivity(intent);
            }
            else {
                Log.i("DEBUG", "begin SQL");
                klasse = dbManager.read("klasse");
                if((!klasse.contains("a"))&(!klasse.contains("b"))&(!klasse.contains("c"))) {
                    login2.save_is_required = true;
                    Intent intent = new Intent(MainActivity.this, login2.class);
                    startActivity(intent);
                }
                Fächer = dbManager.read("subjects");
                Push = dbManager.read("push");
                Log.i("klasse", klasse);
                Log.i("subjects", Fächer);
                Log.i("push", Push);
                Log.i("DEBUG", "read");
            }
            String db_buffer = dbManager.read("url_update");
            if(db_buffer.length()>0) {
                SecurityValues.url_update = db_buffer;
            }
            db_buffer = "";
            db_buffer = dbManager.read("auth_url");
            if(db_buffer.length()>0) {
                SecurityValues.auth_url = db_buffer;
            }
            db_buffer = "";
            db_buffer = dbManager.read("url_data");
            if(db_buffer.length()>0) {
                SecurityValues.url_data = db_buffer;
            }
            db_buffer = "";
            db_buffer = dbManager.read("url_data_beta");
            if(db_buffer.length()>0) {
                SecurityValues.url_data_beta = db_buffer;
            }
            db_buffer = "";
            db_buffer = dbManager.read("url_reg");
            if(db_buffer.length()>0) {
                SecurityValues.url_reg = db_buffer;
            }
            Log.i("DEBUG",SecurityValues.url_update);

            String stringUrl = SecurityValues.url_update;
            stringUrl = stringUrl.replace("@@device_id@@",android_id);
            stringUrl = stringUrl.replace("@@push@@",MainActivity.Push);
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new DownloadWebpageTask().execute(stringUrl);
            } else {
                Toast.makeText(getApplicationContext(),"No network connection available.",Toast.LENGTH_LONG).show();
            }
            DB_loaded = true;
        }

        ArrayList<String> skuList = new ArrayList<String> ();
        skuList.add("premiumUpgrade");
        skuList.add("gas");
        Bundle querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                } else {
                }
            }
        };

        // Registering BroadcastReceiver
        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

        Log.i("DEBUG", "XSS");
        webview.getSettings().setJavaScriptEnabled(true);
        String summary = "<html><body>Bitte warten...</body></html>";
        webview.loadData(summary, "text/html", null);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            url = SecurityValues.url_data;
            String beta = dbManager.read("beta");
            if(beta.equals("true")) {
                url = SecurityValues.url_data_beta;
            }
            url = url.replace("@@klasse@@",klasse);
            url = url.replace("@@device_id@@",android_id);
            url = url.replace("@@version@@",version);
            webview.loadUrl(url);
            Log.i("klasse", klasse);
        } else {
            summary = "<html><body>Keine Internetverbindung!</body></html>";
            webview.loadData(summary, "text/html", null);
        }
        Log.i("Reload", "finish");
        new ReloadWebView(this, 1, webview);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(MainActivity.this, settings.class);
            startActivity(intent);
        }
        if (id == R.id.action_change_class) {
            login2.save_is_required = false;
            Intent intent = new Intent(MainActivity.this, login2.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static Context getContext() {
        return _context;
    }

    protected class ReloadWebView extends TimerTask {
        Activity context;
        Timer timer;
        WebView wv;

        public ReloadWebView(Activity context, int seconds, WebView wv) {
            this.context = context;
            this.wv = wv;

            timer = new Timer();
        /* execute the first task after seconds */
            timer.schedule(this,
                    seconds * 1000,  // initial delay
                    seconds * 1000); // subsequent rate
        }

        @Override
        public void run() {
            if(context == null || context.isFinishing()) {
                // Activity killed
                this.cancel();
                return;
            }

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.counter++;
                    if((counter>60)|reload_forced) {
                        reload_forced = false;
                        counter = 0;
                        Log.i("reg_value","triggered");
                        android_id = Settings.Secure.getString(wv.getContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        String summary = "<html><body>Bitte warten...</body></html>";
                        wv.loadData(summary, "text/html", null);
                        ConnectivityManager connMgr = (ConnectivityManager)
                                getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                        if (networkInfo != null && networkInfo.isConnected()) {
                            wv.loadUrl(url);
                            Log.i("klasse",klasse);
                        } else {
                            summary = "<html><body>Keine Internetverbindung!</body></html>";
                            wv.loadData(summary, "text/html", null);
                        }
                    }
                }
            });
        }
    }
    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    public void OpenWebsite() {
        String url = "https://www.noscio.eu";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
    public static long counter = 0;
    public static boolean reload_forced = false;
    public static void reload() {
        reload_forced = true;
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
            if(!result.contains("!!!")) {
                Log.i("DEBUG","result:"+result);
                return;
            }
            String[] arr = result.split("!!!");
            dbManager.write("url_update",arr[0]);
            dbManager.write("url_data",arr[1]);
            dbManager.write("url_data_beta",arr[2]);
            dbManager.write("url_data_reg",arr[3]);
            Log.i("DEBUG","5#+"+arr[0]);
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
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
}

