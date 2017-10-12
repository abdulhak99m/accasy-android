package com.starglare.accasy.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.starglare.accasy.core.Helper;
import com.starglare.accasy.models.ReportModel;

//import quatja.com.vorolay.VoronoiView;

/**
 * Created by MuhammadAmin on 7/20/2017.
 */

public class BaseFragment extends Fragment {

    public static final String TYPE_LAYOUT = "layout";
    public static final String TYPE_DRAWABLE = "drawable";
    public static final String TYPE_ID = "id";
    public static final String TYPE_ARRAY = "array";
    protected static final String RESOURCE_SUFFIX = "cat_";
    public static String CURRENT_FRAGMENT = "";
    public static String NEXT_FRAGMENT = "";
    public static String PREVIOUS_FRAGMENT = "";

    protected ModelChangeCallback modelChangeCallback;
    protected onViewPagerFragmentChangeCallback  fragmentChangeCallback;
    protected View view;
    //protected VoronoiView voronoiView;
    protected LayoutInflater layoutInflater;
    protected ImageView imageView;
    protected TextView categoryText;
    protected Fragment fragment;
    protected ReportModel model;
    protected String[] categories;
    protected int[] resources;
    protected int layoutResourceId;
    protected int imageResourceId;
    protected int titleResourceId;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //layoutResourceId = Helper.getResourceId(RESOURCE_SUFFIX+ TYPE_LAYOUT, TYPE_LAYOUT,getContext());
        //imageResourceId = Helper.getResourceId(RESOURCE_SUFFIX+"image", TYPE_ID,getContext());
        //titleResourceId = Helper.getResourceId(RESOURCE_SUFFIX+"title", TYPE_ID,getContext());
    }

    protected LayoutInflater getInflate() {
        return getActivity().getLayoutInflater();
    }

    protected void  createAndView() {
        for (int i = 0; i < categories.length; i++) {

            view = getView();// layoutInflater.inflate(layoutResourceId, null, false);
            imageResourceId = Helper.getResourceId(RESOURCE_SUFFIX+"image_"+i, TYPE_ID,getContext());
            imageView = (ImageView) view.findViewById(imageResourceId);
            imageView.setImageResource(resources[i]);

            String categoryName = categories[i];
            titleResourceId = Helper.getResourceId(RESOURCE_SUFFIX+"title_"+i, TYPE_ID,getContext());
            categoryText = (TextView) view.findViewById(titleResourceId);
            categoryText.setText(Helper.formatString(categoryName));
            //voronoiView.addView(view);
        }
    }

    public void setModel(ReportModel model){
        this.model = model;
    }

    public interface ModelChangeCallback {
        void setCategory(String category);

        void setSubCategory(String subCategory);

        void setCoordinates(String coordinates);

        void setComment(String comment);

        void setImage(byte[] image);

        void setTime(long time);

        void setPhoneNumber(String s);
    }

    public interface onViewPagerFragmentChangeCallback {
        void changeFragment();
    }
}
