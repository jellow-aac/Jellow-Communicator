package com.dsource.idc.jellowintl.makemyboard;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dsource.idc.jellowintl.AboutJellowActivity;
import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.FeedbackActivity;
import com.dsource.idc.jellowintl.GlideApp;
import com.dsource.idc.jellowintl.KeyboardInputActivity;
import com.dsource.idc.jellowintl.LanguageSelectActivity;
import com.dsource.idc.jellowintl.ProfileFormActivity;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.ResetPreferencesActivity;
import com.dsource.idc.jellowintl.SettingActivity;
import com.dsource.idc.jellowintl.TutorialActivity;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.UtilityClasses.CustomDialog;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.rey.material.app.Dialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyBoards extends AppCompatActivity {

    public static final int DELETE_MODE =333;
    public static final int NORMAL_MODE =444;
    public static final int CAMERA_REQUEST=211;
    public static final int GALLERY_REQUEST=311;
    public static final int LIBRARY_REQUEST=411;
    public int currentMode =-1;
    private static final int PERMISSION_REQUESTS = 1212;
    RecyclerView mRecyclerView;
    BoardAdapter adapter;
    ArrayList<Board> boardList;
    private final int NEW_BOARD=11;
    private final int EDIT_BOARD=22;
    HashMap<String,Board> boardHashMap;
    PhotoIntentResult mPhotoIntentResult;
    SQLiteDatabase db;
    public static final String BOARD_ID="Board_Id";
    Context ctx;
    BoardDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_boards);
        checkDatabase();
        boardHashMap =new HashMap<>();
        ctx=MyBoards.this;
        database=new BoardDatabase(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#333333'>"+"My Boards"+"</font>"));
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_button_board);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        db=new DataBaseHelper(this).getWritableDatabase();
        initFields(3);
        prepareBoardList(NORMAL_MODE);
        currentMode  =NORMAL_MODE;
     
    }

    private void checkDatabase() {
        new BoardDatabase(this).createTable(new DataBaseHelper(this).getReadableDatabase());
    }

    /**
     * A function to instantiate all the fields
     * @param i
     */
    private void initFields(int i) {
        boardList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.board_recycer_view);
        adapter=new BoardAdapter(this,boardList,NORMAL_MODE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,i));
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * Prepares the board list `
     *
     */
    private void prepareBoardList(int mode) {
        boardList.clear();
        boardList=loadBoardsFromDataBase();
        invalidateOptionsMenu();
        if(boardList.size()<1)
        {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
            mode=NORMAL_MODE;
        }
        else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        }
        if(mode==NORMAL_MODE) {
            boardList.add(new Board("-1", "Add Board", null));
            adapter = new BoardAdapter(this, boardList,NORMAL_MODE);
            adapter.setOnItemClickListener(new BoardAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int Position, int openAddBoard) {
                    if (openAddBoard == BoardAdapter.EDIT_BOARD) {
                        initBoardEditAddDialog(EDIT_BOARD, Position);
                    } else if (openAddBoard == BoardAdapter.OPEN_ADD_BOARD) {
                        if (Position == (boardList.size() - 1)) {
                            initBoardEditAddDialog(NEW_BOARD, -1);
                        } else {
                            if(database.getBoardById(boardList.get(Position).boardID).getBoardCompleteStatus()){
                                Intent intent = new Intent(ctx, BoardHome.class);
                                intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                startActivity(intent);
                            }
                            else
                            if (database.getBoardById(boardList.get(Position).boardID).isBoardIconsSelected()) {
                                Intent intent = new Intent(ctx, EditBoard.class);
                                intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(ctx, IconSelectActivity.class);
                                intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                startActivity(intent);
                            }
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
                                database.deleteBoard(boardToDelete.boardID,new DataBaseHelper(MyBoards.this).getReadableDatabase());
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

    boolean isVisible=false;
    @SuppressLint("ResourceType")
    private void initBoardEditAddDialog(final int code, final int pos) {
        final LayoutInflater dialogLayout = LayoutInflater.from(this);
        View dialogContainerView = dialogLayout.inflate(R.layout.edit_board_dialog, null);
        final Dialog dialogForBoardEditAdd = new Dialog(this,R.style.MyDialogBox);
        dialogForBoardEditAdd.applyStyle(R.style.MyDialogBox);
        dialogForBoardEditAdd.backgroundColor(getResources().getColor(R.color.transparent));

        //List on the dialog.
        final ListView listView=dialogContainerView.findViewById(R.id.camera_list);
        final EditText boardTitleEditText=dialogContainerView.findViewById(R.id.board_name);
        TextView saveBoard=dialogContainerView.findViewById(R.id.save_baord);
        TextView cancelSaveBoard=dialogContainerView.findViewById(R.id.cancel_save_baord);
        if(code==EDIT_BOARD)
            cancelSaveBoard.setText(getString(R.string.next));
        ImageView editBoardIconButton=dialogContainerView.findViewById(R.id.edit_board);
        final ImageView BoardIcon=dialogContainerView.findViewById(R.id.board_icon);
        listView.setVisibility(View.GONE);
       // Glide.with(context).load(url).apply(RequestOptions.circleCropTransform()).into(imageView);

        if(code==EDIT_BOARD)
        {
            byte[] bitmapdata=boardList.get(pos).getBoardIcon();
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
            BoardIcon.setImageBitmap(bitmap);
            boardTitleEditText.setText(boardList.get(pos).boardTitle);
        }

        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        TypedArray mArray=getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Camera",mArray.getDrawable(0)));
        list.add(new ListItem("Gallery ",mArray.getDrawable(1)));
        list.add(new ListItem("Library ",mArray.getDrawable(2)));
        SimpleListAdapter adapter=new SimpleListAdapter(this,list);
        listView.setAdapter(adapter);
        setOnPhotoSelectListener(new PhotoIntentResult() {
            @Override
            public void onPhotoIntentResult(Bitmap bitmap, int code,String FileName) {

                bitmap = cropToSquare(bitmap);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if(code!=LIBRARY_REQUEST)
                {
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
                    if(checkPermissionForCamera()) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePictureIntent,CAMERA_REQUEST);
                    }
                    else
                    {
                        final String [] permissions=new String []{ Manifest.permission.CAMERA};
                        ActivityCompat.requestPermissions(MyBoards.this, permissions, CAMERA_REQUEST);
                    }

                }
                else if(position==1)
                {
                    if(checkPermissionForStorageRead()) {
                        Intent selectFromGalleryIntent = new Intent();
                        selectFromGalleryIntent.setType("image/*");
                        selectFromGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(selectFromGalleryIntent, GALLERY_REQUEST);
                    }
                    else {
                        final String [] permissions=new String []{ Manifest.permission.READ_EXTERNAL_STORAGE};
                        ActivityCompat.requestPermissions(MyBoards.this, permissions, GALLERY_REQUEST);

                    }
                }
                else if(position==2)
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
                //Toast.makeText(MyBoards.this,"Board saved",Toast.LENGTH_SHORT).show();
                currentMode = NORMAL_MODE;
                invalidateOptionsMenu();
                String name=boardTitleEditText.getText().toString();
                Bitmap boardIcon=((BitmapDrawable)BoardIcon.getDrawable()).getBitmap();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                boardIcon.compress(Bitmap.CompressFormat.PNG, 50, bos);
                byte[] bitmapArray = bos.toByteArray();
                if(code==NEW_BOARD)
                {
                    String BoardId=Calendar.getInstance().getTime().getTime()+"";
                    saveNewBoard(name,boardIcon,BoardId);
                    Intent intent=new Intent(ctx,IconSelectActivity.class);
                    intent.putExtra(BOARD_ID,BoardId);
                    startActivity(intent);
                }
                else if(code==EDIT_BOARD)
                updateBoardDetails(name,bitmapArray,pos);
                dialogForBoardEditAdd.dismiss();
            }
        });
        cancelSaveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(code==EDIT_BOARD) {
                    String boardID = boardList.get(pos).boardID;
                    Intent intent = new Intent(MyBoards.this,AddEditIconAndCategory.class);
                    if(!boardList.get(pos).isBoardIconsSelected())
                        intent = new Intent(MyBoards.this,IconSelectActivity.class);
                    intent.putExtra(BOARD_ID,boardID);
                    startActivity(intent);
                }
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
        dialogForBoardEditAdd.show();

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
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);

        return cropImg;
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

    private boolean checkPermissionForCamera() {
        boolean okay=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                okay = false;
            }
        }

        return okay;
    }

    private void updateBoardDetails(String name, byte[] boardIcon, int pos) {
        Board board=boardList.get(pos);
        if(board!=null)
        {
            if(!name.equals(""))
            board.setBoardTitle(name);
            board.setBoardIcon(boardIcon);
            database.updateBoardIntoDatabase(new DataBaseHelper(this).getReadableDatabase(),board);
            prepareBoardList(NORMAL_MODE);
        }
    }

    private ArrayList<Board> loadBoardsFromDataBase()
    {
        return database.getAllBoards();
    }

    private void saveNewBoard(String boardName , Bitmap boardIcon, String boardID) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        boardIcon.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] bArray = bos.toByteArray();
        Board newBoard=new Board(boardID,boardName,bArray);
        database=new BoardDatabase(this);
        boardList.add(newBoard);
        database.addBoardToDatabase(new DataBaseHelper(this).getWritableDatabase(),newBoard);
        prepareBoardList(NORMAL_MODE);
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
            case android.R.id.home: finish(); break;
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

    private void setOnPhotoSelectListener(PhotoIntentResult mPhotoIntentResult)
    {
        this.mPhotoIntentResult=mPhotoIntentResult;
    }
    public interface PhotoIntentResult
    {
        void onPhotoIntentResult(Bitmap bitmap,int code,String fileName);

    }

    //TODO add codes to add photo directly
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if(requestCode==CAMERA_REQUEST)
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                } else {

                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

         if(requestCode==GALLERY_REQUEST)
             if (grantResults.length > 0
                     && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 //Gallery Permissions granted
                 Intent selectFromGalleryIntent = new Intent();
                 selectFromGalleryIntent.setType("image/*");
                 selectFromGalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                 startActivityForResult(selectFromGalleryIntent, GALLERY_REQUEST);
             } else {
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
             }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       Bitmap bitmap=null;
        if(resultCode==RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    bitmap = (Bitmap) extras.get("data");
                }

            } else if (requestCode == GALLERY_REQUEST) {
                Uri uri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (requestCode == LIBRARY_REQUEST) {
                String fileName = data.getStringExtra("result");
                if(fileName!=null)
                    ;
                byte[] resultImage = data.getByteArrayExtra(getString(R.string.search_result));
                bitmap = BitmapFactory.decodeByteArray(resultImage, 0, resultImage.length);
                mPhotoIntentResult.onPhotoIntentResult(bitmap,requestCode,fileName);
            }
            if(requestCode!=LIBRARY_REQUEST)
            mPhotoIntentResult.onPhotoIntentResult(bitmap, requestCode,null);

        }

    }
}
