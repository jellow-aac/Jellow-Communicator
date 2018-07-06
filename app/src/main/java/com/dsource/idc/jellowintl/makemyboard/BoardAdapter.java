package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    public static final int OPEN_ADD_BOARD = 121;
    public static final int EDIT_BOARD = 212;
    private Context mContext;
// private LayoutInflater mInflater;
private ArrayList<Board> mDataSource;
private int size=-1;
    private OnItemClickListener mItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //each data item is just a string in this case
    public TextView boardTitle;
    public ImageView boardIcon;
    public ImageView editBoard;

    public ViewHolder(View v) {
        super(v);
        boardTitle =v.findViewById(R.id.board_title);

        boardIcon=v.findViewById(R.id.board_icon);
        boardIcon.setOnClickListener(this);

        editBoard=v.findViewById(R.id.edit_board);
        editBoard.setOnClickListener(this);
      /*  if(mDataSource.size()<5)
            boardIcon.setLayoutParams(new RelativeLayout.LayoutParams(
                    mContext.getResources().getDimensionPixelSize(R.dimen.my_board_four_grid_icon_size),//Width
                    mContext.getResources().getDimensionPixelSize(R.dimen.my_board_four_grid_icon_size)//Height
            ));*/
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
    public BoardAdapter(Context context, ArrayList<Board> items) {
        mContext = context;
        mDataSource = items;
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
        if(position==(size-1))
        {
            holder.boardIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_plus));
            holder.boardTitle.setText(mDataSource.get(position).boardTitle);
            holder.editBoard.setVisibility(View.GONE);
        }
        else
            {

            Board board = mDataSource.get(position);
            holder.boardTitle.setText(board.boardTitle);
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
