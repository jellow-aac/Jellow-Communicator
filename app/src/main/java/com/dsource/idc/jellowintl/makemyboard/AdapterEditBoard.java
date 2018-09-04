package com.dsource.idc.jellowintl.makemyboard;

import android.content.ClipData;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.dragsorter.DragSortAdapter;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.ByteArrayOutputStream;
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
    private OnItemMovedInListener movedInListener;
    private View parent;
    private RecyclerView mRecyclerView;
    private int dragPosition = -1;
    private View targetView = null;
    private int GridSize = 9;

    public AdapterEditBoard(RecyclerView recyclerView, List<JellowIcon> data, Context context,int Mode,View parent,int GridSize) {
        super(recyclerView);
        this.data = data;
        this.Mode=Mode;
        mContext=context;
        this.parent=parent;
        this.mRecyclerView=recyclerView;
        this.GridSize = GridSize;
    }


    class ViewHolder extends DragSortAdapter.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {


        public ImageView iconImage;
        public TextView iconTitle;
        public ImageView removeIcon;
        public View holder;


        public ViewHolder(DragSortAdapter adapter, final View itemView) {
            super(adapter, itemView);
            iconImage=itemView.findViewById(R.id.icon_image_view);
            iconTitle=itemView.findViewById(R.id.icon_title);
            removeIcon =itemView.findViewById(R.id.icon_remove_button);
            if(Mode==NORMAL_MODE)
            {

                itemView.setOnTouchListener(new View.OnTouchListener() {
                    private GestureDetector gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onDoubleTap(MotionEvent e) {
                            mItemClickListener.onItemClick(itemView,getAdapterPosition());
                            return super.onDoubleTap(e);
                        }

                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {

                            return super.onSingleTapUp(e);
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            prepareDragListeners(itemView);
                            super.onLongPress(e);
                        }
                    });

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        gestureDetector.onTouchEvent(event);
                        return true;
                    }
                });
                itemView.setOnLongClickListener(this);
                removeIcon.setVisibility(View.GONE);
            }
            else if(Mode==DELETE_MODE)
            {
                itemView.setOnClickListener(this);
                removeIcon.setVisibility(View.VISIBLE);
                removeIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        if(pos!=-1)
                        mItemDeleteListener.onItemDelete(v,pos);
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
            //prepareDragListeners(v);
            //startDrag();
            return true;
        }

        void prepareDragListeners(View view)
        {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.GONE);
        }

        boolean draggedOut=false;
        boolean insideLayout=true;
        void drop(final View itemView)
        {



            final Drawable enterShape = mContext.getResources().getDrawable(
                    R.drawable.shape_droptarget);
            final Drawable normalShape = mContext.getResources().getDrawable(R.drawable.shape);

            final RecyclerView iconContainer=mRecyclerView;//parent.findViewById(R.id.recycler_view_container);
            iconContainer.setOnDragListener(new View.OnDragListener() {
                @Override
                public boolean onDrag(View v, DragEvent event) {
                    switch (event.getAction()) {
                        case DragEvent.ACTION_DRAG_STARTED:
                            // do nothing
                            break;
                        case DragEvent.ACTION_DRAG_ENTERED:
                            iconContainer.setBackgroundColor(mContext.getResources().getColor(R.color.dark_base));
                            break;
                        case DragEvent.ACTION_DRAG_EXITED:
                            //iconContainer.setBackground(normalShape);
                            mOnItemDraggedOutListener.onIconDraggedOut(getAdapterPosition());
                            Toast.makeText(mContext,"Dragged out!!",Toast.LENGTH_SHORT).show();
                            break;
                        case DragEvent.ACTION_DROP:

                            if(targetView==null) {
                                ViewGroup owner = (ViewGroup) itemView.getParent();
                                owner.removeView(itemView);
                                mRecyclerView.addView(itemView);
                                itemView.setVisibility(View.VISIBLE);
                                movedInListener.itemDroped();
                            }
                            else
                            {
                                int fromPosition = mRecyclerView.getChildAdapterPosition(itemView);
                                if(fromPosition!=-1&&dragPosition!=-1)
                                movedInListener.itemDraggedIn(fromPosition,dragPosition);

                            }
                            targetView.setBackground(normalShape);
                            itemView.setVisibility(View.VISIBLE);

                            break;
                        case DragEvent.ACTION_DRAG_LOCATION:
                            float x = event.getX();
                            float y = event.getY();
                            int toPosition = -1;

                            View child = mRecyclerView.findChildViewUnder(event.getX(), event.getY());
                            if (child != null) {
                                toPosition = mRecyclerView.getChildViewHolder(child).getAdapterPosition();
                                Log.d("Childfound",data.get(toPosition).IconTitle);
                                dragPosition = toPosition;
                                targetView = child;
                                child.setBackground(enterShape);
                            }
                            else {
                                dragPosition = -1;
                                if(targetView!=null)
                                    targetView.setBackground(normalShape);
                                targetView = null;
                                Log.d("Childfound","Child is null");
                            }

                            break;
                        case DragEvent.ACTION_DRAG_ENDED:
                            iconContainer.setBackground(normalShape);
                            itemView.setVisibility(View.VISIBLE);
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
        View itemView = null;
        if(GridSize<4)
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_layout_3_icons, parent, false);
        else
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.custom_layout_9_icons, parent, false);
        ViewHolder holder = new ViewHolder(this, itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        long itemId = data.get(position).getID();
        JellowIcon thisIcon = data.get(position);
        holder.iconTitle.setText(thisIcon.IconTitle);
      //  holder.holder.setVisibility(getDraggingId() == itemId ? View.INVISIBLE : View.VISIBLE);

        if(thisIcon.parent0==-1)
        {
            byte[] bitmapData=thisIcon.IconImage;//.getBytes(Charset.defaultCharset());
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapData, 0, bitmapData.length);
            //holder.iconImage.setImageBitmap(bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Glide.with(mContext)
                    .asBitmap()
                    .load(stream.toByteArray())
                    .apply(RequestOptions.
                            circleCropTransform()).into(holder.iconImage);
            holder.iconImage.setBackground(mContext.getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else if(thisIcon.parent1==-1)
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
        mItemMovedListener.onItemMoved(fromPosition,toPosition);
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
    public interface OnItemMovedInListener
    {
        void itemDraggedIn(int from,int position);
        void itemDroped();
    }
    public interface OnItemMovedListener {
        void onItemMoved(int fromPosition, int toPosition);
    }
    public void setOnItemDeleteListener(final AdapterEditBoard.OnItemDeleteListener mItemClickListener) { this.mItemDeleteListener = mItemClickListener; }
    public void setOnItemClickListener(final AdapterEditBoard.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;

    }
    public void setOnItemMovedListener(final AdapterEditBoard.OnItemMovedListener mItemMovedListener) {this.mItemMovedListener = mItemMovedListener; }
    public void setmOnItemDraggedOutListener(final AdapterEditBoard.onItemDraggedOut mItemMovedListener) {
        this.mOnItemDraggedOutListener = mItemMovedListener;

    }

    public void setOnItemMovedInListener(OnItemMovedInListener movedInListener) {
        this.movedInListener = movedInListener;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        if(Mode==NORMAL_MODE)
            holder.removeIcon.setVisibility(View.GONE);
        else if(Mode==DELETE_MODE)
        {
            holder.removeIcon.setVisibility(View.VISIBLE);
            Animation jiggle = AnimationUtils.loadAnimation(mContext, R.anim.jiggle);
            holder.iconImage.startAnimation(jiggle);
        }
        super.onViewAttachedToWindow(holder);
    }

}