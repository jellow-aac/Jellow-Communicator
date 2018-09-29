package com.dsource.idc.jellowboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dsource.idc.jellowboard.utility.SessionManager;

/**
 * Created by ekalpa on 4/19/2016.
 */
class MainActivityAdapter extends android.support.v7.widget.RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> {
    private Context mContext;
    private SessionManager mSession;
    private TypedArray mIconArray;
    private String[] mBelowTextArray;
    MainActivityAdapter(Context context) {
        mContext = context;
        mSession = new SessionManager(mContext);
        mIconArray = mContext.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
        mBelowTextArray = mContext.getResources().getStringArray(R.array.arrLevelOneBelowText);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY3 = 0;
        View rowView;
        if (mSession.getGridSize() == GRID_1BY3)
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        else
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        return new MainActivityAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final MainActivityAdapter.MyViewHolder holder, final int position) {
        final int MODE_PICTURE_ONLY = 1;
        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);

        holder.menuItemBelowText.setAllCaps(true);
        holder.menuItemBelowText.setText(mBelowTextArray[position]);
        holder.menuItemImage.setImageDrawable(mIconArray.getDrawable(position));
        holder.menuItemLinearLayout.setContentDescription(mBelowTextArray[position]);
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                ((MainActivity)mContext).tappedCategoryItemEvent(holder.menuItemLinearLayout, v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mIconArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout menuItemLinearLayout;
        private ImageView menuItemImage;
        private TextView menuItemBelowText;

        MyViewHolder(final View view) {
            super(view);
            menuItemImage = view.findViewById(R.id.icon1);
            menuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = view.findViewById(R.id.te1);
            Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Mukta-Regular.ttf");
            menuItemBelowText.setTypeface(font);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
            GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
            gd.setColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
    }
}