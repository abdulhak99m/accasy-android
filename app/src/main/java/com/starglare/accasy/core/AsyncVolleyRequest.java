package com.starglare.accasy.core;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by MuhammadAmin on 8/29/2017.
 */

public class AsyncVolleyRequest extends AsyncTask<String, Void, String>  {

    private static String TAG= "AsyncVolleyRequest";
    Context context;
    String requestBody;
    Helper.onVolleyRequestCompleteCallBack resultListener;
    int idToUpdate;
    String url;


    public AsyncVolleyRequest(Context context, String requestBody, String url, int idToUpdate, Helper.onVolleyRequestCompleteCallBack resultListener){
        this.context= context;
        this.requestBody = requestBody;
        this.url= url;
        this.resultListener= resultListener;
        this.idToUpdate= idToUpdate;
    }


    @Override
    protected void onPostExecute(String o) {
        super.onPostExecute(o);
    }

    @Override
    protected String doInBackground(String... params) {
        StringRequest myReq = new StringRequest(Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, response.toString());
                        try {
                            resultListener.onReportPostSuccess(String.valueOf(idToUpdate));

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }) {

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        // Adding request to request queue
        App.getInstance().addToRequestQueue(myReq);
        return null;
    }
}
