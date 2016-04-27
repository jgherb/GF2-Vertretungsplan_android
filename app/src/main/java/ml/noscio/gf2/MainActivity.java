package ml.noscio.gf2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
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
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ScrollView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static boolean forced_class_change = false;
    private static final String TAG = "MainActivity";
    static Context _context;
    String version = "2.0-dev";
    public static String klasse = "";
    static boolean DB_loaded = false;
    public static String android_id = "not_given";
    public static String Fächer = "";
    public static String Push = "1";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _context = this.getApplicationContext();
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

        WebView webview = (WebView) findViewById(R.id.webView);

        //Only for testing
        //DB_loaded = false;

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
                Intent intent = new Intent(MainActivity.this, login1.class);
                startActivity(intent);
            }
            else {
                Log.i("DEBUG", "begin SQL");
                if(!forced_class_change) {
                    klasse = dbManager.read("klasse");
                }
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
            DB_loaded = true;
        }

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

        /*final Button button = (Button) findViewById(R.id.buttonClassChange);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, login2.class);
                startActivity(intent);
            }
        });
        final Button buttonSettings = (Button) findViewById(R.id.buttonSettings);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, settings.class);
                startActivity(intent);
            }
        });*/

        //final ProgressDialog progDailog = ProgressDialog.show(_context, "Loading","Please wait...", true);
        //progDailog.setCancelable(false);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //progDailog.show();
                view.loadUrl(url);
                Log.i("DEBUG","page load started");
                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                //progDailog.dismiss();
                Log.i("DEBUG","page finished");
            }
        });



        Log.i("DEBUG", "XSS");
        //webview.getSettings().setDomStorageEnabled(true);
        //webview.getSettings().setJavaScriptEnabled(true);
        Log.i("DEBUG", "XSS2");
        String summary = "<html><body>Bitte warten...</body></html>";
        //webview.loadData(summary, "text/html", null);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            //webview.loadUrl(SecurityValues.url_data_klasse + klasse + SecurityValues.url_data_version + version + SecurityValues.url_data_id + android_id);
            webview.loadUrl("http://www.example.com");
            Log.i("url",SecurityValues.url_data_klasse + klasse + SecurityValues.url_data_version + version + SecurityValues.url_data_id + android_id);
            Log.i("klasse", klasse);
        } else {
            summary = "<html><body>Keine Internetverbindung!</body></html>";
            //webview.loadData(summary, "text/html", null);
        }
        Log.i("Reload", "finish");
        new ReloadWebView(this, 1, webview);

        /*final ScrollView scrollView = (ScrollView) findViewById(R.id.scrollWebView);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                int scrollX = scrollView.getScrollX(); //for horizontalScrollView
                int scrollY = scrollView.getScrollY(); //for verticalScrollView
                //DO SOMETHING WITH THE SCROLL COORDINATES
                if(scrollY<1) {
                    reload();
                    Log.i("reload","forced");
                }
            }
        });*/


        //webview.getSettings().setJavaScriptEnabled(true);
        //while(true) {

        //}
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

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

        /* if you want to execute the first task immediatly */
        /*
        timer.schedule(this,
                0,               // initial delay null
                seconds * 1000); // subsequent rate
        */
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
                        wv.loadUrl("https://noscio.eu/gf2/" + klasse + ".php?v=" + version + "&i=" + android_id);
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
}
