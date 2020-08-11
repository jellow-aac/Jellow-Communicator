package com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs;

import android.os.Bundle;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.BaseActivity;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.models.GlobalConstants;

import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.GRID_SIZE;

public class DialogNoOfIconPerScreen extends BaseActivity {

    public static GridSelectListener mGridSelectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_grid_selection);
        setUpGridDialog(getIntent().getIntExtra(GRID_SIZE, GlobalConstants.NINE_ICONS_PER_SCREEN));
    }

    public void closeDialog(View v){
        finish();
    }


    private void setUpGridDialog(int gridSize) {
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))){
            findViewById(R.id.iv_close).setVisibility(View.GONE);
        }

        final ImageView GridSize1 = findViewById(R.id.grid_size_1x1);
        final ImageView GridSize2 = findViewById(R.id.grid_size_1X2);
        final ImageView GridSize3 = findViewById(R.id.grid_size_1X3);
        final ImageView GridSize4 = findViewById(R.id.grid_size_2x2);
        final ImageView GridSize6 = findViewById(R.id.grid_size_3X3);

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

        switch (gridSize){
            case GlobalConstants.ONE_ICON_PER_SCREEN:
                findViewById(R.id.fl_one_icon).setBackground(
                        getResources().getDrawable(R.drawable.border_number_of_icons_per_screen));
                break;
            case GlobalConstants.TWO_ICONS_PER_SCREEN:
                findViewById(R.id.fl_two_icon).setBackground(
                        getResources().getDrawable(R.drawable.border_number_of_icons_per_screen));
                break;
            case GlobalConstants.THREE_ICONS_PER_SCREEN:
                findViewById(R.id.fl_three_icon).setBackground(
                        getResources().getDrawable(R.drawable.border_number_of_icons_per_screen));
                break;
            case GlobalConstants.FOUR_ICONS_PER_SCREEN:
                findViewById(R.id.fl_four_icon).setBackground(
                        getResources().getDrawable(R.drawable.border_number_of_icons_per_screen));
                break;
            case GlobalConstants.NINE_ICONS_PER_SCREEN:
                findViewById(R.id.fl_nine_icon).setBackground(
                        getResources().getDrawable(R.drawable.border_number_of_icons_per_screen));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setNavigationUiConditionally();
    }
}
