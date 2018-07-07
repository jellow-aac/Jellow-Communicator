package com.dsource.idc.jellowintl.makemyboard;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class BoardSearch extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BoardSearchAdapter adapter;
    private ArrayList<JellowIcon> iconList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setGravity(Gravity.LEFT);

            // Reference to the icon database to get access to the Icon list.
            final IconDataBaseHelper iconDatabase=new IconDataBaseHelper(this);

            // To Close on touch outside
            (findViewById(R.id.search_layout)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            EditText searchEditText
                    =findViewById(R.id.search_auto_complete);
            //Initialising the fields
            initFields();
            //Adding text watcher so that we can address dynamic text changes
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {


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
                        JellowIcon noIconFound=new JellowIcon(getString(R.string.not_found),"NULL",-1,-1,-1);
                        iconList.add(noIconFound);
                    }

                    //List should have atleast one Item
                    if(iconList.size()>0)
                        adapter.notifyDataSetChanged();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            adapter.setOnItemClickListner(new BoardSearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Intent returnIntent = new Intent();
                    JellowIcon icon=iconList.get(position);
                    if(icon.parent0!=-1) {
                        returnIntent.putExtra(getString(R.string.search_result), iconList.get(position));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    else setResult(Activity.RESULT_CANCELED);
                }
            });



        }
        @Override
        public void onResume() {
            super.onResume();
            if(!isAnalyticsActive()){
                throw new Error("unableToResume");
            }
            if(Build.VERSION.SDK_INT > 25 &&
                    !isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))) {
                startService(new Intent(getApplication(), JellowTTSService.class));
            }
            // Start measuring user app screen timer.
            startMeasuring();
        }

        @Override
        public void onPause() {
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
            adapter=new BoardSearchAdapter(this,iconList);
            mRecyclerView =findViewById(R.id.icon_search_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void attachBaseContext(Context newBase) {
            super.attachBaseContext((LanguageHelper.onAttach(newBase)));
        }



}


