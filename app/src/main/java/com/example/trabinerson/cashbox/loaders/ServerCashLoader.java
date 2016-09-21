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

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trabinerson.cashbox.RequestQueueSingleton;
import com.example.trabinerson.cashbox.UrlHelper;
import com.example.trabinerson.cashbox.models.FundingOptionsData;
import com.example.trabinerson.cashbox.parsers.SendMoneyParser;

import org.json.JSONObject;

/**
 * TODO: Write Javadoc for ServerCashLoader.
 *
 * @author trabinerson
 */
public class ServerCashLoader extends Loader<FundingOptionsData> {

    private String mAuthToken;
    private int mAmount;

    public ServerCashLoader(Context context, String authToken, int amount) {
        super(context);
        mAuthToken = authToken;
        mAmount = amount;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        String url = UrlHelper.getServerCashUrl(mAuthToken, mAmount);
        JSONObject requestBody = null;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        FundingOptionsData data = SendMoneyParser.parseFundingOptions(response);
                        deliverResult(data);
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        deliverResult(null);
                    }
                }
        );

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Send request
        request.setTag("Test");
        RequestQueueSingleton.getInstance().addToRequestQueue(request);
    }
}
