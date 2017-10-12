package com.starglare.accasy.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.starglare.accasy.R;
import com.starglare.accasy.adapter.ReportHistoryAdapter;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.core.Logger;
import com.starglare.accasy.models.ReportModel;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {


    private int mColumnCount = 1;
    List<ReportModel> reportModels;
    Logger logger;
    Cursor cursor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        reportModels = new ArrayList<>();
        logger = Logger.getInstance(getContext());
        cursor = logger.selectAllReports();
        if(cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                reportModels.add(Helper.generateReportModelFromCursor(cursor));
            }
        }

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new ReportHistoryAdapter(reportModels));
        }
        return view;
    }

}
