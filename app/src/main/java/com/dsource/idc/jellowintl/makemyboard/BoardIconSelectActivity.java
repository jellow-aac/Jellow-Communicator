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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.IconDataBaseHelper;
import com.dsource.idc.jellowintl.utility.JellowIcon;

import java.util.ArrayList;

public class BoardIconSelectActivity extends AppCompatActivity {


    RecyclerView levelSelecterRecycler;
    RecyclerView iconRecycler;
    IconSelecterAdapter iconSelecterAdapter;
    ArrayList<JellowIcon> iconList;
    ListView dropDown;
    ArrayList<String> dropDownList;
    public static ArrayList<JellowIcon> selectedIconList;
    LevelSelecterAdapter levelSelecterAdapter;
    CheckBox selectionCheckBox;
    int previousSelection=0;
    UtilFunctions utilF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        utilF=new UtilFunctions();
        selectionCheckBox=findViewById(R.id.select_deselect_check_box);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        initViews();
        prepareLevelSelectPane();
        prepareIconPane(0,-1);
        dropDownList=getCurrentChildrens();
        initNavBarButtons();



    }

    private void initNavBarButtons() {
        (findViewById(R.id.save_normally)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(BoardIconSelectActivity.this,EditBoard.class);
                intent.putExtra("IconList",selectedIconList);
                startActivity(intent);
            }
        });

        (findViewById(R.id.reset_selection)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedIconList.clear();
                selectionCheckBox.setChecked(false);
                iconSelecterAdapter.notifyDataSetChanged();
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
        iconSelecterAdapter =new IconSelecterAdapter(this,iconList);
        dropDown= findViewById(R.id.filter_menu);
        iconRecycler.setLayoutManager(new GridLayoutManager(this,gridSize()));
        iconRecycler.setAdapter(iconSelecterAdapter);
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

        iconSelecterAdapter.notifyDataSetChanged();
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
        iconSelecterAdapter=new IconSelecterAdapter(this,iconList);
        iconRecycler.setAdapter(iconSelecterAdapter);
        iconSelecterAdapter.notifyDataSetChanged();
        selectionCheckBox.setChecked(utilF.getSelection(selectedIconList,iconList));
        iconSelecterAdapter.setOnItemClickListner(new IconSelecterAdapter.OnItemClickListener() {
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
        levelSelecterAdapter =new LevelSelecterAdapter(this,levelSelectList);
        levelSelecterRecycler.setAdapter(levelSelecterAdapter);
        levelSelecterAdapter.setOnItemClickListner(new LevelSelecterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                setSelection(position);
                prepareIconPane(position,-1);
                dropDownList=getCurrentChildrens();
                if(dropDown.getVisibility()==View.VISIBLE)
                    dropDown.setVisibility(View.GONE);


            }
        });

    }

    private void setSelection(int position) {
        if(previousSelection!=-1)
        levelSelecterRecycler.getChildAt(previousSelection).setBackground(getResources().getDrawable(R.color.colorIntroSelected));
        levelSelecterRecycler.getChildAt(position).setBackground(getResources().getDrawable(R.color.colorIntroSelected));
        levelSelecterRecycler.getChildAt(previousSelection).setBackgroundColor(getResources().getColor(R.color.colorIntro));

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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                setContentView(R.layout.activity_search
                );
                break;
            case R.id.filter:
                showSecondLevelMenu();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private ArrayList<String> getCurrentChildrens() {
        ArrayList<String> list=new ArrayList<>();
        for(int i=0;i<iconList.size();i++)
        {
            JellowIcon icon=iconList.get(i);
            if(icon.parent2!=-1)
                break;
            if(icon.parent0==previousSelection&&icon.parent2==-1&&icon.parent1!=-1)
                list.add(icon.IconTitle);

        }

        return list;
    }



}
