package com.dsource.idc.jellowboard.makemyboard;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowboard.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowboard.makemyboard.UtilityClasses.IconDatabase;
import com.dsource.idc.jellowboard.makemyboard.UtilityClasses.ModelManager;
import com.dsource.idc.jellowboard.utility.CustomGridLayoutManager;
import com.dsource.idc.jellowboard.utility.JellowIcon;

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
    int previousSelection=0;
    UtilFunctions utilF;
    String boardId;
    public static final String BOARD_ID="Board_Id";
    ViewTreeObserver.OnGlobalLayoutListener tempListener;
    ViewTreeObserver.OnGlobalLayoutListener iconLayoutPopulationLister;
    private Button nextButton;
    private Button resetButton;
    private RecyclerView.OnScrollListener scrollListener;


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+getString(R.string.icon_select_text)+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        initViews();
        prepareLevelSelectPane();
        prepareIconPane(0,-1);
        dropDownList= getCurrentChildren();
        initNavBarButtons();
        nextButton.setEnabled(false);
        nextButton.setAlpha(.5f);
    }

    private void initNavBarButtons() {

        nextButton = findViewById(R.id.next_step);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BoardDatabase database=new BoardDatabase(IconSelectActivity.this);
                final Board board=database.getBoardById(boardId);
                if (board != null) {
                    board.setBoardIconModel(
                            new ModelManager(sortList(selectedIconList),IconSelectActivity.this).
                                    getModel());
                    database.updateBoardIntoDatabase(board);
                    Intent intent =new Intent(IconSelectActivity.this,AddEditIconAndCategory.class);
                    intent.putExtra(BOARD_ID,boardId);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(IconSelectActivity.this,"Some error occurred",Toast.LENGTH_LONG).show();
                }


            }
        });

        (findViewById(R.id.reset_selection)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconList.clear();
                selectionCheckBox.setChecked(false);
                iconSelectorAdapter.notifyDataSetChanged();
                nextButton.setAlpha(.5f);
                nextButton.setEnabled(false);
                resetButton.setEnabled(false);
                resetButton.setAlpha(.5f);
                ((TextView)(findViewById(R.id.icon_count))).setText("("+selectedIconList.size()+")");
            }
        });

    }


    /**
     * This function sorts the Icon list as they are present in the hierarchy
     * @param iconsList
     * @return
     */
    ArrayList<JellowIcon> sortList(ArrayList<JellowIcon> iconsList) {

        ArrayList<JellowIcon> listOfAllIcon=new IconDatabase(this).getAllIcons();
        ArrayList<JellowIcon> sortedList=new ArrayList<>();

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
     * @param icon
     * @param list
     * @return boolean
     */
    public boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list) {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        Log.d("Selection: ","Present "+present);
        return present;
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
        iconSelectorAdapter =new IconSelectorAdapter(this,iconList,IconSelectorAdapter.ICON_SELECT_MODE);
        dropDown= findViewById(R.id.filter_menu);
        iconRecycler.setLayoutManager(new CustomGridLayoutManager(this,gridSize(),9));
        iconRecycler.setAdapter(iconSelectorAdapter);
        levelSelecterRecycler=findViewById(R.id.level_select_pane_recycler);
        levelSelecterRecycler.setLayoutManager(new LinearLayoutManager(this));
        selectedIconList=new ArrayList<>();
        resetButton = findViewById(R.id.reset_selection);
        resetButton.setEnabled(false);
        resetButton.setAlpha(.5f);
        selectionCheckBox.setOnCheckedChangeListener(null);
        selectionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectionCheckBox.isChecked())
                    selectAll(0);
                else selectAll(1);
                if(selectedIconList.size()>0)
                {
                    nextButton.setAlpha(1f);
                    nextButton.setEnabled(true);
                    resetButton.setEnabled(true);
                    resetButton.setAlpha(1f);

                }
                else {
                    nextButton.setEnabled(false);
                    nextButton.setAlpha(.5f);
                    resetButton.setEnabled(false);
                    resetButton.setAlpha(.5f);
                }
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
        invalidateOptionsMenu();
        iconList=new IconDatabase(this).myBoardQuery(level_1,level_2);
        iconSelectorAdapter =new IconSelectorAdapter(this,iconList,IconSelectorAdapter.ICON_SELECT_MODE);
        iconRecycler.setAdapter(iconSelectorAdapter);
        iconSelectorAdapter.notifyDataSetChanged();
        selectionCheckBox.setChecked(utilF.getSelection(selectedIconList,iconList));
        iconSelectorAdapter.setOnItemClickListner(new IconSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean checked) {
                scrollCount = 0;
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

        if (selectedIconList.size()>0)
        {
            nextButton.setAlpha(1f);
            nextButton.setEnabled(true);
            resetButton.setEnabled(true);
            resetButton.setAlpha(1f);
        }
        else {
            nextButton.setEnabled(false);
            nextButton.setAlpha(.5f);
            resetButton.setEnabled(false);
            resetButton.setAlpha(.5f);
        }
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
        levelSelectorAdapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(previousSelection!=position) {
                    previousSelection = position;
                    iconRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(scrollingPopulationListener);
                    iconRecycler.removeOnScrollListener(scrollListener);
                    scrollListener=null;
                    scrollingPopulationListener=null;
                    scrollCount = 0;
                    prepareIconPane(position, -1);
                    dropDownList = getCurrentChildren();
                    if (dropDown.getVisibility() == View.VISIBLE)
                        dropDown.setVisibility(View.GONE);
                }
            }
        });

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
        if(previousSelection==5||previousSelection==8)
            menu.findItem(R.id.filter).setVisible(false);
        return true;
    }

    private void showSecondLevelMenu() {
        if(!(dropDownList.size()<2))
          {
            if (dropDown.getVisibility() == View.VISIBLE) {
                dropDown.setVisibility(View.GONE);
                return;
            }
            dropDown.setBackground(getResources().getDrawable(R.color.white));
            dropDown.setVisibility(View.VISIBLE);
            dropDown.bringToFront();
            dropDown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    prepareIconPane(previousSelection, position);
                    dropDown.setVisibility(View.GONE);
                }
            });

            simpleArrayAdapter simpleArrayAdapter=new simpleArrayAdapter(this,dropDownList);
            dropDown.setAdapter(simpleArrayAdapter);
        }

    }

    private ArrayList<String> getCurrentChildren() {
        ArrayList<String> list=new ArrayList<>();
        //This line is added to check for places/people and help
        for(int i=0;i<iconList.size();i++)
        {
            JellowIcon icon=iconList.get(i);
            if(icon.parent2!=-1)
                continue;
            if(icon.parent0==previousSelection&&icon.parent1!=-1)
                list.add(icon.IconTitle);
        }

        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                //Remove listener if already present
                if(scrollListener!=null)
                iconRecycler.removeOnScrollListener(scrollListener);
                scrollListener = null;
                if(scrollingPopulationListener!=null)
                iconRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(scrollingPopulationListener);
                scrollingPopulationListener = null;
                //Starting search Activity
                Intent searchIntent  =  new Intent(this,BoardSearch.class);
                searchIntent.putExtra(BoardSearch.SEARCH_MODE,BoardSearch.NORMAL_SEARCH);
                startActivityForResult(searchIntent,SEARCH_CODE);
                break;
            case R.id.filter:
                showSecondLevelMenu();
                break;
            case android.R.id.home: onBackPressed(); break;
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
                    addSearchedIcon(icon);
            }else if(utilF.listContainsIcon(icon,selectedIconList))
                Toast.makeText(this,"Icon already selected",Toast.LENGTH_SHORT).show();
        }

    }

    private void addSearchedIcon(final JellowIcon icon) {
        int category = icon.parent0;
        scrollCount = 0;
        previousSelection = category;
        prepareIconPane(category,-1);
        levelSelectorAdapter.selectedPosition = icon.parent0;
        levelSelectorAdapter.notifyDataSetChanged();
        selectedIconList.add(icon);
        dropDownList = getCurrentChildren();
        if(selectedIconList.size()>0)
        {
            nextButton.setEnabled(true);
            nextButton.setAlpha(1.0f);
            resetButton.setEnabled(true);
            resetButton.setAlpha(1f);
        }

        ((TextView) (findViewById(R.id.icon_count))).setText("(" + selectedIconList.size() + ")");
        selectionCheckBox.setChecked(utilF.getSelection(selectedIconList, iconList));
        scrollListener =null;
        int position = getPosition(icon);
        Log.d("PositionToScroll",position+"");
        if(position>(gridSize()*numberOfRows())) {
            Log.d("Search","Step 1: scrolling");
            scrollListener = getListener(position);
            iconRecycler.addOnScrollListener(scrollListener);
            iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler, null, position);
        }
        iconSelectorAdapter.notifyDataSetChanged();
    }

    //TODO Make it screen dependent
    private int numberOfRows() {
        return 2;
    }

    private int getPosition(JellowIcon icon) {
        for(int i=0;i<iconList.size();i++)
            if(iconList.get(i).isEqual(icon))
                return i;
        return -1;
    }

    private RecyclerView.OnScrollListener getListener(final int index) {
        Log.d("Search","Step 2: Scroll listener attached");
        scrollListener =new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==RecyclerView.SCROLL_STATE_IDLE)
                {
                    Log.d("Search","Step 3: scrolling stopped");
                    setSearchHighlight(index);//Try highlighting the view after scrolling
                }
            }};

        return scrollListener;
    }

    int scrollCount = 0;
    private ViewTreeObserver.OnGlobalLayoutListener scrollingPopulationListener;
    private void setSearchHighlight(final int index) {
        Log.d("Search","Step 4: Trying to highlight : (Attaching layout listener)");
        scrollingPopulationListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                iconRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(scrollingPopulationListener);
                scrollingPopulationListener = null;
                Log.d("Search","Step 5: Inside Global layout");
                if(itemDisplayed(index)) {
                    iconRecycler.removeOnScrollListener(scrollListener);
                    scrollListener=null;/*
                    View v = iconRecycler.getChildAt(index);
                    Animation jiggleOnce = AnimationUtils.loadAnimation(IconSelectActivity.this,R.anim.jiggle_determinate);
                    if(v!=null)
                    v.startAnimation(jiggleOnce);
                    else Toast.makeText(IconSelectActivity.this,"Cannot Jiggle",Toast.LENGTH_SHORT).show();*/
                    Log.d("Search","Step 6: Completed");
                }
                else {
                    if(scrollCount<5) {
                        iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler, null, index);
                        scrollCount++;
                        Log.d("Search", "Step 0: Not found, scrolling again");
                    }
                    else {
                        iconRecycler.removeOnScrollListener(scrollListener);
                        scrollListener = null;
                        iconRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(scrollingPopulationListener);
                        scrollingPopulationListener = null;
                    }
                }
            }
        };
        iconRecycler.getViewTreeObserver().addOnGlobalLayoutListener(scrollingPopulationListener);
    }

    /**
     * This function checks whether the searched item is present on the current screen,
     * for this we're just using current and last visible item and returning true and false regarding the position
     * @param index
     * @return
     */
    private boolean itemDisplayed(int index) {
        int firstVisiblePos = ((CustomGridLayoutManager)iconRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        int lastVisiblePos = ((CustomGridLayoutManager)iconRecycler.getLayoutManager()).findLastCompletelyVisibleItemPosition();
        if(lastVisiblePos==(index-1))
            return true;
        Log.d("TagScreen","First Pos: "+firstVisiblePos+" Last Pos: "+lastVisiblePos+" Pos "+index);
        return index >= firstVisiblePos && index <= lastVisiblePos;
    }


    @Override
    public void onBackPressed() {
        final CustomDialog dialog = new CustomDialog(this,CustomDialog.NORMAL);
        dialog.setText(getString(R.string.icon_select_exit_warning));
        dialog.setOnPositiveClickListener(new CustomDialog.onPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                finish();
            }
        });
        dialog.setOnNegativeClickListener(new CustomDialog.onNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private class simpleArrayAdapter extends ArrayAdapter<String>
    {
        // View lookup cache
        private class ViewHolder {
            TextView name;
        }

        public simpleArrayAdapter(Context context, ArrayList<String> StringList) {
            super(context, R.layout.level_select_card, StringList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String title = getItem(position);
            ViewHolder viewHolder; // view lookup cache stored in tag
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.level_select_card, parent, false);
                viewHolder.name = convertView.findViewById(R.id.icon_title);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(title);
            return convertView;
        }

    }

}
