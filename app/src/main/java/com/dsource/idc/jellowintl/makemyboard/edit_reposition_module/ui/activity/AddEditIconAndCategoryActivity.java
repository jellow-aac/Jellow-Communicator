package com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import com.dsource.idc.jellowintl.BaseActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.AddVerbiageDialog;
import com.dsource.idc.jellowintl.makemyboard.MyPair;
import com.dsource.idc.jellowintl.makemyboard.iActivity.BoardListActivity;
import com.dsource.idc.jellowintl.makemyboard.BoardSearchActivity;
import com.dsource.idc.jellowintl.makemyboard.HomeActivity;
import com.dsource.idc.jellowintl.makemyboard.databases.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.edit_reposition_module.ui.EditRecyclerManager;
import com.dsource.idc.jellowintl.makemyboard.icon_select_module.presenters.GenCallback;
import com.dsource.idc.jellowintl.makemyboard.interfaces.EditIconCallback;
import com.dsource.idc.jellowintl.makemyboard.interfaces.GridSelectListener;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowintl.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowintl.makemyboard.models.BoardModel;
import com.dsource.idc.jellowintl.makemyboard.utility.AddEditIconDialog;
import com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants;
import com.dsource.idc.jellowintl.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.google.android.gms.tasks.OnSuccessListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static com.dsource.idc.jellowintl.makemyboard.AddVerbiageDialog.JELLOW_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.BOARD_ID;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.CAMERA_REQUEST;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.LIBRARY_REQUEST;
import static com.dsource.idc.jellowintl.makemyboard.utility.BoardConstants.NORMAL_MODE;
import static com.dsource.idc.jellowintl.makemyboard.utility.ImageStorageHelper.storeImageToStorage;

public class AddEditIconAndCategoryActivity extends BaseActivity {

    private static final int SEARCH = 13213;

    private BoardModel currentBoard;
    private boolean iconImageSelected = false;
    private EditRecyclerManager manager;
    private VerbiageEditorReverseInterface revListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNotchDevice())
            setContentView(R.layout.activity_levelx_layout_notch);
        else
            setContentView(R.layout.activity_levelx_layout);


        try {
            String boardId = "";

            if (getIntent().getExtras() != null)
                boardId = getIntent().getExtras().getString(BOARD_ID);
            BoardDatabase database = new BoardDatabase(getAppDatabase());
            currentBoard = database.getBoardById(boardId);
            if (currentBoard != null) {
                setUpIconManager();
            }
        } catch (NullPointerException e) {
            Toast.makeText(this, "Some error occured", Toast.LENGTH_LONG).show();
        }
        enableNavigationBack();

        if (getSupportActionBar() != null) {
            enableNavigationBack();
            getSupportActionBar().setTitle(currentBoard.getBoardName());
            getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        }

        //Disable Expressive Icons for this activity
        findViewById(R.id.save_button).setVisibility(View.VISIBLE);
        findViewById(R.id.keyboard).setAlpha(.5f);
        findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentBoard.setSetupStatus(BoardModel.STATUS_L3);
                BoardDatabase database = new BoardDatabase(getAppDatabase());
                database.updateBoardIntoDatabase(currentBoard);
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra(BOARD_ID, currentBoard.getBoardId());
                startActivity(intent);
                finish();

            }
        });

        findViewById(R.id.et).setVisibility(View.GONE);
        findViewById(R.id.ttsbutton).setVisibility(View.GONE);
    }


    private void setUpIconManager() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        manager = new EditRecyclerManager(this, recyclerView, currentBoard);
        manager.setAddIconCategoryListener(new GenCallback<Void>() {
            @Override
            public void callBack(Void object) {
                initAddNewIconCategoryDialog();
            }
        });

        manager.setOnIconEditCallback(new EditIconCallback() {
            @Override
            public void onIconEditClicked(JellowIcon iconToBeEdited, int positionInTheList) {
                initEditIconDialog(iconToBeEdited, positionInTheList);
            }
        });

    }

    private void getFlags(JellowIcon icon, boolean isNewCustom) {
        String FETCH_ENABLED = "NULL";
        String IS_PRIMARY = "NULL";
        if (!isNewCustom && icon.isCustomIcon()) {
            FETCH_ENABLED = icon.getVerbiageId();
        } else {
            FETCH_ENABLED = icon.getVerbiageId();
            IS_PRIMARY = "TRUE";
            //TODO reset id
        }
    }


    private void initAddNewIconCategoryDialog() {
        iconImageSelected = false;

        final String FETCH_ENABLED = "NULL";
        final String IS_PRIMARY = "NULL";

        final int id = (int) Calendar.getInstance().getTime().getTime();
        AddEditIconDialog dialog = new AddEditIconDialog(this, new VerbiageEditorInterface() {
            @Override
            public void onPositiveButtonClick(final String name, final Bitmap bitmap, final int iconType) {
                Intent intent = new Intent(AddEditIconAndCategoryActivity.this, AddVerbiageDialog.class);
                Bundle bundle = new Bundle();
                intent.putExtra(BOARD_ID, currentBoard.getBoardId());
                bundle.putSerializable(JELLOW_ID, new JellowIcon(name, "" + id, -1, -1, id));

                //Both flags are unset for new icons or categories
                intent.putExtra(AddVerbiageDialog.FETCH_FLAG, FETCH_ENABLED);
                intent.putExtra(AddVerbiageDialog.IS_PRIMARY_FLAG, IS_PRIMARY);

                intent.putExtras(bundle);
                startActivity(intent);
                AddVerbiageDialog.callback = new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        if (iconType == BoardConstants.NORMAL_TYPE)
                            addNewIcon(id, name, bitmap);
                        else
                            addNewCategory(id, name, bitmap);
                        Toast.makeText(getApplicationContext(), "Successfully saved", Toast.LENGTH_LONG).show();
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
        });
        dialog.initAddEditDialog();
        dialog.showDialog();
    }

    private void initEditIconDialog(final JellowIcon thisIcon, final int positionInTheList) {

        iconImageSelected = false;
        final int id = (int) Calendar.getInstance().getTime().getTime();

        AddEditIconDialog dialog = new AddEditIconDialog(this, new VerbiageEditorInterface() {
            @Override
            public void onPositiveButtonClick(final String name, final Bitmap bitmap, final int iconType) {
                //Setting new id if base icon is being edited, if not done then it will replace main verbiage
                String FETCH_ENABLED;
                String IS_PRIMARY;
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

                Intent intent = new Intent(AddEditIconAndCategoryActivity.this, AddVerbiageDialog.class);
                Bundle bundle = new Bundle();
                intent.putExtra(BOARD_ID, currentBoard.getBoardId());


                intent.putExtra(AddVerbiageDialog.FETCH_FLAG, FETCH_ENABLED);
                intent.putExtra(AddVerbiageDialog.IS_PRIMARY_FLAG, IS_PRIMARY);

                bundle.putSerializable(JELLOW_ID, thisIcon);
                intent.putExtras(bundle);
                startActivity(intent);
                AddVerbiageDialog.callback = new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        int removedFrom = manager.remove(thisIcon);
                        saveEditedIcon(thisIcon.getVerbiageId(), name, bitmap, positionInTheList);
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
        });
        dialog.setAlreadyPresentIcon(thisIcon);
        dialog.setTitleText(thisIcon.getIconTitle());
        dialog.initAddEditDialog();
        dialog.showDialog();
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
                        .start(AddEditIconAndCategoryActivity.this);
            } else {

                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
    }

    private void firePhotoIntent(int position) {
        if (position == 0) {
            if (checkPermissionForCamera() && checkPermissionForStorageRead()) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(AddEditIconAndCategoryActivity.this);
            } else {
                final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(AddEditIconAndCategoryActivity.this, permissions, CAMERA_REQUEST);
            }

        } else if (position == 1) {
            Intent intent = new Intent(AddEditIconAndCategoryActivity.this, BoardSearchActivity.class);
            intent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.ICON_SEARCH);
            intent.putExtra(BOARD_ID, currentBoard.getBoardId());
            startActivityForResult(intent, LIBRARY_REQUEST);
        }
    }

    private void addNewCategory(int id, String name, Bitmap bitmap) {
        JellowIcon icon = new JellowIcon(name + "…", "" + id, -1, -1, id);
        icon.setVerbiageId(id + "");
        icon.setType(BoardConstants.CATEGORY_TYPE);
        if (iconImageSelected)
            storeImageToStorage(bitmap, id + "", this);
        currentBoard.getIconModel().addChild(icon);
        currentBoard.addCustomIconID(id + "");
        //TODO Look for replacement of this code selectedPosition = categories.size()-1;
        manager.refresh();
        manager.scrollDown();

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
        currentBoard.getIconModel().addChild(icon);
        currentBoard.addCustomIconID(id + "");
        //TODO Look for replacement of this code selectedPosition = categories.size()-1;
        manager.refresh();
        manager.scrollDown();
    }

    private void saveEditedIcon(String id, String name, Bitmap bitmapArray, int positionInTheList) {

        JellowIcon icon = new JellowIcon(name, id, -1, -1, Integer.parseInt(id));
        icon.setVerbiageId(id);
        storeImageToStorage(bitmapArray, id + "", this);
        currentBoard.getIconModel().getChildren().get(positionInTheList).setIcon(icon);
        manager.refresh();
        manager.scrollToPosition(positionInTheList);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LIBRARY_REQUEST) {
            if (resultCode == RESULT_OK) {
                String fileName = data.getStringExtra("result");
                if (fileName != null) {
                    iconImageSelected = true;
                    revListener.onPhotoResult(null, requestCode, fileName);//mPhotoIntentResult.onPhotoIntentResult(null, requestCode, fileName);
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
                Log.d("CROP_IMAGE_ERROR", error.getMessage());
            }
        }
        if (requestCode == SEARCH) {
            if (resultCode == RESULT_OK) {
                JellowIcon icon = (JellowIcon) data.getSerializableExtra(getString(R.string.search_result));
                MyPair<Integer, Integer> iconPos = currentBoard.getIconModel().getIconPosition(icon);
                if (iconPos.getFirst() != -1)
                    manager.highlightIcon(iconPos);

            }
        }

    }


    private void showGridDialog() {
        new CustomDialog(this, currentBoard.getLanguage(), new GridSelectListener() {
            @Override
            public void onGridSelectListener(int size) {
                currentBoard.setGridSize(size);
                manager.changeGridSize(size);
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(this, BoardListActivity.class));
                finishAffinity();
                if (manager != null) getSession().setCurrentBoardLanguage("");
                break;
            case R.id.grid_size:
                showGridDialog();
                break;
            case R.id.search:
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
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!manager.goBack()) {
            CustomDialog dialog = new CustomDialog(this, CustomDialog.NORMAL, currentBoard.getLanguage());
            dialog.setText(getResources().getString(R.string.exit_warning));
            dialog.setOnPositiveClickListener(new CustomDialog.OnPositiveClickListener() {
                @Override
                public void onPositiveClickListener() {
                    finish();
                }
            });
            dialog.show();
        }

    }

    private void searchInBoard() {
        Intent searchIntent = new Intent(this, BoardSearchActivity.class);
        searchIntent.putExtra(BoardSearchActivity.SEARCH_MODE, BoardSearchActivity.SEARCH_IN_BOARD);
        searchIntent.putExtra(BOARD_ID, currentBoard.getBoardId());
        startActivityForResult(searchIntent, SEARCH);
    }
}
