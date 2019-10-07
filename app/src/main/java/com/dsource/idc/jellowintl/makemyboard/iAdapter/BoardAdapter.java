package com.dsource.idc.jellowintl.makemyboard.iAdapter;

import android.content.Context;
import android.view.View;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import java.util.ArrayList;

public class BoardAdapter extends BaseRecyclerAdapter<BoardModel> {

    public static final int OPEN_ADD_BOARD = 121;
    public static final int EDIT_BOARD = 212;
    private BoardClickListener listener;

    /**
     * public constructor
     * @param context
     * @param items
     */
    public BoardAdapter(Context context,int layoutId,ArrayList<BoardModel> items) {
        super(context,layoutId,items);
    }


    @Override
    public void bindData(final BaseViewHolder viewHolder, BoardModel board, int position) {
        StringBuilder titleText = new StringBuilder(board.getBoardName());
        titleText.append(" (");
        titleText.append(board.getLanguage());
        titleText.append(")");
        viewHolder.setText(R.id.board_title,titleText.toString());
        viewHolder.setImageFromBoard(R.id.board_icon,board.getBoardId());
        viewHolder.setOnClickListener(R.id.remove_board, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onItemDelete(viewHolder.getAdapterPosition());

            }
        });
        viewHolder.setOnClickListener(R.id.edit_board, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onBoardEdit(viewHolder.getAdapterPosition());
            }
        });

        viewHolder.setOnClickListener(R.id.board_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onItemClick(viewHolder.getAdapterPosition());
            }
        });

    }

    public void setOnItemClickListener(final BoardClickListener mItemClickListener) {
        this.listener = mItemClickListener;
    }

    @Override
    public void updateDataOnTouch(int position) {

    }
}
