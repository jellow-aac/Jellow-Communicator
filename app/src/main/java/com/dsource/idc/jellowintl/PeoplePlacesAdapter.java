package com.dsource.idc.jellowintl;

import android.content.Context;
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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by HP on 22/01/2017.
 */
class PeoplePlacesAdapter extends android.support.v7.widget.RecyclerView.Adapter<PeoplePlacesAdapter.MyViewHolder> {
    private Context mContext;
    private SessionManager mSession;
    private ArrayList<String> mIconNameList = new ArrayList<>();
    private ArrayList<String> mBelowTextList = new ArrayList<>();
    private String path;

    PeoplePlacesAdapter(Context context, int levelOneItemPos, String[] mArrAdapterTxt, Integer[] arrSort) {
        mContext = context;
        mSession = new SessionManager(mContext);
        loadArraysFromResources(levelOneItemPos, mArrAdapterTxt, arrSort);
        File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
        path = en_dir.getAbsolutePath()+"/drawables";
    }

    @Override
    public PeoplePlacesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY3 = 0;
        View rowView;
        if (mSession.getGridSize() == GRID_1BY3)
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        else
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        return new PeoplePlacesAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final int MODE_PICTURE_ONLY = 1;

        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);
        holder.menuItemBelowText.setText(mBelowTextList.get(position));
        GlideApp.with(mContext)
                .load(path+"/"+ mIconNameList.get(position)+".png")
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(false)
                .centerCrop()
                .dontAnimate()
                .into(holder.menuItemImage);
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {}
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mIconNameList.size();
    }

    private void loadArraysFromResources(int levelOneItemPos, String[] mArrAdapterTxt, Integer[] arrSort) {
        ArrayList<String> tempIconList = new ArrayList<>();
        mBelowTextList.addAll(Arrays.asList(mArrAdapterTxt));
        if(levelOneItemPos == 5)                    //levelOneItemPos is either 5(People) or 6 (Places)
            tempIconList.addAll(Arrays.asList(mContext.getResources().getStringArray(
                    R.array.arrLevelTwoPeopleIcon)));
        else
            tempIconList.addAll(Arrays.asList(mContext.getResources().getStringArray(
                    R.array.arrLevelTwoPlacesIcon)));

        for (int i = 0; i < tempIconList.size(); i++) {
            mIconNameList.add(tempIconList.get(arrSort[i]));
        }
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