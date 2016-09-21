/*
 * Copyright (c) 2016 PayPal, Inc.
 *
 * All rights reserved.
 *
 * THIS CODE AND INFORMATION ARE PROVIDED "AS IS" WITHOUT WARRANTY OF ANY
 * KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
 * PARTICULAR PURPOSE.
 */

package com.example.trabinerson.cashbox;

import android.net.Uri;
import android.util.Log;

/**
 * TODO: Write Javadoc for UrlHelper.
 *
 * @author trabinerson
 */
public class UrlHelper {

    private static final String LOG_TAG = UrlHelper.class.getSimpleName();

    private static final String CLIENT_ID = "AZuPnw5fx5EL_eT9N6kAONpEYmrjXZL0TMpRRA4FB2YUkKY_RNu4fGfz1ridNlhVVBnwMFGcPSlpJ2G6";

    // Login URL
    private static final String PAYPAL_LOGIN_URL = "https://www.msmaster.qa.paypal.com/signin/authorize?response_type=code&scope=openid%20https://uri.paypal.com/services/wallet/financial-instruments/view%20https://uri.paypal.com/services/wallet/sendmoney";
    private static final String CLIENT_ID_PARAM = "client_id";
    private static final String RETURN_URL_PARAM = "redirect_uri";

    // Redirect URL
    private static final String REDIRECT_URL = "https://ppcashbox.herokuapp.com/link_success";
    private static final String CODE_PARAM = "code";
    private static final String USER_TOKEN_PARAM = "user_token";
    private static final String FIREBASE_TOKEN_PARAM = "firebase_token";

    // Server URL
    private static final String SERVER_HOST = "http://5773218e.ngrok.io"; // We'll need to change this each time server is hosted on different machine
    private static final String SERVER_LOGIN_PATH = "logged_in";
    private static final String SERVER_CASH_PATH = "cash";

    // Cash endpoint params
    private static final String AMOUNT_PARAM = "amount";


    public static String getLoginUrl() {
        Uri.Builder builder = Uri.parse(PAYPAL_LOGIN_URL).buildUpon()
                .appendQueryParameter(CLIENT_ID_PARAM, CLIENT_ID)
                .appendQueryParameter(RETURN_URL_PARAM, REDIRECT_URL);
        return builder.build().toString();
    }

    public static boolean doesUrlMatchRedirectUrl(String url) {
        String withoutParams = url.split("\\?")[0]; // I know I know
        Log.d(LOG_TAG, "doesUrlMatchRedirectUrl, path without params: " + withoutParams);
        return (withoutParams.equalsIgnoreCase(REDIRECT_URL));
    }

    public static String getCodeFromUrl(String url) {
        Uri uri = Uri.parse(url);
        return uri.getQueryParameter(CODE_PARAM);
    }

    public static String getServerLoginUrl(String authCode, String firebaseToken) {
        return getServerUrlWithPath(authCode, firebaseToken, CODE_PARAM, SERVER_LOGIN_PATH);
    }

    public static String getServerCashUrl(String authcode, int amount) {
        return Uri.parse(getServerUrlWithPath(authcode, null, USER_TOKEN_PARAM, SERVER_CASH_PATH))
                .buildUpon()
                .appendQueryParameter(AMOUNT_PARAM, String.valueOf(amount))
                .build().toString();
    }

    private static String getServerUrlWithPath(String authCode, String firebaseToken, String authCodeParam, String path) {
        Uri.Builder builder = Uri.parse(SERVER_HOST).buildUpon()
                .appendPath(path)
                .appendQueryParameter(authCodeParam , authCode);
        if (firebaseToken != null) {
            builder.appendQueryParameter(FIREBASE_TOKEN_PARAM, firebaseToken);
        }
        return builder.build().toString();
    }

}
