package com.unicorns.unicorns;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * SingletonRequestQueue created by Andreas Pribitzer 14.01.2020
 * In order to have only on RequestQueue
 */

public class SingletonRequestQueue {

    private static SingletonRequestQueue instance;
    private RequestQueue mRequestQueue;

    private SingletonRequestQueue(Context context) {
        mRequestQueue = getRequestQueue(context);
    }

    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if(instance == null) {
            instance = new SingletonRequestQueue(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue(Context context) {
        if(mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
        return mRequestQueue;
    }
}
