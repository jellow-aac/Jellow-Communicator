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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.BaseActivity;
import com.dsource.idc.jellowintl.make_my_board_module.activity.BoardSearchActivity;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.ListItem;
import com.dsource.idc.jellowintl.make_my_board_module.expandable_recycler_view.SimpleListAdapter;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.AddIconCallback;
import com.dsource.idc.jellowintl.make_my_board_module.interfaces.OnPhotoResultCallBack;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.GlideApp;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.dsource.idc.jellowintl.factories.IconFactory.EXTENSION;
import static com.dsource.idc.jellowintl.factories.PathFactory.getIconPath;
import static com.dsource.idc.jellowintl.make_my_board_module.custom_dialogs.DialogAddVerbiage.JELLOW_ID;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.CAMERA_REQUEST;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.BoardConstants.LIBRARY_REQUEST;
import static com.dsource.idc.jellowintl.make_my_board_module.utility.ImageStorageHelper.storeImageToStorage;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;
import static com.dsource.idc.jellowintl.utility.Analytics.startMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.stopMeasuring;
import static com.dsource.idc.jellowintl.utility.Analytics.validatePushId;

public class DialogAddEditIcon extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {

    //Static variables to set the modes
    private Context context;
    private boolean isVisible = false;
    private TextView saveButton;
    private EditText titleText;
    private TextView cancelSaveBoard;
    private ImageView editBoardIconButton;
    private ImageView iconImage;
    private ListView listView;
    private JellowIcon thisIcon = null;
    private boolean iconImageSelected = false;
    private static AddIconCallback callback;
    private String boardId;
    private OnPhotoResultCallBack revListener;
    private boolean addIcon = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_edit_icon);

        boardId = getIntent().getStringExtra(BOARD_ID);
        context = this;

        initViews();
        initAddEditDialog();

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable(JELLOW_ID) != null) {
            JellowIcon icon = (JellowIcon) getIntent().getExtras().getSerializable(JELLOW_ID);
            if (icon != null)
                setAlreadyPresentIcon(icon);
        }
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
        stopMeasuring(DialogAddEditIcon.class.getSimpleName());
    }

    public static void subscribe(AddIconCallback addIconCallback) {
        callback = addIconCallback;
    }


    public void setAlreadyPresentIcon(JellowIcon Icon) {
        this.thisIcon = Icon;
        this.addIcon = false;
        setIconImage();
        setTitleText(thisIcon.getIconTitle());
    }

    @SuppressLint("ResourceType")
    public void initAddEditDialog() {


        titleText.setOnFocusChangeListener(this);
        iconImage.setOnClickListener(this);
        //List on the dialog.
        listView.setVisibility(View.INVISIBLE);
        titleText.setHint(context.getResources().getString(R.string.icon_name));
        titleText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        //The list that will be shown with camera options
        final ArrayList<ListItem> list = new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray = context.getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem(context.getResources().getString(R.string.photos), mArray.getDrawable(0)));
        list.add(new ListItem(context.getResources().getString(R.string.library), mArray.getDrawable(1)));
        SimpleListAdapter adapter = new SimpleListAdapter(context, list);
        listView.setAdapter(adapter);
        revListener = new OnPhotoResultCallBack() {
            @Override
            public void onPhotoResult(Bitmap bitmap, int code, String fileName) {
                if (code != LIBRARY_REQUEST) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(context)
                            .asBitmap()
                            .load(stream.toByteArray())
                            .placeholder(R.drawable.ic_board_person)
                            .apply(RequestOptions
                                    .circleCropTransform()).into(iconImage);
                } else {
                    GlideApp.with(context).load(getIconPath(context, fileName + EXTENSION))
                            .into(iconImage);
                }
                iconImage.setBackground(context.getResources().getDrawable(R.drawable.icon_back_grey));
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setVisibility(View.INVISIBLE);
                firePhotoIntent(position);
            }
        });

    }

    public void setTitleText(String name) {
        if (titleText != null) titleText.setText(name);
    }


    private void initViews() {

        //Views related to the Dialogs
        titleText = findViewById(R.id.board_name);
        saveButton = findViewById(R.id.save_board);
        cancelSaveBoard = findViewById(R.id.cancel_save_board);
        editBoardIconButton = findViewById(R.id.edit_board);
        iconImage = findViewById(R.id.board_icon);
        listView = findViewById(R.id.camera_list);
        findViewById(R.id.parent).setOnClickListener(this);
        findViewById(R.id.icon_container).setOnClickListener(this);

        iconImage.setOnClickListener(this);

        //Setting the image icon
        if (thisIcon != null)
            setIconImage();

        //Click Listeners
        saveButton.setOnClickListener(this);
        editBoardIconButton.setOnClickListener(this);
        cancelSaveBoard.setOnClickListener(this);
        findViewById(R.id.parent).setOnClickListener(this);
        findViewById(R.id.touch_inside).setOnClickListener(this);
    }

    /**
     * This funtion takes name and bitmap array of a icon to be added and generates
     * an icon for it and adds it to the postion and scrolls to it.
     *
     * @param name   Name of the Icon
     * @param bitmap bitmap array holding the image
     */
    private void addNewIcon(int id, String name, Bitmap bitmap) {
        JellowIcon icon = new JellowIcon(name, "" + id, -1, -1, id);
        icon.setVerbiageId(id + "");
        if (iconImageSelected)
            storeImageToStorage(bitmap, id + "", this);
        if (callback != null)
            callback.onAddedSuccessfully(icon);
    }

    private void saveEditedIcon(String id, String name, Bitmap bitmapArray) {
        JellowIcon icon = new JellowIcon(name, id, -1, -1, Integer.parseInt(id));
        icon.setVerbiageId(id);
        storeImageToStorage(bitmapArray, id + "", this);
        if (callback != null)
            callback.onAddedSuccessfully(icon);
        callback = null;
    }

    /**
     * Sets image for the Dialog
     */
    private void setIconImage() {

        if (thisIcon.isCustomIcon())//Is a custom Icon
        {
            File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath();
            GlideApp.with(context)
                    .load(path + "/" + thisIcon.getIconDrawable() + ".png")
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .dontAnimate()
                    .placeholder(R.drawable.ic_board_person)
                    .into(iconImage);
            iconImageSelected = true;
        } else {
            GlideApp.with(context).load(getIconPath(context, thisIcon.getIconDrawable() + EXTENSION))
                    .skipMemoryCache(true)
                    .into(iconImage);
            iconImageSelected = true;
        }

    }


    @Override
    public void onClick(View v) {

        if (listView.getVisibility() == View.VISIBLE) listView.setVisibility(View.INVISIBLE);

        editBoardIconButton.bringToFront();

        if (v == null) return;

        if (v == editBoardIconButton) {
            if (isVisible)
                listView.setVisibility(View.INVISIBLE);
            else
                listView.setVisibility(View.VISIBLE);
            isVisible = !isVisible;
        } else if (v == saveButton)
            initSave();
        else if (v == cancelSaveBoard) {
            finish();

        }

    }

    private void initSave() {

        if(!iconImageSelected) {
            Toast.makeText(context,getString(R.string.please_select_icon),Toast.LENGTH_SHORT).show();
            return;
        }

        final int id = (int) Calendar.getInstance().getTimeInMillis();

        String FETCH_ENABLED;
        String IS_PRIMARY;


        Intent intent = new Intent(context, DialogAddVerbiage.class);
        Bundle bundle = new Bundle();
        intent.putExtra(BOARD_ID, boardId);


        final Bitmap bitmap = ((BitmapDrawable) iconImage.getDrawable()).getBitmap();

        if (titleText.getText().toString().equals("")) {
            Toast.makeText(context, context.getResources().getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
            return;
        }

        final String name = titleText.getText().toString();

        if (addIcon) {

            FETCH_ENABLED = "NULL";
            IS_PRIMARY = "NULL";
            bundle.putSerializable(JELLOW_ID, new JellowIcon(name, "" + id, -1, -1, id));

        } else {

            if (thisIcon.isCustomIcon()) {
                //Fetch flag is set for custom icon update.
                FETCH_ENABLED = thisIcon.getVerbiageId();
                IS_PRIMARY = "NULL";
                thisIcon.setIconTitle(name);
            } else {
                //Both flags are set for primary icon to be edited
                FETCH_ENABLED = thisIcon.getVerbiageId();
                IS_PRIMARY = "TRUE";
                thisIcon.setVerbiageId(id + "");
                thisIcon.setDrawable(id + "");
                thisIcon.setIconTitle(name);


            }
            bundle.putSerializable(JELLOW_ID, thisIcon);
        }

        //Both flags are unset for new icons or categories
        intent.putExtra(DialogAddVerbiage.FETCH_FLAG, FETCH_ENABLED);
        intent.putExtra(DialogAddVerbiage.IS_PRIMARY_FLAG, IS_PRIMARY);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();

        DialogAddVerbiage.callback = new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (addIcon)
                    addNewIcon(id, name, bitmap);
                else
                    saveEditedIcon(thisIcon.getVerbiageId(), name, bitmap);
            }
        };
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        onClick(null);
    }

    private void firePhotoIntent(int position) {
        if (position == 0) {
            //Check if the device has a camera hardware
            if (hasCameraHardware()) {
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
            } else {
                Toast.makeText(this, getResources().getString(R.string.camera_missing), Toast.LENGTH_LONG).show();
            }

        } else if (position == 1) {
            Intent intent = new Intent(this, BoardSearchActivity.class);
            intent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.ICON_SEARCH);
            intent.putExtra(BOARD_ID, boardId);
            startActivityForResult(intent, LIBRARY_REQUEST);
        }
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
        if (requestCode == LIBRARY_REQUEST) {
            if (resultCode == RESULT_OK) {
                String fileName = data.getStringExtra("result");
                if (fileName != null) {
                    iconImageSelected = true;
                    revListener.onPhotoResult(null, requestCode, fileName);
                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
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
                if (error.getMessage() != null)
                    Log.d("CROP_IMAGE_ERROR", error.getMessage());
            }
        }

    }


}
