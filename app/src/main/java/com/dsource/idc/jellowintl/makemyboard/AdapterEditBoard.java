package com.dsource.idc.jellowintl.makemyboard;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.makeramen.dragsortadapter.DragSortAdapter;

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
    private onItemDraggedOut mOnItemDraggedOutListener;
    private View parent;
    private RecyclerView mRecyclerView;
    public AdapterEditBoard(RecyclerView recyclerView, List<JellowIcon> data, Context context,int Mode,View parent) {
        super(recyclerView);
        this.data = data;
        this.Mode=Mode;
        mContext=context;
        this.parent=parent;
        this.mRecyclerView=recyclerView;
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
            drop(itemView);


        }

        @Override public void onClick(@NonNull View v) {
            mItemClickListener.onItemClick(v,getAdapterPosition());
        }

        @Override public boolean onLongClick(@NonNull View v) {
            prepareDragListeners(v);
            //startDrag();
            return true;
        }

        void prepareDragListeners(View view)
        {
            Toast.makeText(mContext,"Dragging Started",Toast.LENGTH_SHORT).show();
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.GONE);
        }

        boolean draggedOut=false;
        boolean insideLayout=true;
        void drop(final View view)
        {



            final Drawable enterShape = mContext.getResources().getDrawable(
                    R.drawable.shape_droptarget);
            final Drawable normalShape = mContext.getResources().getDrawable(R.drawable.shape);



    /*        final RecyclerView iconContainer=mRecyclerView;//parent.findViewById(R.id.recycler_view_container);
            iconContainer.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            iconContainer.setBackgroundColor(mContext.getResources().getColor(R.color.dark_base));
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            //iconContainer.setBackground(normalShape);
                            Toast.makeText(mContext,"Dragged out!!",Toast.LENGTH_SHORT).show();
                            break;
                        case DragEvent.ACTION_DROP:

                            *//*
                            ViewGroup owner = (ViewGroup) view.getParent();
                            owner.removeView(view);
                            LinearLayout container = (LinearLayout) v;
                            container.addView(view);
                            view.setVisibility(View.VISIBLE);
                            *//*
                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            float x = event.getX();
                            float y = event.getY();
                            int toPosition = -1;

                            View child = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
                            if (child != null) {

                                toPosition = mRecyclerView.getChildViewHolder(child).getAdapterPosition();
                                Log.d("Childfound",data.get(toPosition).IconTitle);
                            }
                            else {
                                Log.d("Childfound","Child is null");
                            }


                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            iconContainer.setBackground(normalShape);
                        default:
                            break;
                    }
                    return true;
                }
            });

*/
            final RelativeLayout layout=parent.findViewById(R.id.recycler_view_container);
            layout.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    int action = event.getAction();

                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            draggedOut=false;
                            layout.setBackgroundColor(mContext.getResources().getColor(R.color.dark_base));
                            insideLayout=true;
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            insideLayout=false;
                            View vd = (View) event.getLocalState();
                            if(!draggedOut) {
                                mOnItemDraggedOutListener.onIconDraggedOut(mRecyclerView.getChildLayoutPosition(vd));
                                Toast.makeText(mContext, "Dragged Out", Toast.LENGTH_SHORT).show();
                                draggedOut=!draggedOut;
                            }

                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            break;

                        case DragEvent.ACTION_DROP:
                            insideLayout=false;
                            layout.setBackground(normalShape);
                            view.setBackground(normalShape);
                            mOnItemDraggedOutListener.onIconDropped();
                            break;

                        case DragEvent.ACTION_DRAG_ENDED:
                            view.setBackground(normalShape);
                            layout.setBackground(normalShape);

                            insideLayout=false;
                        default:
                            break;
                    }
                    return true;
                }
            });



        }


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


    public interface onItemDraggedOut
    {
        void onIconDraggedOut(int position);
        void onIconDropped();
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
    public void setmOnItemDraggedOutListener(final AdapterEditBoard.onItemDraggedOut mItemMovedListener) {
        this.mOnItemDraggedOutListener = mItemMovedListener;

    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        if(Mode==NORMAL_MODE)
            holder.removeIcon.setVisibility(View.GONE);
        else if(Mode==DELETE_MODE)
            holder.removeIcon.setVisibility(View.VISIBLE);
        super.onViewAttachedToWindow(holder);
    }

}