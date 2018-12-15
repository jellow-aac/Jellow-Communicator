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

    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;
        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
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
            public void onSaveButtonClick(String name, Bitmap bitmap, JellowVerbiageModel verbiageList) {
                currentMode = NORMAL_MODE;
                invalidateOptionsMenu();
                if(code==NEW_BOARD)
                {
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

        boardEditDialog.initAddEditDialog(null);

        if(code==EDIT_BOARD)
        {
            boardEditDialog.setBoardImage(boardList.get(pos).getBoardID());
            boardEditDialog.setSaveButtonText("Next");
        }

        boardEditDialog.show();

       /* final LayoutInflater dialogLayout = LayoutInflater.from(this);

        @SuppressLint("InflateParams") View dialogContainerView = dialogLayout.inflate(R.layout.edit_board_dialog, null);
        final Dialog dialogForBoardEditAdd = new Dialog(this,R.style.MyDialogBox);
        dialogForBoardEditAdd.applyStyle(R.style.MyDialogBox);
        dialogForBoardEditAdd.backgroundColor(getResources().getColor(R.color.transparent));

        dialogForBoardEditAdd.setCancelable(false);
        //List on the dialog.
        final ListView listView=dialogContainerView.findViewById(R.id.camera_list);
        final EditText boardTitleEditText=dialogContainerView.findViewById(R.id.board_name);
        TextView saveBoard=dialogContainerView.findViewById(R.id.save_baord);
        TextView cancelSaveBoard=dialogContainerView.findViewById(R.id.cancel_save_baord);
        ImageView editBoardIconButton=dialogContainerView.findViewById(R.id.edit_board);
        final ImageView BoardIcon=dialogContainerView.findViewById(R.id.board_icon);
        listView.setVisibility(View.GONE);

        if(code==EDIT_BOARD)
        {
            SessionManager mSession = new SessionManager(MyBoards.this);
            File en_dir = MyBoards.this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
            String path = en_dir.getAbsolutePath() + "/boardicon";
            GlideApp.with(MyBoards.this)
                    .load(path+"/"+boardList.get(pos).getBoardID()+".png")
                    .error(R.drawable.ic_board_person).skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(false)
                    .centerCrop()
                    .dontAnimate()
                    .into(BoardIcon);
            boardTitleEditText.setText(boardList.get(pos).boardTitle);
            saveBoard.setText("Next");
        }

        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        @SuppressLint("Recycle") TypedArray mArray=getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Photos",mArray.getDrawable(0)));
        list.add(new ListItem("Library ",mArray.getDrawable(2)));
        SimpleListAdapter adapter=new SimpleListAdapter(this,list);
        listView.setAdapter(adapter);
        setOnPhotoSelectListener(new PhotoIntentResult() {
            @Override
            public void onPhotoIntentResult(Bitmap bitmap, int code,String FileName) {

                iconImageSelected =true;
                if(code!=LIBRARY_REQUEST)
                {
                    bitmap = cropToSquare(bitmap);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    Glide.with(MyBoards.this).load(stream.toByteArray())
                            .apply(new RequestOptions().
                                    transform(new RoundedCorners(50)).
                                    error(R.drawable.ic_board_person).skipMemoryCache(true).
                                    diskCacheStrategy(DiskCacheStrategy.NONE))
                            .into(BoardIcon);
                }
                else
                {
                    SessionManager mSession = new SessionManager(MyBoards.this);
                    File en_dir = MyBoards.this.getDir(mSession.getLanguage(), Context.MODE_PRIVATE);
                    String path = en_dir.getAbsolutePath() + "/drawables";
                    GlideApp.with(MyBoards.this)
                            .load(path+"/"+FileName+".png")
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(false)
                            .centerCrop()
                            .dontAnimate()
                            .into(BoardIcon);
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
        });


        saveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=boardTitleEditText.getText().toString();
                if(name.equals(""))
                {
                    Toast.makeText(MyBoards.this,"Please enter name",Toast.LENGTH_SHORT).show();
                    return;
                }
                currentMode = NORMAL_MODE;
                invalidateOptionsMenu();
                Bitmap boardIcon=((BitmapDrawable)BoardIcon.getDrawable()).getBitmap();
                if(code==NEW_BOARD)
                {
                    String BoardId=Calendar.getInstance().getTime().getTime()+"";
                    saveNewBoard(name,boardIcon,BoardId);
                    Intent intent=new Intent(ctx,IconSelectActivity.class);
                    intent.putExtra(BOARD_ID,BoardId);
                    startActivity(intent);
                    finish();
                }
                else if(code==EDIT_BOARD) {
                    updateBoardDetails(name, boardIcon, pos);
                    Intent intent = new Intent(MyBoards.this,IconSelectActivity.class);
                    intent.putExtra(BOARD_ID,boardList.get(pos).boardID);
                    intent.putExtra(IS_EDIT_MODE,"YES");
                    startActivity(intent);
                    finish();
                }
                dialogForBoardEditAdd.dismiss();
            }
        });
        cancelSaveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogForBoardEditAdd.dismiss();
            }
        });
        editBoardIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible)
                    listView.setVisibility(View.GONE);
                else listView.setVisibility(View.VISIBLE);
                isVisible=!isVisible;
            }
        });
        dialogForBoardEditAdd.setContentView(dialogContainerView);
        dialogForBoardEditAdd.show();*/

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
        FileOutputStream fos = null;
        File en_dir = MyBoards.this.getDir(new SessionManager(this).getLanguage(), Context.MODE_PRIVATE);
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
        }
    }

    public void deleteImageFromStorage(String fileID) {
        FileOutputStream fos = null;
        File en_dir = MyBoards.this.getDir(new SessionManager(this).getLanguage(), Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            File file = new File(root, fileID+ ".png");
            if(file.exists())
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
                startActivity(new Intent(this, LanguageSelectActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                break;
            case R.id.info:
                startActivity(new Intent(this, AboutJellowActivity.class));
                break;
            case R.id.usage:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.keyboardinput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                break;
            case R.id.settings:
                startActivity(new Intent(getApplication(), SettingActivity.class));
                break;
            case R.id.reset:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                break;
            case R.id.feedback:
                startActivity(new Intent(this,FeedbackActivity.class));
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
