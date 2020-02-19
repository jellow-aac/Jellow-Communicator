package com.dsource.idc.jellowintl;

/**
 * Created by Shruti on 15-08-2016.
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SampleSlideFragment extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";
    private static final String ARG_LAYOUT_NAME = "layoutName";
    private String mLayoutName;

    public static SampleSlideFragment newInstance(int layoutResId, String layoutName) {
        SampleSlideFragment sampleSlide = new SampleSlideFragment();

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        args.putString(ARG_LAYOUT_NAME, layoutName);
        sampleSlide.setArguments(args);

        return sampleSlide;
    }

    private int layoutResId;

    public SampleSlideFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID) && getArguments().containsKey(ARG_LAYOUT_NAME)) {
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
            mLayoutName = getArguments().getString(ARG_LAYOUT_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(layoutResId, container, false);
    }

    public String getLayoutName() {
        return mLayoutName;
    }
}