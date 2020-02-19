package com.dsource.idc.jellowintl.utility;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.models.GlobalConstants;

import static com.dsource.idc.jellowintl.models.GlobalConstants.DIALOG_TYPE;
import static com.dsource.idc.jellowintl.models.GlobalConstants.GRID_DIALOG;

public class DialogBox extends BaseActivity {

    public static GridSelectListener mGridSelectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getStringExtra(DIALOG_TYPE).equals(GRID_DIALOG)) {
            setContentView(R.layout.grid_dialog);
            setUpGridDialog();
        }
        View v = findViewById(R.id.touch_outside);
        if (v != null) v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpGridDialog() {

        final ImageView GridSize1 = findViewById(R.id.grid_size_1x1);
        final ImageView GridSize2 = findViewById(R.id.grid_size_1X2);
        final ImageView GridSize3 = findViewById(R.id.grid_size_1X3);
        final ImageView GridSize6 = findViewById(R.id.grid_size_3X3);
        final ImageView GridSize4 = findViewById(R.id.grid_size_2x2);



        GridSize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mGridSelectionListener != null)
                    mGridSelectionListener.onGridSelectListener(GlobalConstants.ONE_ICON_PER_SCREEN);
            }
        });
        GridSize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mGridSelectionListener != null)
                    mGridSelectionListener.onGridSelectListener(GlobalConstants.TWO_ICONS_PER_SCREEN);
            }
        });
        GridSize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mGridSelectionListener != null)
                    mGridSelectionListener.onGridSelectListener(GlobalConstants.THREE_ICONS_PER_SCREEN);
            }
        });
        GridSize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mGridSelectionListener != null)
                    mGridSelectionListener.onGridSelectListener(GlobalConstants.FOUR_ICONS_PER_SCREEN);
            }
        });
        GridSize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if (mGridSelectionListener != null)
                    mGridSelectionListener.onGridSelectListener(GlobalConstants.NINE_ICONS_PER_SCREEN);
            }
        });
    }
}
