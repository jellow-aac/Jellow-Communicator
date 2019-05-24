package com.dsource.idc.jellowintl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
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
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;
import static com.dsource.idc.jellowintl.MainActivity.isNotchDevice;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;

/**
 * Created by Sumeet on 19-04-2016.
 */
class LevelThreeAdapter extends android.support.v7.widget.RecyclerView.Adapter<LevelThreeAdapter.MyViewHolder>{
    private Context mContext;
    private SessionManager mSession;
    private ArrayList<String> mIconList = new ArrayList<>();
    private ArrayList<String> mBelowTextList = new ArrayList<>();
    private RequestManager glide;

    LevelThreeAdapter(Context context, int levelOneItemPos, int levelTwoItemPos, int sort[]){
        mContext = context;
        glide = GlideApp.with(mContext);
        mSession = new SessionManager(mContext);
        loadArraysFromResources(levelOneItemPos, levelTwoItemPos, sort);
    }

    @Override
    public LevelThreeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY3 = 0;
        View rowView;
        if(isNotchDevice(mContext) && mSession.getGridSize() != GRID_1BY3) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons_notch, parent, false);
        } else if(isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons_notch, parent, false);
        }else if (mSession.getGridSize() != GRID_1BY3) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        }else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        }
        return new LevelThreeAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ViewCompat.setAccessibilityDelegate(holder.menuItemLinearLayout,
                new TalkbackHints_SingleClick());

        final int MODE_PICTURE_ONLY = 1;
        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);
        holder.menuItemBelowText.setText(mBelowTextList.get(position));

        glide.load(getIconPath(mContext, mIconList.get(position)))
                .into(holder.menuItemImage);
        holder.menuItemLinearLayout.setContentDescription(mBelowTextList.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mIconList.size();
    }

    private void loadArraysFromResources(int levelOneItemPos, int levelTwoItemPos, int[] sort) {

        String[] icons = IconFactory.getL3Icons(
                PathFactory.getIconDirectory(mContext),
                LanguageFactory.getCurrentLanguageCode(mContext),
                getLevel2_3IconCode(levelOneItemPos),
                getLevel2_3IconCode(levelTwoItemPos)
        );

        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(mContext),
                IconFactory.removeFileExtension(icons)
        );

        String[] iconsText = TextFactory.getDisplayText(iconObjects);

        if(levelOneItemPos == 3 && (levelTwoItemPos == 3 || levelTwoItemPos == 4)){

            loadAdapterMenuTextIconsWithoutSort(icons,iconsText);

        } else if(levelOneItemPos == 4 && levelTwoItemPos == 9){

            loadAdapterMenuTextIconsWithoutSort(icons,iconsText);

        } else if(levelOneItemPos == 7 && (levelTwoItemPos == 0 || levelTwoItemPos == 1 ||
                levelTwoItemPos == 2 || levelTwoItemPos == 3 || levelTwoItemPos == 4)){

            loadAdapterMenuTextIconsWithoutSort(icons,iconsText);

        } else {
            loadAdapterMenuTextIconsWithSort(icons,iconsText,sort);
        }
    }

    private void loadAdapterMenuTextIconsWithoutSort(String[] typeIconArray, String[] stringBelowTextArray) {
        ArrayList<String> tempIconArr = new ArrayList<>();
        ArrayList<String> tempBelowTextArr = new ArrayList<>();

        for (int j = 0; j < typeIconArray.length; j++) {

            tempIconArr.add(typeIconArray[j]);
            tempBelowTextArr.add(stringBelowTextArray[j]);
        }
        mIconList = tempIconArr;
        mBelowTextList = tempBelowTextArr;
    }

    private void loadAdapterMenuTextIconsWithSort(String[] typeIconArray, String[] stringBelowTextArray, int[] sort) {

        ArrayList<String> tempIconArr = new ArrayList<>();
        ArrayList<String> tempBelowTextArr = new ArrayList<>();
        for (int j = 0; j < typeIconArray.length; j++) {

            tempIconArr.add(typeIconArray[sort[j]]);
            tempBelowTextArr.add(stringBelowTextArray[sort[j]]);
        }
        mIconList = tempIconArr;
        mBelowTextList = tempBelowTextArr;
    }

    private String getLevel2_3IconCode(int level2_3Position){
        if(level2_3Position+1 <= 9){
            return "0" + Integer.toString(level2_3Position+1);
        } else {
            return Integer.toString(level2_3Position+1);
        }
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
            if(isAccessibilityTalkBackOn((AccessibilityManager) mContext.getSystemService(ACCESSIBILITY_SERVICE))) {
                Typeface tf = ResourcesCompat.getFont(mContext, R.font.mukta_semibold);
                menuItemBelowText.setTypeface(tf);
            }
            GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
            gd.setColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
    }
}
