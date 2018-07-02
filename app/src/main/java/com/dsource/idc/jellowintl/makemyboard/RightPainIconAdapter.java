package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

public class RightPainIconAdapter extends DragSortAdapter<RightPainIconAdapter.ViewHolder> {

    public static final String TAG = RightPainIconAdapter.class.getSimpleName();

    private final List<JellowIcon> data;
    private Context mContext;

    public RightPainIconAdapter(RecyclerView recyclerView, List<JellowIcon> data,Context context) {
        super(recyclerView);
        this.data = data;
        mContext=context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.icon_edit_pain_card, parent, false);
        ViewHolder holder = new ViewHolder(this, view);
        view.setOnClickListener(holder);
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
        return true;
    }




    static class ViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {


        public ImageView iconImage;
        public TextView iconTitle;
        public ImageView removeIcom;
        public View holder;
        public ViewHolder(DragSortAdapter adapter, View itemView) {
            super(adapter, itemView);
            iconImage=itemView.findViewById(R.id.icon_image_view);
            iconTitle=itemView.findViewById(R.id.icon_title);
            removeIcom=itemView.findViewById(R.id.icon_remove_button);
            holder=itemView;
            itemView.setOnLongClickListener(this);

        }

        @Override public void onClick(@NonNull View v) {

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