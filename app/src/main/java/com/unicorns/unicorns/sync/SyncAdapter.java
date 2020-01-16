package com.unicorns.unicorns.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.unicorns.unicorns.SingletonRequestQueue;
import com.unicorns.unicorns.UnicornsApp;
import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.database.UnicornDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SyncAdapter created by Andreas Pribitzer 14.01.2020
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public static final String TAG = "SyncAdapter";

    //Base Url
    private static final String BASE_URL = "https://crudcrud.com/api/131c7082e3e642f9b411bd296bb310bb";
    //Endpoint where Unicorns get saved to
    private static final String EP_UNICORNS = "/unicorns";

    private UnicornDao unicornDao;
    private Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        unicornDao = ((UnicornsApp) context).getDaoSession().getUnicornDao();
        mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.i(TAG, "onPerformSync called");
        getUnicorns();
    }

    private void getUnicorns() {
        //Fetch all Unicorns from the webserver and convert them into a List with Gson
        final Gson gson = new GsonBuilder().create();
        String url = BASE_URL + EP_UNICORNS;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Successfully fetched unicorns: " + response);
                        Type unicornListType = new TypeToken<ArrayList<Unicorn>>(){}.getType();
                        ArrayList<Unicorn> unicorns = gson.fromJson(response, unicornListType);
                        //If this operation was successful and I got the unicorns, I start syncing
                        sync(unicorns);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error fetching unicorns");
                        error.printStackTrace();
                    }
                });

        SingletonRequestQueue.getInstance(mContext).getRequestQueue(mContext).add(getRequest);
    }

    private void sync(List<Unicorn> unicorns) {
        //Checking if there is a Unicorn in the local database that is not in the webserver
        //If yes, add it to the webserver
        final List<Unicorn> unicornsLocal = unicornDao.loadAll();
        for(int i = 0; i < unicornsLocal.size(); i++) {
            if(!unicorns.contains(unicornsLocal.get(i))) {
                addUnicorn(unicornsLocal.get(i));
            }
        }

        //Checking if there is a Unicorn in the webserver that is not in the local database
        //If yes, delete it to the webserver
        for (int i = 0; i < unicorns.size(); i++) {
            if(!unicornsLocal.contains(unicorns.get(i))) {
                deleteUnicorn(unicorns.get(i));
            }
        }
    }

    //Adding Unicorn to the webserver
    private void addUnicorn(Unicorn unicorn) {
        //Create a JSON object of the Unicorn with Gson
        String url = BASE_URL + EP_UNICORNS;
        Gson gson = new GsonBuilder().create();
        String jsonStringUnicorn = gson.toJson(unicorn);
        JSONObject jsonUnicorn;
        try {
            jsonUnicorn = new JSONObject(jsonStringUnicorn);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }
        Log.i(TAG, "add Unicorn: " + jsonUnicorn.toString());

        //POST Request to create Unicorn in the webserver
        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url, jsonUnicorn,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "add Unicorn successful: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "add Unicorn Error: " + error);
                        error.printStackTrace();
                    }
                }) {

            //Declaring the content type as JSON
            //Without this, Unexpected response code 415 occurs
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        SingletonRequestQueue.getInstance(mContext).getRequestQueue(mContext).add(postRequest);
    }

    //Delete unicorn from the webserver
    private void deleteUnicorn(Unicorn unicorn) {
        //Creating a JSON object of the Unicorn
        String url = BASE_URL + EP_UNICORNS;
        Gson gson = new GsonBuilder().create();
        String jsonStringUnicorn = gson.toJson(unicorn);
        JSONObject jsonUnicorn;
        try {
            jsonUnicorn = new JSONObject(jsonStringUnicorn);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        //DELETE request to delete Unicorn from webserver
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, jsonUnicorn,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Delete Unicorn success: " + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Delete Unicorn error: " + error);
                        error.printStackTrace();
                    }
                });

        SingletonRequestQueue.getInstance(mContext).getRequestQueue(mContext).add(deleteRequest);
    }
}