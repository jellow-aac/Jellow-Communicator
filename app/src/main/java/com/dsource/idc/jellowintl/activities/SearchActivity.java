package com.dsource.idc.jellowintl.activities;

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

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.TalkBack.TalkbackHints_SingleClick;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.IconDatabaseFacade;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

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

    private SearchViewIconAdapter adapter;
    private ArrayList<JellowIcon> iconList;
    //This variable holds the event "Icon not found"
    private boolean iconNotFound=false;
    //This variable holds the text that user has searched
    private String notFoundIconText="Null";

    private int beforeTextChanged;
    private int afterTextChanged;
    private IconDatabaseFacade database;
    private boolean firedEvent=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        database = new IconDatabaseFacade(getSession().getLanguage(), getAppDatabase());
        EditText searchEditText = findViewById(R.id.search_auto_complete);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            findViewById(R.id.close_button).setVisibility(View.GONE);
        }
        searchEditText.setHint(getString(R.string.enter_icon_name));
        getWindow().setGravity(Gravity.LEFT);

        // To Close on touch outside
        (findViewById(R.id.parent)).setOnClickListener(new View.OnClickListener() {
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
        searchEditText.addTextChangedListener(new TextWatcher() {
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
                ArrayList<JellowIcon> icon  = database.query(query.concat("%"));
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
                    JellowIcon noIconFound=new JellowIcon(getResources().getString(R.string.icon_not_found),"NULL",-1,-1,-1);
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

    public void closeSearchBar(View v){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer.
        startMeasuring();
        setNavigationUiConditionally();
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
        adapter=new SearchViewIconAdapter(this,iconList, getAppDatabase());
        RecyclerView mRecyclerView = findViewById(R.id.icon_search_recycler_view);
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
        ImageView iconImage;
        public TextView iconTitle;
        TextView iconDir;
        ImageView speakIcon;
        LinearLayout llSearchParent;
        ViewHolder(View v) {
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
                    ((SearchActivity)mContext).speak(mDataSource.get(getAdapterPosition()).getIconSpeech());
                    //Firebase event to log the "SearchBar" event with
                    // "IconSpeak" parameter.
                    Bundle bundle = new Bundle();
                    bundle.putString("IconSpeak",mDataSource.get(getAdapterPosition()).getIconTitle());
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
            if(icon.getLevelOne()==-1)//No icon found
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
                bundle.putString("IconOpened", icon.getIconTitle());
                bundle.putString("IconNotFound", "");
                bundleEvent("SearchBar", bundle);
                target.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mContext.startActivity(target);
                
            }
        }
    }

/**
 * This code is to get activity from SearchIcon Class object
 * */
    String menuPath;
    private Intent getActivityFromIcon(JellowIcon icon) {
        menuPath=mContext.getString(R.string.intent_menu_path_tag);
        Intent activityIntent=null;

        if(icon.getLevelTwo()==-1&&icon.getLevelThree()==-1)//Level 1
        {
            activityIntent=new Intent(mContext, MainActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.search_parent_0),icon.getLevelOne());
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
        }
        else if(icon.getLevelOne()!=-1&&icon.getLevelTwo()!=-1&&icon.getLevelThree()==-1)//Level 2
        {
            activityIntent=new Intent(mContext, LevelTwoActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),icon.getLevelOne());
            //Passing the index of the icon to be highlighted.
            activityIntent.putExtra(mContext.getString(R.string.search_parent_1),icon.getLevelTwo());
            //activityIntent.putExtra(menuPath,getActionBarTitle(icon.getLevelOne()));
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
            String breadCrumbPath = mContext.getString(R.string.home) + "/ " +
                    getLevel1IconLabels()[icon.getLevelOne()].replace("…","") + "/ ";
            activityIntent.putExtra(mContext.getString(R.string.intent_menu_path_tag), breadCrumbPath);

        }
        else if (!icon.isSequenceIcon())//Level 3
        {
            activityIntent=new Intent(mContext, LevelThreeActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
            //Level 1 parent
            activityIntent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),icon.getLevelOne());
            //Level 2 parent
            activityIntent.putExtra(mContext.getString(R.string.level_2_item_pos_tag),icon.getLevelTwo());
            //Level 3 Icon reference to be highlighted
            activityIntent.putExtra(mContext.getString(R.string.search_parent_2),icon.getLevelThree());
            String breadCrumbPath =  mContext.getString(R.string.home) + "/ " +
                    getLevel1IconLabels()[icon.getLevelOne()].replace("…","") + "/ "
                    + getIconTitleLevel2(icon.getLevelOne())[icon.getLevelTwo()].replace("…","")+"/ ";
            activityIntent.putExtra(mContext.getString(R.string.intent_menu_path_tag), breadCrumbPath);
        }else{ //Level 3, Sequence
            activityIntent=new Intent(mContext, SequenceActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
            //Level 2 parent
            activityIntent.putExtra(mContext.getString(R.string.level_2_item_pos_tag),icon.getLevelTwo());
            //Level 3 Icon reference to be highlighted
            activityIntent.putExtra(mContext.getString(R.string.search_parent_2),icon.getLevelThree());
            String breadCrumbPath =  mContext.getString(R.string.home) + "/ " +
                    getLevel1IconLabels()[icon.getLevelOne()].replace("…","") + "/ "
                    + getIconTitleLevel2(icon.getLevelOne())[icon.getLevelTwo()].replace("…","")+"/ ";
            activityIntent.putExtra(mContext.getString(R.string.intent_menu_path_tag), breadCrumbPath);
        }
        return activityIntent;
    }

    public IconDatabaseFacade database;
    public SearchViewIconAdapter(Context context, ArrayList<JellowIcon> items, AppDatabase appDatabase) {
        mContext = context;
        mDataSource = items;
        database=new IconDatabaseFacade(new SessionManager(mContext).getLanguage(), appDatabase);
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

        //If the "No icon found" condition comes then remove speaker
       if(thisIcon.getLevelOne()==-1)
       {
           holder.iconTitle.setText(R.string.icon_not_found);
           holder.speakIcon.setVisibility(View.GONE);
           holder.iconDir.setVisibility(View.GONE);
           holder.iconImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_icon_not_found));
           return;
       }else {
          GlideApp.with(mContext)
                   .load(getIconPath(mContext,thisIcon.getIconDrawable())+".png")
                   .diskCacheStrategy(DiskCacheStrategy.NONE)
                   .skipMemoryCache(false)
                   .centerCrop()
                   .dontAnimate()
                   .into(holder.iconImage);

           holder.speakIcon.setVisibility(View.VISIBLE);
           holder.iconDir.setVisibility(View.VISIBLE);
       }

       holder.iconTitle.setText(thisIcon.getIconTitle());



        /*
        * Adding the directory hint in the search list item
        * To this, we are using the function same as in menuSelected path used
        * */


      String[] arr = getLevel1IconLabels();

          for(int i=0;i<arr.length;i++){
            arr[i] = arr[i].split("…")[0];
        }

        String dir="";
        if(thisIcon.getLevelTwo()==-1)
        {
            dir=mContext.getResources().getString(R.string.home);
        }
        else if(thisIcon.getLevelThree()==-1)
        {
            dir=arr[thisIcon.getLevelOne()];
        }
        else {
            try {
                String levelTitle = getIconTitleLevel2(thisIcon.getLevelOne())[thisIcon.getLevelTwo()].
                        replace("…", "");
                dir=arr[thisIcon.getLevelOne()] + "->" + levelTitle;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        holder.iconDir.setText(dir);
    }

    @NonNull
    private String[] getLevel1IconLabels() {
        ArrayList<String> list = database.getLevelOneIconsTitles();
        return list.toArray(new String[list.size()]);
    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
    private String[] getIconTitleLevel2(int pos)
    {
        ArrayList<String> list = database.getLevelTwoIconsTitles(pos);
        if(list!=null&&list.size()>0)
            list.remove(0);
        return list.toArray(new String[list.size()]);
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }
}
