package com.dsource.idc.jellowintl.makemyboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.adapters.IconSelectorAdapter;
import com.dsource.idc.jellowintl.makemyboard.adapters.LevelSelectorAdapter;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowintl.makemyboard.models.Board;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowintl.makemyboard.utility.VerbiageEditor;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.VerbiageDatabaseHelper;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.*;
import static com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper.deleteImageFromStorage;
import static com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper.storeImageToStorage;

public class AddEditIconAndCategory extends AppCompatActivity implements View.OnClickListener {

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
    private ArrayList<JellowIcon> iconList;
    private RelativeLayout addCategory,addIcon,editIcon;
    private int selectedPosition=0;
    private VerbiageDatabaseHelper verbiageDatbase;
    private int currentMode = ADD_EDIT_ICON_MODE;

    public VerbiageEditorReverseInterface revListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>" + "Add/Edit Icons" + "</font>"));
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        }
        verbiageDatbase = new VerbiageDatabaseHelper(this);

        try{
            if(getIntent().getExtras()!=null)
            boardId =getIntent().getExtras().getString(BOARD_ID);
            database=new BoardDatabase(this);
            currentBoard=database.getBoardById(boardId);
            if(currentBoard!=null)
            boardModel = currentBoard.getBoardIconModel();
            modelManager =new ModelManager(this,boardModel);
            iconList = new ArrayList<>();
            categoryManager = new CategoryManager(boardModel);
            categories = categoryManager.getAllCategories();
            iconList = categoryManager.getAllChildOfCategory(0);
            initViews();
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this,"Some error occured",Toast.LENGTH_LONG).show();
        }
    }

    private void initViews() {

        categoryRecycler = findViewById(R.id.level_select_pane_recycler);
        categoryRecycler.setLayoutManager(new LinearLayoutManager(this));
        prepareLevelSelectPane();
        iconRecycler = findViewById(R.id.icon_select_pane_recycler);
        iconRecycler.setLayoutManager(new GridLayoutManager(this,gridSize()));
        iconAdapter = new IconSelectorAdapter(this,
                categoryManager.getAllChildOfCategory(0),
                ADD_EDIT_ICON_MODE);
        iconRecycler.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
        /*
            Hiding unwanted views of the layout
         */
        findViewById(R.id.reset_selection).setVisibility(View.GONE);
        findViewById(R.id.next_step).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(findViewById(R.id.add_edit_icon_option).getVisibility()==View.VISIBLE) {
                    findViewById(R.id.add_edit_icon_option).setVisibility(View.GONE);
                    findViewById(R.id.touch_area).setVisibility(View.GONE);
                }

                    CustomDialog dialog = new CustomDialog(AddEditIconAndCategory.this, CustomDialog.GRID_SIZE);
                    dialog.show();
                    dialog.setCancelable(true);
                    dialog.setGridSelectListener(new CustomDialog.GridSelectListener() {
                        @Override
                        public void onGridSelectListener(int size) {
                            currentBoard.setGridSize(size);
                            currentBoard.setAddEditIconScreenPassed();
                            database.updateBoardIntoDatabase(currentBoard);
                            Intent intent = new Intent(AddEditIconAndCategory.this, RepositionIcons.class);
                            intent.putExtra(BOARD_ID, boardId);
                            startActivity(intent);
                            finish();
                        }
                    });

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
    private void prepareLevelSelectPane() {

        categoryAdapter =new LevelSelectorAdapter(this,categories);
        categoryRecycler.setAdapter(categoryAdapter);
        categoryAdapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if(selectedPosition!=position) {
                            selectedPosition = position;
                            prepareIconPane(position,currentMode);
                }
            }
        });

    }
    /**
     * Creates and fetches the left pane for icon select
     */
    private void targetLevelSelectPane() {

        categoryAdapter =new LevelSelectorAdapter(this,categories);
        categoryRecycler.setAdapter(categoryAdapter);
        categoryAdapter.selectedPosition = (categories.size()-1);
        selectedPosition = categoryAdapter.selectedPosition;
        categoryAdapter.notifyDataSetChanged();
        categoryAdapter.setOnItemClickListner(new LevelSelectorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                if(selectedPosition!=position) {
                    selectedPosition = position;
                    prepareIconPane(position,currentMode);
                }
            }
        });
        if(categoryRecycler.getLayoutManager()!=null)
        categoryRecycler.getLayoutManager().smoothScrollToPosition(categoryRecycler,null,(categories.size()-1));
        prepareIconPane(categoryAdapter.selectedPosition,currentMode);

    }
    private void prepareIconPane(int position,int mode) {
        iconList = categoryManager.getAllChildOfCategory(position);
        if(iconList.size()>0)
        if(mode==ADD_EDIT_ICON_MODE)
            iconAdapter = new IconSelectorAdapter(this,iconList, ADD_EDIT_ICON_MODE);
        else if(mode ==EDIT_ICON_MODE)
            iconAdapter = new IconSelectorAdapter(this,iconList,EDIT_ICON_MODE);

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
            public void onPositiveButtonClick(final String name, final Bitmap bitmap, JellowVerbiageModel verbiage) {

                new VerbiageEditor(AddEditIconAndCategory.this, VerbiageEditor.VERBIAGE_MODE, new VerbiageEditorInterface() {
                    @Override
                    public void onPositiveButtonClick(String noString, Bitmap noBitmap, JellowVerbiageModel verbiageList) {
                        //Don't use local scope name and bitmap because they are null
                        saveEditedIcon(name,bitmap,parent1,parent2,parent3, verbiageList);
                        if (thisIcon.isCustomIcon())
                            deleteImageFromStorage(thisIcon.IconDrawable,AddEditIconAndCategory.this);
                    }

                    @Override
                    public void onPhotoModeSelect(int position) {
                        //DO NOTHING
                    }

                    @Override
                    public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {
                        //DO NOTHING
                    }
                }).presentVerbiage(verbiageDatbase.getVerbiageById(thisIcon.IconDrawable)).initVerbiageDialog(name).show();

            }

            @Override
            public void onPhotoModeSelect(int position) {
                firePhotoIntent(position);
            }

            @Override
            public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {
                    revListener = verbiageEditorReverseInterface;
            }
        });

       dialog.initAddEditDialog(thisIcon.IconTitle);
       dialog.setAlreadyPresentIcon(thisIcon);
       dialog.setPositiveButtonText("Next");
       dialog.setTitleText(thisIcon.IconTitle);

       dialog.show();
    }

    /**
     * This function will save the edited Icon
     * @param name Name of the Icon
     * @param bitmapArray image of the Icon
     * @param p1 parent 1
     * @param p2 parent 2
     * @param p3 parent 3
     */
    private void saveEditedIcon(String name, Bitmap bitmapArray, int p1, int p2, int p3, JellowVerbiageModel verbiage) {
        int id  = (int)Calendar.getInstance().getTime().getTime();
        JellowIcon icon = new JellowIcon(name,id+"",-1,-1,id);
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
        currentBoard.setBoardIconModel(modelManager.getModel());
        verbiageDatbase.addNewVerbiage(id+"",verbiage);
        currentBoard.addCustomIconID(id+"");
        targetLevelSelectPane();

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
                initBoardEditAddDialog(mode,"Icon Name");
            else if(mode==ADD_CATEGORY)
                initBoardEditAddDialog(mode,"Category Name");
            else if(mode==EDIT_ICON)
            {
                prepareIconPane(selectedPosition,EDIT_ICON_MODE);
                currentMode = EDIT_ICON_MODE;
                invalidateOptionsMenu();
            }
        }
    }

    private void initBoardEditAddDialog(final int mode,String editTextHint) {
        iconImageSelected =false;
        VerbiageEditor dialog = new VerbiageEditor(this, VerbiageEditor.ADD_EDIT_ICON_MODE, new VerbiageEditorInterface() {
            @Override
            public void onPositiveButtonClick(final String name, final Bitmap bitmap, JellowVerbiageModel verbiageList) {
                new VerbiageEditor(AddEditIconAndCategory.this, VerbiageEditor.VERBIAGE_MODE, new VerbiageEditorInterface() {
                    @Override
                    public void onPositiveButtonClick(String noName, Bitmap noBitmap, JellowVerbiageModel verbiageList) {
                        //DON"T USE LOCAL SCOPE VARIABLE HERE BECAUSE THEY'RE NULL
                        if (mode == ADD_CATEGORY)
                            addNewCategory(name, bitmap,verbiageList);
                        else if (mode == ADD_ICON)
                            addNewIcon(name, bitmap, selectedPosition,verbiageList);
                    }

                    @Override
                    public void onPhotoModeSelect(int position) {

                    }

                    @Override
                    public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {

                    }
                }).initVerbiageDialog(name).show();
            }

            @Override
            public void onPhotoModeSelect(int position) {
                firePhotoIntent(position);
            }

            @Override
            public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {
                        revListener = verbiageEditorReverseInterface;
            }
        });
        dialog.initAddEditDialog(editTextHint);
        dialog.show();
    }

    private void firePhotoIntent(int position) {
        if(position==0)
        {
            if(checkPermissionForCamera()&&checkPermissionForStorageRead()) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(AddEditIconAndCategory.this);
            }
            else
            {
                final String [] permissions=new String []{ Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(AddEditIconAndCategory.this, permissions, CAMERA_REQUEST);
            }

        }
        else if(position==1)
        {
            Intent intent = new Intent(AddEditIconAndCategory.this,BoardSearch.class);
            intent.putExtra(BoardSearch.SEARCH_MODE,BoardSearch.ICON_SEARCH);
            startActivityForResult(intent,LIBRARY_REQUEST);
        }
    }

    private void addNewCategory(String name, Bitmap bitmap,JellowVerbiageModel verbiage) {
        int id  = (int)Calendar.getInstance().getTime().getTime();
        JellowIcon icon = new JellowIcon(name,""+id,-1,-1,id);
        if(iconImageSelected)
        storeImageToStorage(bitmap,id+"",this);
        boardModel.addChild(icon);
        categoryManager = new CategoryManager(boardModel);
        categories = categoryManager.getAllCategories();
        verbiageDatbase.addNewVerbiage( id+"",verbiage);
        modelManager.setModel(boardModel);
        currentBoard.setBoardIconModel(modelManager.getModel());
        currentBoard.addCustomIconID(id+"");
        targetLevelSelectPane();
    }

    /**
     * This funtion takes name and bitmap array of a icon to be added and generates
     * an icon for it and adds it to the postion and scrolls to it.
     * @param name Name of the Icon
     * @param bitmap bitmap array holding the image
     * @param selectedPosition postion on which new icon is to be added.
     */
    private void addNewIcon(String name, Bitmap bitmap, int selectedPosition,JellowVerbiageModel verbiage) {
        int id  = (int)Calendar.getInstance().getTime().getTime();
        JellowIcon icon = new JellowIcon(name,id+"",-1,-1,id);
        if(iconImageSelected)
        storeImageToStorage(bitmap,id+"",this);
        boardModel.getChildren().get(selectedPosition).addChild(icon);
        if(iconRecycler.getLayoutManager()!=null)
        iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler,null,(boardModel.getChildren().get(selectedPosition).getChildren().size()-1));
        categoryManager = new CategoryManager(boardModel);
        prepareIconPane(selectedPosition,ADD_EDIT_ICON_MODE);
        modelManager.setModel(boardModel);
        currentBoard.setBoardIconModel(modelManager.getModel());
        verbiageDatbase.addNewVerbiage(icon.IconDrawable,verbiage);
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
                        .start(AddEditIconAndCategory.this);
            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            if (requestCode == LIBRARY_REQUEST) {
                String fileName = data.getStringExtra("result");
                if(fileName!=null)
                    revListener.onPhotoResult(null,requestCode,fileName);//mPhotoIntentResult.onPhotoIntentResult(null, requestCode, fileName);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap1 = result.getBitmap();
                if(bitmap1!=null)
                {
                    revListener.onPhotoResult(result.getBitmap(),requestCode,null);
                    iconImageSelected =true;
                    //mPhotoIntentResult.onPhotoIntentResult(result.getBitmap(), requestCode,null);
                }
                else {
                    try {
                        iconImageSelected =true;
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        revListener.onPhotoResult(bitmap1,requestCode,null);// mPhotoIntentResult.onPhotoIntentResult(bitmap1,requestCode,null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("CROP_IMAGE_ERROR",error.getMessage());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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
            case android.R.id.home: startActivity(new Intent(this, MyBoards.class));finish(); break;

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
                list.add(model.getChildren().get(i).getIcon().IconTitle);
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

}
