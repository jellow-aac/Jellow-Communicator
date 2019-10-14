package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SelectionManager;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_EDIT_ICON_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.EDIT_ICON_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ICON_SELECT_MODE;

public class IconSelectorAdapter extends RecyclerView.Adapter<IconSelectorAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<JellowIcon> mDataSource;
    private IconSelectorAdapter.OnItemClickListener mItemClickListener;
    private int mode;
    private OnIconEditListener mIconEditListener;
    private String langCode;

    /**
     * public constructor
     * @param context
     * @param items
     */
    public IconSelectorAdapter(Context context, ArrayList<JellowIcon> items,int mode,String lang) {
        mContext = context;
        mDataSource = items;
        this.mode = mode;
        this.langCode =lang;
    }

    public void setDataList(ArrayList<JellowIcon> icons) {
        mDataSource =icons;
        notifyDataSetChanged();
    }

    public void setNoCheckBoxMode(boolean set) {
        mode = set?ADD_EDIT_ICON_MODE:ICON_SELECT_MODE;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each iconList item is just a string in this case
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
        holder.iconTitle.setText(thisIcon.getIconTitle());
        holder.iconSelected.setChecked(false);

        holder.iconTitle.setTextColor(Color.rgb(64, 64, 64));
        //Some logic to regain the check
        boolean found=false;

        if(mode!=EDIT_ICON_MODE) {
            if(mode==ADD_EDIT_ICON_MODE)
                holder.iconSelected.setVisibility(View.GONE);
            else if (mode == ICON_SELECT_MODE) {
                holder.iconSelected.setVisibility(View.VISIBLE);
                for (int i = 0; i < SelectionManager.getInstance().getList().size(); i++) {
                    JellowIcon selIcon = SelectionManager.getInstance().getList().get(i);
                    if (thisIcon.isEqual(selIcon)) {
                        found = true;
                        break;
                    }
                }
            }else {
                holder.editIcon.setVisibility(View.VISIBLE);
            }
            holder.iconSelected.setChecked(found);
        }

        //Load from boardicon folder in case of the custom icons
        if(thisIcon.getParent0()==-1)
        {
            File en_dir = mContext.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(mContext)
                    .load(path+"/"+ thisIcon.getIconDrawable()+".png")
                    .apply(RequestOptions.
                            circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .placeholder(mContext.getResources().getDrawable(R.drawable.ic_board_person))
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
            holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else //load from drawables in case of the normal icons
        {
            GlideApp.with(mContext).load(getIconPath(mContext, thisIcon.getIconDrawable()+EXTENSION))
                    .skipMemoryCache(true)
                    .into(holder.iconImage);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

}
