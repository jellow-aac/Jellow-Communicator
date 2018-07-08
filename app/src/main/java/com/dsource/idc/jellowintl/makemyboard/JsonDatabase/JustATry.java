package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.utility.JellowIcon;
import com.google.gson.Gson;

import java.util.ArrayList;

public class JustATry extends AppCompatActivity {

    public static final String BOARD_ID="Board_Id";
    private String boardId;
    private ArrayList<JellowIcon> iconList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_atry);
        try{
            boardId =getIntent().getExtras().getString(BOARD_ID);
        }
        catch (NullPointerException e)
        {

        }
        iconList=new BoardDatabase(this).getBoardById(boardId).getIconList();
        TheNode Parent=new TheNode(null);
        Parent.addChild(iconList.get(0));
        Parent.addChild(iconList.get(1));
        Parent.addChild(iconList.get(2));
        Parent.getChildren().get(0).addChild(iconList.get(3));
        Parent.getChildren().get(0).addChild(iconList.get(4));
        Log.d("The_Node",new Gson().toJson(Parent));


    }




}

class TempAdapter
{




}
