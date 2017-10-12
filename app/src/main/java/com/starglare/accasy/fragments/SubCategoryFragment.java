package com.starglare.accasy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.starglare.accasy.activity.BaseActivity;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.R;

//import quatja.com.vorolay.VoronoiView;

import static com.starglare.accasy.models.FragmentNames.CATEGORY_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.FORM_FRAGMENT;
import static com.starglare.accasy.models.FragmentNames.SUB_CATEGORY_FRAGMENT;

public class SubCategoryFragment extends BaseFragment {

    private static final String TAG = "SUB_CATEGORY_FRAGMENT";
    public static final String RESOURCE_SUFFIX = "cat_";
    ImageView subcat1, subcat2, subcat3;
    public SubCategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNavigationStack();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sub_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();

        addImagesToViews();

        subcat1 = (ImageView) view.findViewById(R.id.cat_image_0);
        subcat2 = (ImageView) view.findViewById(R.id.cat_image_1);
        subcat3 = (ImageView) view.findViewById(R.id.cat_image_2);

        subcat1.setOnClickListener(onClickListener);
        subcat2.setOnClickListener(onClickListener);
        subcat3.setOnClickListener(onClickListener);

        subcat1.setTag(0);
        subcat2.setTag(1);
        subcat3.setTag(2);


        //Toast.makeText(getContext(),model.getCategory(),Toast.LENGTH_SHORT).show();
        /*voronoiView = (VoronoiView) view.findViewById(R.id.cat_View);
        layoutInflater = getInflate();

        voronoiView.setOnRegionClickListener(new VoronoiView.OnRegionClickListener() {
            @Override
            public void onClick(View view, int position) {
                String category = getCategoryNames()[position].toLowerCase();
                //Toast.makeText(getContext(), getCategoryNames()[position], Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Sub Category: "+category);
                modelChangeCallback.setSubCategory(category);
                loadNextFragment();
            }
        });*/
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            String category = getCategoryNames()[position].toLowerCase();
            //Toast.makeText(getContext(), getCategoryNames()[position], Toast.LENGTH_SHORT).show();
            Log.d(TAG,"Sub Category: "+category);
            modelChangeCallback.setSubCategory(category);
            loadNextFragment();
        }
    };

    private void loadNextFragment() {
        fragment = Helper.getCurrentFragment(NEXT_FRAGMENT,null,null); //new FormFragment();
        ((BaseActivity)getActivity()).loadFragment(fragment,NEXT_FRAGMENT,false);
    }

    private void addImagesToViews() {
        categories = getCategoryNames();
        resources = getCategoryResourceIds();

        createAndView();
    }

    public int[] getCategoryResourceIds() {
        String[] categoryNames = getCategoryNames();
        int[] categoryResourceIds = new int[categoryNames.length];
        for (int i = 0; i < categoryNames.length; i++) {
            int id = Helper.getResourceId( categoryNames[i].toLowerCase(), TYPE_DRAWABLE,getContext());
            categoryResourceIds[i] = id;
        }
        return categoryResourceIds;
    }

    public String[] getCategoryNames() {
        int resourceId = Helper.getResourceId(RESOURCE_SUFFIX+model.getCategory().toLowerCase(),TYPE_ARRAY,getContext());
        return getResources().getStringArray(resourceId);
    }

    private void updateNavigationStack() {
        CURRENT_FRAGMENT = SUB_CATEGORY_FRAGMENT;
        NEXT_FRAGMENT = FORM_FRAGMENT;
        PREVIOUS_FRAGMENT = CATEGORY_FRAGMENT;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ModelChangeCallback) {
            modelChangeCallback = (ModelChangeCallback) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        modelChangeCallback = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateNavigationStack();
    }
}
