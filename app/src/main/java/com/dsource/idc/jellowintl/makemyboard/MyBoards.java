package com.dsource.idc.jellowintl.makemyboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import com.dsource.idc.jellowintl.DataBaseHelper;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.BoardDatabase;
import com.dsource.idc.jellowintl.makemyboard.JsonDatabase.CustomDialog;
import com.rey.material.app.Dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyBoards extends AppCompatActivity {

    public static final int DELETE_MODE =333;
    public static final int NORMAL_MODE =444;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_boards);
        checkDatabase();
        boardHashMap =new HashMap<>();
        ctx=MyBoards.this;
        database=new BoardDatabase(this);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        db=new DataBaseHelper(this).getWritableDatabase();
        initFields();
        prepareBoardList(NORMAL_MODE);


    }

    private void checkDatabase() {
            new BoardDatabase(this).createTable(new DataBaseHelper(this).getReadableDatabase());
    }

    /**
     * A function to instantiate all the fields
     */
    private void initFields() {
        boardList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.board_recycer_view);
        adapter=new BoardAdapter(this,boardList,NORMAL_MODE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * prepares the board list `
     *
     */
    private void prepareBoardList(int mode) {
        boardList.clear();
        boardList=loadBoardsFromDataBase();
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
                            if (true) {
                                Intent intent = new Intent(ctx, BoardIconSelectActivity.class);
                                intent.putExtra(BOARD_ID, boardList.get(Position).getBoardID());
                                startActivity(intent);
                            } else {
                                //TODO when the board is already created completely
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
                        CustomDialog customDialog=new CustomDialog(ctx);
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
                                prepareBoardList(DELETE_MODE);
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
        ImageView editBoardIconButton=dialogContainerView.findViewById(R.id.edit_board);
        final ImageView BoardIcon=dialogContainerView.findViewById(R.id.board_icon);
        listView.setVisibility(View.GONE);

        //The list that will be shown with camera options
        final ArrayList<ListItem> list=new ArrayList<>();
        TypedArray mArray=getResources().obtainTypedArray(R.array.add_photo_option);
        list.add(new ListItem("Camera",mArray.getDrawable(0)));
        list.add(new ListItem("Gallery ",mArray.getDrawable(1)));
        list.add(new ListItem("Library ",mArray.getDrawable(2)));
        SimpleListAdapter adapter=new SimpleListAdapter(this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    Toast.makeText(MyBoards.this,"Camera Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(position==1)
                {
                    Toast.makeText(MyBoards.this,"Gallery Clicked",Toast.LENGTH_SHORT).show();
                }
                else if(position==2)
                {
                    Toast.makeText(MyBoards.this,"Library Clicked",Toast.LENGTH_SHORT).show();
                }
            }
        });

        saveBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyBoards.this,"Save board",Toast.LENGTH_SHORT).show();
                String name=boardTitleEditText.getText().toString();
                Bitmap boardIcon=((BitmapDrawable)BoardIcon.getDrawable()).getBitmap();
                if(code==NEW_BOARD)
                {
                    String BoardId=Calendar.getInstance().getTime().getTime()+"";
                    saveNewBoard(name,boardIcon,BoardId);
                    Intent intent=new Intent(ctx,BoardIconSelectActivity.class);
                    intent.putExtra(BOARD_ID,BoardId);
                    startActivity(intent);
                }
                else if(code==EDIT_BOARD)
                {
                    updateBoardDetails(name,boardIcon,pos);
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
        dialogForBoardEditAdd.show();

    }

    private void updateBoardDetails(String name, Bitmap boardIcon, int pos) {
        Board board=boardList.get(pos);
        if(board!=null)
        {
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
        Board newBoard=new Board(boardID,boardName,boardIcon);
        database=new BoardDatabase(this);
        boardList.add(newBoard);
        database.addBoardToDatabase(new DataBaseHelper(this).getWritableDatabase(),newBoard);
        Log.d("NewBoard",""+database.count());
        prepareBoardList(NORMAL_MODE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.boards_activity_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_boards:
               activateDeleteMode();
                break;
            case R.id.option:
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
            else
            prepareBoardList(DELETE_MODE);
        }
            else prepareBoardList(NORMAL_MODE);
            deleteModeOpen = !deleteModeOpen;

    }
}
