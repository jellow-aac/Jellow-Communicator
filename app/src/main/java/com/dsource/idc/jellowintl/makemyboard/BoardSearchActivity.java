package com.dsource.idc.jellowintl.makemyboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.makemyboard.adapters.BoardSearchAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.databases.IconDatabase;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class BoardSearchActivity extends SpeechEngineBaseActivity {
    public static final String BASE_ICON_SEARCH = "base_icon_search";
    private RecyclerView mRecyclerView;
    private BoardSearchAdapter adapter;
    private ArrayList<JellowIcon> iconList;
    public static final String SEARCH_MODE = "search_mode";
    public static final String NORMAL_SEARCH = "normal_search";
    public static final String ICON_SEARCH = "icon_search";
    public static final String SEARCH_IN_BOARD = "board_search";
    private BoardModel currentBoard;
    private String Mode;
    private EditText searchBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setGravity(Gravity.LEFT);
        setContentView(R.layout.activity_search);
        EditText SearchEditText = findViewById(R.id.search_auto_complete);
        currentBoard = new BoardDatabase(this).getBoardById(getIntent().getStringExtra(BOARD_ID));
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            SearchEditText.setContentDescription("Enter to search");
            findViewById(R.id.close_button).setVisibility(View.GONE);
        } else {
            SearchEditText.setHint("Search icon..");
        }
        getWindow().setGravity(Gravity.LEFT);

        initFields();
        Mode = getIntent().getStringExtra(SEARCH_MODE);
        if(Mode!=null)
        {
            switch (Mode) {
                case NORMAL_SEARCH:
                    normalSearch();
                    break;
                case SEARCH_IN_BOARD:
                {
                    searchInBoard(currentBoard);
                }
                    break;
                case ICON_SEARCH:
                    searchForIcon();
                    break;
                case BASE_ICON_SEARCH:
                    searchInBaseDatabase();
            }
        }



        //Initialising the fields

        // To Close on touch outside
        (findViewById(R.id.search_layout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        }

    private void searchInBaseDatabase() {
        final IconDatabase database =new IconDatabase(getSession().getLanguage(), getAppDatabase());
        EditText searchEditText =findViewById(R.id.search_auto_complete);

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

                //Clear the current list
                iconList.clear();
                ArrayList<JellowIcon>  icon = database.query(query+"%");
                //Prepare the list
                if(icon!=null&&icon.size()>0)
                    iconList.addAll(icon);
                //Prepare the list
                if(iconList.size()==0)
                {
                    JellowIcon noIconFound=new JellowIcon(getResources().getString(R.string.icon_not_found),"NULL",-1,-1,-1);
                    iconList.add(noIconFound);
                }

                //List should have at least one Item
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

                JellowIcon icon=iconList.get(position);
                if(icon.getParent0()!=-1) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(getString(R.string.search_result),icon.getIconDrawable());
                    returnIntent.putExtra("result",icon.getIconDrawable());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else setResult(Activity.RESULT_CANCELED);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setVisibleAct(BoardSearchActivity.class.getSimpleName());
    }

    private void searchForIcon() {
        final IconDatabase iconDatabase=new IconDatabase(currentBoard.getLanguage(), getAppDatabase());


        EditText searchEditText
                =findViewById(R.id.search_auto_complete);

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
                    iconList.addAll(icon);
                //when no Icon matches the searched string then "Icon not found" condition occurs
                assert icon != null;
                if(icon.size()==0)
                {
                    JellowIcon noIconFound=new JellowIcon("Icon not found","NULL",-1,-1,-1);
                    iconList.add(noIconFound);
                }

                //List should have at least one Item
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

                JellowIcon icon=iconList.get(position);
                if(icon.getParent0()!=-1) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(getString(R.string.search_result),icon.getIconDrawable());
                    returnIntent.putExtra("result",icon.getIconDrawable());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else setResult(Activity.RESULT_CANCELED);
            }
        });

    }

    private void searchInBoard(final BoardModel currentBoard) {
        if(currentBoard!=null)
        {
            IconModel model = currentBoard.getIconModel();
            final ModelManager modelManager  = new ModelManager(this,model);
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Getting the string to search in the database
                    String query=s.toString().trim();
                    iconList.clear();
                    iconList = modelManager.searchIconsForText(query);
                    //when no Icon matches the searched string then "Icon not found" condition occurs
                    if(iconList.size()==0)
                    {
                        JellowIcon noIconFound=new JellowIcon(getString(R.string.not_found),"NULL",-1,-1,-1);
                        iconList.add(noIconFound);
                    }

                    Log.d("SearchQuery","AListSize "+iconList.size());
                    //List should have at least one Item
                    if(iconList.size()>0) {
                        adapter = new BoardSearchAdapter(BoardSearchActivity.this,
                                iconList,currentBoard.getLanguage(), getAppDatabase());
                        adapter.setOnItemClickListner(new BoardSearchAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(!iconList.get(position).getIconTitle().equals(getString(R.string.not_found)))
                                {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra(getString(R.string.search_result), iconList.get(position));
                                    setResult(Activity.RESULT_OK, returnIntent);
                                    finish();
                                }
                                else{
                                    Intent returnIntent = new Intent();
                                    setResult(Activity.RESULT_CANCELED, returnIntent);
                                    finish();
                                }

                            }
                        });
                        mRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });





        }

    }

    private void normalSearch() {
        // Reference to the icon database to get access to the Icon list.
        final IconDatabase iconDatabase=new IconDatabase(currentBoard.getLanguage(), getAppDatabase());


        EditText searchEditText
                =findViewById(R.id.search_auto_complete);

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
                 * {@link IconDatabase.query(String)} returns a {@link ArrayList< JellowIcon >}  object
                 * having all the JellowIcon matching the database
                 * */
                ArrayList<JellowIcon> icon  =iconDatabase.query(query);
                //Clear the current list
                iconList.clear();
                //Prepare the list
                if(icon!=null&&icon.size()>0)
                    iconList.addAll(icon);
                //when no Icon matches the searched string then "Icon not found" condition occurs
                assert icon != null;
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
                if(icon.getParent0()!=-1) {
                    returnIntent.putExtra(getString(R.string.search_result), iconList.get(position));
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else setResult(Activity.RESULT_CANCELED);
            }
        });



    }

    private void initFields() {
            searchBox = findViewById(R.id.search_auto_complete);
            iconList=new ArrayList<>();
            if(currentBoard!=null)
                adapter=new BoardSearchAdapter(this,iconList,currentBoard.getLanguage(), getAppDatabase());
            else
                adapter=new BoardSearchAdapter(this,iconList, getSession().getLanguage(), getAppDatabase());
            mRecyclerView =findViewById(R.id.icon_search_recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
    }

    //TODO @Ayaz INFO: Added this function to get close functionality on search bar. Please remove this todo once you verify the effect of this code.
    public void closeSearchBar(View view) {
        finish();
    }
}


