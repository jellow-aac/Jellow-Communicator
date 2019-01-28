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

public class LanguageSelectAdapter extends RecyclerView.Adapter<LanguageSelectAdapter.ViewHolder>{

private Context mContext;
// private LayoutInflater mInflater;
public ArrayList<String> mDataSource,flagCountry;
        LanguageSelectAdapter.OnItemClickListener mItemClickListener=null;
        public TypedArray flags;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView country_title;
    public ImageView country_flag;
    public ViewHolder(View v) {
        super(v);
        country_title =v.findViewById(R.id.country_text);
        country_flag = v.findViewById(R.id.country_image);
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

    public void setOnItemClickListner(final LanguageSelectAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     */
    public LanguageSelectAdapter(Context context) {
        mContext = context;
        flagCountry = new ArrayList<>();
        //Please add new language name here and also add the flag of the country in the typed array
        flagCountry.add("English (India)");
        flagCountry.add("हिंदी");
        flagCountry.add("English (United States)");
        flagCountry.add("English (United Kingdom)");
        mDataSource = flagCountry;
        flags = mContext.getResources().obtainTypedArray(R.array.country_flags);

    }


    @Override
    public LanguageSelectAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.square_language_card, parent, false);


        return new LanguageSelectAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String country = mDataSource.get(position);
        holder.country_title.setText(country);
        holder.country_flag.setImageDrawable(flags.getDrawable(flagCountry.indexOf(country)));
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
