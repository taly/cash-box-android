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

import android.content.Context;
import android.net.Uri;
import android.content.Loader;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.List;

/**
 * TODO: Write Javadoc for PlacesLoader.
 *
 * @author slorber
 */
public class PlacesLoader extends Loader<List<Merchant>> {

    private static final String URL = "https://maps.googleapis.com";///maps/api/place/nearbysearch/json&radius=2000&type=store&key=AIzaSyBesqSxaaDFBNkRWPJD6YdI1P2yVEUBPS8?location=32.0628141,34.7704065";
    private List<Merchant> mMerchantList;
    private Context mContext;
    private double mLat;
    private double mLng;

    public PlacesLoader(Context context,double lat,double lng) {
        super(context);
        mContext = context;
        mLat = lat;
        mLng = lng;
    }

    @Override
    protected void onStartLoading() {
        if (mMerchantList != null) {
            deliverResult(mMerchantList);
        } else {
            forceLoad();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
        getRequest(mContext, Uri.parse(URL)
                .buildUpon()
                .appendPath("maps")
                .appendPath("api")
                .appendPath("place")
                .appendPath("nearbysearch")
                .appendPath("json")
                .appendQueryParameter("location", mLat+","+mLng)
                .appendQueryParameter("radius", "20000")
                .appendQueryParameter("type", "store")
                .appendQueryParameter("key", "AIzaSyBesqSxaaDFBNkRWPJD6YdI1P2yVEUBPS8")
                .appendQueryParameter("language", "en")
                .build().toString(),null);

}
    @Override
    public void deliverResult(List<Merchant> list) {
        mMerchantList = list;
        super.deliverResult(list);
    }


    public void getRequest(Context context, String url, final Response.Listener listener) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson g = new Gson();
                        MerchantContainer mc = g.fromJson(response.toString(), MerchantContainer.class);
                        deliverResult(mc.getMerchants());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Error.Response", error.toString());

                    }
                }
        );
        queue.add(getRequest);
    }
}

