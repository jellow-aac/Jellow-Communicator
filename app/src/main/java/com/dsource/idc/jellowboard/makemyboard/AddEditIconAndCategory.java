package com.dsource.idc.jellowboard.makemyboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowboard.GlideApp;
import com.dsource.idc.jellowboard.Nomenclature;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.makemyboard.models.ListItem;
import com.dsource.idc.jellowboard.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowboard.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowboard.makemyboard.adapters.IconSelectorAdapter;
import com.dsource.idc.jellowboard.makemyboard.adapters.LevelSelectorAdapter;
import com.dsource.idc.jellowboard.makemyboard.models.Board;
import com.dsource.idc.jellowboard.makemyboard.models.IconModel;
import com.dsource.idc.jellowboard.makemyboard.utility.ModelManager;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.dsource.idc.jellowboard.verbiage_model.JellowVerbiageModel;
import com.dsource.idc.jellowboard.verbiage_model.VerbiageDatabaseHelper;
import com.rey.material.app.Dialog;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.dsource.idc.jellowboard.makemyboard.adapters.IconSelectorAdapter.ADD_EDIT_ICON_MODE;
import static com.dsource.idc.jellowboard.makemyboard.adapters.IconSelectorAdapter.EDIT_ICON_MODE;
import static com.dsource.idc.jellowboard.makemyboard.MyBoards.BOARD_ID;
import static com.dsource.idc.jellowboard.makemyboard.MyBoards.CAMERA_REQUEST;
import static com.dsource.idc.jellowboard.makemyboard.MyBoards.LIBRARY_REQUEST;

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
    private ArrayList<JellowIcon> iconList;
    private RelativeLayout addCategory,addIcon,editIcon;
    private MyBoards.PhotoIntentResult mPhotoIntentResult;
    private int selectedPosition=0;
    private VerbiageDatabaseHelper verbiageDatbase;
    private int currentMode = ADD_EDIT_ICON_MODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_select);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"Add/Edit Icons"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
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
            Log.d("No board id found", boardId);
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


                if(currentMode==EDIT_ICON_MODE)
                    Toast.makeText(AddEditIconAndCategory.this,"Please exit the Edit Mode first",Toast.LENGTH_SHORT).show();
                else {
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



    @SuppressLint("ResourceType")
    private void initEditModeDialog(final int parent1, final int parent2, final int parent3, final JellowIcon thisIcon) {
        @SuppressLint("InflateParams") View dialogContainerView = LayoutInflater.from(this).inflate(R.layout.edit_board_dialog, null);
        final Dialog dialogForBoardEditAdd = new Dialog(this,R.style.MyDialogBox);
        dialogForBoardEditAdd.applyStyle(R.style.MyDialogBox);
        dialogForBoardEditAdd.backgroundColor(getResources().getColor(R.color.transparent));

        //List on the dialog.
        final ListView listView=dialogContainerView.findViewById(R.id.camera_list);
        final EditText boardTitleEditText=dialogContainerView.findViewById(R.id.board_name);
        boardTitleEditText.setText(thisIcon.IconTitle);
        TextView saveBoard=dialogContainerView.findViewById(R.id.save_baord);
        TextView cancelSaveBoard=dialogContainerView.findViewById(R.id.cancel_save_baord);
        ImageView editBoardIconButton=dialogContainerView.findViewById(R.id.edit_board);
        final ImageView IconImage=dialogContainerView.findViewById(R.id.board_icon);
        IconImage.setBackground(getResources().getDrawable(R.drawable.icon_back_grey));
        listView.setVisibility(View.GONE);
        dialogForBoardEditAdd.setCancelable(false);

        if(thisIcon.parent0==-1)//Is a custom Icon
        {
            SessionManager mSession = new SessionManager(this);
            File en_dir = getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(this)
                    .load(path+"/"+ thisIcon.IconDrawable+".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .into(IconImage);
            IconImage.setBackground(getResources().getDrawable(R.drawable.icon_back_grey));
        }
        else
            {
                SessionManager mSession = new SessionManager(this);
                File en_dir = getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
                String path = en_dir.getAbsolutePath() + "/drawables";
                GlideApp.with(this)
                        .load(path+"/"+ thisIcon.IconDrawable+".png")
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(false)
                        .centerCrop()
                        .dontAnimate()
                        .into(IconImage);
        }

        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray=getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Photos",mArray.getDrawable(0)));
        list.add(new ListItem("Library ",mArray.getDrawable(2)));
        SimpleListAdapter adapter=new SimpleListAdapter(this,list);
        listView.setAdapter(adapter); 

        setOnPhotoSelectListener(new MyBoards.PhotoIntentResult() {
            @Override
            public void onPhotoIntentResult(Bitmap bitmap, int code,String fileName) {

                if(code!=LIBRARY_REQUEST)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(AddEditIconAndCategory.this)
                            .asBitmap()
                            .load(stream.toByteArray())
                            .apply(RequestOptions.
                                    circleCropTransform()).into(IconImage);
                }
                else
                {
                    SessionManager mSession = new SessionManager(AddEditIconAndCategory.this);
                    File en_dir = AddEditIconAndCategory.this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
                    String path = en_dir.getAbsolutePath() + "/drawables";
                    GlideApp.with(AddEditIconAndCategory.this)
                            .load(path+"/"+fileName+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(IconImage);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
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
        });


        saveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=boardTitleEditText.getText().toString();
                if(name.equals("")) Toast.makeText(getApplicationContext(),"Please enter name",Toast.LENGTH_SHORT).show();
                else {
                    Bitmap icon = ((BitmapDrawable) IconImage.getDrawable()).getBitmap();
                    saveEditedIcon(name, icon, parent1, parent2, parent3, thisIcon);
                    dialogForBoardEditAdd.dismiss();
                }
            }
        });
        cancelSaveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialogForBoardEditAdd.dismiss();
            }
        });
        editBoardIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible)
                    listView.setVisibility(View.GONE);
                else
                    listView.setVisibility(View.VISIBLE);
                isVisible=!isVisible;
            }
        });
        dialogForBoardEditAdd.setContentView(dialogContainerView);
        dialogForBoardEditAdd.show();
    }

    /**
     * This function will save the edited Icon
     * @param name Name of the Icon
     * @param bitmapArray image of the Icon
     * @param p1 parent 1
     * @param p2 parent 2
     * @param p3 parent 3
     * @param prevIcon previous jellow Icon
     */
    private void saveEditedIcon(String name, Bitmap bitmapArray, int p1, int p2,int p3, JellowIcon prevIcon) {
        int id  = (int)Calendar.getInstance().getTime().getTime();
        JellowIcon icon = new JellowIcon(name,id+"",-1,-1,id);
        storeImageToStorage(bitmapArray,id+"");
        if(p2==-1&&p3==-1)//if level one icon is being edited.
            boardModel.getChildren().get(p1).setIcon(icon);
        else if(p3==-10)//If level two icon is being edited
            boardModel.getChildren().get(p1).getChildren().get(p2).setIcon(icon);
        else // if level three icon is being deleted
            boardModel.getChildren().get(p1).getChildren().get(p2).getChildren().get(p3).setIcon(icon);

        categoryManager = new CategoryManager(boardModel);
        prepareIconPane(selectedPosition,EDIT_ICON_MODE);
        modelManager.setModel(boardModel);
        currentBoard.setBoardIconModel(modelManager.getModel());
        JellowVerbiageModel verbiageModel = verbiageDatbase.getVerbiageById(Nomenclature.getIconName(prevIcon,this));
        verbiageDatbase.addNewVerbiage(Nomenclature.getIconName(icon,this),getNewVerbiage(verbiageModel,name,prevIcon.IconTitle));

    }

    private JellowVerbiageModel getNewVerbiage(JellowVerbiageModel verbiageModel, String name, String iconTitle) {
        String L = verbiageModel.L;
        L = L.replace(iconTitle,name);
        String LL = verbiageModel.LL;

        LL = LL.replace(iconTitle,name);
        String Y = verbiageModel.Y;
        Y = Y.replace(iconTitle,name);
        String YY = verbiageModel.YY;
        YY = YY.replace(iconTitle,name);

        String M = verbiageModel.M;
        M = M.replace(iconTitle,name);
        String MM = verbiageModel.MM;
        MM = MM.replace(iconTitle,name);

        String S = verbiageModel.S;
        S = S.replace(iconTitle,name);
        String SS = verbiageModel.SS;
        SS = SS.replace(iconTitle,name);

        String D = verbiageModel.D;
        D = D.replace(iconTitle,name);
        String DD = verbiageModel.DD;
        DD = DD.replace(iconTitle,name);

        String N = verbiageModel.N;
        N = N.replace(iconTitle,name);
        String NN = verbiageModel.NN;
        NN = NN.replace(iconTitle,name);

        return new JellowVerbiageModel(name,name,L,LL,Y,YY,M,MM,D,DD,N,NN,S,SS);
    }

    private int gridSize() {
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

    boolean isVisible=false;
    @SuppressLint("ResourceType")
    private void initBoardEditAddDialog(final int mode,String editTextHint) {

        @SuppressLint("InflateParams") View dialogContainerView = LayoutInflater.from(this).inflate(R.layout.edit_board_dialog, null);
        final Dialog dialogForBoardEditAdd = new Dialog(this,R.style.MyDialogBox);
        dialogForBoardEditAdd.applyStyle(R.style.MyDialogBox);
        dialogForBoardEditAdd.backgroundColor(getResources().getColor(R.color.transparent));
        dialogForBoardEditAdd.setCancelable(false);

        //List on the dialog.
        final ListView listView=dialogContainerView.findViewById(R.id.camera_list);
        final EditText boardTitleEditText=dialogContainerView.findViewById(R.id.board_name);
        boardTitleEditText.setHint(editTextHint);
        TextView saveBoard=dialogContainerView.findViewById(R.id.save_baord);
        TextView cancelSaveBoard=dialogContainerView.findViewById(R.id.cancel_save_baord);
        ImageView editBoardIconButton=dialogContainerView.findViewById(R.id.edit_board);
        final ImageView IconImage=dialogContainerView.findViewById(R.id.board_icon);
        IconImage.setBackground(getResources().getDrawable(R.drawable.icon_back_grey));
        listView.setVisibility(View.GONE);
        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray=getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Photos",mArray.getDrawable(0)));
        list.add(new ListItem("Library ",mArray.getDrawable(2)));
        SimpleListAdapter adapter=new SimpleListAdapter(this,list);
        listView.setAdapter(adapter);
        setOnPhotoSelectListener(new MyBoards.PhotoIntentResult() {
            @Override
            public void onPhotoIntentResult(Bitmap bitmap, int code,String fileName) {

                if(code!=LIBRARY_REQUEST)
                {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    Glide.with(AddEditIconAndCategory.this)
                            .asBitmap()
                            .load(stream.toByteArray())
                            .apply(RequestOptions.
                                    circleCropTransform()).into(IconImage);
                }
                else //When request code is  Library
                {
                    SessionManager mSession = new SessionManager(AddEditIconAndCategory.this);
                    File en_dir = AddEditIconAndCategory.this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
                    String path = en_dir.getAbsolutePath() + "/drawables";
                    GlideApp.with(AddEditIconAndCategory.this)
                            .load(path+"/"+fileName+".png").
                            apply(RequestOptions.
                            circleCropTransform().error(R.drawable.ic_board_person))
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(IconImage);
                }

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
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
        });


        saveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=boardTitleEditText.getText().toString();
                if(name.equals("")) Toast.makeText(getApplicationContext(),"Please enter name", Toast.LENGTH_SHORT).show();
                else {
                    Bitmap icon = ((BitmapDrawable) IconImage.getDrawable()).getBitmap();
                    if (mode == ADD_CATEGORY)
                        addNewCategory(name, icon);
                    else if (mode == ADD_ICON)
                        addNewIcon(name, icon, selectedPosition);
                    dialogForBoardEditAdd.dismiss();
                }
            }
        });
        cancelSaveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { dialogForBoardEditAdd.dismiss();
            }
        });
        editBoardIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible)
                    listView.setVisibility(View.GONE);
                else
                    listView.setVisibility(View.VISIBLE);
                isVisible=!isVisible;
            }
        });
        dialogForBoardEditAdd.setContentView(dialogContainerView);
        dialogForBoardEditAdd.show();

    }

    private void addNewCategory(String name, Bitmap bitmap) {
        int id  = (int)Calendar.getInstance().getTime().getTime();
        JellowIcon icon = new JellowIcon(name,""+id,-1,-1,id);
        storeImageToStorage(bitmap,id+"");
        boardModel.addChild(icon);
        categoryManager = new CategoryManager(boardModel);
        categories = categoryManager.getAllCategories();
        verbiageDatbase.addNewVerbiage( Nomenclature.getIconName(icon,this),new JellowVerbiageModel(name));
        modelManager.setModel(boardModel);
        currentBoard.setBoardIconModel(modelManager.getModel());
        targetLevelSelectPane();
    }

    /**
     * This funtion takes name and bitmap array of a icon to be added and generates
     * an icon for it and adds it to the postion and scrolls to it.
     * @param name Name of the Icon
     * @param bitmap bitmap array holding the image
     * @param selectedPosition postion on which new icon is to be added.
     */
    private void addNewIcon(String name, Bitmap bitmap, int selectedPosition) {
        int id  = (int)Calendar.getInstance().getTime().getTime();
        JellowIcon icon = new JellowIcon(name,id+"",-1,-1,id);
        storeImageToStorage(bitmap,id+"");
        boardModel.getChildren().get(selectedPosition).addChild(icon);
        iconRecycler.getLayoutManager().smoothScrollToPosition(iconRecycler,null,(boardModel.getChildren().get(selectedPosition).getChildren().size()-1));
        categoryManager = new CategoryManager(boardModel);
        prepareIconPane(selectedPosition,ADD_EDIT_ICON_MODE);
        modelManager.setModel(boardModel);
        currentBoard.setBoardIconModel(modelManager.getModel());
        verbiageDatbase.addNewVerbiage(Nomenclature.getIconName(icon,this),new JellowVerbiageModel(name));
    }

    /**
     * To store the bitmap Image into local storage
     * @param bitmap image to be stored
     * @param fileID id of the targetImage
     * @return path of the stored Image
     */
    public String storeImageToStorage(Bitmap bitmap, String fileID) {
        FileOutputStream fos = null;
        File en_dir = this.getDir(new SessionManager(this).getLanguage(), Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();
            }
            Toast.makeText(this,""+root.getAbsolutePath(),Toast.LENGTH_LONG).show();
            File file = new File(root, fileID+ ".png");

            try {
                if(file.exists())
                {
                    file.delete();//Delete the previous image if image is a replace
                    file = new File(root,fileID+".png");
                }
                fos = new FileOutputStream(file);
                if (fos != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                    fos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Uri muri = Uri.fromFile(file);
            return muri.getPath();
        }
        return null;
    }

    private void setOnPhotoSelectListener(MyBoards.PhotoIntentResult mPhotoIntentResult) {
        this.mPhotoIntentResult=mPhotoIntentResult;
    }

    private boolean checkPermissionForStorageRead() {
        boolean okay=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                okay = false;
            }
        }

        return okay;
    }

    public boolean checkPermissionForCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
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
                    mPhotoIntentResult.onPhotoIntentResult(null, requestCode, fileName);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap bitmap1 = result.getBitmap();
                if(bitmap1!=null)
                mPhotoIntentResult.onPhotoIntentResult(result.getBitmap(), requestCode,null);
                else {
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        mPhotoIntentResult.onPhotoIntentResult(bitmap1,requestCode,null);
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
