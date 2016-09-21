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
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Used to keep only one instance of the request queue throughout the application.
 */
public class RequestQueueSingleton {
    private static RequestQueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;

    private static String LOG_TAG = RequestQueueSingleton.class.getSimpleName();

    private RequestQueueSingleton() {

        // Make sure we've been initialized
        if (mCtx == null) {
            throw new ExceptionInInitializerError(
                    "RequestQueueSingleton constructor called without having initialized it first");
        }

        mRequestQueue = getRequestQueue();
    }

    public static synchronized void init(Context context) {
        if (context == null) {
            Log.w(LOG_TAG, "Trying to initialize RequestQueueSingleton with null context");
            return;
        }
        Log.i(LOG_TAG, "Initializing RequestQueueSingleton");
        mCtx = context;
    }

    public static synchronized RequestQueueSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new RequestQueueSingleton();
        }
        return mInstance;
    }

    public void cancelAllRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

