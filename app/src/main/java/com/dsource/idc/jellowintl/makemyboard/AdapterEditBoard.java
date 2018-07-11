package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.makeramen.dragsortadapter.DragSortAdapter;
import com.makeramen.dragsortadapter.NoForegroundShadowBuilder;

import java.io.File;
import java.util.List;

public class AdapterEditBoard extends DragSortAdapter<AdapterEditBoard.ViewHolder> {


    private final List<JellowIcon> data;
    private Context mContext;
    public OnItemClickListener mItemClickListener;
    public final static int DELETE_MODE=112;
    public final static int NORMAL_MODE=113;
    public int Mode=112;
    private OnItemDeleteListener mItemDeleteListener;
    private OnItemMovedListener mItemMovedListener;
    public AdapterEditBoard(RecyclerView recyclerView, List<JellowIcon> data, Context context,int Mode) {
        super(recyclerView);
        this.data = data;
        this.Mode=Mode;
        mContext=context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.icon_edit_pain_card, parent, false);
        ViewHolder holder = new ViewHolder(this, view);
        view.setOnLongClickListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        long itemId = data.get(position).getID();
        JellowIcon thisIcon = data.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
      //  holder.holder.setVisibility(getDraggingId() == itemId ? View.INVISIBLE : View.VISIBLE);

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
    public long getItemId(int position) {
        return data.get(position).getID();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getPositionForId(long id) {
        for(int i=0;i<data.size();i++)
            if(data.get(i).getID()==id)
                return i;

        return -1;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        data.add(toPosition, data.remove(fromPosition));
        mItemMovedListener.onItemDelete(fromPosition,toPosition);
        return true;
    }



    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public interface OnItemDeleteListener {
        void onItemDelete(View view, int position);
    }
    public interface OnItemMovedListener {
        void onItemDelete(int fromPosition, int toPosition);
    }
    public void setOnItemDeleteListener(final AdapterEditBoard.OnItemDeleteListener mItemClickListener) { this.mItemDeleteListener = mItemClickListener; }
    public void setOnItemClickListener(final AdapterEditBoard.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;

    }
    public void setOnItemMovedListener(final AdapterEditBoard.OnItemMovedListener mItemMovedListener) {this.mItemMovedListener = mItemMovedListener; }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        if(Mode==NORMAL_MODE)
            holder.removeIcon.setVisibility(View.GONE);
        else if(Mode==DELETE_MODE)
            holder.removeIcon.setVisibility(View.VISIBLE);
        super.onViewAttachedToWindow(holder);
    }

   class ViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {


        public ImageView iconImage;
        public TextView iconTitle;
        public ImageView removeIcon;
        public View holder;


        public ViewHolder(DragSortAdapter adapter, View itemView) {
            super(adapter, itemView);
            iconImage=itemView.findViewById(R.id.icon_image_view);
            iconTitle=itemView.findViewById(R.id.icon_title);
            removeIcon =itemView.findViewById(R.id.icon_remove_button);
            if(Mode==NORMAL_MODE)
            {
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
                removeIcon.setVisibility(View.GONE);
            }
            else if(Mode==DELETE_MODE)
            {
                removeIcon.setVisibility(View.VISIBLE);
                removeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Delete Mode","Adapter Set");
                        mItemDeleteListener.onItemDelete(v,getAdapterPosition());
                    }
                });
            }
            holder=itemView;


        }

        @Override public void onClick(@NonNull View v) {
            mItemClickListener.onItemClick(v,getAdapterPosition());
        }

        @Override public boolean onLongClick(@NonNull View v) {
            startDrag();
            return true;
        }

        @Override public View.DragShadowBuilder getShadowBuilder(View itemView, Point touchPoint) {
            return new NoForegroundShadowBuilder(itemView, touchPoint);
        }

    }
}