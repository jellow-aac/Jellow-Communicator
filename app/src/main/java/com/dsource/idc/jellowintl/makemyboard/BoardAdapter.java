package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

private Context mContext;
// private LayoutInflater mInflater;
private ArrayList<Board> mDataSource;
private int size=-1;

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
        final float scale = mContext.getResources().getDisplayMetrics().density;
        float pixel=mContext.getResources().getDimension(R.dimen.my_board_four_grid_icon_size);
        int size=(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, mContext.getResources().getDisplayMetrics());
        //int pixels = (int) (dps * scale + 0.5f);
        if(mDataSource.size()<5)
            boardIcon.setLayoutParams(new RelativeLayout.LayoutParams(
                    size,//Width
                    size//Height
            ));
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==editBoard)
        {
            Toast.makeText(mContext, "Edit board clicked", Toast.LENGTH_SHORT).show();
        }
    }
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
            holder.boardTitle.setText("Add board");
            holder.boardIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext,BoardIconSelectActivity.class));
                }
            });
            holder.editBoard.setVisibility(View.GONE);
        }
        else
            {

            Board board = mDataSource.get(position);
            holder.boardTitle.setText(board.boardTitle);
            }


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
