package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class EditBoardAdapter extends RecyclerView.Adapter<EditBoardAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<LeftIconPane> mDataSource;
    EditBoardAdapter.OnItemClickListener mItemClickListener;

    /**
     * public constructor
     * @param context
     * @param items
     */
    public EditBoardAdapter(Context context, ArrayList<LeftIconPane> items) {
        mContext = context;
        mDataSource = items;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //each data item is just a string in this case
        public TextView iconTitle;
        public RecyclerView sub_recycler;


        public ViewHolder(View v) {
            super(v);
            iconTitle =v.findViewById(R.id.header);
            sub_recycler=v.findViewById(R.id.sub_recycler_view);
            v.setOnClickListener(null);
        }

        @Override
        public void onClick(View view) {
            mItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListner(final EditBoardAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public EditBoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_with_recycler, parent, false);
        return new EditBoardAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EditBoardAdapter.ViewHolder holder, int position) {

        LeftIconPane data = mDataSource.get(position);
        holder.iconTitle.setText(data.category);
        holder.sub_recycler.setLayoutManager(new GridLayoutManager(mContext,3));
        subAdapter adapter=new subAdapter(mContext,data.subList);
        holder.sub_recycler.setAdapter(adapter);

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }



}

class subAdapter  extends RecyclerView.Adapter<subAdapter.ViewHolder>{

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<JellowIcon> mDataSource;
    private subAdapter.OnItemClickListener mItemClickListener;

    /**
     * public constructor
     * @param context
     * @param items
     */
    public subAdapter(Context context, ArrayList<JellowIcon> items) {
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
            iconSelected.setVisibility(View.GONE);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d("EditBoard: ","SubItem Clicked: Pos "+getAdapterPosition());
            //mItemClickListener.onItemClick(view,getAdapterPosition());
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListner(final subAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }





    @Override
    public subAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_card, parent, false);


        return new subAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(subAdapter.ViewHolder holder, int position) {

        JellowIcon thisIcon = mDataSource.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);

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