package com.dsource.idc.jellowintl.makemyboard.LevelAdapers;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.LevelAdapers.beans.LevelParent;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GenCallback;

public class ViewHolderParent extends ParentViewHolder {

        private TextView genreTitle;
        private View holder;
        private Context context;

        public ViewHolderParent(View itemView, final GenCallback<Integer> positionCallback, final GenCallback<Integer> expandCollapseCallback, Context context) {
            super(itemView);
            holder = itemView;
            this.context=context;
            genreTitle = itemView.findViewById(R.id.icon_title);
            genreTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    positionCallback.callBack(getParentAdapterPosition());
                }
            });
            itemView.findViewById(R.id.collapseView).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isExpanded()) collapseView();
                    else expandView();
                    expandCollapseCallback.callBack(getParentAdapterPosition());
                }
            });

        }

        void setParentTitle(LevelParent group) {
            genreTitle.setText(group.getTitle().replaceAll("…",""));
        }
        void setSelected(boolean isSelected){

            if(isSelected){
                genreTitle.setTextColor(context.getResources().getColor(R.color.app_background));
                holder.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                ((ImageView)holder.findViewById(R.id.collapseView))
                        .setColorFilter(context.getResources().getColor(R.color.app_background),
                                PorterDuff.Mode.MULTIPLY);
            }
            else{
                genreTitle.setTextColor(context.getResources().getColor(R.color.level_select_text_color));
                holder.setBackgroundColor(context.getResources().getColor(R.color.app_background));
                ((ImageView)holder.findViewById(R.id.collapseView)).setColorFilter(context.getResources().
                        getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            }
            ((ImageView) holder.findViewById(R.id.collapseView)).setColorFilter(ContextCompat.getColor(context,
                   isSelected ? R.color.app_background : R.color.colorPrimary));

        }

        void disableCollapse(boolean disable){
            if(disable)holder.findViewById(R.id.collapseView).setVisibility(View.INVISIBLE);
            else holder.findViewById(R.id.collapseView).setVisibility(View.VISIBLE);
        }
}