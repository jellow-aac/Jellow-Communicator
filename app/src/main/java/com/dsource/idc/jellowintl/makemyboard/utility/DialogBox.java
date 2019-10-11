package com.dsource.idc.jellowintl.makemyboard.utility;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.makemyboard.BoardSearchActivity;
import com.dsource.idc.jellowintl.makemyboard.adapters.SimpleListAdapter;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.interfaces.AddBoardListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.BoardClickListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.makemyboard.models.ListItem;
import com.dsource.idc.jellowintl.models.GlobalConstants;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.ADD_BOARD;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.CAMERA_REQUEST;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.DIALOG_TYPE;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.GRID_DIALOG;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.LIBRARY_REQUEST;
import static com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper.storeImageToStorage;

public class DialogBox extends BaseActivity {


    public static GridSelectListener mGridSelectionListener;
    private static AddBoardListener mAddBoardListener;
    private VerbiageEditorReverseInterface reverseInterface;
    private boolean iconImageSelected=false;
    private BoardDatabase database;
    private BoardModel currentBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = new BoardDatabase(getAppDatabase());
        if(getIntent().getStringExtra(DIALOG_TYPE).equals(GRID_DIALOG))
        {
            setContentView(R.layout.grid_dialog);
            setUpGridDialog();
        }
        else if (getIntent().getStringExtra(DIALOG_TYPE).equals(ADD_BOARD)){
            setContentView(R.layout.add_board_layout);
            if(getIntent().getStringExtra(BOARD_ID)!=null){
                String id = getIntent().getStringExtra(BOARD_ID);
                currentBoard = database.getBoardById(id);
            }
            setUpAddBoardDialog(currentBoard);
        }
        View v =findViewById(R.id.touch_outside);
        if(v!=null) v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @SuppressLint("ResourceType")
    private void setUpAddBoardDialog(final BoardModel board) {
        final ImageView boardIcon = findViewById(R.id.board_icon);
        final Button saveButton =findViewById(R.id.save_button);
        final Button cancel =findViewById(R.id.cancel_button);
        final ImageView imageChange =findViewById(R.id.edit_image);
        final EditText boardName = findViewById(R.id.board_name);
        final Spinner languageSelect =findViewById(R.id.langSelectSpinner);
        final ListView listView =findViewById(R.id.camera_list);
        ArrayList<String> languageList = new ArrayList<>(Arrays.asList(LanguageFactory.getAvailableLanguages()));

        for(String lang:SessionManager.NoTTSLang)
            languageList.remove(SessionManager.LangValueMap.get(lang));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, languageList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSelect.setAdapter(arrayAdapter);

        if(board!=null){
            boardName.setText(board.getBoardName());
            File en_dir = DialogBox.this.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(DialogBox.this)
                    .load(path + "/" + board.getBoardId() + ".png")
                    .placeholder(R.drawable.ic_board_person)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(boardIcon);
            languageSelect.setSelection(BoardLanguageManager.getPosition(board.getLanguage()));
            languageSelect.setEnabled(false);
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(boardName.getText().toString().equals(""))
                {
                    Toast.makeText(DialogBox.this,getResources().getString(R.string.please_enter_name),Toast.LENGTH_LONG).show();
                    return;
                }
                //Returns code for each language in board
                String langCode = languageSelect.getSelectedItem().toString();
                if(board==null)
                saveNewBoard(boardName.getText().toString(),((BitmapDrawable)boardIcon.getDrawable()).getBitmap(),null,langCode);
                else
                   updateBoardDetails(board,boardName.getText().toString(),((BitmapDrawable)boardIcon.getDrawable()).getBitmap(),langCode);
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAddBoardListener!=null)
                    mAddBoardListener.onCancel();
                finish();
            }
        });

        imageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility()==View.VISIBLE)
                listView.setVisibility(View.INVISIBLE);
                else listView.setVisibility(View.VISIBLE);
            }
        });

        //List on the dialog.
        listView.setVisibility(View.GONE);
        if(board!=null){
            boardName.setText(board.getBoardName());
        }
        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray=getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem(getResources().getString(R.string.photos),mArray.getDrawable(0)));
        list.add(new ListItem(getResources().getString(R.string.library),mArray.getDrawable(1)));
        SimpleListAdapter adapter=new SimpleListAdapter(this,list);
        listView.setAdapter(adapter);
        reverseInterface =new VerbiageEditorReverseInterface() {
            @Override
            public void onPhotoResult(Bitmap bitmap, int code, String fileName) {
                if (code != LIBRARY_REQUEST) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Glide.with(DialogBox.this).load(stream.toByteArray())
                                .apply(new RequestOptions().
                                        transform(new RoundedCorners(50)).
                                        placeholder(R.drawable.ic_board_person).
                                        error(R.drawable.ic_board_person).skipMemoryCache(true).
                                        diskCacheStrategy(DiskCacheStrategy.NONE))
                                .into(boardIcon);
                } else {

                    GlideApp.with(DialogBox.this).load(getIconPath(DialogBox.this, fileName+EXTENSION))
                            .into(boardIcon);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.GONE);
                firePhotoIntent(position);
            }
        });


    }


    private void firePhotoIntent(int position) {
        if(position==0)
        {
            if(checkPermissionForCamera()&&checkPermissionForStorageRead()) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(this);
            }
            else
            {
                final String [] permissions=new String []{ Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(this, permissions, CAMERA_REQUEST);
            }

        }
        else if(position==1)
        {
            Intent intent = new Intent(this, BoardSearchActivity.class);
            intent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.BASE_ICON_SEARCH);
            startActivityForResult(intent,LIBRARY_REQUEST);
        }
    }

    private boolean checkPermissionForStorageRead() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissionForCamera() {
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
                        .start(this);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            /*
             * In this, we are collecting the name of the icon clicked on the search bar and using that to fetch the icon from the database.
             */
            if (requestCode == LIBRARY_REQUEST) {
                String fileName = data.getStringExtra("result");
                if (fileName != null) {
                    reverseInterface.onPhotoResult(null, requestCode, fileName);
                    iconImageSelected = true;
                }

            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                Uri resultUri = result.getUri();
                Bitmap bitmap1 = result.getBitmap();
                if (bitmap1 != null) {
                    reverseInterface.onPhotoResult(result.getBitmap(), requestCode, null);
                    iconImageSelected = true;
                } else {
                    try {
                        bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                        {
                            reverseInterface.onPhotoResult(bitmap1, requestCode, null);
                            iconImageSelected = true;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void updateBoardDetails(BoardModel board,String name, Bitmap boardIcon, String langCode) {

        if(board!=null)
        {
            if(!name.equals(""))
                board.setBoardName(name);
            if(iconImageSelected)
                storeImageToStorage(boardIcon,board.getBoardId(),this);
            board.setLanguage(SessionManager.LangMap.get(langCode));
            database.updateBoardIntoDatabase(board);
            if(mAddBoardListener!=null)
                mAddBoardListener.onBoardUpdated(board);
        }
    }

    private void saveNewBoard(String boardName, Bitmap boardIcon, String boardID, String langCode) {
        if(boardID==null)
            boardID = (int) Calendar.getInstance().getTime().getTime()+"";

        if(iconImageSelected)
            storeImageToStorage(boardIcon,boardID,this);
        BoardModel newBoard=new BoardModel();
        newBoard.setBoardName(boardName);
        newBoard.setBoardId(boardID);
        newBoard.setLanguage(SessionManager.LangMap.get(langCode));
        newBoard.setIconModel(new IconModel(new JellowIcon("","",-1,-1,-1)));
        if (database==null) database=new BoardDatabase(getAppDatabase());
        database.addBoardToDatabase(newBoard);
        iconImageSelected = false;
        if(mAddBoardListener!=null)
            mAddBoardListener.onBoardCreated(newBoard);
    }

    private void setUpGridDialog() {

        final ImageView GridSize1=findViewById(R.id.grid_size_1x1);
        final ImageView GridSize2=findViewById(R.id.grid_size_1X2);
        final ImageView GridSize3=findViewById(R.id.grid_size_1X3);
        final ImageView GridSize6=findViewById(R.id.grid_size_3X3);
        final ImageView GridSize4=findViewById(R.id.grid_size_2x2);


        //TODO: @Ayaz please change/update the code, wherever onGridSelectListener() is callback value is provided in MakeMyBoard module only.
        GridSize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(GlobalConstants.ONE_ICON_PER_SCREEN);
            }
        });
        GridSize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(GlobalConstants.TWO_ICONS_PER_SCREEN);
            }
        });
        GridSize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(GlobalConstants.THREE_ICONS_PER_SCREEN);
            }
        });
        GridSize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(GlobalConstants.FOUR_ICONS_PER_SCREEN);
            }
        });
        GridSize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                if(mGridSelectionListener!=null)
                mGridSelectionListener.onGridSelectListener(GlobalConstants.NINE_ICONS_PER_SCREEN);
            }
        });
    }

    public static void setAddBoardListener(AddBoardListener listener){
        mAddBoardListener = listener;
    }

}
