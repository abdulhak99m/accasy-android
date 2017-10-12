package com.starglare.accasy.services;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.starglare.accasy.R;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.core.Logger;
import com.starglare.accasy.models.ReportModel;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SyncService extends IntentService {
    Logger logger;
    URL url;
    JSONObject jsonObject;
    HttpURLConnection urlConn;
    DataOutputStream printout;
    DataInputStream input;
    Cursor cursor;

    public SyncService() {
        super(SyncService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        logger = Logger.getInstance(getBaseContext());
        Intent intent1 = new Intent(Intent.ACTION_VIEW);
        cursor = logger.selectReportNotSentToServer();
            while (cursor.moveToNext()){
                ReportModel model = Helper.generateReportModelFromCursor(cursor);

                send(model);
            }
    }

    private void sendReport(ReportModel model) {
        jsonObject = Helper.convertModelToJson(model);
        Log.i(getClass().getSimpleName(),jsonObject.toString());
        String host = getBaseContext().getString(R.string.localhost);
        String endpoint = getBaseContext().getString(R.string.endpoint);
        String address = String.format("%s/%s",host,endpoint);
        try{
            url = new URL(address);
            urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestMethod("POST");
            //urlConn.setConnectTimeout(10000);
            //urlConn.setReadTimeout(10000);
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestProperty("User-Agent", Helper.USER_AGENT);
            urlConn.setRequestProperty("Content-Type","application/json; charset = utf-8");
            //urlConn.setRequestProperty("Content-Length",String.valueOf(jsonObject.toString().length()));
            urlConn.setRequestProperty("charset","utf-8");
            urlConn.connect();
            printout = new DataOutputStream(urlConn.getOutputStream());
            String requestbody = jsonObject.toString().replace("\\n","");
            printout.write(requestbody.getBytes());
            printout.flush();
            printout.close();
            Log.i(getClass().getSimpleName(),"Request sent");
            int responseCode = urlConn.getResponseCode();
            Log.i(getClass().getSimpleName(),responseCode+" "+urlConn.getResponseMessage()/*new DataInputStream(urlConn.getInputStream()).readLine()*/);
            if( responseCode == HttpURLConnection.HTTP_OK){
                input = new DataInputStream(urlConn.getInputStream());
                Log.i(getClass().getSimpleName(),input.readLine());
                Log.i(getClass().getSimpleName(),"Response received");
               /* logger = Logger.getInstance(getBaseContext());
                logger.updateReport(String.valueOf(model.getId()));*/
            }
        }catch (Exception e){
            Log.e(getClass().getSimpleName(),e.toString());
            e.printStackTrace();
        }
    }

    private void send(ReportModel model){
        RequestQueue queue = Volley.newRequestQueue(getBaseContext());
        String host = getBaseContext().getString(R.string.host);
        String endpoint = getBaseContext().getString(R.string.endpoint);
        String url = String.format("%s/%s",host,endpoint); //"http://192.168.137.1:8080/accasy/public/api/report";

        RequestFuture<JSONObject> future = RequestFuture.newFuture();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, Helper.convertModelToJson(model), future, future){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("User-Agent", Helper.USER_AGENT);
                return params;
            }
        };
        queue.add(request);

        try {
            JSONObject response = future.get(); // this will block
            Log.d(getClass().getSimpleName(),String.valueOf(model.getId()));
            Log.d(getClass().getSimpleName(),response.toString());
            if(response.optString("message").equals("ok"))
                 logger.updateReport(String.valueOf(model.getId()));
            Log.i(getClass().getSimpleName(),response.toString());
        } catch (InterruptedException e) {
            Log.e(getClass().getSimpleName(),e.toString());
        } catch (ExecutionException e) {
            Log.e(getClass().getSimpleName(),e.toString());
        }catch (Exception e){
            Log.e(getClass().getSimpleName(),e.toString());
        }
    }

  /*  @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(getClass().getSimpleName(),"Service started");

        if(Helper.hasActiveInternetConnection(getBaseContext())) {
            logger = Logger.getInstance(getBaseContext());
            cursor = logger.selectReportNotSentToServer();
            String host = getBaseContext().getString(R.string.host);
            String endpoint = getBaseContext().getString(R.string.endpoint);
            String url = String.format("%s/%s",host,endpoint); //"http://192.168.137.1:8080/accasy/public/api/report";
            while (cursor.moveToNext()){
                ReportModel model = Helper.generateReportModelFromCursor(cursor);
                String params = Helper.convertModelToJson(model).toString();
                //int id = cursor.getInt(cursor.getColumnIndex(Logger.ReportEntry._ID));
                Helper.sendReport(getBaseContext(),model,true,callBack);
               // AsyncVolleyRequest request = new AsyncVolleyRequest(getBaseContext(),params,url,id,callBack);
               // request.execute();
                break;
            }
        }
        return START_STICKY;
    }*/
}
