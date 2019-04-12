package com.dsource.idc.jellowintl;

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

import com.bumptech.glide.RequestManager;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

/**
 * Created by HP on 22/01/2017.
 */
class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> {
    private LevelTwoActivity mAct;
    private SessionManager mSession;
    private ArrayList<String> mIconNameList = new ArrayList<>();
    private ArrayList<String> mBelowTextList = new ArrayList<>();
    private RequestManager glide;


    PeopleAdapter(Context context, int levelOneItemPos, String[] mArrAdapterTxt, Integer[] arrSort) {
        mAct = (LevelTwoActivity) context;
        glide = GlideApp.with(mAct);
        mSession = mAct.getSession();
        loadArraysFromResources(levelOneItemPos, mArrAdapterTxt, arrSort);
    }

    @Override
    public PeopleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY1 = 0, GRID_1BY2 = 1, GRID_1BY3 = 2, GRID_2BY2 = 3;
        View rowView;
        if(mAct.isNotchDevice() && mSession.getGridSize() == GRID_1BY1) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon_notch, parent, false);
        }else if(!mAct.isNotchDevice() && mSession.getGridSize() == GRID_1BY1){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon, parent, false);
        }else if(mAct.isNotchDevice() && mSession.getGridSize() == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && mSession.getGridSize() == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons, parent, false);
        }else if(mAct.isNotchDevice() && mSession.getGridSize() == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && mSession.getGridSize() == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        }else if(mAct.isNotchDevice() && mSession.getGridSize() == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons_notch, parent, false);
        }else if(!mAct.isNotchDevice() && mSession.getGridSize() == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons, parent, false);
        }else if(mAct.isNotchDevice()){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons_notch, parent, false);
        }else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        }
        return new PeopleAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        ViewCompat.setAccessibilityDelegate(holder.menuItemLinearLayout,
                new TalkbackHints_SingleClick());

        final int MODE_PICTURE_ONLY = 1;

        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);
        holder.menuItemBelowText.setText(mBelowTextList.get(position));

       glide.load(getIconPath(mAct, mIconNameList.get(position)))
                .into(holder.menuItemImage);
        holder.menuItemLinearLayout.setContentDescription(mBelowTextList.get(position));
        holder.menuItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAct.tappedCategoryItemEvent(holder.menuItemLinearLayout, position);
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mIconNameList.size();
    }

    private void loadArraysFromResources(int levelOneItemPos, String[] mArrAdapterTxt, Integer[] arrSort) {
        ArrayList<String> tempIconList = new ArrayList<>();
        mBelowTextList.addAll(Arrays.asList(mArrAdapterTxt));

        tempIconList.addAll(Arrays.asList(IconFactory.getAllL2Icons(
                    PathFactory.getIconDirectory(mAct),
                    LanguageFactory.getCurrentLanguageCode(mAct),
                    getLevel2IconCode(levelOneItemPos)
        )));


        for (int i = 0; i < tempIconList.size(); i++) {
            mIconNameList.add(tempIconList.get(arrSort[i]));
        }
    }

    private String getLevel2IconCode(int level1Position){
        if(level1Position+1 <= 9){
            return "0" + Integer.toString(level1Position+1);
        } else {
            return Integer.toString(level1Position+1);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout menuItemLinearLayout;
        private ImageView menuItemImage;
        private TextView menuItemBelowText;

        MyViewHolder(final View view) {
            super(view);
            menuItemImage = view.findViewById(R.id.icon1);
            menuItemLinearLayout = view.findViewById(R.id.linearlayout_icon1);
            menuItemBelowText = view.findViewById(R.id.te1);
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
            if(mAct.isAccessibilityTalkBackOn((AccessibilityManager) mAct.getSystemService(ACCESSIBILITY_SERVICE))) {
                Typeface tf = ResourcesCompat.getFont(mAct, R.font.mukta_semibold);
                menuItemBelowText.setTypeface(tf);
            }
            GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
            gd.setColor(ContextCompat.getColor(mAct, android.R.color.transparent));
        }
    }
}
