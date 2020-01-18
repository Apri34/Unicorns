package com.unicorns.unicorns.utils;

import com.unicorns.unicorns.database.Unicorn;
import com.unicorns.unicorns.models.UnicornWithId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * JsonFormatter created by Andreas Pribitzer 18.01.2020
 * This class is used to parse Objects into JSONObjects and vice versa
 *
 * When posting an Unicorn to the webserver, it is important that it
 * does not contain an id, because it gets generated automatically by
 * the crudcrud API. Therefore, parsing Objects from the webserver and
 * parsing objects from the local database require different methods.
 */

public class JsonFormatter {
    //Method to get parsed JSON for webserver without id
    public static JSONObject getParsedUnicornWeb(Unicorn unicorn) throws JSONException {
        String jsonString = "{\"name\":\"" + unicorn.getName() + "\",\"age\":" + unicorn.getAge() + ",\"colour\":\"" + unicorn.getColour() + "\"}";
        return new JSONObject(jsonString);
    }

    //Method to get parsed Unicorn for DialogFragment
    public static JSONObject getParsedUnicornLocal(Unicorn unicorn) throws JSONException {
        String jsonString = "{\"id\":\"" + unicorn.getId() + "\",\"name\":\"" + unicorn.getName() + "\",\"age\":" + unicorn.getAge() + ",\"colour\":\"" + unicorn.getColour() + "\"}";
        return new JSONObject(jsonString);
    }

    //Method to get a List of UnicornsWithId from JsonString. UnicornsWithId contains
    // a Unicorn object and the id from the webserver, which is important to hae if the
    // unicorn is meant to be deleted from the webserver.
    public static List<UnicornWithId> getUnicornList(String jsonString) throws JSONException {
        ArrayList<UnicornWithId> unicorns = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(jsonString);
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Unicorn unicorn = getUnicornFromWebJsonObject(jsonObject);
            UnicornWithId unicornWithId = new UnicornWithId(jsonObject.getString("_id"), unicorn);
            unicorns.add(unicornWithId);
        }
        return unicorns;
    }

    //Method to get Unicorn from Webserver without id (it is not needed,
    // because the Unicorn object is only used to compare its atributes).
    public static Unicorn getUnicornFromWebJsonObject(JSONObject jsonObject) throws JSONException {
        Unicorn unicorn = new Unicorn();
        unicorn.setName(jsonObject.getString("name"));
        unicorn.setAge(jsonObject.getInt("age"));
        unicorn.setColour(jsonObject.getString("colour"));
        return unicorn;
    }

    //Method to get Unicorn from JSOnObject. Used for deserialization in DialogFragment
    public static Unicorn getUnicornFromLocalJsonObject(JSONObject jsonObject) throws JSONException {
        Unicorn unicorn = new Unicorn();
        unicorn.setId(jsonObject.getString("id"));
        unicorn.setName(jsonObject.getString("name"));
        unicorn.setAge(jsonObject.getInt("age"));
        unicorn.setColour(jsonObject.getString("colour"));
        return unicorn;
    }
}
