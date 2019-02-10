package com.dsource.idc.jellowboard.makemyboard.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.IconSelectActivity;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class IconSelectorAdapter extends RecyclerView.Adapter<IconSelectorAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<JellowIcon> mDataSource;
    IconSelectorAdapter.OnItemClickListener mItemClickListener;
    public static final int ICON_SELECT_MODE = 111;
    public static final int ADD_EDIT_ICON_MODE = 222;
    public static final int EDIT_ICON_MODE = 333;
    public int mode;
    private IconSelectActivity activity;
    private OnIconEditListener mIconEditListener;

    /**
     * public constructor
     * @param context
     * @param items
     */
    public IconSelectorAdapter(Context context, ArrayList<JellowIcon> items,int mode) {
        mContext = context;
        mDataSource = items;
        this.mode = mode;
        if(mode==ICON_SELECT_MODE)
        activity = (IconSelectActivity)context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each data item is just a string in this case
        public TextView iconTitle;
        public ImageView iconImage;
        public CheckBox iconSelected;
        public ImageView editIcon;


        public ViewHolder(View v) {
            super(v);
            iconTitle =v.findViewById(R.id.icon_title);
            iconImage=v.findViewById(R.id.icon_image_view);
            iconSelected=v.findViewById(R.id.icon_selection_checkbox);
            editIcon = v.findViewById(R.id.edit_icon);
            if(mode==ADD_EDIT_ICON_MODE)
                iconSelected.setVisibility(View.GONE);
            else if(mode == EDIT_ICON_MODE)
            {
                iconSelected.setVisibility(View.GONE);
                editIcon.setVisibility(View.VISIBLE);
                editIcon.setOnClickListener(this);
            }
            //Disable the click listener of the checkbox to ensure the full icon is clickable
            iconSelected.setClickable(false);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(mode==ICON_SELECT_MODE) {
                iconSelected.setChecked(!iconSelected.isChecked());
                mItemClickListener.onItemClick(view, getAdapterPosition(), iconSelected.isChecked());
            }
            else if(mode == EDIT_ICON_MODE)
            {
                if(mIconEditListener!=null)
                    mIconEditListener.onIconEdit(getAdapterPosition());
            }
        }
    }

    //Callback for item click
    public interface OnItemClickListener { void onItemClick(View view, int position, boolean checked);}
    public void setOnItemClickListner(final IconSelectorAdapter.OnItemClickListener mItemClickListener) { this.mItemClickListener = mItemClickListener; }
    //Callback for when icon is edit.
    public interface OnIconEditListener { void onIconEdit(int pos);}
    public void setOnIconEditListener(OnIconEditListener onIconEditListener){this.mIconEditListener = onIconEditListener;}







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
        if(mode==ICON_SELECT_MODE)
        for(int i = 0; i< activity.selectedIconList.size(); i++)
        {
            JellowIcon selIcon= activity.selectedIconList.get(i);
            if(thisIcon.isEqual(selIcon))
            {
             found=true;
             break;
            }
        }
        holder.iconSelected.setChecked(found);

        //Load from boardicon folder in case of the custom icons
        if(thisIcon.parent0==-1)
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(mContext)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .apply(RequestOptions.
                            circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
            holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else //load from drawables in case of the normal icons
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
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
