package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.IconModel;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.MyBoards.BOARD_ID;

public class AddEditIconAndCategory extends AppCompatActivity implements View.OnClickListener {

    private static final int ADD_CATEGORY = 212;
    private static final int ADD_ICON = 210;
    private static final int EDIT_ICON = 211;
    private String boardId;
    private BoardDatabase database;
    private Board currentBoard;
    private IconModel boardModel;
    private ModelManager modelManager;
    private RecyclerView categoryRecycler;
    private RecyclerView iconRecycler;
    private ArrayList<String> categories;
    private CategoryManager categoryManager;
    private LevelSelectorAdapter categoryAdapter;
    private IconSelectorAdapter iconAdapter;
    private int previousSelection = 0;
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener;
    private ArrayList<JellowIcon> iconList;
    private ViewTreeObserver.OnGlobalLayoutListener populationListener;
    private RelativeLayout addCategory,addIcon,editIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Select icons"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));

        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }
        database=new BoardDatabase(this);
        currentBoard=database.getBoardById(boardId);
        boardModel = currentBoard.getBoardIconModel();
        modelManager =new ModelManager(this,boardModel);
        categoryManager = new CategoryManager(boardModel);
        categories = categoryManager.getAllCategories();
        iconList = new ArrayList<>();
        iconList = categoryManager.getAllChildOfCategory(0);
        initViews();






    }

    private void initViews() {

        categoryRecycler = findViewById(R.id.level_select_pane_recycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        prepareLevelSelectPane();

        iconRecycler = findViewById(R.id.icon_select_pane_recycler);
        iconRecycler.setLayoutManager(new GridLayoutManager(this,gridSize()));
        iconAdapter = new IconSelectorAdapter(this,
                categoryManager.getAllChildOfCategory(0),
                IconSelectorAdapter.ADD_EDIT_ICON_MODE);
        iconRecycler.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
        /*
            Hiding unwanted views of the layout
         */
        ((TextView)findViewById(R.id.reset_selection)).setText("Skip");
        findViewById(R.id.reset_selection).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEditIconAndCategory.this,EditBoard.class);
                intent.putExtra(BOARD_ID,boardId);
                startActivity(intent);
            }
        });
        findViewById(R.id.select_deselect_check_box).setVisibility(View.GONE);
        findViewById(R.id.icon_count).setVisibility(View.GONE);

        addCategory = findViewById(R.id.add_category);
        addIcon = findViewById(R.id.add_icon);
        editIcon = findViewById(R.id.edit_icon);
        addCategory.setOnClickListener(this);
        editIcon.setOnClickListener(this);
        addIcon.setOnClickListener(this);
    }


    /**
     * Creates and fetches the left pane for icon select
     */
    private void prepareLevelSelectPane() {

        categoryAdapter =new LevelSelectorAdapter(this,categories);
        categoryRecycler.setAdapter(categoryAdapter);

        layoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setSelection(previousSelection, null);
            }
        };

        categoryRecycler.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        categoryAdapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if(previousSelection!=position) {
                            setSelection(position, view);
                            prepareIconPane(position);
                }
            }
        });

    }

    private void prepareIconPane(int position) {
        iconList = categoryManager.getAllChildOfCategory(position);
        iconAdapter = new IconSelectorAdapter(this,iconList,IconSelectorAdapter.ADD_EDIT_ICON_MODE);
        iconRecycler.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
    }

    private ViewTreeObserver.OnGlobalLayoutListener tempListener;
    private void setSelection(final int position, final View view) {
                if(view!=null) {

                    View newSelection=categoryRecycler.getChildAt(position);
                    ((TextView)newSelection.findViewById(R.id.icon_title)).setTextColor(getResources().getColor(R.color.colorIntro));
                    view.setBackgroundColor(getResources().getColor(R.color.colorIntroSelected));
                    View prevSelection=categoryRecycler.getChildAt(previousSelection);
                    prevSelection.setBackgroundColor(getResources().getColor(R.color.colorIntro));
                    ((TextView)prevSelection.findViewById(R.id.icon_title)).setTextColor(getResources().getColor(R.color.level_select_text_color));

                }
                else {
                    View s=categoryRecycler.getChildAt(0);
                    s.setBackgroundColor(getResources().getColor(R.color.colorIntroSelected));
                    ((TextView)s.findViewById(R.id.icon_title)).setTextColor(getResources().getColor(R.color.colorIntro));
                    categoryRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);

                }

        previousSelection =position;
    }


    private int gridSize()
    {
        int gridSize=6;
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            gridSize=8;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            gridSize=6;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            gridSize=4;
        }

        return gridSize;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_edit_icon_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_edit_icons:
                if(findViewById(R.id.add_edit_icon_option).getVisibility()==View.VISIBLE)
                    findViewById(R.id.add_edit_icon_option).setVisibility(View.GONE);
                else findViewById(R.id.add_edit_icon_option).setVisibility(View.VISIBLE);
                break;
            case android.R.id.home: finish(); break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int mode = -1;
        if(v ==addCategory)
            mode = ADD_CATEGORY;
        else if(v==addIcon)
            mode = ADD_ICON;
        else if(v==editIcon)
            mode = EDIT_ICON;
        showCustomDialog(mode);

    }

    private void showCustomDialog(int mode) {
        if(mode!=-1)
        {
            CustomDialog customDialog = new CustomDialog(this);



        }
    }

    private class CategoryManager
    {
        private IconModel model;


        private CategoryManager(IconModel model) {
            this.model = model;
        }

        private ArrayList<String> getAllCategories()
        {
            ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<model.getChildren().size();i++)
                list.add(model.getChildren().get(i).getIcon().IconTitle);
            return list;
        }

        private ArrayList<JellowIcon> getAllChildOfCategory(int categoryIndex)
        {
            ArrayList<JellowIcon> iconList  = new ArrayList<>();
            IconModel thisCategory =  model.getChildren().get(categoryIndex);
            //LevelTwo Icons
            ArrayList<IconModel> list = new ArrayList<>(thisCategory.getChildren());

            //Fetching Level three icons of the category.
            for(int i=0;i<thisCategory.getChildren().size();i++)
                list.addAll(thisCategory.getChildren().get(i).getChildren());
            for(int i = 0;i<list.size();i++)
                iconList.add(list.get(i).getIcon());

            return iconList;
        }

    }
}
