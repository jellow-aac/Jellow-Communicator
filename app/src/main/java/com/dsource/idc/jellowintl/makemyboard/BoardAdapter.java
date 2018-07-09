package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    public static final int OPEN_ADD_BOARD = 121;
    public static final int EDIT_BOARD = 212;
    private Context mContext;
    private int mode;
// private LayoutInflater mInflater;
private ArrayList<Board> mDataSource;
private int size=-1;
    private OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //each data item is just a string in this case
    public TextView boardTitle;
    public ImageView boardIcon;
    public ImageView editBoard;
    public ImageView deleteBoard;

    public ViewHolder(View v) {
        super(v);
        boardTitle =v.findViewById(R.id.board_title);
        boardIcon=v.findViewById(R.id.board_icon);
        deleteBoard=v.findViewById(R.id.remove_board);
        boardIcon.setOnClickListener(this);
        editBoard = v.findViewById(R.id.edit_board);
        if(mode==MyBoards.NORMAL_MODE) {
            editBoard.setOnClickListener(this);
            deleteBoard.setVisibility(View.GONE);
        }
         if(mode==MyBoards.DELETE_MODE)
        {
            editBoard.setVisibility(View.GONE);
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
            mItemClickListener.onItemClick(view,getAdapterPosition(),MyBoards.DELETE_MODE);
        }
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


    @Override
    public void onBindViewHolder(BoardAdapter.ViewHolder holder, int position) {
        size=mDataSource.size();
        Board board=mDataSource.get(position);
        if(board.boardID.equals("-1")&&mode==MyBoards.NORMAL_MODE)
             {
                     holder.boardIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_plus));
                     holder.boardTitle.setText(mDataSource.get(position).boardTitle);
                     holder.editBoard.setVisibility(View.GONE);
             }
        else
            {

                    byte[] bitmapdata=board.getBoardIcon();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    holder.boardTitle.setText(board.boardTitle);
                    holder.boardIcon.setImageBitmap(bitmap);
            }


    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
