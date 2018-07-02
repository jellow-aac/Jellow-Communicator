package com.dsource.idc.jellowintl.makemyboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

private Context mContext;
// private LayoutInflater mInflater;
private ArrayList<Board> mDataSource;

public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //each data item is just a string in this case
    public TextView boardTitle;
    public TextView totalIconsInTheBoard;

    public ViewHolder(View v) {
        super(v);
        boardTitle =v.findViewById(R.id.board_title);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //TODO OnClick Item
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

        Board board = mDataSource.get(position);
        holder.boardTitle.setText(board.boardTitle);


    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
