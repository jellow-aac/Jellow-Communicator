package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
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
public class SearchActivity extends AppCompatActivity {

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

        EditText SearchEditText=findViewById(R.id.search_auto_complete);
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
                    JellowIcon noIconFound=new JellowIcon(getString(R.string.not_found),"NULL",-1,-1,-1);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, new SessionManager(this).getCaregiverNumber().substring(1));
        }
        if(Build.VERSION.SDK_INT > 25 &&
                !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
            startService(new Intent(getApplication(), JellowTTSService.class));
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
        SessionManager session = new SessionManager(this);
        long sessionTime = validatePushId(session.getSessionCreatedAt());
        session.setSessionCreatedAt(sessionTime);

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
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
        public ViewHolder(View v) {
            super(v);
            iconImage =v.findViewById(R.id.search_icon_drawable);
            iconTitle = v.findViewById(R.id.search_icon_title);
            iconDir = v.findViewById(R.id.parent_directory);
            speakIcon=v.findViewById(R.id.speak_button);
            speakIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    speakSpeech(mDataSource.get(getAdapterPosition()).IconTitle);
                    //Firebase event to log the "SearchBar" event with
                    // "IconSpeak" parameter.
                    Bundle bundle = new Bundle();
                    bundle.putString("IconSpeak",mDataSource.get(getAdapterPosition()).IconTitle);
                    bundle.putString("IconOpened", "");
                    bundle.putString("IconNotFound", "");
                    bundleEvent("SearchBar", bundle);
                }
            });
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
                bundle.putString("IconOpened", icon.IconTitle);
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
            activityIntent=new Intent(mContext,MainActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.search_parent_0),icon.parent0);
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));
        }
        else if(icon.parent0!=-1&&icon.parent1!=-1&&icon.parent2==-1)//Level 2
        {
            activityIntent=new Intent(mContext,LevelTwoActivity.class);
            activityIntent.putExtra(mContext.getString(R.string.level_one_intent_pos_tag),icon.parent0);
            //Passing the index of the icon to be highlighted.
            activityIntent.putExtra(mContext.getString(R.string.search_parent_1),icon.parent1);
            activityIntent.putExtra(menuPath,getActionBarTitle(icon.parent0));
            activityIntent.putExtra(mContext.getString(R.string.from_search),mContext.getString(R.string.search_tag));

        }
        else //Level 3
        {
            activityIntent=new Intent(mContext,LevelThreeActivity.class);
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
        String[] tempTextArr = mContext.getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
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
        if(thisIcon.parent1==-1)
        {
            TypedArray mArray=mContext.getResources().obtainTypedArray(R.array.arrLevelOneIconAdapter);
            holder.iconImage.setImageDrawable(mArray.getDrawable(thisIcon.parent0));
        }
        else
        {
            SessionManager mSession = new SessionManager(mContext);
            File en_dir = mContext.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/drawables";
            GlideApp.with(mContext)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(holder.iconImage);
        }

        /*
        * Adding the directory hint in the search list item
        * To this, we are using the function same as in menuSelected path used
        * */

        String[] arr=mContext.getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
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
            String levelTitle = getIconTitleLevel2(thisIcon.parent0)[thisIcon.parent1].
                    replace("…", "");
            dir=arr[thisIcon.parent0] + "->" + levelTitle;

        }
        holder.iconDir.setText(dir);
    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
    private String[] getIconTitleLevel2(int pos)
    {
        String arr[]=null;
        switch (pos)
        {
            case 0:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoGreetFeelAdapterText);
                break;
            case 1:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoDailyActAdapterText);
                break;
            case 2:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoEatAdapterText);
                break;
            case 3:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoFunAdapterText);
                break;
            case 4:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoLearningAdapterText);
                break;
            case 5:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterText);
                break;
            case 6:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterText);
                break;
            case 7:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoTimeWeatherAdapterText);
                break;
            case 8:arr=mContext.getResources().getStringArray(R.array.arrLevelTwoHelpAdapterText);
                break;
            default:
        }


        return arr;
    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    /**
     * <p>This function will send speech output request to
     * {@link com.dsource.idc.jellowintl.utility.JellowTTSService} Text-to-speech Engine.
     * The string in {@param speechText} is speech output request string.</p>
     * */

    private void speakSpeech(String speechText){
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText.toLowerCase());
        mContext.sendBroadcast(intent);
    }
}
