package com.dsource.idc.jellowintl.makemyboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.IconSelectorAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.databases.TextDatabase;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.view.managers.LevelManager;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.makemyboard.utility.VerbiageEditor;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.dsource.idc.jellowintl.makemyboard.AddVerbiageDialog.JELLOW_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_CATEGORY;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_EDIT_ICON_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_ICON;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.CAMERA_REQUEST;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.EDIT_ICON;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.EDIT_ICON_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.LIBRARY_REQUEST;
import static com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper.storeImageToStorage;

public class AddEditIconAndCategoryActivity extends BaseActivity implements View.OnClickListener {

    private String boardId;
    private BoardDatabase database;
    private BoardModel currentBoard;
    private IconModel boardModel;
    private ModelManager modelManager;
    private RecyclerView iconRecycler;
    private ArrayList<String> categories;
    private CategoryManager categoryManager;
    private IconSelectorAdapter iconAdapter;
    private ArrayList<JellowIcon> iconList;
    private RelativeLayout addCategory,addIcon,editIcon;
    private int selectedPosition=0;
    private TextDatabase verbiageDatbase;
    private int currentMode = ADD_EDIT_ICON_MODE;
    private LevelManager levelManager;
    public VerbiageEditorReverseInterface revListener;
    private SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
        manager = new SessionManager(this);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            getSupportActionBar().setTitle(getResources().getString(R.string.addicon_title));
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        }

        try{
            if(getIntent().getExtras()!=null)
            boardId =getIntent().getExtras().getString(BOARD_ID);
            database=new BoardDatabase(this);
            currentBoard=database.getBoardById(boardId);
            if(currentBoard!=null) {
                verbiageDatbase = new TextDatabase(this,currentBoard.getLanguage(), getAppDatabase());
                boardModel = currentBoard.getIconModel();
                modelManager = new ModelManager(this, boardModel);
                iconList = new ArrayList<>();
                categoryManager = new CategoryManager(boardModel);
                categories = categoryManager.getAllCategories();
                iconList = categoryManager.getAllChildOfCategory(0);
                initViews();
            }
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this,"Some error occured",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setupLevelSelect() {
        levelManager= new LevelManager((RecyclerView) (findViewById(R.id.level_select_pane_recycler)), this, new GenCallback<Integer>() {
            @Override
            public void callBack(Integer position) {
                prepareLevelSelectPane(position);
            }
        }).setList(getLevelPaneList());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(BoardLanguageHelper.getInstance().changeLanguage(newBase));
    }

    private ArrayList<String> getLevelPaneList() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(categories);
        return list;
    }

    private void initViews() {
        iconRecycler = findViewById(R.id.icon_select_pane_recycler);
        iconRecycler.setLayoutManager(new GridLayoutManager(this,gridSize()));
        iconAdapter = new IconSelectorAdapter(this,
                categoryManager.getAllChildOfCategory(0),
                ADD_EDIT_ICON_MODE,currentBoard.getLanguage());
        iconRecycler.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();

        setupLevelSelect();

        /*
            Hiding unwanted views of the layout
         */
        findViewById(R.id.reset_selection).setVisibility(View.GONE);
        ((Button)findViewById(R.id.next_step)).setText(R.string.next);
        findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(findViewById(R.id.add_edit_icon_option).getVisibility()==View.VISIBLE) {
                    findViewById(R.id.add_edit_icon_option).setVisibility(View.GONE);
                    findViewById(R.id.touch_area).setVisibility(View.GONE);
                }

                if(currentBoard.getIconModel().getAllIcons().size()>0) {
                    CustomDialog dialog = new CustomDialog(AddEditIconAndCategoryActivity.this,currentBoard.getLanguage(), new GridSelectListener() {
                        @Override
                        public void onGridSelectListener(int size) {
                            currentBoard.setGridSize(size);
                            currentBoard.setSetupStatus(BoardModel.STATUS_L2);
                            database.updateBoardIntoDatabase(currentBoard);
                            Intent intent = new Intent(AddEditIconAndCategoryActivity.this, RepositionIconsActivity.class);
                            intent.putExtra(BOARD_ID, boardId);
                            startActivity(intent);
                            finish();
                        }
                    });
                    dialog.setCancelable(true);
                }
                else Toast.makeText(AddEditIconAndCategoryActivity.this,"Please make atleast one icon",Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.select_deselect_check_box).setVisibility(View.GONE);
        findViewById(R.id.icon_count).setVisibility(View.GONE);
        findViewById(R.id.touch_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(findViewById(R.id.add_edit_icon_option).getVisibility()==View.VISIBLE)
                {
                    findViewById(R.id.add_edit_icon_option).setVisibility(View.GONE);
                    findViewById(R.id.touch_area).setVisibility(View.GONE);
                }
            }
        });

        addCategory = findViewById(R.id.add_category);
        addIcon = findViewById(R.id.add_icon);
        editIcon = findViewById(R.id.edit_icon);
        addCategory.setOnClickListener(this);
        editIcon.setOnClickListener(this);
        addIcon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        toggleOptionLayout();
        int mode = -1;
        if(v ==addCategory)
            mode = ADD_CATEGORY;
        else if(v==addIcon)
            mode = ADD_ICON;
        else if(v==editIcon)
            mode = EDIT_ICON;
        showCustomDialog(mode);

    }

    /**
     * Creates and fetches the left pane for icon select
     */
    private void prepareLevelSelectPane(int position) {
        if(selectedPosition!=position) {
            selectedPosition = position;
            prepareIconPane(position,currentMode);
        }
    }

    /**
     * Creates and fetches the left pane for icon select
     * @param mode defines the mode which decides whether to scroll or not
     */
    private void targetLevelSelectPane(int mode) {

        levelManager.setList(categories);
        levelManager.updateSelection(selectedPosition);
        if(levelManager.getRecycler().getLayoutManager()!=null&&mode==ADD_CATEGORY)
            levelManager.getRecycler().getLayoutManager().smoothScrollToPosition(levelManager.getRecycler(),null,(categories.size()-1));
        prepareIconPane(selectedPosition,currentMode);

    }
    private void prepareIconPane(int position,int mode) {
        iconList = categoryManager.getAllChildOfCategory(position);
        if(iconList.size()>0)
        if(mode==ADD_EDIT_ICON_MODE)
            iconAdapter = new IconSelectorAdapter(this,iconList, ADD_EDIT_ICON_MODE,currentBoard.getLanguage());
        else if(mode ==EDIT_ICON_MODE)
            iconAdapter = new IconSelectorAdapter(this,iconList,EDIT_ICON_MODE,currentBoard.getLanguage());

        iconAdapter.setOnIconEditListener(new IconSelectorAdapter.OnIconEditListener() {
            @Override
            public void onIconEdit(int pos) {
                if(pos==0)
                    initEditModeDialog(selectedPosition,-1,-1,boardModel.getChildren().get(selectedPosition).getIcon());
                else {
                    JellowIcon icon = iconList.get(pos);
                    int[] posOfIconInModel = categoryManager.getPositionOfIcon(selectedPosition, icon);
                    initEditModeDialog(selectedPosition, posOfIconInModel[0], posOfIconInModel[1], icon);
                }
            }
        });
        iconRecycler.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
    }

    private boolean iconImageSelected =false;
    private void initEditModeDialog(final int parent1, final int parent2, final int parent3, final JellowIcon thisIcon) {

       iconImageSelected =false;
        VerbiageEditor dialog =  new VerbiageEditor(this,VerbiageEditor.ADD_EDIT_ICON_MODE,new VerbiageEditorInterface() {

            @Override
            public void onPositiveButtonClick(final String name, final Bitmap bitmap, Icon verbiage) {

                //Setting new id if base icon is being edited, if not done then it will replace main verbiage
                final JellowIcon icon  = thisIcon;
                if(!thisIcon.isCustomIcon()) {
                    int id  = (int)Calendar.getInstance().getTime().getTime();
                    icon.setDrawable(id+"");
                }
                Intent intent  = new Intent(AddEditIconAndCategoryActivity.this,AddVerbiageDialog.class);
                Bundle bundle = new Bundle();
                intent.putExtra(BOARD_ID,currentBoard.getBoardId());
                intent.putExtra(BoardConstants.CURRENT_VERBIAGE,icon.getVerbiageId());
                bundle.putSerializable(JELLOW_ID,icon);
                intent.putExtras(bundle);
                startActivity(intent);
                AddVerbiageDialog.callback = new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        saveEditedIcon(icon.getIconDrawable(),name,bitmap,parent1,parent2,parent3);
                        prepareIconPane(parent1,EDIT_ICON_MODE);
                    }
                };
            }

            @Override
            public void onPhotoModeSelect(int position) {
                firePhotoIntent(position);
            }

            @Override
            public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {
                    revListener = verbiageEditorReverseInterface;
            }
        },currentBoard.getLanguage());
       dialog.initAddEditDialog(thisIcon.getIconTitle());
       dialog.setAlreadyPresentIcon(thisIcon);
       dialog.setPositiveButtonText(getResources().getString(R.string.next));
       dialog.setTitleText(thisIcon.getIconTitle());

       dialog.showDialog();
    }


    /**
     * This function will save the edited Icon
     * @param id
     * @param name Name of the Icon
     * @param bitmapArray image of the Icon
     * @param p1 parent 1
     * @param p2 parent 2
     * @param p3 parent 3
     */
    private void saveEditedIcon(String id, String name, Bitmap bitmapArray, int p1, int p2, int p3) {

        JellowIcon icon = new JellowIcon(name,id,-1,-1,Integer.parseInt(id));
        icon.setVerbiageId(id);
        storeImageToStorage(bitmapArray,id+"",this);
        if(p2==-1&&p3==-1)//if level one icon is being edited.
            boardModel.getChildren().get(p1).setIcon(icon);
        else if(p3==-10)//If level two icon is being edited
            boardModel.getChildren().get(p1).getChildren().get(p2).setIcon(icon);
        else // if level three icon is being edited
            boardModel.getChildren().get(p1).getChildren().get(p2).getChildren().get(p3).setIcon(icon);

        categoryManager = new CategoryManager(boardModel);
        prepareIconPane(selectedPosition,EDIT_ICON_MODE);
        modelManager.setModel(boardModel);
        currentBoard.setIconModel(modelManager.getModel());
        currentBoard.addCustomIconID(id+"");
        targetLevelSelectPane(EDIT_ICON);

    }

    private int gridSize() {
        int gridSize=6;
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            gridSize=8;
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            gridSize=4;
        }
        return gridSize;
    }

    private void toggleOptionLayout() {
        if(findViewById(R.id.add_edit_icon_option).getVisibility()==View.VISIBLE)
        {
            findViewById(R.id.add_edit_icon_option).setVisibility(View.GONE);
            findViewById(R.id.touch_area).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.add_edit_icon_option).setVisibility(View.VISIBLE);
            findViewById(R.id.touch_area).setVisibility(View.VISIBLE);

        }
    }

    private void showCustomDialog(int mode) {
        if(mode!=-1)
        {
            if(mode==ADD_ICON)
                initAddNewIconCategoryDialog(mode,getResources().getString(R.string.icon_name));
            else if(mode==ADD_CATEGORY)
                initAddNewIconCategoryDialog(mode,getResources().getString(R.string.category_name));
            else if(mode==EDIT_ICON)
            {
                prepareIconPane(selectedPosition,EDIT_ICON_MODE);
                currentMode = EDIT_ICON_MODE;
                invalidateOptionsMenu();
            }
        }
    }

    private void initAddNewIconCategoryDialog(final int mode, String editTextHint) {
        iconImageSelected =false;
        final int id  = (int)Calendar.getInstance().getTime().getTime();
        VerbiageEditor dialog = new VerbiageEditor(this, VerbiageEditor.ADD_EDIT_ICON_MODE, new VerbiageEditorInterface() {
            @Override
            public void onPositiveButtonClick(final String name, final Bitmap bitmap, Icon verbiageList) {
                Intent intent  = new Intent(AddEditIconAndCategoryActivity.this,AddVerbiageDialog.class);
                Bundle bundle = new Bundle();
                intent.putExtra(BOARD_ID,currentBoard.getBoardId());
                bundle.putSerializable(JELLOW_ID,new JellowIcon(name,""+id,-1,-1,id));
                intent.putExtras(bundle);
                startActivity(intent);
                AddVerbiageDialog.callback = new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if(mode==ADD_ICON)
                            addNewIcon(id,name,bitmap,selectedPosition);
                        else
                            addNewCategory(id,name,bitmap);
                        Toast.makeText(getApplicationContext(),"Successfully saved",Toast.LENGTH_LONG).show();
                    }
                };
            }

            @Override
            public void onPhotoModeSelect(int position) {
                firePhotoIntent(position);
            }

            @Override
            public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {
                        revListener = verbiageEditorReverseInterface;
            }
        },currentBoard.getLanguage());
        dialog.initAddEditDialog(editTextHint);
        dialog.showDialog();
    }

    private void firePhotoIntent(int position) {
        if(position==0)
        {
            if(checkPermissionForCamera()&&checkPermissionForStorageRead()) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(AddEditIconAndCategoryActivity.this);
            }
            else
            {
                final String [] permissions=new String []{ Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(AddEditIconAndCategoryActivity.this, permissions, CAMERA_REQUEST);
            }

        }
        else if(position==1)
        {
            Intent intent = new Intent(AddEditIconAndCategoryActivity.this, BoardSearchActivity.class);
            intent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.ICON_SEARCH);
            intent.putExtra(BOARD_ID,currentBoard.getBoardId());
            startActivityForResult(intent,LIBRARY_REQUEST);
        }
    }

    private void addNewCategory(int id, String name, Bitmap bitmap) {
        JellowIcon icon = new JellowIcon(name,""+id,-1,-1,id);
        icon.setVerbiageId(id+"");
        if(iconImageSelected)
        storeImageToStorage(bitmap,id+"",this);
        boardModel.addChild(icon);
        categoryManager = new CategoryManager(boardModel);
        categories = categoryManager.getAllCategories();
        modelManager.setModel(boardModel);
        currentBoard.setIconModel(modelManager.getModel());
        currentBoard.addCustomIconID(id+"");
        selectedPosition = categories.size()-1;
        targetLevelSelectPane(ADD_CATEGORY);
    }

    /**
     * This funtion takes name and bitmap array of a icon to be added and generates
     * an icon for it and adds it to the postion and scrolls to it.
     * @param name Name of the Icon
     * @param bitmap bitmap array holding the image
     * @param selectedPosition postion on which new icon is to be added.
     */
    private void addNewIcon(int id, String name, Bitmap bitmap, int selectedPosition) {
        JellowIcon icon = new JellowIcon(name,id+"",-1,-1,id);
        icon.setVerbiageId(id+"");
        if(iconImageSelected)
        storeImageToStorage(bitmap,id+"",this);
        boardModel.getChildren().get(selectedPosition).addChild(icon);
        if(iconRecycler.getLayoutManager()!=null)
        iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler,null,(boardModel.getChildren().get(selectedPosition).getChildren().size()-1));
        categoryManager = new CategoryManager(boardModel);
        prepareIconPane(selectedPosition,ADD_EDIT_ICON_MODE);
        modelManager.setModel(boardModel);
        currentBoard.setIconModel(modelManager.getModel());
        currentBoard.addCustomIconID(id+"");
    }

    private boolean checkPermissionForStorageRead() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean checkPermissionForCamera() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==CAMERA_REQUEST)
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(AddEditIconAndCategoryActivity.this);
            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == LIBRARY_REQUEST) {
                String fileName = data.getStringExtra("result");
                if (fileName != null) {
                    iconImageSelected = true;
                    revListener.onPhotoResult(null, requestCode, fileName);//mPhotoIntentResult.onPhotoIntentResult(null, requestCode, fileName);
                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap1 = result.getBitmap();
                if (bitmap1 != null) {
                    revListener.onPhotoResult(result.getBitmap(), requestCode, null);
                    iconImageSelected = true;
                    //mPhotoIntentResult.onPhotoIntentResult(result.getBitmap(), requestCode,null);
                } else {
                    try {
                        iconImageSelected = true;
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        revListener.onPhotoResult(bitmap1, requestCode, null);// mPhotoIntentResult.onPhotoIntentResult(bitmap1,requestCode,null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("CROP_IMAGE_ERROR", error.getMessage());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_edit_icon_screen_menu, menu);
        MenuItem item = menu.findItem(R.id.done);
        MenuItem showOption = menu.findItem(R.id.add_edit_icons);
        if(currentMode!=EDIT_ICON_MODE) {
            item.setVisible(false);
            showOption.setVisible(true);
        }
        else
        {
            item.setVisible(true);
            showOption.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add_edit_icons:
                toggleOptionLayout();
                break;
            case R.id.done:currentMode=ADD_EDIT_ICON_MODE;prepareIconPane(selectedPosition,ADD_EDIT_ICON_MODE);invalidateOptionsMenu();break;
            case android.R.id.home: startActivity(new Intent(this, BoardListActivity.class));finishAffinity();
                if(manager!=null) manager.setCurrentBoardLanguage("");break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Class to handle categories for AddEdit Icon Level
    private class CategoryManager {
        private IconModel model;


        private CategoryManager(IconModel model) {
            this.model = model;
        }

        private ArrayList<String> getAllCategories() {
            ArrayList<String> list = new ArrayList<>();
            for(int i=0;i<model.getChildren().size();i++)
                list.add(model.getChildren().get(i).getIcon().getIconTitle());
            return list;
        }

        private ArrayList<JellowIcon> getAllChildOfCategory(int categoryIndex) {
            ArrayList<JellowIcon> iconList  = new ArrayList<>();
            iconList.add(model.getChildren().get(categoryIndex).getIcon());
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

        private int[] getPositionOfIcon(int selectedPosition, JellowIcon icon) {

            IconModel catModel = model.getChildren().get(selectedPosition);

            for(int i=0;i<catModel.getChildren().size();i++)//To traverse second level
                if(catModel.getChildren().get(i).getIcon().isEqual(icon))
                        return new int[]{i,-10};

            for(int i=0;i<catModel.getChildren().size();i++)//To traverse second level
            {
                IconModel levelThreeModel = catModel.getChildren().get(i);

                for(int j=0;j<levelThreeModel.getChildren().size();j++)//To traverse Third Level level
                     if(levelThreeModel.getChildren().get(j).getIcon().isEqual(icon))
                            return new int[]{i,j};
            }


            return new int[0];
        }

    }

    @Override
    public void onBackPressed() {
        if(manager!=null) manager.setCurrentBoardLanguage("");
        super.onBackPressed();
    }


}
