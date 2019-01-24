package com.dsource.idc.jellowboard.makemyboard.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

public class BoardSearchAdapter extends RecyclerView.Adapter<BoardSearchAdapter.ViewHolder>{

private Context mContext;
// private LayoutInflater mInflater;
private ArrayList<JellowIcon> mDataSource;
        BoardSearchAdapter.OnItemClickListener mItemClickListener=null;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView iconTitle;
    public ImageView iconImage;
    public TextView iconDir;
    public ImageView speakIcon;
    public ViewHolder(View v) {
        super(v);
        iconImage =v.findViewById(R.id.search_icon_drawable);
        iconTitle = v.findViewById(R.id.search_icon_title);
        iconDir = v.findViewById(R.id.parent_directory);
        speakIcon=v.findViewById(R.id.speak_button);
        speakIcon.setVisibility(View.GONE);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        mItemClickListener.onItemClick(view,getAdapterPosition());
    }
}



public interface OnItemClickListener {
    void onItemClick(View view, int position);
}

    public void setOnItemClickListner(final BoardSearchAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public BoardSearchAdapter(Context context, ArrayList<JellowIcon> items) {
        mContext = context;
        mDataSource = items;
    }


    @Override
    public BoardSearchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_search_list_item, parent, false);


        return new BoardSearchAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        JellowIcon icon = mDataSource.get(position);
        holder.iconTitle.setText(icon.IconTitle);
        Log.d("Adapter","IconAdaper: "+icon.IconTitle);
        //If the "No icon found" condition comes the remove speakButton
        if(icon.IconDrawable.equals("NULL")&&icon.IconTitle.equals(mContext.getString(R.string.not_found)))
        {
            holder.iconTitle.setText(R.string.icon_not_found);
            holder.iconDir.setVisibility(View.GONE);
            holder.iconImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_icon_not_found));
            return;
        }


        holder.iconDir.setVisibility(View.VISIBLE);
        holder.iconTitle.setText(icon.IconTitle);
        if(icon.parent1==-1)
        {
            TypedArray mArray=mContext.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
            if(!(icon.parent0==-1))
            holder.iconImage.setImageDrawable(mArray.getDrawable(icon.parent0));
            else //if custom icon load from folder
            {
                SessionManager mSession = new SessionManager(mContext);
                File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
                String path = en_dir.getAbsolutePath() + "/boardicon";
                GlideApp.with(mContext)
                        .load(path+"/"+ icon.IconDrawable+".png")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .dontAnimate()
                        .into(holder.iconImage);
                holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));
            }
        }
        else
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(mContext)
                    .load(path+"/"+ icon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }

        /*
         * Adding the directory hint in the search list item
         * To this, we are using the function same as in menuSelected path used
         * */

        try {
            String[] arr = mContext.getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
            String dir = "";
            if (icon.parent1 == -1) {
                dir = mContext.getResources().getString(R.string.home);
            } else if (icon.parent2 == -1) {
                dir = arr[icon.parent0];
            } else {

                dir = arr[icon.parent0] + "->" + getIconTitleLevel2(icon.parent0)[icon.parent1];

            }
            holder.iconDir.setText(dir);
        } catch(ArrayIndexOutOfBoundsException e){

         }


    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
    private String[] getIconTitleLevel2(int pos)
    {
        String arr[]=null;
        switch (pos)
        {
            case 0:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoGreetFeelSpeechText);
                break;
            case 1:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoDailyActSpeechText);
                break;
            case 2:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoEatSpeechText);
                break;
            case 3:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoFunSpeechText);
                break;
            case 4:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoLearningSpeechText);
                break;
            case 5:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoPeopleSpeechText);
                break;
            case 6:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoPlacesSpeechText);
                break;
            case 7:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoTimeWeatherSpeechText);
                break;
            case 8:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoHelpSpeechText);
                break;
            default:
        }


        return arr;
    }


    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
