package com.starglare.accasy.core;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by MuhammadAmin on 8/29/2017.
 */

public class App extends Application {
    private static App  Instance;
    public static final String TAG = App.class.getSimpleName();
    private RequestQueue requestQueue;
    public App() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Instance = this;


    }
    public static synchronized App getInstance(){return Instance;}

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return requestQueue;
    }
    public <T> void addToRequestQueue(com.android.volley.Request<T> req) {
        // set the default tag if tag is empty
       // req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
}
