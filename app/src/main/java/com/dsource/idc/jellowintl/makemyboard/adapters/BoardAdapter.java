package com.dsource.idc.jellowintl.makemyboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    public static final int OPEN_ADD_BOARD = 121;
    public static final int EDIT_BOARD = 212;
    private Context mContext;
// private LayoutInflater mInflater;
    private ArrayList<BoardModel> mDataSource;
    private OnItemClickListener mItemClickListener;

    @Override
    public void onBindViewHolder(BoardAdapter.ViewHolder holder, int position) {
        BoardModel board=mDataSource.get(position);
        File en_dir =mContext.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
        GlideApp.with(mContext)
                .load(path+"/"+board.getBoardId()+".png")
                .apply(new RequestOptions().transform(new RoundedCorners(50)))
                .error(R.drawable.ic_board_person)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .dontAnimate()
                .into(holder.boardIcon);
        StringBuilder titleText = new StringBuilder(board.getBoardName());
        titleText.append(" (");
        titleText.append(board.getLanguage());
        titleText.append(")");
        holder.boardTitle.setText(titleText.toString());
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int Position, int code);
        void onItemDelete(int position);
        void onBoardEdit(int position);
    }

    public void setOnItemClickListener(final BoardAdapter.OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * public constructor
     * @param context
     * @param items
     */
    public BoardAdapter(Context context, ArrayList<BoardModel> items) {
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //each iconList item is just a string in this case
    TextView boardTitle;
    ImageView boardIcon;
    ImageView editBoard;
    ImageView deleteBoard;

    public ViewHolder(View v) {
        super(v);
        boardTitle =v.findViewById(R.id.board_title);
        boardIcon=v.findViewById(R.id.board_icon);
        deleteBoard=v.findViewById(R.id.remove_board);
        editBoard = v.findViewById(R.id.edit_board);
        deleteBoard.setVisibility(View.VISIBLE);
        deleteBoard.setOnClickListener(this);
        boardIcon.setOnClickListener(this);
        editBoard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view==editBoard)
        {
            mItemClickListener.onBoardEdit(getAdapterPosition());
        }
        else if(view==boardIcon)
        {
            mItemClickListener.onItemClick(view,getAdapterPosition(),EDIT_BOARD);
        }
        else if(view==deleteBoard)
        {
            mItemClickListener.onItemDelete(getAdapterPosition());
        }
    }
    }
    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
