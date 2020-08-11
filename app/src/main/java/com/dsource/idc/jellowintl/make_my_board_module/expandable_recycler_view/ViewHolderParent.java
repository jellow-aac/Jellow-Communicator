package com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.datamodels.LevelParent;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.GenCallback;

public class ViewHolderParent extends ParentViewHolder {

        private TextView genreTitle;
        private View holder;
        private Context context;

        public ViewHolderParent(View itemView, final GenCallback<Integer> expandCollapseCallback, Context context) {
            super(itemView);
            holder = itemView;
            this.context=context;
            genreTitle = itemView.findViewById(R.id.icon_title);
            genreTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpanded()){
                        collapseView();
                        ((ImageView)holder.findViewById(R.id.collapseView)).
                                setImageResource(R.drawable.ic_action_navigation_arrow_down);
                    }else{
                        ((ImageView)holder.findViewById(R.id.collapseView))
                                .setImageResource(R.drawable.ic_action_navigation_arrow_up);
                        expandView();
                        expandCollapseCallback.callBack(getParentAdapterPosition());
                    }

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
                        .setImageResource(R.drawable.ic_action_navigation_arrow_up);
            }
            else{
                genreTitle.setTextColor(context.getResources().getColor(R.color.level_select_text_color));
                holder.setBackgroundColor(context.getResources().getColor(R.color.app_background));
                ((ImageView)holder.findViewById(R.id.collapseView)).
                        setImageResource(R.drawable.ic_action_navigation_arrow_down);
            }

        }

        void disableCollapse(boolean disable){
            if(disable)holder.findViewById(R.id.collapseView).setVisibility(View.INVISIBLE);
            else holder.findViewById(R.id.collapseView).setVisibility(View.VISIBLE);
        }

        void makeTextBold(boolean makeBold){
            if(makeBold) {
                Typeface tf = ResourcesCompat.getFont(context, R.font.mukta_semibold);
                genreTitle.setTypeface(tf);
            }
        }
}