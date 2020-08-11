package com.dsource.idc.jellowintl.make_my_board_module.activity;

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
import com.dsource.idc.jellowintl.activities.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.make_my_board_module.adapters.BoardSearchAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.BoardIconModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.BoardDatabase;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases.IconDatabaseFacade;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnItemClickListener;
import com.dsource.idc.jellowintl.make_my_board_module.managers.ModelManager;
import com.dsource.idc.jellowintl.make_my_board_module.models.BoardListModel;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.ENABLE_DROPDOWN_SPEAKER;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class BoardSearchActivity extends SpeechEngineBaseActivity {
    public static final String BASE_ICON_SEARCH = "base_icon_search";
    private RecyclerView mRecyclerView;
    private BoardSearchAdapter adapter;
    public static final String SEARCH_MODE = "search_mode";
    private ArrayList<JellowIcon> iconList;
    public static final String NORMAL_SEARCH = "normal_search";
    public static final String ICON_SEARCH = "icon_search";
    public static final String SEARCH_IN_BOARD = "board_search";
    public static final String SEARCH_FOR_BOARD = "search_the_board";
    private BoardModel currentBoard;
    private String Mode;
    private EditText searchBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        EditText SearchEditText = findViewById(R.id.search_auto_complete);
        currentBoard = new BoardDatabase(getAppDatabase()).getBoardById(getIntent().getStringExtra(BOARD_ID));
        initFields();
        Mode = getIntent().getStringExtra(SEARCH_MODE);
        if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
            findViewById(R.id.close_button).setVisibility(View.GONE);
        }
        SearchEditText.setHint(getString(R.string.enter_icon_name));
        getWindow().setGravity(Gravity.LEFT);
        if(Mode!=null)
        {
            switch (Mode) {
                case NORMAL_SEARCH:
                    setTitle(getString(R.string.search_icon_available_in_jellow));
                    normalSearch();
                    break;
                case SEARCH_IN_BOARD:
                    setTitle(getString(R.string.search_icon_the_board));
                    searchInBoard(currentBoard);
                    break;
                case ICON_SEARCH:
                    searchForIcon();
                    break;
                case BASE_ICON_SEARCH:
                    searchInBaseDatabase();
                    break;
                case SEARCH_FOR_BOARD:
                    setTitle(getString(R.string.search_board_screen));
                    SearchEditText.setHint(getString(R.string.enter_board_name_to_search));
                    searchForBoard();
                    break;
            }
        }

        //Initialising the fields
        // To Close on touch outside
        (findViewById(R.id.parent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        }

    private void searchInBaseDatabase() {
        final IconDatabaseFacade database =new IconDatabaseFacade(getSession().getLanguage(), getAppDatabase());
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

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

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
        // Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring(BoardSearchActivity.class.getSimpleName());
    }

    private void searchForBoard() {
        final BoardListModel blm = new BoardListModel(getAppDatabase());
        EditText searchEditText = findViewById(R.id.search_auto_complete);
        adapter.setSearchingBoardName(true);

        //Adding text watcher so that we can address dynamic text changes
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Getting the string to search in the database
                final String query=s.toString().trim();
                iconList.clear();
                ArrayList<BoardModel> boardModels = blm.getAllBoardsStartWithName(query+"%");
                //Prepare the list
                if(boardModels!=null&&boardModels.size()>0){
                    for (BoardModel bm : boardModels){
                        JellowIcon icon= new JellowIcon(bm.getBoardName(), bm.getBoardId(),-1,-1,-1);
                        iconList.add(icon);
                    }
                }else{
                        JellowIcon noIconFound=new JellowIcon(getResources().getString(R.string.board_not_found),"NULL",-1,-1,-1);
                        iconList.add(noIconFound);
                    }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                JellowIcon icon=iconList.get(position);
                if(!icon.getIconDrawable().isEmpty()) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra(getString(R.string.search_result),icon.getIconTitle());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else setResult(Activity.RESULT_CANCELED);
            }
        });
    }

    private void searchForIcon() {
        final IconDatabaseFacade iconDatabase=new IconDatabaseFacade(currentBoard.getLanguage(), getAppDatabase());


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

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(position==-1) {
                    setResult(Activity.RESULT_CANCELED);
                    return;
                }
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
            BoardIconModel model = currentBoard.getIconModel();
            final ModelManager modelManager  = new ModelManager(model);
            searchBox.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
                        if(getIntent().getBooleanExtra(ENABLE_DROPDOWN_SPEAKER,false)){
                            adapter.activateSearchDropdownSpeaker();
                        }
                        adapter.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
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
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    private void normalSearch() {
        // Reference to the icon database to get access to the Icon list.
        final IconDatabaseFacade iconDatabase=new IconDatabaseFacade(currentBoard.getLanguage(), getAppDatabase());


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
                 * {@link IconDatabaseFacade.query(String)} returns a {@link ArrayList< JellowIcon >}  object
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

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
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

    public void closeSearchBar(View view) {
        finish();
    }

    public void speakOnly(int position) {
        speakFromMMB(iconList.get(position).getIconSpeech());
    }
}


