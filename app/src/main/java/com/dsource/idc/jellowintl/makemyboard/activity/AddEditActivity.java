package com.dsource.idc.jellowintl.makemyboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.MainActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.AddEditAdapter;
import com.dsource.idc.jellowintl.makemyboard.custom_dialogs.DialogAddEditIcon;
import com.dsource.idc.jellowintl.makemyboard.custom_dialogs.DialogCustom;
import com.dsource.idc.jellowintl.makemyboard.interfaces.AddIconCallback;
import com.dsource.idc.jellowintl.makemyboard.interfaces.EditAdapterCallback;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.makemyboard.managers.SearchScrollManager;
import com.dsource.idc.jellowintl.makemyboard.models.AddEditModel;
import com.dsource.idc.jellowintl.makemyboard.presenter_interfaces.IAddEditPresenter;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomPair;
import com.dsource.idc.jellowintl.makemyboard.view_interfaces.IAddEditView;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import static com.dsource.idc.jellowintl.makemyboard.custom_dialogs.DialogAddVerbiage.JELLOW_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;

public class AddEditActivity extends BaseBoardActivity<IAddEditView, IAddEditPresenter, AddEditAdapter> implements IAddEditView {

    private static final int SEARCH = 1234;
    private SearchScrollManager manager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_levelx_layout;
    }

    @Override
    public AddEditAdapter getAdapter() {
        final AddEditAdapter adapter = new AddEditAdapter(this, getLevelXAdapterLayout(currentBoard.getGridSize()), currentBoard.getIconModel().getAllIcons());
        adapter.setListener(new EditAdapterCallback() {
            @Override
            public void onIconClicked(int adapterPosition) {
                manager.clearListener();
                if (adapterPosition == 0)
                    showAddIconDialog();
            }

            @Override
            public void onIconEdit(int adapterPosition) {
                showEditIconDialog(adapter.getItem(adapterPosition), adapterPosition);
            }

            @Override
            public void onIconRemove(int adapterPosition) {
                askBeforeDeleteIcon(adapterPosition, adapter);
            }
        });
        return adapter;
    }

    private void askBeforeDeleteIcon(final int adapterPosition, final AddEditAdapter adapter) {
        final DialogCustom dialog = new DialogCustom(this);
        dialog.setText(getString(R.string.icon_delete_warning).replace("-",
                adapter.getItem(adapterPosition).getIconTitle()));
        dialog.setOnPositiveClickListener(new DialogCustom.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                adapter.remove(adapterPosition);
                mPresenter.removeIcon(adapterPosition - 1);
            }
        });
        dialog.setOnNegativeClickListener(new DialogCustom.OnNegativeClickListener() {
            @Override
            public void onNegativeClickListener() {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    @Override
    public void initViewsAndEvents() {
        //Disable Expressive Icons for this activity
        setVisibility(R.id.save_button, true);
        getView(R.id.keyboard).setAlpha(.5f);
        setVisibility(R.id.et, false);
        setVisibility(R.id.ttsbutton, false);
        getView(R.id.expressiveOne).setAlpha(.6f);
        getView(R.id.expressiveTwo).setAlpha(.6f);

        //Call presenter to load the items
        mPresenter.loadIcons();

        manager = new SearchScrollManager(this, mRecyclerView);

        setupToolBar(R.string.addicon_title);
        if (getSupportActionBar() != null)
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));

        getView(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Don't let  user proceed when there is no icon present in the board
                if(currentBoard.getIconModel().getAllIcons().size()==0)
                    Toast.makeText(mContext, getString(R.string.no_icon_warning),Toast.LENGTH_LONG).show();
                else {
                    mPresenter.nextPressed(mContext);
                    finish();
                }
            }
        });

        getView(R.id.ivback).setEnabled(false);
        getView(R.id.ivback).setAlpha(.6f);
        getView(R.id.ivhome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecyclerView.getLayoutManager()!=null)
                    mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,null,0);
            }
        });
    }


    @Override
    public IAddEditPresenter createPresenter() {
        return new AddEditModel(mContext, currentBoard, getAppDatabase());
    }

    @Override
    public void setLayoutManager(RecyclerView mRecyclerView) {
        switch (currentBoard.getGridSize()) {
            case 0:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
                break;
            case 1:
            case 3:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                break;
            default:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                break;
        }
    }

    @Override
    public void onIconLoaded(ArrayList<JellowIcon> icons) {
        mAdapter.update(icons);
    }


    /**
     * Function to instantiate the add icon dialog
     */

    private void showAddIconDialog() {
        Intent intent = new Intent(this,DialogAddEditIcon.class);
        intent.putExtra(BOARD_ID,currentBoard.getBoardId());
        DialogAddEditIcon.subscribe(new AddIconCallback() {
            @Override
            public void onAddedSuccessfully(JellowIcon icon) {
                currentBoard.getIconModel().addChild(icon);
                currentBoard.addCustomIconID(icon.getVerbiageId());
                mAdapter.add(icon);
                mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);
                mPresenter.updateBoard(currentBoard);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            }
        });
        startActivity(intent);
    }

    /**
     * Function to instantiate the edit icon dialog
     *
     * @param jellowIcon        icon to be edited
     * @param positionInTheList position of the icon
     */
    private void showEditIconDialog(final JellowIcon jellowIcon, final int positionInTheList) {
        Intent intent = new Intent(this,DialogAddEditIcon.class);
        intent.putExtra(BOARD_ID,currentBoard.getBoardId());
        Bundle bundle = new Bundle();
        bundle.putSerializable(JELLOW_ID,jellowIcon);

        DialogAddEditIcon.subscribe(new AddIconCallback() {
            @Override
            public void onAddedSuccessfully(JellowIcon icon) {
                currentBoard.getIconModel().getChildren().get(positionInTheList - 1).setIcon(icon);
                mAdapter.replaceItem(positionInTheList, icon);
                mRecyclerView.smoothScrollToPosition(positionInTheList);
                mPresenter.updateBoard(currentBoard);
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
            }
        });

        intent.putExtras(bundle);
        startActivity(intent);
    }

    private int getLevelXAdapterLayout(int gridSize) {
        switch (gridSize) {
            case 0:
                return R.layout.layout_level_xadapter_1_icon;
            case 1:
                return R.layout.layout_level_xadapter_2_icons;
            case 2:
                return R.layout.layout_level_xadapter_3_icons;
            case 3:
                return R.layout.layout_level_xadapter_4_icons;
            default:
                return R.layout.layout_level_xadapter_9_icons;
        }
    }


    private void showGridDialog() {
        showGridDialog(new GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                changeGridSize();
            }
        });
    }

    private void changeGridSize() {
        switch (currentBoard.getGridSize()) {
            case 0:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 1));
                break;
            case 1:
            case 3:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                break;
            default:
                mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
                break;
        }
        resetLayout();
    }

    private void resetLayout() {
        mAdapter = getAdapter();
        mRecyclerView.setAdapter(mAdapter);
        initViewsAndEvents();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grid_size:
                showGridDialog();
                break;
            case R.id.action_search:
                searchInBoard();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.board_home_menu, menu);
        menu.findItem(R.id.reposition_lock).setVisible(false);
        menu.findItem(R.id.action_home_app).setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        DialogCustom dialog = new DialogCustom(this);
        dialog.setText(getResources().getString(R.string.exit_warning));
        dialog.setOnPositiveClickListener(new DialogCustom.OnPositiveClickListener() {
            @Override
            public void onPositiveClickListener() {
                if(getSession()!=null) getSession().setCurrentBoardLanguage("");
                startActivities(new Intent[]{
                        new Intent(getApplicationContext(), MainActivity.class),
                        new Intent(getApplicationContext(), BoardListActivity.class)
                });
                /*startActivity(new Intent(mContext, BoardListActivity.class));*/
                finishAffinity();
            }
        });
        dialog.show();
    }

    private void searchInBoard() {
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID, currentBoard.getBoardId());
        startActivityForResult(searchIntent, SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEARCH) {
            if (resultCode == RESULT_OK) {
                JellowIcon icon = (JellowIcon) data.getSerializableExtra(getString(R.string.search_result));
                CustomPair<Integer, Integer> iconPos = currentBoard.getIconModel().getIconPosition(icon);
                if (iconPos.getFirst() != -1) {
                    scroll(iconPos);
                }

            }
        }

    }

    private void scroll(CustomPair<Integer, Integer> iconPos) {

        //if the searched icon is on level 2
        if (iconPos.getSecond() != -1) {
            //Don't scroll if the searched item is already on screen
            if (iconPos.getSecond() > getNumberOfIconPerScreen()||
                    iconPos.getSecond()< getFirstVisibleItem())//This conditions manages bottom up searching
                manager.scrollToPosition(iconPos.getSecond()+1);

            //Set the highlighted icon
            mAdapter.setHighlightedPosition(iconPos.getSecond() + 1);

            //Update the board accordingly
            mAdapter.notifyItemChanged(iconPos.getSecond() + 1);

        } //if the searched icon is on level 1
        else if (iconPos.getFirst() != -1) {

            //Don't scroll if the searched item is already on screen
            if ((iconPos.getFirst()+1) >= getNumberOfIconPerScreen()||
                    (iconPos.getFirst()+1)< getFirstVisibleItem())
                manager.scrollToPosition(iconPos.getFirst()+1);

            //Set the highlighted icon
            mAdapter.setHighlightedPosition(iconPos.getFirst() + 1);

            //Update the board accordingly
            mAdapter.notifyItemChanged(iconPos.getFirst() + 1);
        }
    }

    private Integer getFirstVisibleItem() {
        if(mRecyclerView.getLayoutManager()!=null)
            return ((GridLayoutManager)mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        return -1;
    }


}