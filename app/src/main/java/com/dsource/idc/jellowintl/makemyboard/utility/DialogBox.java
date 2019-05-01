package com.dsource.idc.jellowintl.makemyboard.utility;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;

import static android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.*;

public class DialogBox extends BaseActivity {


    public static CustomDialog.GridSelectListener mGridSelectionListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras =getIntent().getExtras();
        if(getIntent().getStringExtra(DIALOG_TYPE).equals(GRID_DIALOG))
        {
            setContentView(R.layout.grid_dialog);
            setUpGridDialog();
        }
        getWindow().setFlags(FLAG_FULLSCREEN,FLAG_FULLSCREEN);

    }

    private void setUpGridDialog() {

        //final CustomDialog.GridSelectListener mGridSizeSelectListener= (CustomDialog.GridSelectListener)extras.getSerializable(LISTENER);


        final ImageView GridSize1=findViewById(R.id.grid_size_1x1);
        final ImageView GridSize2=findViewById(R.id.grid_size_1X2);
        final ImageView GridSize3=findViewById(R.id.grid_size_1X3);
        final ImageView GridSize6=findViewById(R.id.grid_size_3X3);
        final ImageView GridSize4=findViewById(R.id.grid_size_2x2);

        GridSize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mGridSelectionListener.onGridSelectListener(1);
            }
        });
        GridSize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(2);
            }
        });
        GridSize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(3);
            }
        });
        GridSize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(4);
            }
        });
        GridSize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(6);
            }
        });
    }

}
