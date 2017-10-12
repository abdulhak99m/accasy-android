package com.starglare.accasy.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starglare.accasy.R;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.core.Logger;
import com.starglare.accasy.models.ReportModel;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.List;

public class ReportMapFragment extends Fragment {


    RotationGestureOverlay mRotationGestureOverlay;
    Logger logger;
    Cursor cursor;
    Marker marker;

    public ReportMapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_report_map, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //allow network activity on main thread
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        logger = Logger.getInstance(getContext());
       // Configuration.getInstance().setUserAgentValue(...)
        View view = getView();
        MapView map = (MapView) view.findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //enable rotation
        mRotationGestureOverlay = new RotationGestureOverlay(getContext(), map);
        mRotationGestureOverlay.setEnabled(true);
        map.setMultiTouchControls(true);
        map.getOverlays().add(this.mRotationGestureOverlay);

        //add compass
        CompassOverlay mCompassOverlay = new CompassOverlay(getContext(), new InternalCompassOrientationProvider(getContext()), map);
        mCompassOverlay.enableCompass();
        map.getOverlays().add(mCompassOverlay);

       // map.setUseDataConnection(false);
        GeoPoint startPoint = new GeoPoint(9.071798, 7.486378);
       // waypoints.add(startPoint);
        IMapController mapController = map.getController();
        mapController.setZoom(13);
        mapController.setCenter(startPoint);


        cursor = logger.selectAllReports();
        if(cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()){
                ReportModel reportModel = Helper.generateReportModelFromCursor(cursor);
                if(reportModel.getCoordinates() == null) continue;
                double lat = Double.valueOf(reportModel.getCoordinates().split(",")[0]);
                double lng = Double.valueOf(reportModel.getCoordinates().split(",")[1]);
                marker = new Marker(map);
                marker.setPosition(new GeoPoint(lat,lng));
                marker.setInfoWindow(null);
                marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                marker.setIcon(getActivity().getDrawable(R.mipmap.ic_launcher));
                map.getOverlays().add(marker);
            }
        }



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}
