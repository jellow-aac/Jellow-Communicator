package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.models.Board;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DELETE_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.NORMAL_MODE;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    public static final int OPEN_ADD_BOARD = 121;
    public static final int EDIT_BOARD = 212;
    private Context mContext;
    private int mode;
// private LayoutInflater mInflater;
    private ArrayList<Board> mDataSource;
    private OnItemClickListener mItemClickListener;

    @Override
    public void onBindViewHolder(BoardAdapter.ViewHolder holder, int position) {
        Board board=mDataSource.get(position);
        //To control visibility of the icons according to the mode
        if(mode==NORMAL_MODE)
        {
            holder.editBoard.setVisibility(View.VISIBLE);
            holder.deleteBoard.setVisibility(GONE);
        }
        else
        {
            holder.editBoard.setVisibility(GONE);
            holder.deleteBoard.setVisibility(View.VISIBLE);
        }
        if(board.boardID.equals("-1")&&mode==NORMAL_MODE)
             {
                     holder.boardIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_plus));
                     holder.boardTitle.setText(mDataSource.get(position).boardTitle);
                     holder.editBoard.setVisibility(GONE);
             }
        else
            {
                File en_dir =mContext.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
                String path = en_dir.getAbsolutePath() + "/boardicon";
                GlideApp.with(mContext)
                        .load(path+"/"+board.getBoardID()+".png")
                        .apply(new RequestOptions().transform(new RoundedCorners(50)))
                        .error(R.drawable.ic_board_person)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .centerCrop()
                        .dontAnimate()
                        .into(holder.boardIcon);
                holder.boardTitle.setText(board.boardTitle);
            }


    }
    public interface OnItemClickListener {
        void onItemClick(View view, int Position, int code);
    }

    public void setOnItemClickListener(final BoardAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public BoardAdapter(Context context, ArrayList<Board> items,int mode) {
        mContext = context;
        mDataSource = items;
        this.mode=mode;
    }


    @Override
    public BoardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_board_card, parent, false);
        return new BoardAdapter.ViewHolder(itemView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //each data item is just a string in this case
    TextView boardTitle;
    ImageView boardIcon;
    ImageView editBoard;
    ImageView deleteBoard;

    public ViewHolder(View v) {
        super(v);
        boardTitle =v.findViewById(R.id.board_title);
        boardIcon=v.findViewById(R.id.board_icon);
        deleteBoard=v.findViewById(R.id.remove_board);
        boardIcon.setOnClickListener(this);
        editBoard = v.findViewById(R.id.edit_board);
        if(mode==NORMAL_MODE) {
            editBoard.setOnClickListener(this);
            deleteBoard.setVisibility(GONE);
        }
         if(mode==DELETE_MODE)
        {
            editBoard.setVisibility(GONE);
            deleteBoard.setVisibility(View.VISIBLE);
            deleteBoard.setOnClickListener(this);
        }
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==editBoard)
        {
            mItemClickListener.onItemClick(view,getAdapterPosition(),EDIT_BOARD);
        }
        else if(view==boardIcon)
        {
            mItemClickListener.onItemClick(view,getAdapterPosition(),OPEN_ADD_BOARD);
        }
        else if(view==deleteBoard)
        {
            mItemClickListener.onItemClick(view,getAdapterPosition(),DELETE_MODE);
        }
    }
}

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        if(mode==DELETE_MODE) {
            Animation jiggle = AnimationUtils.loadAnimation(mContext, R.anim.jiggle);
            holder.boardIcon.startAnimation(jiggle);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
