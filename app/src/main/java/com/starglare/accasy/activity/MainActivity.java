package com.starglare.accasy.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.starglare.accasy.R;
import com.starglare.accasy.fragments.BaseFragment;
import com.starglare.accasy.fragments.CategoryFragment;
import com.starglare.accasy.models.ReportModel;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_test);
        reportModel = ReportModel.getModelInstance();
        Fragment  fragment = new CategoryFragment();
        loadFragment(fragment,"",false);
    }
}
