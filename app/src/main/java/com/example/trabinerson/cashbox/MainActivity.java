package com.example.trabinerson.cashbox;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.trabinerson.cashbox.loaders.ServerConnectLoader;
import com.example.trabinerson.cashbox.models.UserData;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 *
 * @author trabinerson
 */
public class MainActivity extends Activity {

    public static final String EXTRA_USER_DATA = "extra_user_data";
    public static final String EXTRA_MERCHANT = "extra_merchant";
    public static final String EXTRA_AMOUNT = "extra_amount";

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private static final int SERVER_CONNECT_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RequestQueueSingleton.init(this);

        WebView webView = (WebView) findViewById(R.id.web_view);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(LOG_TAG, "shouldOverrideUrlLoading: " + url);
                if (UrlHelper.doesUrlMatchRedirectUrl(url)) {
                    String authCode = UrlHelper.getCodeFromUrl(url);
                    String firebaseToken = FirebaseInstanceId.getInstance().getToken();
                    connectToServer(authCode, firebaseToken);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(UrlHelper.getLoginUrl());
    }


    private void connectToServer(final String authCode, final String firebaseToken) {
        Log.d(LOG_TAG, "Connecting to server...");

        final Context context = this;
        getLoaderManager().initLoader(SERVER_CONNECT_LOADER_ID, null, new LoaderManager.LoaderCallbacks<UserData>() {
            @Override
            public Loader<UserData> onCreateLoader(int id, Bundle args) {
                return new ServerConnectLoader(context, authCode, firebaseToken);
            }

            @Override
            public void onLoadFinished(Loader<UserData> loader, final UserData result) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        if (result == null || result.isError()) {
                            goToErrorFragment();
                        } else {
                            goToMapFragment(result);
                        }
                    }
                });
            }

            @Override
            public void onLoaderReset(Loader<UserData> loader) {

            }
        });

    }

    private void goToErrorFragment() {
        // TODO
    }

    private void goToMapFragment(UserData userData) {
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(EXTRA_USER_DATA, userData);
        intent.putExtras(extras);
        startActivity(intent);
        finish();
    }
}
