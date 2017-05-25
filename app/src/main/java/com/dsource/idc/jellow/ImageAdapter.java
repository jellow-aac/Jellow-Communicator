package com.dsource.idc.jellow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ekalpa on 4/19/2016.
 */

public class ImageAdapter extends android.support.v7.widget.RecyclerView.Adapter<ImageAdapter.MyViewHolder> {
    private Context mContext;
    private SessionManager session;

    // Keep all Images in array
    public static Integer[] mThumbId = {
            R.drawable.level1_greet_300, R.drawable.level1_daily_300,
            R.drawable.level1_eat_300, R.drawable.level1_fun_300,
            R.drawable.level1_learn_300, R.drawable.level1_people_300,
            R.drawable.level1_places_300, R.drawable.level1_time_300, R.drawable.level1_help_300
    };

    public static String[] belowText_english = {"Greet and Feel...", "Daily Activities...", "Eating...", "Fun...", "Learning...", "People...", "Places...", "Time and Weather...", "Help..."};
    public static String[] belowText_hindi = {"शुभकामना और भावना...", "रोज़ के काम...", "खाना...", "मज़े...", "सीखना...", "लोग...", "जगह...", "समय और मौसम...", "मदद..."};

    public class MyViewHolder extends RecyclerView.ViewHolder {
        Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Mukta-Regular.ttf");
        private CircularImageView menuItemImage;
        private LinearLayout menuItemLinearLayout;
        private TextView menuItemBelowText;

        public MyViewHolder(final View view) {
            super(view);
            menuItemImage = (CircularImageView) view.findViewById(R.id.icon1);
            menuItemLinearLayout = (LinearLayout) view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = (TextView) view.findViewById(R.id.te1);
            menuItemBelowText.setTypeface(custom_font);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
        }
    }

    public ImageAdapter(Context c) {
        this.mContext = c;
        session = new SessionManager(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        /*DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;*/
        /*if (session.getGridSize()==0){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        }else if (dpHeight >= 720 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myscrolllist2, parent, false);
        else if (dpWidth >640 && dpWidth <=1024 && session.getGridSize()==1)
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist2, parent, false);
        else if (dpWidth > 600 && dpWidth <=640 && session.getGridSize()==1) {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }else {
            rowView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.myscrolllist33, parent, false);
        }*/
        return new ImageAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final ImageAdapter.MyViewHolder holder, final int position) {
        if (session.getGridSize() == 0) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 336, 0, 336);
            holder.menuItemLinearLayout.setLayoutParams(params);
        }else {
            holder.menuItemLinearLayout.setScaleX(0.7f);
            holder.menuItemLinearLayout.setScaleY(0.7f);
        }
        if(session.getLanguage() == 0)
            holder.menuItemBelowText.setText(belowText_english[position]);
        else
            holder.menuItemBelowText.setText(belowText_hindi[position]);
        holder.menuItemImage.setImageResource(mThumbId[position]);
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {}
        });
    }

    @Override
    public int getItemCount() {
        return mThumbId.length;
    }
}