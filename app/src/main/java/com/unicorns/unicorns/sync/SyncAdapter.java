package com.unicorns.unicorns.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.unicorns.unicorns.SingletonRequestQueue;
import com.unicorns.unicorns.UnicornsApp;
import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.database.UnicornDao;
import com.unicorns.unicorns.models.UnicornWithId;
import com.unicorns.unicorns.utils.JsonFormatter;

import org.json.JSONException;
import org.json.JSONObject;

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
    private static final String BASE_URL = "https://crudcrud.com/api/7df2ecf6b0d8445ba587cac195310c36";
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
        //Fetch all Unicorns from the webserver and convert them into a List
        String url = BASE_URL + EP_UNICORNS;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Successfully fetched unicorns: " + response);
                        List<UnicornWithId> unicorns;
                        try {
                            unicorns = JsonFormatter.getUnicornList(response);
                            //If this operation was successful and I got the unicorns, I start syncing
                            sync(unicorns);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private void sync(List<UnicornWithId> unicorns) {
        //Checking if there is a Unicorn in the local database that is not in the webserver
        //If yes, add it to the webserver
        final List<Unicorn> unicornsLocal = unicornDao.loadAll();
        final ArrayList<Unicorn> unicornsWeb = new ArrayList<>();
        for(UnicornWithId unicorn: unicorns) {
            unicornsWeb.add(unicorn.getUnicorn());
        }
        for(int i = 0; i < unicornsLocal.size(); i++) {
            if(!unicornsWeb.contains(unicornsLocal.get(i))) {
                addUnicorn(unicornsLocal.get(i));
            }
        }

        //Checking if there is a Unicorn in the webserver that is not in the local database
        //If yes, delete it to the webserver
        for(int i = 0; i < unicornsWeb.size(); i ++) {
            if(!unicornsLocal.contains(unicornsWeb.get(i))) {
                deleteUnicorn(unicorns.get(i).getId());
            }
        }
    }

    //Adding Unicorn to the webserver
    private void addUnicorn(Unicorn unicorn) {
        //Create a JSON object of the Unicorn
        String url = BASE_URL + EP_UNICORNS;
        JSONObject jsonUnicorn;
        try {
            jsonUnicorn = JsonFormatter.getParsedUnicornWeb(unicorn);
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
    private void deleteUnicorn(String _id) {
        //Creating a JSON object of the Unicorn
        String url = BASE_URL + EP_UNICORNS + "/" + _id;

        //DELETE request to delete Unicorn from webserver
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "Delete Unicorn successful");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Delete Unicorn error: " + error);
                        error.printStackTrace();
                    }
                });

        SingletonRequestQueue.getInstance(mContext).getRequestQueue(mContext).add(stringRequest);
    }
}