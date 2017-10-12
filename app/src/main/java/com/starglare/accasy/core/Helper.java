package com.starglare.accasy.core;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.amulyakhare.textdrawable.TextDrawable;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.starglare.accasy.R;
import com.starglare.accasy.fragments.CategoryFragment;
import com.starglare.accasy.fragments.FeedbackFragment;
import com.starglare.accasy.fragments.FormFragment;
import com.starglare.accasy.fragments.HistoryFragment;
import com.starglare.accasy.fragments.ReportMapFragment;
import com.starglare.accasy.fragments.SubCategoryFragment;
import com.starglare.accasy.models.ReportModel;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.starglare.accasy.models.FragmentNames.CATEGORY_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.FEEDBACK_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.FORM_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.HISTORY_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.REPORT_MAP_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.SUB_CATEGORY_FRAGMENT;

/**
 * Created by MuhammadAmin on 7/20/2017.
 */

public class Helper {

    public static final int GPS_PERMISSION_CODE = 1;
    public static final int CAMERA_PERMISSION_CODE = 2;
    public static final String USER_AGENT = "accasy";
    public static final String KEY = "";
    public static String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static int getResourceId(String name, String type, Context context) {
        //String resourceFolder = name.contains("layout") ? "layout" : "drawable";
        return context.getResources().getIdentifier(name,type, context.getPackageName());
    }

    public static String formatString(String categoryName) {
        return categoryName.replace("_"," ");
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            //Check for granular permissions in marshmallow and above
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            }
        }
        return  true;
    }

    public static void requestForPermission(Activity activity, int PERMISSION_CODE) {
        ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_CODE);
    }

    public static void showYesNoAlert(Context context, String title, String content, String cancelText, String confirmText, final Void callback) {
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setCancelText(cancelText)
                .setConfirmText(confirmText)
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }

    public static SweetAlertDialog showAlert(Context context, int alertType,  String title, String content){
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, alertType);
        sweetAlertDialog.setTitleText(title);
        sweetAlertDialog .setContentText(content);
        sweetAlertDialog .show();
        return sweetAlertDialog;
    }
    public static boolean isGPSEnabled(Context context) {
        LocationManager contentResolver = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gpsStatus = contentResolver.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            //allow network activity on main thread
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            String host = /*new REST(context).invoke().getHost();*/ "http://www.google.com";
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL(host).openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("", "Error checking internet connection", e);
            }
        } else {
            Log.d("", "No network available!");
        }
        return false;
    }

    public static void hasActiveInternetConnectionAsync(Context context, final onVolleyRequestCompleteCallBack completeCallBack) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = /*new REST(context).invoke().getHost();*/ "http://www.google.com";

        // Request a string response from the provided REST.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        completeCallBack.onSuccessPing();
                        Log.i(getClass().getSimpleName(),"Ping success");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                completeCallBack.onErrorPing();
                Log.e(getClass().getSimpleName(),"Ping error");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * Function to show settings alert dialog
     */
    public static AlertDialog showSettingsAlert(final Context context, String title, String message, String positiveButton, String negativeButton)
    {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        //Setting Dialog Title
        alertDialog.setTitle(title);

        //Setting Dialog Message
        alertDialog.setMessage(message);


        alertDialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
            }
        });


        alertDialog.setNegativeButton(negativeButton, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // dialogCancelListener.onCancel();
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();
        return  alert;
    }

    public static void sendTestRequest(final Context context) {

        RequestQueue queue = Volley.newRequestQueue(context);
        REST rest = new REST(context).invoke();
        String url = rest.getHost(); //context.getString(R.string.host); // "http://192.168.137.1:8080/accasy/public/api/report";
        String endpoint = rest.getEndpoint(); //context.getString(R.string.endpoint);
        StringRequest stringRequest = new StringRequest( String.format("%s/%s",url,endpoint),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("Response: ", response.toString());
                        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Request Successful")
                            .setContentText(response.toString())
                            .show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Response: ", error.toString());
            }
        }
        );
        queue.add(stringRequest);
    }

    public static void sendReport(final Context context, ReportModel model, final onVolleyRequestCompleteCallBack onVolleyRequestCompleteCallBack) {
            sendReport(context,model,false,onVolleyRequestCompleteCallBack);
    }

    public static void sendReport(final Context context, ReportModel model, boolean retry, final onVolleyRequestCompleteCallBack onVolleyRequestCompleteCallBack) {
        RequestQueue queue = Volley.newRequestQueue(context);
        REST REST = new REST(context).invoke();
        String host = REST.getHost();
        String endpoint = REST.getEndpoint();
        String url = String.format("%s/%s",host,endpoint); //"http://192.168.137.1:8080/accasy/public/api/report";
        final String requestBody = convertModelToJson(model).toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String jResponse = jsonObject.optString("message");
                            Log.i("User details", response);
                            if (jResponse.equals("ok"))
                                onVolleyRequestCompleteCallBack.onReportPostSuccess(response);
                            else {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Error")
                                        .setContentText(response)
                                        .show();
                            }
                        }catch (Exception e) {
                            Log.e(getClass().getSimpleName(),e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onVolleyRequestCompleteCallBack.onReportPostError();
                  Log.e("VolleyError", error.toString());
                error.printStackTrace();
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

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("User-Agent", USER_AGENT);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(new String(response.data));
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }

        };
        if(retry){
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }

        queue.add(stringRequest);
    }

    public static JSONObject convertModelToJson(ReportModel model) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category", model.getCategory());
            jsonObject.put("sub_category", model.getSubCategory());
            jsonObject.put("coordinates", model.getCoordinates());
            jsonObject.put("comment", model.getComment());
            jsonObject.put("phone_number",model.getPhoneNumber());
            if(model.getImage() != null)
            jsonObject.put("photo", convertImageToString(BitmapFactory.decodeByteArray(model.getImage(), 0, model.getImage().length)));
            jsonObject.put("report_timestamp", model.getTime());
        } catch (Exception e) {
             Log.e("Json object", e.getMessage());
        }
        return jsonObject;
    }

    public static String convertImageToString(Bitmap image) {
         byte[] imageBytes = convertImageToByte(image);
        String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return imageString;
    }

    public static byte[] convertImageToByte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        return baos.toByteArray();
    }

    public static TextDrawable getDrawable(String letter) {
       // int color = letter == "S" ? Color.GRAY : Color.LTGRAY;
        TextDrawable drawable = TextDrawable.builder()
                .buildRect(letter, Color.GRAY);

        return drawable;
    }


    //call backs for volley request response and error
    public interface onVolleyRequestCompleteCallBack {
        void onReportPostSuccess(String id);
        void onReportPostError();
        void onSuccessPing();
        void onErrorPing();
    }


    public static Fragment getCurrentFragment(String fragmentName, String key, String data) {

        Fragment fragment = getFragment(fragmentName);
        if(data != null) addBundleToFragment(fragment,key,data);
        return fragment;
    }

    public static Fragment getCurrentFragment(String fragmentName, Map<String,String> keyValues) {
        Fragment fragment = getFragment(fragmentName);
        if (keyValues != null) {
            addBundleToFragment(fragment,keyValues);
        }

        return fragment;
    }

    private static Fragment getFragment(String name) {
        Fragment fragment;
        switch (name) {
            case CATEGORY_FRAGMENT:
                fragment = new CategoryFragment();
                break;
            case SUB_CATEGORY_FRAGMENT:
                fragment = new SubCategoryFragment();
                break;
            case FORM_FRAGMENT:
                fragment = new FormFragment();
                break;
            case REPORT_MAP_FRAGMENT:
                fragment = new ReportMapFragment();
                break;
            case HISTORY_FRAGMENT:
                fragment = new HistoryFragment();
                break;
            case FEEDBACK_FRAGMENT:
                fragment = new FeedbackFragment();
                break;
            default:
                fragment = new Fragment();
        }
        return fragment;
    }

    private static void addBundleToFragment(Fragment fragment, String key, String data) {
        Bundle args = new Bundle();
        args.putString(key, data);
        fragment.setArguments(args);
    }

    private static void addBundleToFragment(Fragment fragment, Map<String,String> keyValues) {
        Bundle args = new Bundle();
        for(String key : keyValues.keySet()) {
            args.putString(key, keyValues.get(key));
        }

        fragment.setArguments(args);
    }


    public static ReportModel generateReportModelFromCursor(Cursor result) {
        ReportModel model = ReportModel.getModelInstance();
        model.setId(result.getInt(result.getColumnIndex(Logger.ReportEntry._ID)));
        model.setCategory(result.getString(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_CATEGORY)));
        model.setSubCategory(result.getString(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_SUB_CATEGORY)));
        model.setCoordinates(result.getString(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_COORDINATES)));
        model.setComment(result.getString(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_COMMENT)));
        model.setPhoneNumber(result.getString(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_PHONE_NUMBER)));
        model.setTime(result.getLong(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_TIME_STAMP)));
        model.setImage(result.getBlob(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_IMAGE)));
        model.setPosted(result.getInt(result.getColumnIndex(Logger.ReportEntry.COLUMN_NAME_POSTED)));
        return model;
    }

    private static class REST {
        private Context context;
        private String host;
        private String endpoint;

        public REST(Context context) {
            this.context = context;
        }

        public String getHost() {
            return host;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public REST invoke() {
            host = context.getString(R.string.host);
            endpoint = context.getString(R.string.endpoint);
            return this;
        }
    }
}
