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
import com.rey.material.app.Dialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class MyBoards extends AppCompatActivity {

    RecyclerView mRecyclerView;
    BoardAdapter adapter;
    ArrayList<Board> boardList;
    private final int NEW_BOARD=11;
    private final int EDIT_BOARD=22;
    HashMap<String,Board> boardHashMap;
    SQLiteDatabase db;
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_boards);
        startActivity(new Intent(this,BoardIconSelectActivity.class));
        /*checkDatabase();
        boardHashMap =new HashMap<>();
        ctx=MyBoards.this;
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
        db=new DataBaseHelper(this).getWritableDatabase();
        initFields();
        prepareBoardList();
        */


    }

    private void checkDatabase() {
    }

    /**
     * A function to instantiate all the fields
     */
    private void initFields() {
        boardList=new ArrayList<>();
        mRecyclerView=findViewById(R.id.board_recycer_view);
        adapter=new BoardAdapter(this,boardList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListner(new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int Position) {
                if(Position==(boardList.size()-1))
                    initBoardEditDialog(NEW_BOARD);
                else
                initBoardEditDialog(EDIT_BOARD);
            }
        });

    }

    /**
     * prepares the board list
     */
    private void prepareBoardList() {
        Calendar calendar=Calendar.getInstance();
        boardList.add(new Board(calendar.getTime().getTime()+"","Board 1",null));
        boardList.add(new Board(calendar.getTime().getTime()+"","Board 2",null));
        notifyDateSetChanged();

    }
    int tempIndex=-1;
    private void notifyDateSetChanged()
    {
        if(tempIndex!=-1)
            boardList.remove(tempIndex);
        boardList.add(new Board("-1","Add Board",null));
        tempIndex=boardList.size()-1;
        if(boardList.size()>3)
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        else
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }




    boolean isVisible=false;
    @SuppressLint("ResourceType")
    private void initBoardEditDialog(int code) {


        final LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.edit_board_dialog, null);
        final Dialog alertDialogBuilder = new Dialog(this,R.style.MyDialogBox);
        final ListView listView=promptsView.findViewById(R.id.camera_list);
        final EditText title=promptsView.findViewById(R.id.board_name);
        TextView Save=promptsView.findViewById(R.id.save_baord);
        TextView Cancel=promptsView.findViewById(R.id.cancel_save_baord);
        ImageView edit=promptsView.findViewById(R.id.edit_board);
        final ImageView BoardIcon=promptsView.findViewById(R.id.board_icon);
        listView.setVisibility(View.GONE);

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
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MyBoards.this,"Save board",Toast.LENGTH_SHORT).show();
                String name=title.getText().toString();
                Bitmap boardIcon=((BitmapDrawable)BoardIcon.getDrawable()).getBitmap();
                saveNewBoard(name,boardIcon,Calendar.getInstance().getTime().getTime()+"");
                alertDialogBuilder.dismiss();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder.dismiss();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isVisible)
                listView.setVisibility(View.GONE);
                else listView.setVisibility(View.VISIBLE);
                isVisible=!isVisible;
            }
        });

        adapter.notifyDataSetChanged();
        alertDialogBuilder.setContentView(promptsView);
        alertDialogBuilder.show();

    }


    private void saveNewBoard(String boardName , Bitmap boardIcon, String boardID) {
        Board newBoard=new Board(boardID,boardName,boardIcon);
        BoardDatabase database=new BoardDatabase(this);
        boardList.add(newBoard);
        database.addBoardToDatabase(new DataBaseHelper(this).getWritableDatabase(),newBoard);
        Log.d("Board : ",""+database.getBoard(newBoard.boardTitle).boardTitle);
        notifyDateSetChanged();
    }
}
