package com.dsource.idc.jellowboard.utility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dsource.idc.jellowboard.R;

public class ImageHelper extends AppCompatActivity {

    public static final int CAMERA = 1122;
    public static final int GALLERY = 2211;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_helper);

    }
}
