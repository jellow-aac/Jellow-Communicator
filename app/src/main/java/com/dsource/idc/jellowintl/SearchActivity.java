package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dsource.idc.jellowintl.factories.PathFactory.getIconDirectory;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;


/*
* Created by Ayaz on 31/5/2018
*/

/**
 * <p>
 * {@link SearchActivity} which overlays on other activity
 * and performs the search, this blurs the background and
 * can be closed by touching outside.
 * @Author AyazAlam
 * </p>
 */
public class SearchActivity extends SpeechEngineBaseActivity {

    private RecyclerView mRecyclerView;
    private SearchViewIconAdapter adapter;
    private ArrayList<JellowIcon> iconList;
    //This variable holds the event "Icon not found"
    private boolean iconNotFound=false;
    //This variable holds the text that user has searched
    private String notFoundIconText="Null";

    private int beforeTextChanged;
    private int afterTextChanged;
    private boolean firedEvent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EditText SearchEditText = findViewById(R.id.search_auto_complete);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            SearchEditText.setContentDescription("Enter to search");
            findViewById(R.id.close_button).setVisibility(View.GONE);
        } else {
            SearchEditText.setHint("Search icon..");
        }
        getWindow().setGravity(Gravity.LEFT);

        // Reference to the icon database to get access to the Icon list.
        final IconDataBaseHelper iconDatabase=new IconDataBaseHelper(this);

        // To Close on touch outside
        (findViewById(R.id.search_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(iconNotFound&&!(notFoundIconText.equals("Null"))) {
                    //Firebase event to log the "SearchBar" event with
                    // "IconNotFound" parameter.
                    Bundle bundle = new Bundle();
                    bundle.putString("IconSpeak", "");
                    bundle.putString("IconOpened", "");
                    bundle.putString("IconNotFound", notFoundIconText);
                    bundleEvent("SearchBar", bundle);
                }
                finish();
            }
        });

        //Initialising the fields
        initFields();
        //Adding text watcher so that we can address dynamic text changes
        SearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChanged=s.length();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Reset Icon not found tag
                iconNotFound=false;
                afterTextChanged=s.length();
                //Getting the string to search in the database
                String query=s.toString().trim();

                /**
                 * {@link IconDataBaseHelper.query(String)} returns a {@link ArrayList< JellowIcon >}  object
                 * having all the JellowIcon matching the database
                 * */
                ArrayList<JellowIcon> icon  =iconDatabase.query(query);
                //Clear the current list
                iconList.clear();
                //Prepare the list
                if(icon!=null&&icon.size()>0)
                for(int i=0;i<icon.size();i++)
                    iconList.add(icon.get(i));
                //when no Icon matches the searched string then "Icon not found" condition occurs
                if(icon.size()==0)
                {
                    iconNotFound=true;
                    notFoundIconText=s.toString();
                    JellowIcon noIconFound=new JellowIcon(getString(R.string.not_found),"NULL","NULL",-1,-1,-1);
                    iconList.add(noIconFound);
                }

                if(beforeTextChanged>afterTextChanged)
                {
                    if((!firedEvent)&&iconNotFound) {
                        //Firebase event to log the "SearchBar" event with
                        // "IconNotFound" parameter.
                        Bundle bundle = new Bundle();
                        bundle.putString("IconSpeak", "");
                        bundle.putString("IconOpened", "");
                        bundle.putString("IconNotFound", notFoundIconText);
                        bundleEvent("SearchBar", bundle);
                        firedEvent = true;
                    }
                }
                else
                {
                    firedEvent=false;
                }




                //List should have atleast one Item
                if(iconList.size()>0)
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });






    }

    public void closeActivity(View v){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getCaregiverNumber().substring(1));
        }
        // Start measuring user app screen timer.
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ///Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring("SearchActivity");
    }

    private void initFields() {
        iconList=new ArrayList<>();
        adapter=new SearchViewIconAdapter(this,iconList);
        mRecyclerView =findViewById(R.id.icon_search_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

/**
 * This is a different class namde {@link SearchViewIconAdapter}
 * {@link SearchViewIconAdapter} This class is responsible for populating and managing the recycler view of
 * the {@link SearchActivity}
 * */
class SearchViewIconAdapter extends RecyclerView.Adapter<SearchViewIconAdapter.ViewHolder> {

    private Context mContext;
    // private LayoutInflater mInflater;
    private ArrayList<JellowIcon> mDataSource;
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Each data item is just a string in this case
        public ImageView iconImage;
        public TextView iconTitle;
        public TextView iconDir;
        public ImageView speakIcon;
        public LinearLayout llSearchParent;
        public ViewHolder(View v) {
            super(v);
            iconImage =v.findViewById(R.id.search_icon_drawable);
            iconTitle = v.findViewById(R.id.search_icon_title);
            iconDir = v.findViewById(R.id.parent_directory);
            llSearchParent = v.findViewById(R.id.llSearchParent);
            ViewCompat.setAccessibilityDelegate(v.findViewById(R.id.llSearchParent), new TalkbackHints_SingleClick());
            speakIcon=v.findViewById(R.id.speak_button);
            speakIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SearchActivity)mContext).speak(mDataSource.get(getAdapterPosition()).IconSpeech);
                    //Firebase event to log the "SearchBar" event with
                    // "IconSpeak" parameter.
                    Bundle bundle = new Bundle();
                    bundle.putString("IconSpeak",mDataSource.get(getAdapterPosition()).IconTitle.replace("…",""));
                    bundle.putString("IconOpened", "");
                    bundle.putString("IconNotFound", "");
                    bundleEvent("SearchBar", bundle);
                }
            });
            llSearchParent.setOnClickListener(this);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            JellowIcon icon=mDataSource.get(getAdapterPosition());
            if(icon.parent0==-1&&icon.IconTitle.equals(mContext.getString(R.string.not_found)))//No icon found
            {
                //Do nothing when icon is not found
            }
            else {
                //Open the activity when icon is found
                Intent target = getActivityFromIcon(icon);
                //Firebase event to log the "SearchBar" event with
                // "IconOpened" parameter.
                Bundle bundle = new Bundle();
                bundle.putString("IconSpeak", "");
                bundle.putString("IconOpened", icon.IconTitle.replace("…",""));
                bundle.putString("IconNotFound", "");
                bundleEvent("SearchBar", bundle);
                target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(target);
                
            }
        }
    }

/**
 * This code is to get activity from JellowIcon Class object
 * */
    String menuPath;
    private Intent getActivityFromIcon(JellowIcon icon) {
        menuPath=mContext.getString(R.string.intent_menu_path_tag);
        Intent activityIntent=null;

        if(icon.parent1==-1&&icon.parent2==-1)//Level 1
        {
            activityIntent=new Intent(mContext, MainActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.search_parent_0),icon.parent0);
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
        }
        else if(icon.parent0!=-1&&icon.parent1!=-1&&icon.parent2==-1)//Level 2
        {
            activityIntent=new Intent(mContext, LevelTwoActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),icon.parent0);
            //Passing the index of the icon to be highlighted.
            activityIntent.putExtra(mContext.getString(R.string.search_parent_1),icon.parent1);
            activityIntent.putExtra(menuPath,getActionBarTitle(icon.parent0));
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));

        }
        else //Level 3
        {
            activityIntent=new Intent(mContext, LevelThreeActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
            //Level 1 parent
            activityIntent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),icon.parent0);
            //Level 2 parent
            activityIntent.putExtra(mContext.getString(R.string.level_2_item_pos_tag),icon.parent1);
            //Level 3 Icon reference to be highlighted
            activityIntent.putExtra(mContext.getString(R.string.search_parent_2),icon.parent2);
            String levelTitle = getIconTitleLevel2(icon.parent0)[icon.parent1].replace("…", "");
            //Bread crumb paths
            activityIntent.putExtra(menuPath,getActionBarTitle(icon.parent0)+""+levelTitle+"/");
        }
        return activityIntent;
    }

    /**
     * <p>This function will provide action bar title to be set.
     * @param position, position of the category icon pressed.
     * @return the actionbarTitle string.</p>
     * */
    private String getActionBarTitle(int position) {

        String[] tempTextArr = getLevel1IconLabels();
        return tempTextArr[position]+"/";
    }
    public SearchViewIconAdapter(Context context, ArrayList<JellowIcon> items) {
        mContext = context;
        mDataSource = items;
    }

    @Override
    public SearchViewIconAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.icon_search_list_item, parent, false);
        return new SearchViewIconAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchViewIconAdapter.ViewHolder holder, int position) {

        JellowIcon thisIcon = mDataSource.get(position);
        /*
        * Loading image to the search list, load directly from asset if MainActivity
        * else if LevelTwoActivity or LevelThreeActivity then use glide to load from storage
        * */

        //If the "No icon found" condition comes the remove speaker
       if(thisIcon.IconDrawable.equals("NULL")&&thisIcon.IconTitle.equals(mContext.getString(R.string.not_found)))
        {
            holder.iconTitle.setText(R.string.icon_not_found);
            holder.speakIcon.setVisibility(View.GONE);
            holder.iconDir.setVisibility(View.GONE);
            holder.iconImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_icon_not_found));
            return;
        }

        holder.speakIcon.setVisibility(View.VISIBLE);
        holder.iconDir.setVisibility(View.VISIBLE);
        holder.iconTitle.setText(thisIcon.IconTitle.replace("…",""));

            GlideApp.with(mContext)
                    .load(getIconPath(mContext,thisIcon.IconDrawable))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);

        /*
        * Adding the directory hint in the search list item
        * To this, we are using the function same as in menuSelected path used
        * */


        String[] arr = getLevel1IconLabels();

        for(int i=0;i<arr.length;i++){
            arr[i] = arr[i].split("…")[0];
        }

        String dir="";
        if(thisIcon.parent1==-1)
        {
            dir=mContext.getResources().getString(R.string.home);
        }
        else if(thisIcon.parent2==-1)
        {
            dir=arr[thisIcon.parent0];
        }
        else {
            try {
                String levelTitle = getIconTitleLevel2(thisIcon.parent0)[thisIcon.parent1].
                        replace("…", "");
                dir=arr[thisIcon.parent0] + "->" + levelTitle;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        holder.iconDir.setText(dir);
    }

    @NonNull
    private String[] getLevel1IconLabels() {
        String[] level1Icons = IconFactory.getL1Icons(
                getIconDirectory(mContext),
                LanguageFactory.getCurrentLanguageCode(mContext)
        );

        Icon[] level1IconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(mContext),
                IconFactory.removeFileExtension(level1Icons)
        );

        return TextFactory.getDisplayText(level1IconObjects);
    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
    private String[] getIconTitleLevel2(int pos)
    {
        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(mContext),
                IconFactory.removeFileExtension(IconFactory.getAllL2Icons(
                        PathFactory.getIconDirectory(mContext),
                        LanguageFactory.getCurrentLanguageCode(mContext),
                        getLevel2_3IconCode(pos)
                ))
        );

        return TextFactory.getDisplayText(iconObjects);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    private String getLevel2_3IconCode(int level1Position){
        if(level1Position+1 <= 9){
            return "0" + Integer.toString(level1Position+1);
        } else {
            return Integer.toString(level1Position+1);
        }
    }

}
