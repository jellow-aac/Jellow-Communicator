package com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.AddEditIconAndCategoryActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardLanguageHelper;
import com.dsource.idc.jellowintl.makemyboard.BoardListActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardSearchActivity;
import com.dsource.idc.jellowintl.makemyboard.adapters.SimpleArrayAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.databases.IconDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iPresenterIconManager;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.IconListManager;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.LevelManager;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SearchManager;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.SelectionManager;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.makemyboard.utility.UtilFunctions;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.IS_EDIT_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.SEARCH_CODE;

public class IconSelectActivity extends BaseActivity implements iPresenterIconManager {


    private static final String CURRENT_POSITION = "current_position";
    private static final String LIST_OF_ICON = "list_of_icons";
    private String boardId;
    private boolean isEditMode;
    private int previousSelection=0;
    private BoardDatabase boardDatabase;
    private BoardModel currentBoard;
    private LevelManager levelManager;
    private SelectionManager selectionManager;
    private IconListManager iconListManager;
    private SearchManager searchManager;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
        selectionManager = SelectionManager.getInstance();
        manager = new SessionManager(this);
        try{
            if(getIntent().getExtras()!=null)
                boardId =getIntent().getExtras().getString(BOARD_ID);
            if(getIntent().getExtras().getString(IS_EDIT_MODE)!=null)
                if(getIntent().getExtras().getString(IS_EDIT_MODE).equals("YES"))
                    isEditMode  = true;
        }
        catch (NullPointerException e) {
            return;
        }
        if(savedInstanceState!=null) {
            ArrayList<JellowIcon> selectedIconList  = (ArrayList<JellowIcon>) savedInstanceState.getSerializable(LIST_OF_ICON);
            selectionManager.setList(selectedIconList);
            previousSelection = savedInstanceState.getInt(CURRENT_POSITION);
        }

        boardDatabase = new BoardDatabase(this);
        currentBoard = boardDatabase.getBoardById(boardId);

        if(getSupportActionBar()!=null) {
            //enableNavigationBack();
            getSupportActionBar().setTitle(R.string.select_icon_title);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            //TODO Check color to keep or remove.
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        }
        initViews();
    }

    private void setupLevelSelect() {
        levelManager= new LevelManager((RecyclerView) (findViewById(R.id.level_select_pane_recycler)), this, new GenCallback<Integer>() {
            @Override
            public void callBack(Integer position) {
                selectionUpdated(position);
                invalidateOptionsMenu();
                searchManager.clear();
                if(!levelManager.hideFilter())
                    prepareFilterMenu();
            }
        }).setList(getLevelPaneList());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(BoardLanguageHelper.getInstance().changeLanguage(newBase));
    }

    private ArrayList<String> getLevelPaneList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.current_board));
        list.addAll(new IconDatabase(currentBoard.getLanguage(), getAppDatabase()).getLevelOneIconsTitles());
        return list;
    }

    private void selectionUpdated(Integer position) {
        iconListManager.updateIcons(position,-1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setVisibleAct(IconSelectActivity.class.getSimpleName());
        //BoardLanguageHelper.getInstance().changeLanguage(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // LanguageHelper.onAttach(this);
    }

    /**
     * Initializes all the views of the layout
     */
    private void initViews() {
        setupLevelSelect();
        prepareIconPane();
        findViewById(R.id.touch_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.touch_area).setVisibility(View.GONE);
            }
        });
        searchManager = new SearchManager(iconListManager.getRecycler(),this);

    }

    private void prepareIconPane() {
       iconListManager  = new IconListManager(findViewById(R.id.right_icon_select_pane),
               this, currentBoard.getLanguage(),this,isEditMode, getAppDatabase());
       if(isEditMode&&currentBoard!=null)
           iconListManager.setCurrentBoardIcons(currentBoard.getIconModel().getAllIcons());
    }


    /**
     * This function saves the current state of the application, including list of Icon Selected and previous position,
     * this prevents the loss of iconList during screen orientation change and device lock.
     * @param outState current state of the activity
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(LIST_OF_ICON,SelectionManager.getInstance().getList());
        outState.putInt(CURRENT_POSITION,previousSelection);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_board_menu, menu);
        if(levelManager.hideFilter())
            menu.findItem(R.id.filter).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            onBackPressed();
        else if(item.getItemId()==R.id.filter)
            showFilter();
        else if(item.getItemId()==R.id.search) {
            searchManager.clear();
            Intent searchIntent  =  new Intent(this, BoardSearchActivity.class);
            searchIntent.putExtra(BOARD_ID,currentBoard.getBoardId());
            searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.NORMAL_SEARCH);
            startActivityForResult(searchIntent,SEARCH_CODE);
        }

        return super.onOptionsItemSelected(item);
    }

    private void showFilter(){
        searchManager.clear();
        View view = findViewById(R.id.touch_area);
        findViewById(R.id.filter_menu).setVisibility(View.VISIBLE);
        if(view.getVisibility()==View.VISIBLE) findViewById(R.id.touch_area).setVisibility(View.GONE);
        else findViewById(R.id.touch_area).setVisibility(View.VISIBLE);
    }

    private void prepareFilterMenu(){
        ArrayList<String> dropDownList = new IconDatabase(currentBoard.getLanguage(), getAppDatabase()).
                getLevelTwoIconsTitles(levelManager.getLevel());
        if(dropDownList !=null&& dropDownList.size()>1)
            dropDownList.remove(0);
        else return;
        ListView dropDown = findViewById(R.id.filter_menu);
        dropDown.setBackground(getResources().getDrawable(R.color.white));
        dropDown.setVisibility(View.VISIBLE);
        dropDown.bringToFront();
        dropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iconListManager.updateIcons((levelManager.getLevel()+1),position);
                showFilter();
            }
        });

        SimpleArrayAdapter simpleArrayAdapter=new SimpleArrayAdapter(this, dropDownList);
        dropDown.setAdapter(simpleArrayAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SEARCH_CODE&&resultCode==RESULT_OK)
        {
            if(data.getExtras()!=null) {
                JellowIcon icon = (JellowIcon) data.getExtras().getSerializable(getString(R.string.search_result));
                if (icon != null && !UtilFunctions.listContainsIcon(icon, SelectionManager.getInstance().getList())) {
                    SelectionManager.getInstance().getList().add(icon);
                    iconListManager.updateIcons(icon.getParent0()+1,-1);
                    searchManager.setFromSearch(icon);
                    levelManager.updateSelection((icon.getParent0()+1));
                    prepareFilterMenu();
                }else
                    Toast.makeText(this, "Icon already selected", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void onListUpdated() {
        if(searchManager.isFromSearch()){
            searchManager.addSearchedIcon(iconListManager.getPosition(searchManager.getIconToBeSearched()));
            searchManager.setFromSearch(null);
            searchManager.clear();
        }
    }

    @Override
    public void onItemSelected(JellowIcon icon, boolean isChecked) {
        searchManager.clear();
        if(isChecked) selectionManager.addIconToList(icon);
        else selectionManager.removeIconFromList(icon);
    }

    @Override
    public void onItemEdited() {

    }

    @Override
    public void onNextPressed() {
        searchManager.clear();
        if (!isEditMode) {
            currentBoard.setIconModel(
                    new ModelManager(sortList(SelectionManager.getInstance().getList()), this).
                            getModel());

            currentBoard.setSetupStatus(BoardModel.STATUS_L1);
            boardDatabase.updateBoardIntoDatabase(currentBoard);
            Intent intent =new Intent(this, AddEditIconAndCategoryActivity.class);
            intent.putExtra(BOARD_ID,boardId);
            startActivity(intent);
            SelectionManager.getInstance().delete();
            finish();
        }
        else{
            IconModel newModel = new ModelManager(sortList(SelectionManager.getInstance().getList()),this).getModel();
            IconModel currentBoardModel = currentBoard.getIconModel();
            currentBoardModel.appendNewModelToPrevious(newModel);
            currentBoard.setIconModel(currentBoardModel);
            boardDatabase.updateBoardIntoDatabase(currentBoard);
            Intent intent =new Intent(this, AddEditIconAndCategoryActivity.class);
            intent.putExtra(BOARD_ID,boardId);
            startActivity(intent);
            SelectionManager.getInstance().delete();
            finish();

        }
    }

    @Override
    public void onSelectionClear() {
        searchManager.clear();
        currentBoard.clearAllIcons();
        iconListManager.setCurrentBoardIcons(new ArrayList<JellowIcon>());
        SelectionManager.getInstance().getList().clear();
    }


    /**
     * This function sorts the Icon list as they are present in the hierarchy
     * @param iconsList list of the icon to be sorted.
     * @return returns the sorted list
     */
    ArrayList<JellowIcon> sortList(ArrayList<JellowIcon> iconsList) {
        IconDatabase iconDatabase = new IconDatabase(currentBoard.getLanguage(), getAppDatabase());
        ArrayList<JellowIcon> listOfAllIcon=iconDatabase.getAllIcons();
        ArrayList<JellowIcon> sortedList=new ArrayList<>();
        if(listOfAllIcon!=null)
            for(int i=0;i<listOfAllIcon.size();i++)
            {
                if(listContainsIcon(listOfAllIcon.get(i),iconsList))
                    sortedList.add(listOfAllIcon.get(i));
                if(sortedList.size()==iconsList.size())
                    break;
            }

        return sortedList;
    }

    /***
     * This function checks whether a icon is present in the list or not
     * @param icon Icon that is being checked
     * @param list list of all icons
     * @return boolean
     */
    public boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list) {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        return present;
    }

    @Override
    public void onBackPressed() {
        searchManager.clear();
        final CustomDialog dialog = new CustomDialog(this,CustomDialog.NORMAL,currentBoard.getLanguage());
        dialog.setText(getString(R.string.icon_select_exit_warning));
        dialog.setOnPositiveClickListener(new CustomDialog.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                SelectionManager.getInstance().delete();
                if(manager!=null) manager.setCurrentBoardLanguage("");
                startActivity(new Intent(IconSelectActivity.this, BoardListActivity.class));
                finish();
            }
        });
        dialog.setOnNegativeClickListener(new CustomDialog.OnNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                dialog.cancel();
            }
        });
        dialog.show();
    }
}
