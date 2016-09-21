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

package com.example.trabinerson.cashbox.loaders;

import android.content.Context;
import android.content.Loader;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabinerson.cashbox.RequestQueueSingleton;
import com.example.trabinerson.cashbox.UserData;
import com.example.trabinerson.cashbox.UrlHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author trabinerson
 */
public class ServerConnectLoader extends Loader<UserData> {

    private static final String LOG_TAG = ServerConnectLoader.class.getSimpleName();

    private String mAuthToken;

    public ServerConnectLoader(Context context, String authToken) {
        super(context);
        mAuthToken = authToken;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        String url = UrlHelper.getServerLoginUrl(mAuthToken);

        JSONObject requestBody = null;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, requestBody,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(LOG_TAG, "Connected to server");
                        String token = null;
                        String email = null;
                        String fullName = null;
                        try {
                            token = response.getString("user_token");
                            email = response.getString("user_email");
                            fullName = response.getString("user_fullname");
                        } catch (JSONException e) {}
                        deliverResult(new UserData(token, email, fullName));
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, "Error while connecting to server", error);
                        deliverResult(null);
                    }
                });
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Send request
        request.setTag("Test");
        RequestQueueSingleton.getInstance().addToRequestQueue(request);
    }
}