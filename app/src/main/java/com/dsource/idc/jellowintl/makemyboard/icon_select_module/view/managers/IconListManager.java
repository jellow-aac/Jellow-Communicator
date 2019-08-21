package com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.IconSelectorAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.IconDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.model.IconLoader;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iDataPresenter;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.iPresenterIconManager;
import com.dsource.idc.jellowintl.makemyboard.utility.UtilFunctions;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.CustomGridLayoutManager;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ICON_SELECT_MODE;

public class IconListManager {
    private final String langCode;
    private RecyclerView recyclerView;
    private IconSelectorAdapter adapter;
    private Context context;
    private IconDatabase iconDatabase;
    private ArrayList<JellowIcon> icons;
    private int levelOneParent = 0, levelTwoParent = -1;
    private CheckBox cbSelectionCheckBox;
    private TextView tvIconCount;
    private Button btnReset;
    private TextView tvNoItemHint;
    private Button nextButton;
    private boolean isEditMode = false;
    private iPresenterIconManager presenter;
    private ArrayList<JellowIcon> currentList;

    public IconListManager(View view, Context context, String langCode, iPresenterIconManager presenter, boolean isEditMode, AppDatabase appDatabase) {
        this.context = context;
        this.presenter = presenter;
        this.isEditMode  = isEditMode;
        initViews(view);
        recyclerView.setLayoutManager(new CustomGridLayoutManager(context, gridSize(), 9));
        iconDatabase = new IconDatabase(langCode, appDatabase);
        this.langCode = langCode;
        icons = new ArrayList<>();
        currentList = new ArrayList<>();
        setUpAdapter();
        loadIcons();
        manageSelection();
    }

    private void initViews(View view) {
        this.recyclerView = view.findViewById(R.id.icon_select_pane_recycler);
        this.tvIconCount = view.findViewById(R.id.icon_count);
        this.btnReset = view.findViewById(R.id.reset_selection);
        this.cbSelectionCheckBox = view.findViewById(R.id.select_deselect_check_box);
        this.tvNoItemHint = view.findViewById(R.id.place_holder_text);
        this.nextButton = view.findViewById(R.id.next_step);
        cbSelectionCheckBox.setOnCheckedChangeListener(null);
        cbSelectionCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbSelectionCheckBox.isChecked()) {
                    SelectionManager.getInstance().selectAll(true, icons);
                    manageSelection();
                } else {
                    SelectionManager.getInstance().selectAll(false, icons);
                    manageSelection();
                }
                adapter.notifyDataSetChanged();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (levelOneParent == 0) {
                    /*
                        if currentBoard is selected-
                        1. Clear all the selection if not in editing mode
                        2. Clear all the selection plus all the items in the board.
                     */
                    if(!isEditMode)
                        SelectionManager.getInstance().getList().clear();
                    else {
                        presenter.onSelectionClear();
                        currentList.clear();
                    }
                    showPlaceHolder(true);
                    adapter.setDataList(new ArrayList<JellowIcon>());
                }else
                    SelectionManager.getInstance().selectAll(false, icons);
                manageSelection();
                adapter.notifyDataSetChanged();

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextPressed();
            }
        });

    }

    private void setUpAdapter() {
        adapter = new IconSelectorAdapter(context, icons, ICON_SELECT_MODE, langCode);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(new IconSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, boolean checked) {
                if (presenter != null) {
                    presenter.onItemSelected(icons.get(position), checked);
                    manageSelection();
                }
            }
        });
    }

    public void updateIcons(int p1, int p2) {
        levelOneParent = p1;
        levelTwoParent = p2;
        loadIcons();
    }

    private void updateDataList(ArrayList<JellowIcon> icons) {
        this.icons = icons;
        adapter.setDataList(icons);
    }

    private void loadIcons() {
        if (levelOneParent != 0) {
            //Load icons from the database
            IconLoader iconLoader = new IconLoader(iconDatabase, new iDataPresenter<ArrayList<JellowIcon>>() {
                @Override
                public void onSuccess(ArrayList<JellowIcon> icons) {
                    adapter.setNoCheckBoxMode(false);
                    cbSelectionCheckBox.setVisibility(View.VISIBLE);
                    tvIconCount.setVisibility(View.VISIBLE);
                    updateDataList(icons);
                    showPlaceHolder(false);
                    manageSelection();
                    if (presenter != null)
                        presenter.onListUpdated();
                }

                @Override
                public void onFailure(String msg) {

                }
            });
            iconLoader.execute((levelOneParent - 1), levelTwoParent);
        } else if(isEditMode) {
            //Return selected icon list
            adapter.setNoCheckBoxMode(true);
            cbSelectionCheckBox.setVisibility(View.INVISIBLE);
            tvIconCount.setVisibility(View.INVISIBLE);
            ArrayList<JellowIcon> currentIconList = new ArrayList<>();
            //Add all the current board list
            currentIconList.addAll(currentList);
            //Add all the selected icons list
            currentIconList.addAll(SelectionManager.getInstance().getList());
            if(currentIconList.size()==0)
                disableView(btnReset,true);
            else disableView(btnReset,false);

            if (currentIconList.size() == 0)
                showPlaceHolder(true);
            else showPlaceHolder(false);
            manageSelection();
            updateDataList(currentIconList);

        }else{

            adapter.setNoCheckBoxMode(true);
            cbSelectionCheckBox.setVisibility(View.INVISIBLE);
            tvIconCount.setVisibility(View.INVISIBLE);
            if (SelectionManager.getInstance().getList().size() == 0)
                showPlaceHolder(true);
            else showPlaceHolder(false);
            manageSelection();
            updateDataList(SelectionManager.getInstance().getList());
        }
    }

    public void setCurrentBoardIcons(ArrayList<JellowIcon> list){
        currentList.addAll(list);
        loadIcons();
    }

    private void showPlaceHolder(boolean b) {
        if (b) {
            tvNoItemHint.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvNoItemHint.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void manageSelection() {
        StringBuilder builder = new StringBuilder("(");
        builder.append(SelectionManager.getInstance().getList().size());
        builder.append(")");
        tvIconCount.setText(builder.toString());
        if(levelOneParent!=0) {
            btnReset.setVisibility(View.VISIBLE);
            boolean reset = UtilFunctions.doReset(SelectionManager.getInstance().getList(), icons);
            if (reset)
                disableView(btnReset, false);
            else if (levelOneParent == 0 && SelectionManager.getInstance().getList().size() > 0)
                disableView(btnReset, false);
            else
                disableView(btnReset, true);


            if (reset) {
                boolean resetCheckBox = UtilFunctions.getSelection(SelectionManager.getInstance().getList(), icons);
                cbSelectionCheckBox.setChecked(resetCheckBox);
            } else cbSelectionCheckBox.setChecked(false);
        }else btnReset.setVisibility(View.GONE);

        if (SelectionManager.getInstance().getList().size() > 0) {
            disableView(nextButton,false);
        } else {
            disableView(nextButton,true);
        }

        if(isEditMode&&levelOneParent==0) {
            if((currentList.size()+SelectionManager.getInstance().getList().size())>0)
            {
                disableView(nextButton,false);
            }
            else {
                disableView(nextButton,true);
            }

        }

    }

    private void disableView(View view, boolean disable){
        if(disable){
            view.setAlpha(.6f);
            view.setEnabled(false);
        }
        else{
            view.setAlpha(1f);
            view.setEnabled(true);
        }

    }


    public int getPosition(JellowIcon icon) {
        for (int i = 0; i < icons.size(); i++)
            if (icons.get(i).isEqual(icon))
                return i;
        return -1;
    }

    private int gridSize() {
        int gridSize = 6;
        if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            gridSize = 10;
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            gridSize = 9;
        } else if ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            gridSize = 4;
        }

        return gridSize;
    }

    public void enableEditMode(boolean yes) {
        isEditMode = yes;
    }

    public RecyclerView getRecycler() {
        return recyclerView;
    }

}
