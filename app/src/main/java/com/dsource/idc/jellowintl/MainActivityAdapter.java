package com.dsource.idc.jellowintl;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.dsource.idc.jellowintl.cache.MemoryCache;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.dsource.idc.jellowintl.MainActivity.isAccessibilityTalkBackOn;
import static com.dsource.idc.jellowintl.MainActivity.isNotchDevice;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconDirectory;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.factories.PathFactory.getJSONFile;

/**
 * Created by ekalpa on 4/19/2016.
 */
class MainActivityAdapter extends android.support.v7.widget.RecyclerView.Adapter<MainActivityAdapter.MyViewHolder> {
    private Context mContext;
    private SessionManager mSession;
    private String[] icons;
    private String[] mBelowTextArray;
    private RequestManager glide;

    public MainActivityAdapter(Context context) {
        mContext = context;
        glide = GlideApp.with(mContext);
        mSession = new SessionManager(mContext);

        icons = IconFactory.getL1Icons(
                getIconDirectory(context),
                LanguageFactory.getCurrentLanguageCode(context)
        );

        File map = getJSONFile(context);
        Icon[] iconObjects = TextFactory.getIconObjects(map, IconFactory.removeFileExtension(icons));
        mBelowTextArray = TextFactory.getDisplayText(iconObjects);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final int GRID_1BY1 = 0, GRID_1BY2 = 1, GRID_1BY3 = 2, GRID_2BY2 = 3;
        View rowView;
        if(isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY1) {
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon_notch, parent, false);
        }else if(!isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY1){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_1_icon, parent, false);
        }else if(isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons_notch, parent, false);
        }else if(!isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_2_icons, parent, false);
        }else if(isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons_notch, parent, false);
        }else if(!isNotchDevice(mContext) && mSession.getGridSize() == GRID_1BY3){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_3_icons, parent, false);
        }else if(isNotchDevice(mContext) && mSession.getGridSize() == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons_notch, parent, false);
        }else if(!isNotchDevice(mContext) && mSession.getGridSize() == GRID_2BY2){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_4_icons, parent, false);
        }else if(isNotchDevice(mContext)){
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons_notch, parent, false);
        }else{
            rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_level_xadapter_9_icons, parent, false);
        }
        return new MainActivityAdapter.MyViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final MainActivityAdapter.MyViewHolder holder, final int position) {
        final int MODE_PICTURE_ONLY = 1;
        ViewCompat.setAccessibilityDelegate(holder.menuItemLinearLayout,
                new TalkbackHints_SingleClick());


        if (mSession.getPictureViewMode() == MODE_PICTURE_ONLY)
            holder.menuItemBelowText.setVisibility(View.INVISIBLE);

        holder.menuItemBelowText.setAllCaps(true);
        holder.menuItemBelowText.setText(mBelowTextArray[position]);

        Bitmap iconBitmap = MemoryCache.getBitmapFromMemCache(icons[position]);

        if (iconBitmap != null) {
            holder.menuItemImage.setImageBitmap(iconBitmap);
        } else {
            glide.load(getIconPath(mContext, icons[position]))
                    .into(holder.menuItemImage);
        }

        holder.menuItemLinearLayout.setContentDescription(mBelowTextArray[position]);
    }

    @Override
    public int getItemCount() {
        return icons.length;
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
            if(isAccessibilityTalkBackOn((AccessibilityManager) mContext.getSystemService(ACCESSIBILITY_SERVICE))) {
                Typeface tf = ResourcesCompat.getFont(mContext, R.font.mukta_semibold);
                menuItemBelowText.setTypeface(tf);
            }
            menuItemBelowText.setTextColor(Color.rgb(64, 64, 64));
            GradientDrawable gd = (GradientDrawable) view.findViewById(R.id.borderView).getBackground();
            gd.setColor(ContextCompat.getColor(mContext, android.R.color.transparent));
        }
    }
}
