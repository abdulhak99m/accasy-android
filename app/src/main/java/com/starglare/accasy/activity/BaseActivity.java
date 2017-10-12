package com.starglare.accasy.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.starglare.accasy.R;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.fragments.BaseFragment;
import com.starglare.accasy.models.ReportModel;

/**
 * Created by MuhammadAmin on 8/11/2017.
 */

public class BaseActivity extends AppCompatActivity implements BaseFragment.ModelChangeCallback {
    protected ReportModel reportModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        reportModel = ReportModel.getModelInstance();
    }

    @Override
    public void setCategory(String category) {
        reportModel.setCategory(category);
    }

    @Override
    public void setSubCategory(String subCategory) {
        reportModel.setSubCategory(subCategory);
    }

    @Override
    public void setCoordinates(String coordinates) {
        reportModel.setCoordinates(coordinates);
    }

    @Override
    public void setComment(String comment) {
        reportModel.setComment(comment);
    }

    @Override
    public void setImage(byte[] image) {
        reportModel.setImage(image);
    }

    @Override
    public void setTime(long time) {
        reportModel.setTime(time);
    }

    @Override
    public void setPhoneNumber(String s) {
        reportModel.setPhoneNumber(s);
    }

    public void loadFragment(Fragment fragment, String attribute, Boolean recycle) {
        if (fragment == null) return;
        if(recycle && (getSupportFragmentManager().findFragmentByTag(attribute) != null))
            fragment = getSupportFragmentManager().findFragmentByTag(attribute);
        if(fragment instanceof BaseFragment)
            ((BaseFragment)fragment).setModel(reportModel);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment, attribute)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if(!BaseFragment.PREVIOUS_FRAGMENT.equals("")) {
            Fragment fragment = Helper.getCurrentFragment(BaseFragment.PREVIOUS_FRAGMENT,null,null);
            ((BaseFragment)fragment).setModel(reportModel);
            loadFragment(fragment,BaseFragment.PREVIOUS_FRAGMENT,true);
        }
        else
            finish();

    }
}
