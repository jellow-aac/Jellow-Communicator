package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

public class IconSelectorAdapter extends RecyclerView.Adapter<IconSelectorAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<JellowIcon> mDataSource;
    IconSelectorAdapter.OnItemClickListener mItemClickListener;

    /**
     * public constructor
     * @param context
     * @param items
     */
    public IconSelectorAdapter(Context context, ArrayList<JellowIcon> items) {
        mContext = context;
        mDataSource = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each data item is just a string in this case
        public TextView iconTitle;
        public ImageView iconImage;
        public CheckBox iconSelected;


        public ViewHolder(View v) {
            super(v);
            iconTitle =v.findViewById(R.id.icon_title);
            iconImage=v.findViewById(R.id.icon_image_view);
            iconSelected=v.findViewById(R.id.icon_selection_checkbox);
            //Disable the click listener of the checkbox to ensure the full icon is clickable
            iconSelected.setClickable(false);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iconSelected.setChecked(!iconSelected.isChecked());
            mItemClickListener.onItemClick(view,getAdapterPosition(),iconSelected.isChecked());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, boolean checked);
    }

    public void setOnItemClickListner(final IconSelectorAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }





    @Override
    public IconSelectorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_card, parent, false);


        return new IconSelectorAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(IconSelectorAdapter.ViewHolder holder, int position) {

        JellowIcon thisIcon = mDataSource.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
        holder.iconSelected.setChecked(false);

        //Some logic to regain the check
        boolean found=false;
        for(int i = 0; i< IconSelectActivity.selectedIconList.size(); i++)
        {
            JellowIcon selIcon= IconSelectActivity.selectedIconList.get(i);
            if(thisIcon.isEqual(selIcon))
            {
             found=true;
             break;
            }
        }
        holder.iconSelected.setChecked(found);


        if(thisIcon.parent1==-1)
        {
            TypedArray mArray=mContext.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
            holder.iconImage.setImageDrawable(mArray.getDrawable(thisIcon.parent0));
        }
        else
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(mContext)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

}
