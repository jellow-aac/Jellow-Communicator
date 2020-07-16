package com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs;

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
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.BaseActivity;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.make_my_board_module.activity.BoardSearchActivity;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.BoardIconModel;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.ListItem;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.BoardModel;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.SimpleListAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnPhotoResultCallBack;
import com.dsource.idc.jellowintl.make_my_board_module.managers.BoardLanguageManager;
import com.dsource.idc.jellowintl.make_my_board_module.models.AddBoardDialogModel;
import com.dsource.idc.jellowintl.make_my_board_module.presenter_interfaces.IAddBoardDialogPresenter;
import com.dsource.idc.jellowintl.make_my_board_module.view_interfaces.IAddBoardDialogView;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.GlideApp;
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
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.CAMERA_REQUEST;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.LIBRARY_REQUEST;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.ImageStorageHelper.storeImageToStorage;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class DialogAddBoard extends BaseActivity implements IAddBoardDialogView,View.OnClickListener, View.OnFocusChangeListener {

    private IAddBoardDialogPresenter mPresenter;
    private Context mContext;
    private OnPhotoResultCallBack reverseInterface;
    private boolean iconImageSelected = false;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_board);
        mContext = this;
        mPresenter = new AddBoardDialogModel(getAppDatabase());
        mPresenter.attachView(this);

        //Fetch Board Id
        if (getIntent().getStringExtra(BOARD_ID) != null) {
            String id = getIntent().getStringExtra(BOARD_ID);
            if (TextUtils.isEmpty(id))
                setUpAddBoardDialog(null);
            else mPresenter.getBoardModel(id);
        } else
            setUpAddBoardDialog(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()){
            resetAnalytics(this, getSession().getUserId());
        }
        // Start measuring user app screen timer.
        startMeasuring();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Check if pushId is older than 24 hours (86400000 millisecond).
        // If yes then create new pushId (user session)
        // If no then do not create new pushId instead user existing and
        // current session time is saved.
        long sessionTime = validatePushId(getSession().getSessionCreatedAt());
        getSession().setSessionCreatedAt(sessionTime);

        // Stop measuring user app screen timer.
        stopMeasuring(DialogAddBoard.class.getSimpleName());
    }

    @SuppressLint("ResourceType")
    private void setUpAddBoardDialog(final BoardModel board) {
        final ImageView boardIcon = findViewById(R.id.board_icon);
        final Button saveButton = findViewById(R.id.save_button);
        final Button cancel = findViewById(R.id.cancel_button);
        final ImageView imageChange = findViewById(R.id.edit_image);
        final EditText boardName = findViewById(R.id.board_name);
        final Spinner languageSelect = findViewById(R.id.langSelectSpinner);

        findViewById(R.id.parent).setOnClickListener(this);
        findViewById(R.id.touch_inside).setOnClickListener(this);
        boardName.setOnFocusChangeListener(this);
        boardIcon.setOnClickListener(this);

        boardName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(60)});
        listView = findViewById(R.id.camera_list);
        ArrayList<String> languageList = new ArrayList<>(Arrays.asList(LanguageFactory.getAvailableLanguages()));

        for (String lang : SessionManager.NoTTSLang)
            languageList.remove(SessionManager.LangValueMap.get(lang));

        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP){
            for (String lang : SessionManager.NOT_SUPPORTED_API_BELOW_21)
                languageList.remove(SessionManager.LangValueMap.get(lang));
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, languageList);
        arrayAdapter.setDropDownViewResource(R.layout.popup_menu_item);
        languageSelect.setAdapter(arrayAdapter);

        if (board != null) {
            boardName.setText(board.getBoardName());
            File en_dir = mContext.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();

            iconImageSelected = true;

            GlideApp.with(mContext)
                    .load(path + "/" + board.getBoardId() + ".png")
                    .placeholder(R.drawable.ic_board_person)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .transform(new CircleCrop())
                    .dontAnimate()
                    .into(boardIcon);
            int position = 0;
            for (int i = 0; i < languageList.size(); i++) {
                if (board.getLanguage().equals(SessionManager.LangMap.get(languageList.get(i))))
                    position = i;
            }
            languageSelect.setSelection(position);
            languageSelect.setVisibility(View.GONE);
            TextView tvLanguage = findViewById(R.id.tv_language);
            tvLanguage.setVisibility(View.VISIBLE);
            tvLanguage.setText(SessionManager.LangValueMap.get(board.getLanguage()));
            saveButton.setText(getString(R.string.txtSave));
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (boardName.getText().toString().trim().equals("")) {
                    Toast.makeText(mContext, getResources().getString(R.string.please_enter_name), Toast.LENGTH_LONG).show();
                    return;
                }

                if(!iconImageSelected){
                    Toast.makeText(mContext, getResources().getString(R.string.please_select_icon), Toast.LENGTH_LONG).show();
                    return;
                }

                //Returns code for each language in board
                String langCode = languageSelect.getSelectedItem().toString();
                if (board == null)
                    saveNewBoard(boardName.getText().toString().trim(), ((BitmapDrawable) boardIcon.getDrawable()).getBitmap(), langCode);
                else
                    updateBoardDetails(board, boardName.getText().toString().trim(), ((BitmapDrawable) boardIcon.getDrawable()).getBitmap());
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listView.getVisibility() == View.VISIBLE)
                    listView.setVisibility(View.INVISIBLE);
                else{
                    listView.setVisibility(View.VISIBLE);
                    listView.requestFocus();
                }
            }
        });

        //List on the dialog.
        listView.setVisibility(View.INVISIBLE);
        if (board != null) {
            boardName.setText(board.getBoardName());
        }
        //The list that will be shown with camera options
        final ArrayList<ListItem> list = new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray = getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem(getResources().getString(R.string.photos), mArray.getDrawable(0)));
        list.add(new ListItem(getResources().getString(R.string.library), mArray.getDrawable(1)));
        SimpleListAdapter adapter = new SimpleListAdapter(this, list);
        listView.setAdapter(adapter);
        reverseInterface = new OnPhotoResultCallBack() {
            @Override
            public void onPhotoResult(Bitmap bitmap, int code, String fileName) {
                if (code != LIBRARY_REQUEST) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    GlideApp.with(mContext).load(stream.toByteArray()).
                            transform(new CircleCrop()).
                            placeholder(R.drawable.ic_board_person).
                            error(R.drawable.ic_board_person).skipMemoryCache(true).
                            diskCacheStrategy(DiskCacheStrategy.NONE).
                            into(boardIcon);
                } else {

                    GlideApp.with(mContext).load(getIconPath(mContext, fileName + EXTENSION))
                            .into(boardIcon);
                }
            }
        };
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.INVISIBLE);
                firePhotoIntent(position);
            }
        });

        View.OnTouchListener spinnerOnTouch = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    onClick(null);
                }
                return false;
            }
        };
        View.OnKeyListener spinnerOnKey = new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                    onClick(null);
                    return true;
                } else {
                    return false;
                }
            }
        };


        languageSelect.setOnTouchListener(spinnerOnTouch);
        languageSelect.setOnKeyListener(spinnerOnKey);

    }


    private void firePhotoIntent(int position) {
        if (position == 0) {
            //Check if the device has a camera hardware
            if(hasCameraHardware()) {
                if (checkPermissionForCamera() && checkPermissionForStorageRead()) {
                    CropImage.activity()
                            .setAspectRatio(1, 1)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setFixAspectRatio(true)
                            .start(this);
                } else {
                    final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                    ActivityCompat.requestPermissions(this, permissions, CAMERA_REQUEST);
                }
            }else{
                Toast.makeText(this, getResources().getString(R.string.camera_missing),Toast.LENGTH_LONG).show();
            }

        } else if (position == 1) {
            Intent intent = new Intent(this, BoardSearchActivity.class);
            intent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.BASE_ICON_SEARCH);
            startActivityForResult(intent, LIBRARY_REQUEST);
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


        if (requestCode == CAMERA_REQUEST)
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
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

    private void updateBoardDetails(BoardModel board, String name, Bitmap boardIcon) {

        if (board != null) {
            if (!name.equals(""))
                board.setBoardName(name);
            if (iconImageSelected)
                storeImageToStorage(boardIcon, board.getBoardId(), this);
            mPresenter.updateBoard(board);
        }
    }

    private void saveNewBoard(String boardName, Bitmap boardIcon, String langCode) {
        String boardID = (int) Calendar.getInstance().getTime().getTime() + "";

        if (iconImageSelected)
            storeImageToStorage(boardIcon, boardID, this);
        BoardModel newBoard = new BoardModel();
        newBoard.setBoardName(boardName);
        newBoard.setBoardId(boardID);
        newBoard.setGridSize(4);
        newBoard.setLanguage(SessionManager.LangMap.get(langCode));
        newBoard.setIconModel(new BoardIconModel(new JellowIcon("", "", -1, -1, -1)));
        mPresenter.saveBoard(newBoard);
    }

    @Override
    public void boardRetrieved(BoardModel board) {
        setUpAddBoardDialog(board);
    }

    @Override
    public void savedSuccessfully(BoardModel boardId) {
        new BoardLanguageManager(boardId,mContext,getAppDatabase()).checkLanguageAvailabilityInBoard();
        finish();
    }

    @Override
    public void updatedSuccessfully(BoardModel board) {}

    @Override
    public void error(String msg) {
        Log.d(getClass().getSimpleName(), msg);
    }

    @Override
    public void onClick(View v) {
        if(listView.getVisibility()==View.VISIBLE)
            listView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        onClick(null);
    }
}
