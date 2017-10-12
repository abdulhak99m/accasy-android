package com.starglare.accasy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import static com.starglare.accasy.models.FragmentNames.SUB_CATEGORY_FRAGMENT;


public class CategoryFragment extends BaseFragment {

    private static final String TAG = "CATEGORY_FRAGMENT";
    ImageView streetImageView, utiltiyImageView;

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateNavigationStack();
        if (getArguments() != null) {

        }
    }

    private void updateNavigationStack() {
        CURRENT_FRAGMENT = CATEGORY_FRAGMENT;
        NEXT_FRAGMENT = SUB_CATEGORY_FRAGMENT;
        PREVIOUS_FRAGMENT = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        view = getView();
        layoutResourceId = Helper.getResourceId("fragment_category", TYPE_LAYOUT,getContext());

        layoutInflater = getInflate();
        addImagesToViews();
         streetImageView = (ImageView) view.findViewById(R.id.cat_image_0);
         utiltiyImageView = (ImageView) view.findViewById(R.id.cat_image_1);
         streetImageView.setOnClickListener(onClickListener);
         utiltiyImageView.setOnClickListener(onClickListener);
        //set array index for corresponding string name;
         streetImageView.setTag(0);
         utiltiyImageView.setTag(1);

      /*  voronoiView = (VoronoiView) view.findViewById(R.id.cat_View);



        voronoiView.setOnRegionClickListener(new VoronoiView.OnRegionClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Toast.makeText(getContext(), "position: " + position, Toast.LENGTH_SHORT).show();
                String category = getCategoryNames()[position].toLowerCase();
                Log.d(TAG,"Category: "+ category);
                modelChangeCallback.setCategory(category);
                loadNextFragment();
            }
        });*/
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int)v.getTag();
            String category = getCategoryNames()[position].toLowerCase();
            Log.d(TAG,"Category: "+ category);
            modelChangeCallback.setCategory(category);
            loadNextFragment();
        }
    };

    private void loadNextFragment() {
        //fragmentChangeCallback.changeFragment();
        fragment = Helper.getCurrentFragment(NEXT_FRAGMENT,null,null);
        ((BaseActivity)getActivity()).loadFragment(fragment,NEXT_FRAGMENT,true);
    }

    private void addImagesToViews() {
        categories = getCategoryNames();
        //get list of resourceIds for corresponding category images
        resources = getCategoryResourceIds();
        //add the images to the views
        createAndView();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
          if (context instanceof ModelChangeCallback) {
           modelChangeCallback = (ModelChangeCallback) context;
        }
        if (context instanceof onViewPagerFragmentChangeCallback) {
            fragmentChangeCallback = (onViewPagerFragmentChangeCallback) context;
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

    public int[] getCategoryResourceIds() {
        String[] categoryNames = getCategoryNames();
        int[] categoryResourceIds = new int[categoryNames.length];
        for (int i = 0; i < categoryNames.length; i++) {
            int id = Helper.getResourceId(categoryNames[i].toLowerCase(), TYPE_DRAWABLE,getContext());
            categoryResourceIds[i] = id;
        }
        return categoryResourceIds;
    }

    public String[] getCategoryNames() {
        //get list of categories from string.xml
        return getResources().getStringArray(R.array.category);
    }


}
