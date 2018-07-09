package com.dsource.idc.jellowintl.makemyboard;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.BoardDatabase;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class IconSelectActivity extends AppCompatActivity {


    private static final int SEARCH_CODE = 121;
    RecyclerView levelSelecterRecycler;
    RecyclerView iconRecycler;
    IconSelectorAdapter iconSelectorAdapter;
    ArrayList<JellowIcon> iconList;
    ListView dropDown;
    ArrayList<String> dropDownList;
    public static ArrayList<JellowIcon> selectedIconList;
    LevelSelectorAdapter levelSelectorAdapter;
    CheckBox selectionCheckBox;
    int previousSelection=-1;
    UtilFunctions utilF;
    String boardId;
    public static final String BOARD_ID="Board_Id";
    ViewTreeObserver.OnGlobalLayoutListener tempListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);

        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {
            Log.d("No board id found", boardId);
        }



        utilF=new UtilFunctions();
        selectionCheckBox=findViewById(R.id.select_deselect_check_box);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        initViews();
        prepareLevelSelectPane();
        prepareIconPane(0,-1);
        dropDownList= getCurrentChildren();
        initNavBarButtons();



    }

    private void initNavBarButtons() {
        (findViewById(R.id.save_normally)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardDatabase database=new BoardDatabase(IconSelectActivity.this);
                Board board=database.getBoardById(boardId);
                if(board!=null) {
                    board.setIconList(selectedIconList);
                    database.updateBoardIntoDatabase(new DataBaseHelper(IconSelectActivity.this).getReadableDatabase(),board);
                    Intent intent = new Intent(IconSelectActivity.this, EditBoard.class);
                    intent.putExtra(BOARD_ID, boardId);
                    startActivity(intent);
                }
            }
        });

        (findViewById(R.id.reset_selection)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconList.clear();
                selectionCheckBox.setChecked(false);
                iconSelectorAdapter.notifyDataSetChanged();
                ((TextView)(findViewById(R.id.icon_count))).setText("("+selectedIconList.size()+")");
            }
        });

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

    /**
     * Add the icon to the list if not already present
     * @param icon
     */
    private void addIconToList(JellowIcon icon)
    {
        boolean alreadyPresent=false;
        for(int i=0;i<selectedIconList.size();i++)
            if(icon.isEqual(selectedIconList.get(i)))
                alreadyPresent=true;
        if(!alreadyPresent)
            selectedIconList.add(icon);
    }

    /**
     * removes the element from the current list
     * @param icon
     */
    private void removeIconFromList(JellowIcon icon)
    {
        int index=-1;
        for(int i=0;i<selectedIconList.size();i++)
            if(icon.isEqual(selectedIconList.get(i)))
                index=i;
        if(index!=-1)
            selectedIconList.remove(index);
    }

    /**
     * Initializes all the views of the layout
     */
    private void initViews() {

        iconList=new ArrayList<>();
        iconRecycler =findViewById(R.id.icon_select_pane_recycler);
        iconSelectorAdapter =new IconSelectorAdapter(this,iconList);
        dropDown= findViewById(R.id.filter_menu);
        iconRecycler.setLayoutManager(new GridLayoutManager(this,gridSize()));
        iconRecycler.setAdapter(iconSelectorAdapter);
        levelSelecterRecycler=findViewById(R.id.level_select_pane_recycler);
        levelSelecterRecycler.setLayoutManager(new LinearLayoutManager(this));
        selectedIconList=new ArrayList<>();

        selectionCheckBox.setOnCheckedChangeListener(null);
        selectionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectionCheckBox.isChecked())
                    selectAll(0);
                else selectAll(1);

            }
        });
    }

    /**
     * Code to implement select all/Deselect all capability
     * @param code
     */
    private void selectAll(int code) {
        if(code==0)
        {
            JellowIcon icon;
            for (int i = 0; i < iconList.size(); i++) {
                icon=iconList.get(i);
                boolean notAlreadyPresent=true;
                for(int j=0;j<selectedIconList.size();j++)
                    if(icon.isEqual(selectedIconList.get(j)))
                        notAlreadyPresent=false;

                if(notAlreadyPresent)
                addIconToList(iconList.get(i));
            }
        }
        else if(code==1)// Deselect current list
            for(int i=0;i<iconList.size();i++)
                removeIconFromList(iconList.get(i));

        iconSelectorAdapter.notifyDataSetChanged();
        ((TextView)(findViewById(R.id.icon_count))).setText("("+selectedIconList.size()+")");

    }

    /**
     * This function fetches the icon list matching the level postion and displays it to the
     * screen
     * @param level_1
     * @param level_2
     */
    private void prepareIconPane(int level_1,int level_2) {
        iconList=new IconDataBaseHelper(this).myBoardQuery(level_1,level_2);
        iconSelectorAdapter =new IconSelectorAdapter(this,iconList);
        iconRecycler.setAdapter(iconSelectorAdapter);
        iconSelectorAdapter.notifyDataSetChanged();
        selectionCheckBox.setChecked(utilF.getSelection(selectedIconList,iconList));
        iconSelectorAdapter.setOnItemClickListner(new IconSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean checked) {
                updateSelectionList(view,position,checked);

            }
        });
    }

    /**
     * This section updates the selection of the Icon
     * @param view
     * @param position
     * @param checked
     */
    private void updateSelectionList(View view, int position, boolean checked) {

        if(dropDown.getVisibility()==View.VISIBLE)
            dropDown.setVisibility(View.GONE);

        if(checked)
            addIconToList(iconList.get(position));
        else
            removeIconFromList(iconList.get(position));

        ((TextView)(findViewById(R.id.icon_count))).setText("("+selectedIconList.size()+")");
        Log.d("Selection: ","Selection List: "+selectedIconList.size()+" IconList: "+iconList.size()+" Selection: "+utilF.getSelection(selectedIconList,iconList));
        selectionCheckBox.setChecked(utilF.getSelection(selectedIconList,iconList));
    }




    /**
     * Creates and fetches the left pane for icon select
     */
    private void prepareLevelSelectPane() {
        ArrayList<String> levelSelectList=new ArrayList<>();
        String levelOne[]=getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        for(int i=0;i<levelOne.length;i++)
            levelSelectList.add(levelOne[i]);

        levelSelecterRecycler.hasFixedSize();
        levelSelectorAdapter =new LevelSelectorAdapter(this,levelSelectList);
        levelSelecterRecycler.setAdapter(levelSelectorAdapter);
        tempListener=new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setSelection(0,null);
            }
        };

        levelSelecterRecycler.getViewTreeObserver().addOnGlobalLayoutListener(tempListener);
        levelSelectorAdapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setSelection(position,view);
                prepareIconPane(position,-1);
                dropDownList= getCurrentChildren();
                if(dropDown.getVisibility()==View.VISIBLE)
                    dropDown.setVisibility(View.GONE);


            }
        });

    }

    private void setSelection(int position,View view) {

        if(view!=null) {

            View newSelection=levelSelecterRecycler.getChildAt(position);

            ((TextView)newSelection.findViewById(R.id.icon_title)).setTextColor(getResources().getColor(R.color.colorIntro));
            view.setBackgroundColor(getResources().getColor(R.color.colorIntroSelected));

            View prevSelection=levelSelecterRecycler.getChildAt(previousSelection);
            Log.d("Previons location","Loc: "+previousSelection+ "new "+position);
            prevSelection.setBackgroundColor(getResources().getColor(R.color.colorIntro));
            ((TextView)prevSelection.findViewById(R.id.icon_title)).setTextColor(getResources().getColor(R.color.light_grey));

        }
        else {
            View s=levelSelecterRecycler.getChildAt(0);
            s.setBackgroundColor(getResources().getColor(R.color.colorIntroSelected));
            ((TextView)s.findViewById(R.id.icon_title)).setTextColor(getResources().getColor(R.color.colorIntro));
            levelSelecterRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(tempListener);
        }
        previousSelection =position;

    }

    /**
     * To inflate menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.my_board_menu, menu);
        return true;
    }

    private void showSecondLevelMenu() {

        if(dropDown.getVisibility()==View.VISIBLE)
        {
            dropDown.setVisibility(View.GONE);
            return;
        }
        dropDown.setBackground(getResources().getDrawable(R.color.white));
        dropDown.setVisibility(View.VISIBLE);
        dropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prepareIconPane(previousSelection,position);
                dropDown.setVisibility(View.GONE);
            }
        });
        ArrayAdapter<String> dropDownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dropDownList);
        dropDown.setAdapter(dropDownAdapter);

    }

    private ArrayList<String> getCurrentChildren() {
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<iconList.size();i++)
        {
            JellowIcon icon=iconList.get(i);
            if(icon.parent2!=-1)
                break;
            if(icon.parent0==previousSelection&&icon.parent1!=-1)
                list.add(icon.IconTitle);
        }

        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                startActivityForResult(new Intent(this, BoardSearch.class),SEARCH_CODE);
                break;
            case R.id.filter:
                showSecondLevelMenu();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    /**
     * This function holds the onActivityResult from {@link BoardSearch} activity.
     * If user selects Icon that is already present in the list, then the list is not updated
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==SEARCH_CODE&&resultCode==RESULT_OK)
        {
            JellowIcon icon=(JellowIcon)data.getExtras().getSerializable(getString(R.string.search_result));
            if(icon!=null&&!utilF.listContainsIcon(icon,selectedIconList)) {
                selectedIconList.add(icon);
                ((TextView) (findViewById(R.id.icon_count))).setText("(" + selectedIconList.size() + ")");
                selectionCheckBox.setChecked(utilF.getSelection(selectedIconList, iconList));
                iconSelectorAdapter.notifyDataSetChanged();
            }
        }

    }
}
