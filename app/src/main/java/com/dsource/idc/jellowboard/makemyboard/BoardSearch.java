package com.dsource.idc.jellowboard.makemyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.dsource.idc.jellowboard.Nomenclature;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowboard.makemyboard.utility.IconDatabase;
import com.dsource.idc.jellowboard.makemyboard.adapters.BoardSearchAdapter;
import com.dsource.idc.jellowboard.makemyboard.models.Board;
import com.dsource.idc.jellowboard.makemyboard.models.IconModel;
import com.dsource.idc.jellowboard.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowboard.utility.IconDataBaseHelper;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.LanguageHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.dsource.idc.jellowboard.makemyboard.RepositionIcons.BOARD_ID;

public class BoardSearch extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private BoardSearchAdapter adapter;
    private ArrayList<JellowIcon> iconList;
    public static final String SEARCH_MODE = "search_mode";
    public static final String NORMAL_SEARCH = "normal_search";
    public static final String ICON_SEARCH = "icon_search";
    public static final String SEARCH_IN_BOARD = "board_search";
    private Board currentBoard;
    private String Mode;
    private EditText searchBox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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
                    currentBoard = new BoardDatabase(this).getBoardById(getIntent().getStringExtra(BOARD_ID));
                    searchInBoard(currentBoard);

                }
                    break;
                case ICON_SEARCH:
                    getWindow().setGravity(Gravity.CENTER);
                    searchForIcon();
                    break;
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

    private void searchForIcon() {
        final IconDataBaseHelper iconDatabase=new IconDataBaseHelper(this);


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
                    JellowIcon noIconFound=new JellowIcon(getString(R.string.not_found),"NULL",-1,-1,-1);
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
                String name = Nomenclature.getIconName(icon.parent0,icon.parent1,icon.parent2,BoardSearch.this);
                if(icon.parent0!=-1) {
                    Intent returnIntent = new Intent();
                    ImageView iconImage = view.findViewById(R.id.search_icon_drawable);
                    Bitmap bitmap = ((BitmapDrawable)iconImage.getDrawable()).getBitmap();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                    byte[] bitmapArray = bos.toByteArray();
                    returnIntent.putExtra(getString(R.string.search_result),bitmapArray);
                    returnIntent.putExtra("result",name);
                    Log.d("ResultOkay",name);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
                else setResult(Activity.RESULT_CANCELED);
            }
        });

    }

    private void searchInBoard(Board currentBoard) {
        if(currentBoard!=null)
        {
            IconModel  model = currentBoard.getBoardIconModel();
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
                    Log.d("SearchQuery","ListSize "+iconList.size());
                    //when no Icon matches the searched string then "Icon not found" condition occurs
                    if(iconList.size()==0)
                    {
                        JellowIcon noIconFound=new JellowIcon(getString(R.string.not_found),"NULL",-1,-1,-1);
                        iconList.add(noIconFound);
                    }

                    Log.d("SearchQuery","AListSize "+iconList.size());
                    //List should have atleast one Item
                    if(iconList.size()>0) {
                        adapter = new BoardSearchAdapter(BoardSearch.this,iconList);
                        adapter.setOnItemClickListner(new BoardSearchAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                if(!iconList.get(position).IconTitle.equals(getString(R.string.not_found)))
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
        final IconDatabase iconDatabase=new IconDatabase(this);


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
                if(icon.parent0!=-1) {
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


