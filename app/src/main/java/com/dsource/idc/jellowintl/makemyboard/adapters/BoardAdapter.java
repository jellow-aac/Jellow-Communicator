package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.utility.SessionManager.LangValueMap;

public class BoardAdapter extends BaseRecyclerAdapter<BoardModel> {

    private BoardClickListener listener;
    private int selectedPosition = -1;

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
        SpannableString spannedStr;
        viewHolder.setMenuImageBorder(R.id.borderView, false, -1);
        if (position == 0){
            viewHolder.setVisible(R.id.edit_board, false);
            viewHolder.setVisible(R.id.remove_board, false);
            spannedStr = new SpannableString(getContext().getString(R.string.add_board));
            spannedStr.setSpan(new ForegroundColorSpan (ContextCompat.getColor(getContext(),
                    R.color.colorPrimary)), 0, spannedStr.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.setText(R.id.board_title, spannedStr);
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

        spannedStr = new SpannableString(board.getBoardName()+"\n"+ LangValueMap.get(board.getLanguage()));
        spannedStr.setSpan(new ForegroundColorSpan (ContextCompat.getColor(getContext(),
                R.color.colorPrimary)), 0, board.getBoardName().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.setText(R.id.board_title, spannedStr);
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
        if(selectedPosition == position) {
            viewHolder.setMenuImageBorder(R.id.borderView, true, 100);
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewHolder.setMenuImageBorder(R.id.borderView,false,-1);
                    selectedPosition = -1;
                }
            }, 1500);
        }
    }

    public void highlightSearchedBoard(int position){
        selectedPosition = position;
        notifyItemChanged(position);
    }

    public void setOnItemClickListener(final BoardClickListener mItemClickListener) {
        this.listener = mItemClickListener;
    }
}
