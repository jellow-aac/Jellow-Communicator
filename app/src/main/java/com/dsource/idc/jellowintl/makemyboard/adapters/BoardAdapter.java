package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;

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
        if (position == 0){
            viewHolder.setVisible(R.id.edit_board, false);
            viewHolder.setVisible(R.id.remove_board, false);
            viewHolder.setText(R.id.board_title, getContext().getString(R.string.add_board));
            ((ImageView) viewHolder.getView(R.id.board_icon)).setImageResource(R.drawable.ic_plus);
            viewHolder.setOnClickListener(R.id.board_icon, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onItemClick(viewHolder.getAdapterPosition());
                }
            });
            return;
        }
        viewHolder.setText(R.id.board_title, board.getBoardName() +
                "\n"+ LangValueMap.get(board.getLanguage()));
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
}
