package ml.noscio.gf2;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by jgher on 04.05.2016.
*/
public class PopUp extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);
        final WebView webview = (WebView) findViewById(R.id.popupView);
        Button button = (Button) findViewById(R.id.button3);
        assert button != null;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PopUp.this, MainActivity.class);
                startActivity(intent);
            }
        });
        webview.getSettings().setJavaScriptEnabled(true);
        String summary = "<html><body>Bitte warten...</body></html>";
        webview.loadData(summary, "text/html", null);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            webview.loadUrl(url);
        } else {
            summary = "<html><body>Keine Internetverbindung!</body></html>";
            webview.loadData(summary, "text/html", null);
            Intent intent = new Intent(PopUp.this, MainActivity.class);
            startActivity(intent);
        }
    }
    public static String url = "";
}