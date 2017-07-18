package com.facebook.unity;

import android.os.Bundle;
import android.util.Log;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class UnityParams {
    JSONObject json;

    public UnityParams(String s) throws JSONException {
        this.json = new JSONObject(s);
    }

    public UnityParams(JSONObject obj) {
        this.json = obj;
    }

    public UnityParams(Map<String, Serializable> map) {
        this.json = new JSONObject(map);
    }

    public static UnityParams parse(String data, String msg) {
        try {
            return new UnityParams(data);
        } catch (JSONException e) {
            Log.e(FB.TAG, msg);
            return null;
        }
    }

    public static UnityParams parse(String data) {
        return parse(data, "couldn't parse params: " + data);
    }

    public String getString(String key) {
        try {
            return this.json.getString(key);
        } catch (JSONException e) {
            Log.e(FB.TAG, "cannot get string " + key + " from " + toString());
            return "";
        }
    }

    public double getDouble(String key) {
        try {
            return this.json.getDouble(key);
        } catch (JSONException e) {
            Log.e(FB.TAG, "cannot get double " + key + " from " + toString());
            return 0.0d;
        }
    }

    public UnityParams getParamsObject(String key) {
        try {
            return new UnityParams(this.json.getJSONObject(key));
        } catch (JSONException e) {
            Log.e(FB.TAG, "cannot get object " + key + " from " + toString());
            return null;
        }
    }

    public void put(String name, Object value) {
        try {
            this.json.put(name, value);
        } catch (JSONException e) {
            Log.e(FB.TAG, "couldn't add key " + name + " to " + toString());
        }
    }

    public boolean has(String key) {
        return this.json.has(key) && !this.json.isNull(key);
    }

    public Boolean hasString(String key) {
        boolean z = has(key) && getString(key) != "";
        return Boolean.valueOf(z);
    }

    public Bundle getStringParams() {
        Bundle result = new Bundle();
        Iterator<?> keys = this.json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                String value = this.json.getString(key);
                if (value != null) {
                    result.putString(key, value);
                }
            } catch (JSONException e) {
            }
        }
        return result;
    }

    public String toString() {
        return this.json.toString();
    }
}
