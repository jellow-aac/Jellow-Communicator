package com.dsource.idc.jellowintl.makemyboard.UtilityClasses;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

/**
 * This class is for managing the clicks of expressive icons
 */
public class ExpressiveIconManager implements View.OnClickListener{

    private ArrayList<ImageView> expIconList;
    private ArrayList<Integer> buttonFlags;
    private expIconClickListener mExpIconClickListener;
    private TypedArray pressed;
    private TypedArray unPressed;
    private Context context;

    public ExpressiveIconManager(Context context,View parent) {

        this.context=context;
        expIconList=new ArrayList<>();
        expIconList.add((ImageView)parent.findViewById(R.id.ivlike));
        expIconList.add((ImageView)parent.findViewById(R.id.ivyes));
        expIconList.add((ImageView)parent.findViewById(R.id.ivadd));
        expIconList.add((ImageView)parent.findViewById(R.id.ivdislike));
        expIconList.add((ImageView)parent.findViewById(R.id.ivno));
        expIconList.add((ImageView)parent.findViewById(R.id.ivminus));

        buttonFlags=new ArrayList<>();
        for(int i=0;i<expIconList.size();i++)
            buttonFlags.add(0);

        for(int i=0;i<expIconList.size();i++)
            expIconList.get(i).setOnClickListener(this);

        pressed=context.getResources().obtainTypedArray(R.array.expressive_icon_pressed);
        unPressed=context.getResources().obtainTypedArray(R.array.expressive_icon_unpressed);
    }

    @Override
    public void onClick(View v) {
        int index=expIconList.indexOf(v);
        mExpIconClickListener.expressiveIconClicked(index,buttonFlags.get(index));
        if(buttonFlags.get(index)==1)
            buttonFlags.set(index,0);
        else buttonFlags.set(index,1);
        expIconList.get(index).setImageDrawable(pressed.getDrawable(index));
        setSelection(index);
    }

    private void setSelection(int index) {
        for(int i=0;i<expIconList.size();i++) {
            if(i==index) {
                Log.d("ButtonFlag","index: "+buttonFlags.get(i));
                expIconList.get(i).setImageDrawable(pressed.getDrawable(i));
            }
            else {
                expIconList.get(i).setImageDrawable(unPressed.getDrawable(i));
                Log.d("ButtonFlag","IndexBefore "+buttonFlags.get(i));
                buttonFlags.set(i,0);
                Log.d("ButtonFlag","IndexAfter "+buttonFlags.get(i));
            }

        }


    }
    public void resetOther(int index)
    {
        Log.d("ExpIcon","index "+index);
        for(int i=0;i<expIconList.size();i++) {
            if(index!=i) {
                buttonFlags.set(i,0);
            }
        }

    }

    public void resetSelection(){
        for(int i=0;i<expIconList.size();i++) {
            expIconList.get(i).setImageDrawable(unPressed.getDrawable(i));
            buttonFlags.set(i,0);
        }
    }

    public void setClickListener(expIconClickListener expIconClickListener){
        this.mExpIconClickListener=expIconClickListener;
    }

    public interface expIconClickListener{ void expressiveIconClicked(int expIconPos, int time);}
}

