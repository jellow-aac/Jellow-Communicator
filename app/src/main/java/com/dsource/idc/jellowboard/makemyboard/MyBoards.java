package com.dsource.idc.jellowboard.makemyboard;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.dsource.idc.jellowboard.AboutJellowActivity;
import com.dsource.idc.jellowboard.DataBaseHelper;
import com.dsource.idc.jellowboard.FeedbackActivity;
import com.dsource.idc.jellowboard.KeyboardInputActivity;
import com.dsource.idc.jellowboard.LanguageSelectActivity;
import com.dsource.idc.jellowboard.MainActivity;
import com.dsource.idc.jellowboard.ProfileFormActivity;
import com.dsource.idc.jellowboard.R;
import com.dsource.idc.jellowboard.ResetPreferencesActivity;
import com.dsource.idc.jellowboard.SettingActivity;
import com.dsource.idc.jellowboard.TutorialActivity;
import com.dsource.idc.jellowboard.makemyboard.interfaces.VerbiageEditorInterface;
import com.dsource.idc.jellowboard.makemyboard.interfaces.VerbiageEditorReverseInterface;
import com.dsource.idc.jellowboard.makemyboard.utility.BoardDatabase;
import com.dsource.idc.jellowboard.makemyboard.utility.CustomDialog;
import com.dsource.idc.jellowboard.makemyboard.adapters.BoardAdapter;
import com.dsource.idc.jellowboard.makemyboard.models.Board;
import com.dsource.idc.jellowboard.makemyboard.utility.VerbiageEditor;
import com.dsource.idc.jellowboard.utility.SessionManager;
import com.dsource.idc.jellowboard.verbiage_model.JellowVerbiageModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyBoards extends AppCompatActivity {

    public static final int DELETE_MODE =333;
    public static final int NORMAL_MODE =444;
    public static final int CAMERA_REQUEST=211;
    public static final int LIBRARY_REQUEST=411;
    public static final String IS_EDIT_MODE = "is_edit_mode";
    public int currentMode =-1;
    RecyclerView mRecyclerView;
    BoardAdapter adapter;
    ArrayList<Board> boardList;
    private final int NEW_BOARD=11;
    private final int EDIT_BOARD=22;
    HashMap<String,Board> boardHashMap;
    SQLiteDatabase db;
    public static final String BOARD_ID="Board_Id";
    Context ctx;
    BoardDatabase database;
    private VerbiageEditorReverseInterface revPhotoInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_boards);
        checkDatabase();
        boardHashMap =new HashMap<>();
        ctx=MyBoards.this;
        database=new BoardDatabase(this);
        if(getSupportActionBar()!=null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"My Boards"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        db=new DataBaseHelper(this).getWritableDatabase();
        initFields();
        prepareBoardList(NORMAL_MODE);
        currentMode  =NORMAL_MODE;

    }

    private void checkDatabase() {
        new BoardDatabase(this).createTable();
    }

    /**
     * A function to instantiate all the fields
     */
    private void initFields() {
        boardList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.board_recycer_view);
        adapter=new BoardAdapter(this,boardList,NORMAL_MODE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setAdapter(adapter);
    }

    boolean iconImageSelected =false;

    /**
     * Prepares the board list `
     *
     */
    private void prepareBoardList(int mode) {
        boardList.clear();
        if(mode==NORMAL_MODE)
            boardList.add(new Board("-1", "Add Board"));
        ArrayList<Board> list  = loadBoardsFromDataBase();
        boardList.addAll(list);
        invalidateOptionsMenu();
        if(list.size()<1)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
            mode=NORMAL_MODE;
        }
        else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }
        if(mode==NORMAL_MODE) {
            adapter = new BoardAdapter(this, boardList,NORMAL_MODE);
            adapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int Position, int openAddBoard) {


                    if (openAddBoard == BoardAdapter.EDIT_BOARD) {
                        initBoardEditAddDialog(EDIT_BOARD, Position);
                    } else if (openAddBoard == BoardAdapter.OPEN_ADD_BOARD) {
                        //Open the ADD_NEW_BOARD Dialog if position is '0'
                        if (Position == 0) {
                            initBoardEditAddDialog(NEW_BOARD, -1);
                        } else {
                            /*
                             * Board can have four stages.
                             * 1. Created but no icon selected -> IconSelectActivity Opens
                             * 2. Icon selected and closed, didn't pass ADD_EDIT_SCREEN
                             * 3. Passed ADD_EDIT_ICON_SCREEN but repositions screen is not pass
                             * 4. Board Setup completed.
                             */
                            Board board = database.getBoardById(boardList.get(Position).boardID);
                            if(board!=null) {
                                if (board.getBoardCompleteStatus()) {
                                    Intent intent = new Intent(ctx, Home.class);
                                    intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                    startActivity(intent);
                                } else if (board.isAddEditIconScreenPassed()) {
                                    Intent intent = new Intent(ctx, RepositionIcons.class);
                                    intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                    startActivity(intent);
                                } else if (board.isBoardIconsSelected()) {
                                    Intent intent = new Intent(ctx, AddEditIconAndCategory.class);
                                    intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(ctx, IconSelectActivity.class);
                                    intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                    startActivity(intent);
                                }
                                finish();
                            }
                            else
                            Toast.makeText(getApplicationContext(),"Some error occurred",Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }
        else if(mode==DELETE_MODE)
        {
            adapter = new BoardAdapter(this, boardList,DELETE_MODE);
            adapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, final int Position, int code) {
                    if(code==DELETE_MODE) {
                        CustomDialog customDialog=new CustomDialog(ctx,CustomDialog.NORMAL);
                        customDialog.setText("Are you sure that you want to delete "+boardList.get(Position).boardTitle+" ?");
                        customDialog.show();
                        customDialog.setOnNegativeClickListener(new CustomDialog.onNegativeClickListener() {
                            @Override
                            public void onNegativeClickListener() {

                            }
                        });
                        customDialog.setOnPositiveClickListener(new CustomDialog.onPositiveClickListener() {
                            @Override
                            public void onPositiveClickListener() {
                                Board boardToDelete = boardList.get(Position);
                                deleteImageFromStorage(boardToDelete.boardID);
                               /* for(int i = 0 ; i<boardToDelete.getCustomIconLists().size();i++)
                                    deleteImageFromStorage(boardToDelete.getCustomIconLists().get(i));*/
                                database.deleteBoard(boardToDelete.boardID);
                                boardList.remove(Position);
                                if(boardList.size()<1)
                                    prepareBoardList(NORMAL_MODE);
                                else
                                adapter.notifyDataSetChanged();
                            }
                        });



                    }
                }
            });

        }


        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }


    private void initBoardEditAddDialog(final int code, final int pos) {
        VerbiageEditor boardEditDialog = new VerbiageEditor(this, VerbiageEditor.ADD_BOARD_MODE, new VerbiageEditorInterface() {
            @Override
            public void onPositiveButtonClick(String name, Bitmap bitmap, JellowVerbiageModel verbiageList) {
                currentMode = NORMAL_MODE;
                invalidateOptionsMenu();
                if(code==NEW_BOARD) {
                    String BoardId=Calendar.getInstance().getTime().getTime()+"";
                    saveNewBoard(name,bitmap,BoardId);
                    Intent intent=new Intent(ctx,IconSelectActivity.class);
                    intent.putExtra(BOARD_ID,BoardId);
                    startActivity(intent);
                    finish();
                }
                else if(code==EDIT_BOARD) {
                    updateBoardDetails(name, bitmap, pos);
                    Intent intent = new Intent(MyBoards.this,IconSelectActivity.class);
                    intent.putExtra(BOARD_ID,boardList.get(pos).boardID);
                    intent.putExtra(IS_EDIT_MODE,"YES");
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onPhotoModeSelect(int position) {
                firePhotoIntent(position);
            }

            @Override
            public void initPhotoResultListener(VerbiageEditorReverseInterface verbiageEditorReverseInterface) {
                MyBoards.this.revPhotoInterface = verbiageEditorReverseInterface;
            }
        });

        if(code==EDIT_BOARD) {
            boardEditDialog.initAddEditDialog(boardList.get(pos).getBoardTitle());
            boardEditDialog.setBoardImage(boardList.get(pos).getBoardID());
            boardEditDialog.setPositiveButtonText("Next");
            boardEditDialog.setTitleText(boardList.get(pos).boardTitle);
        }
        else
            boardEditDialog.initAddEditDialog("New Board");
        boardEditDialog.show();
    }

    private void firePhotoIntent(int position) {
        if(position==0)
        {
            if(checkPermissionForCamera()&&checkPermissionForStorageRead()) {
                CropImage.activity()
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        .start(MyBoards.this);
            }
            else
            {
                final String [] permissions=new String []{ Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions(MyBoards.this, permissions, CAMERA_REQUEST);
            }

        }
        else if(position==1)
        {
            Intent intent = new Intent(MyBoards.this,BoardSearch.class);
            intent.putExtra(BoardSearch.SEARCH_MODE,BoardSearch.ICON_SEARCH);
            startActivityForResult(intent,LIBRARY_REQUEST);
        }
    }

    private boolean checkPermissionForStorageRead() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkPermissionForCamera() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateBoardDetails(String name, Bitmap boardIcon, int pos) {
        Board board=boardList.get(pos);
        if(board!=null)
        {
            if(!name.equals(""))
            board.setBoardTitle(name);
            if(iconImageSelected)
            storeImageToStorage(boardIcon,board.boardID);
            database.updateBoardIntoDatabase(board);
            prepareBoardList(NORMAL_MODE);
        }
    }

    private ArrayList<Board> loadBoardsFromDataBase()
    {
        return database.getAllBoards();
    }

    private void saveNewBoard(String boardName , Bitmap boardIcon, String boardID) {
        if(iconImageSelected)
        storeImageToStorage(boardIcon,boardID);
        Board newBoard=new Board(boardID,boardName);
        database=new BoardDatabase(this);
        boardList.add(newBoard);
        database.addBoardToDatabase(newBoard);
        prepareBoardList(NORMAL_MODE);
        iconImageSelected = false;
    }

    public void storeImageToStorage(Bitmap bitmap, String fileID) {
        FileOutputStream fos;
        File en_dir = MyBoards.this.getDir(new SessionManager(this).getLanguage(), Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            if (!root.exists()) {
                //noinspection ResultOfMethodCallIgnored
                root.mkdirs();
            }
            Toast.makeText(this,""+root.getAbsolutePath(),Toast.LENGTH_LONG).show();
            File file = new File(root, fileID+ ".png");

            try {
                if(file.exists())
                {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();//Delete the previous image if image is a replace
                    file = new File(root,fileID+".png");
                }
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteImageFromStorage(String fileID) {
        File en_dir = MyBoards.this.getDir(new SessionManager(this).getLanguage(), Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            File file = new File(root, fileID+ ".png");
            if(file.exists())
                //noinspection ResultOfMethodCallIgnored
                file.delete();//Delete the previous image
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.boards_activity_menu, menu);

        if(database==null)
            database=new BoardDatabase(this);
        if(database.getAllBoards()!=null&&database.getAllBoards().size()<1)
        {
            MenuItem item = menu.findItem(R.id.delete_boards);
            item.setVisible(false);
        }
        else if(currentMode == DELETE_MODE)
        {
            MenuItem item = menu.findItem(R.id.delete_boards);
            item.setIcon(R.drawable.ic_done);
            item.setVisible(true);
        }

        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_boards:
                activateDeleteMode();
                break;
            case android.R.id.home: startActivity(new Intent(this, MainActivity.class));finish(); break;
            case R.id.languageSelect:
                startActivity(new Intent(this, LanguageSelectActivity.class)); finish();
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));finish();
                break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));finish();
                break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));finish();
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));finish();
                break;
            case R.id.settings:
                startActivity(new Intent(getApplication(), SettingActivity.class));finish();
                break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));finish();
                break;
            case R.id.feedback:
                startActivity(new Intent(this,FeedbackActivity.class));finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    boolean deleteModeOpen=false;
    private void activateDeleteMode() {


        if (!deleteModeOpen)
        {
            if(boardList.size()<1)
                Toast.makeText(ctx,"There's no board to delete",Toast.LENGTH_SHORT).show();
            else {
                prepareBoardList(DELETE_MODE);
                currentMode = DELETE_MODE;
            }
        }
        else {
            prepareBoardList(NORMAL_MODE);
            currentMode = NORMAL_MODE;
        }
        deleteModeOpen = !deleteModeOpen;
        invalidateOptionsMenu();
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
                        .start(MyBoards.this);
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode==RESULT_OK) {

            /*
             * In this, we are collecting the name of the icon clicked on the search bar and using that to fetch the icon from the database.
             */
           if (requestCode == LIBRARY_REQUEST) {
               String fileName = data.getStringExtra("result");
               if(fileName!=null) {
                   revPhotoInterface.onPhotoResult(null, requestCode, fileName);
                   iconImageSelected =true;
               }

           } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Uri resultUri = result.getUri();
                    Bitmap bitmap1 = result.getBitmap();
                    if (bitmap1 != null)
                    {
                        revPhotoInterface.onPhotoResult(result.getBitmap(), requestCode, null);
                        iconImageSelected =true;
                    }
                    else {
                        try {
                            bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
                            {
                                revPhotoInterface.onPhotoResult(bitmap1, requestCode, null);
                                iconImageSelected =true;
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
        }

    }
}
