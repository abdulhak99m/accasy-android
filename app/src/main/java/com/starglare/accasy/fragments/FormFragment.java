package com.starglare.accasy.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.starglare.accasy.activity.BaseActivity;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.R;
import com.starglare.accasy.core.Logger;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import pl.droidsonroids.gif.GifImageView;

import static android.app.Activity.RESULT_OK;
import static com.starglare.accasy.models.FragmentNames.CATEGORY_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.FORM_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.SUB_CATEGORY_FRAGMENT;


public class FormFragment extends BaseFragment implements LocationListener, Helper.onVolleyRequestCompleteCallBack {

    private static final String TAG = "FORM_FRAGMENT";
    private EditText coordinates;
    private EditText comment;
    private EditText phoneNumber;
    private ImageView photo;
    private GifImageView refresh;
    private Button submit;
    private long minTime = 0;
    private float minDistance = 0;
    BroadcastReceiver gpsReceiver;
    LocationManager locationManager;
    private int CAMERA_START_REQUEST = 1;
    Logger logger;
    boolean gettingCoordinates = false;
    boolean canSendReport = true;
    boolean imageCaptured = false;
    SweetAlertDialog progressDialog;
    public FormFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNavigationStack();
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        logger = Logger.getInstance(getContext());
        progressDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        progressDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        progressDialog.setTitleText("Please wait...");
        progressDialog.setCancelable(false);
        view = getView();

        coordinates = (EditText) view.findViewById(R.id.coordinates);
       // coordinates.setText("9.071856,7.486240");
       // modelChangeCallback.setCoordinates("9.071856,7.486240");
        comment = (EditText) view.findViewById(R.id.comment);
        phoneNumber = (EditText) view.findViewById(R.id.phone_number);
        photo = (ImageView) view.findViewById(R.id.photo);
        refresh = (GifImageView) view.findViewById(R.id.refresh);
        submit = (Button) view.findViewById(R.id.submit);

        photo.setOnClickListener(onClickListener);
        refresh.setOnClickListener(onClickListener);
        submit.setOnClickListener(onClickListener);

        gpsReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                getcoordinates();
                changeRefreshIcon();
                //context.startActivity(new Intent(context, MainActivity.class));
            }
        };
        getContext().registerReceiver(gpsReceiver,new IntentFilter("android.location.PROVIDERS_CHANGED"));

        getcoordinates();
        changeRefreshIcon();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.refresh:
                    /*getcoordinates();
                    changeRefreshIcon();*/
                    break;
                case R.id.photo:
                    startCamera();
                    break;
                case R.id.submit:
                    processReport();
                    //showAlert();
                    break;
            }
        }
    };

    private void processReport() {
        if(requiredFieldsProvided()){
            progressDialog.show();
            modelChangeCallback.setComment(comment.getText().toString());
            modelChangeCallback.setPhoneNumber(phoneNumber.getText().toString());
            modelChangeCallback.setTime(System.currentTimeMillis()/1000);
            logger.insertReport(model);
            Helper.hasActiveInternetConnectionAsync(getContext(),this);
        }

    }

    private void changeRefreshIcon() {
        if(gettingCoordinates) {
            refresh.setImageResource(R.drawable.loading);
        }else {
            refresh.setImageResource(R.drawable.load);
        }

    }

    private void sendReport() {
        Helper.sendReport(getContext(),model,this);
    }

    private boolean requiredFieldsProvided() {
        //reset flag
        canSendReport = canSendReport ? canSendReport : !canSendReport;
        if(coordinates.getText().toString().trim().length() == 0) {
            coordinates.setError("Required");
            canSendReport = false;
        }
        if(comment.getText().toString().trim().length() == 0) {
            comment.setError("Required");
            canSendReport = false;
        }
        if(!imageCaptured) {
            Toast.makeText(getContext(),"Image is required",Toast.LENGTH_SHORT).show();
            canSendReport = false;
        }
        return canSendReport;
    }

    private void startCamera() {
        Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(cameraIntent, CAMERA_START_REQUEST);
    }

    private void showAlert() {
        Helper.sendTestRequest(getContext());
    }


    @SuppressWarnings("MissingPermission")
    public void getcoordinates() {
       if(Helper.hasPermissions(getContext(),Helper.PERMISSIONS)) {
           if(Helper.isGPSEnabled(getContext())) {
               locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
               /*Log.i("Last known location", String.valueOf(locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)));*/
               locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
               gettingCoordinates = true;
           }else {
               new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                       .setTitleText("Settings")
                       .setContentText("You need to enable your gps. Goto settings?")
                       .setCancelText(getString(R.string.permissionCancelText))
                       .setConfirmText(getString(R.string.permissionConfirmText))
                       .showCancelButton(true)
                       .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sweetAlertDialog) {
                               Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                               startActivity(intent);
                               sweetAlertDialog.cancel();
                           }
                       })
                       .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                           @Override
                           public void onClick(SweetAlertDialog sDialog) {
                               sDialog.cancel();
                           }
                       })
                       .show();
           }
       }else {
           requestPermissions(Helper.PERMISSIONS,Helper.GPS_PERMISSION_CODE);
       }
    }

    private void updateNavigationStack() {
        CURRENT_FRAGMENT = FORM_FRAGMENT;
        NEXT_FRAGMENT = CATEGORY_FRAGMENT;
        PREVIOUS_FRAGMENT = SUB_CATEGORY_FRAGMENT;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (gpsReceiver != null) {
            getContext().unregisterReceiver(gpsReceiver);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final List<String> permissionsNotGrantedWithRationale = new ArrayList<>();
        final List<String> permissionsNotGrantedWithNoRationale = new ArrayList<>();
        final List<String> permissionsGranted = new ArrayList<>();
        boolean requestAgain = true;
        if(grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                    permissionsNotGrantedWithRationale.add(permissions[i]);
                } else if(grantResults[i] != PackageManager.PERMISSION_GRANTED && !ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i])) {
                    permissionsNotGrantedWithNoRationale.add(permissions[i]);
                }else if(grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionsGranted.add(permissions[i]);
                }
            }
            if(permissionsNotGrantedWithRationale.size() > 0) {
                new SweetAlertDialog(getContext(), SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(getString(R.string.permissionTitle))
                        .setContentText(getString(R.string.permissionContent))
                        .setCancelText(getString(R.string.permissionCancelText))
                        .setConfirmText(getString(R.string.permissionConfirmText))
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                requestPermissions(Helper.PERMISSIONS,Helper.GPS_PERMISSION_CODE);
                                sweetAlertDialog.cancel();
                            }
                        })
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.cancel();
                            }
                        })
                        .show();
            }
            else if(permissionsNotGrantedWithNoRationale.size() > 0) {
                Toast.makeText(getContext(),"Please enable the required permissions under settings.",Toast.LENGTH_LONG).show();
            } else if(permissionsGranted.size() == Helper.PERMISSIONS.length) {

            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_START_REQUEST) {
            if(resultCode == RESULT_OK) {
                Bitmap image = (Bitmap) data.getExtras().get("data");
                photo.setImageBitmap(image);
                modelChangeCallback.setImage(Helper.convertImageToByte(image));
                imageCaptured = true;
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ModelChangeCallback) {
            modelChangeCallback = (ModelChangeCallback) context;
        } else {

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNavigationStack();
    }

    @Override
    public void onLocationChanged(Location location) {
        String loc = String.format("%.6f,%.6f",location.getLatitude(),location.getLongitude());
        coordinates.setText(loc);
        modelChangeCallback.setCoordinates(loc);
        locationManager.removeUpdates(this);
        gettingCoordinates = false;
        changeRefreshIcon();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(getClass().getSimpleName(),"status changed");
        Log.i(getClass().getSimpleName(),extras.toString());
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(getClass().getSimpleName(),"provider enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(getClass().getSimpleName(),"provider disabled");
        gettingCoordinates = false;
        changeRefreshIcon();
    }

    @Override
    public void onReportPostSuccess(String id) {
        progressDialog.dismiss();
        logger.updateReport(String.valueOf(logger.lastInsertId));
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("Report sent!")
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                        ((BaseActivity)getActivity()).loadFragment(Helper.getCurrentFragment(NEXT_FRAGMENT,null,null),NEXT_FRAGMENT,true);
                    }
                })
                .show();
    }

    @Override
    public void onReportPostError() {
        progressDialog.dismiss();
        Helper.showAlert(getContext(),SweetAlertDialog.ERROR_TYPE,"Oops!","Something went wrong.")
        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                ((BaseActivity)getActivity()).loadFragment(Helper.getCurrentFragment(NEXT_FRAGMENT,null,null),NEXT_FRAGMENT,true);
            }
        });
    }

    @Override
    public void onSuccessPing() {
        sendReport();
    }

    @Override
    public void onErrorPing() {
        progressDialog.dismiss();
            progressDialog.dismiss();
            Helper.showAlert(getContext(),SweetAlertDialog.ERROR_TYPE,"Oops!","It seems you are offline. Your report was not sent. We will send it when you come online.")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            ((BaseActivity)getActivity()).loadFragment(Helper.getCurrentFragment(NEXT_FRAGMENT,null,null),NEXT_FRAGMENT,true);
                        }
                    });
    }
}
