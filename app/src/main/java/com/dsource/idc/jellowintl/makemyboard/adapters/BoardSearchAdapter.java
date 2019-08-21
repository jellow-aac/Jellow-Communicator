package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.databases.IconDatabase;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

public class BoardSearchAdapter extends RecyclerView.Adapter<BoardSearchAdapter.ViewHolder>{

private Context mContext;
private IconDatabase iconDatabase;
// private LayoutInflater mInflater;
private ArrayList<JellowIcon> mDataSource;
private BoardSearchAdapter.OnItemClickListener mItemClickListener=null;
public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private TextView iconTitle;
    ImageView iconImage;
    TextView iconDir;
    ImageView speakIcon;

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
    public BoardSearchAdapter(Context context, ArrayList<JellowIcon> items, String langCode, AppDatabase appDatabase) {
        mContext = context;
        mDataSource = items;
        if(!langCode.equals(""))
            iconDatabase = new IconDatabase(langCode, appDatabase);
    }


    @NonNull
    @Override
    public BoardSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_search_list_item, parent, false);


        return new BoardSearchAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JellowIcon icon = mDataSource.get(position);
        holder.iconTitle.setText(icon.getIconTitle());
        //If the "No icon found" condition comes the remove speakButton
        if(icon.getIconDrawable().equals("NULL")||icon.getIconTitle().equals(mContext.getResources().getString(R.string.not_found)))
        {
            holder.iconTitle.setText(R.string.icon_not_found);
            holder.iconDir.setVisibility(View.GONE);
            holder.iconImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_icon_not_found));
            return;
        }



        holder.iconDir.setVisibility(View.VISIBLE);
        holder.iconTitle.setText(icon.getIconTitle());
        if((icon.getParent0()==-1))//if custom icon load from folder
            {
                File en_dir = mContext.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
                String path = en_dir.getAbsolutePath();
                GlideApp.with(mContext)
                        .load(path+"/"+ icon.getIconDrawable()+".png")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .into(holder.iconImage);
                holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));

        }
        else
        {
            GlideApp.with(mContext).load(getIconPath(mContext, icon.getIconDrawable()+EXTENSION))
                    .into(holder.iconImage);
        }

        /*
         * Adding the directory hint in the search list item
         * To this, we are using the function same as in menuSelected path used
         * */

        String dir="";
  try {
            String[] arr = getLevel1IconLabels();
      for(int i=0;i<arr.length;i++){
          arr[i] = arr[i].split("…")[0];
      }

            if(icon.getLevelOne()==-1&&icon.getLevelTwo()==-1)
            {
                dir = mContext.getResources().getString(R.string.custom_icon);
            }else if (icon.getLevelTwo() == -1) {
                dir = mContext.getResources().getString(R.string.home);
            } else if (icon.getLevelThree() == -1) {
                dir = arr[icon.getParent0()];
            } else {
                String levelTitle = getIconTitleLevel2(icon.getParent0())[icon.getParent1()].
                        replace("…", "");
                dir = arr[icon.getParent0()] + "->" + levelTitle;

            }
        } catch(ArrayIndexOutOfBoundsException ignored){
            ignored.printStackTrace();
         }

        holder.iconDir.setText(dir);

    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
  private String[] getIconTitleLevel2(int pos)
    {
        if(iconDatabase==null) return new String[]{""};
        ArrayList<String> list = iconDatabase.getLevelTwoIconsTitles(pos);
        if(list!=null&&list.size()>0)
            list.remove(0);
        return list.toArray(new String[list.size()]);
    }

    @NonNull
    private String[] getLevel1IconLabels() {
        if(iconDatabase==null) return new String[]{""};
        ArrayList<String> list = iconDatabase.getLevelOneIconsTitles();
        return list.toArray(new String[list.size()]);
    }
    @Override
    public int getItemCount() {
        return mDataSource.size();
    }


}
