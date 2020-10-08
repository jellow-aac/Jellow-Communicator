package com.dsource.idc.jellowintl.activities.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.activities.LevelThreeActivity;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.SessionManager;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

/**
 * Created by Sumeet on 19-04-2016.
 */
public class LevelThreeAdapter extends RecyclerView.Adapter<LevelThreeAdapter.MyViewHolder>{
    private LevelThreeActivity mAct;
    private SessionManager mSession;
    private String[] iconNameArray, belowTextArray;
    private RequestManager glide;

    public LevelThreeAdapter(Context context, String[] iconCodes, Icon[] level3IconObjects, Integer[] sort){
        mAct = (LevelThreeActivity) context;
        glide = GlideApp.with(mAct);
        mSession = mAct.getSession();

        String[] iconsText = TextFactory.getDisplayText(level3IconObjects);
        iconNameArray = new String[iconCodes.length];
        belowTextArray = new String[iconCodes.length];

        if(mAct.isCategoryWithPreference()){
            for (int i = 0; i < iconCodes.length; i++) {
                iconNameArray[i] = level3IconObjects[sort[i]].getEvent_Tag();
                belowTextArray[i] = iconsText[sort[i]];
            }
        } else {
            for (int i = 0; i < iconCodes.length; i++) {
                iconNameArray[i] =  level3IconObjects[i].getEvent_Tag();
                belowTextArray[i] = iconsText[i];
            }
        }
    }

    @Override
    public LevelThreeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView;
        if(mSession.getGridSize() == GlobalConstants.ONE_ICON_PER_SCREEN){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon, parent, false);
        }else if(mSession.getGridSize() == GlobalConstants.TWO_ICONS_PER_SCREEN){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons, parent, false);
        }else if(mSession.getGridSize() == GlobalConstants.THREE_ICONS_PER_SCREEN){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        }else if(mSession.getGridSize() == GlobalConstants.FOUR_ICONS_PER_SCREEN){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons, parent, false);
        }else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        }
        return new LevelThreeAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ViewCompat.setAccessibilityDelegate(holder.menuItemLinearLayout,
                new TalkbackHints_SingleClick());

        if (mSession.getPictureViewMode() == GlobalConstants.DISPLAY_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);
        holder.menuItemBelowText.setText(belowTextArray[position]);

        glide.load(getIconPath(mAct, iconNameArray[position]+EXTENSION))
                .into(holder.menuItemImage);
        holder.menuItemLinearLayout.setContentDescription(belowTextArray[position]);
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.tappedCategoryItemEvent(holder.menuItemLinearLayout);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return iconNameArray.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder  {
        private LinearLayout menuItemLinearLayout;
        private ImageView menuItemImage;
        private TextView menuItemBelowText;

        MyViewHolder(final View view) {
            super(view);
            menuItemImage = view.findViewById(R.id.icon1);
            menuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = view.findViewById(R.id.te1);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
            if(mAct.isAccessibilityTalkBackOn(
                    (AccessibilityManager) mAct.getSystemService(ACCESSIBILITY_SERVICE))) {
                Typeface tf = ResourcesCompat.getFont(mAct, R.font.mukta_semibold);
                menuItemBelowText.setTypeface(tf);
            }
            GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
            gd.setColor(ContextCompat.getColor(mAct, android.R.color.transparent));
        }
    }
}