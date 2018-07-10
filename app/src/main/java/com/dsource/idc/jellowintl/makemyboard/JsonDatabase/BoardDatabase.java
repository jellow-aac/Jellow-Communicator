package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dsource.idc.jellowintl.makemyboard.Board;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Created by Ayaz Alam on 31/05/2018.
 */
public class BoardDatabase extends SQLiteOpenHelper {

    Context context;
    // Declaring all these as constants makes code a lot more readable, and looking like SQL.
    // Versions has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;
    private static final String BOARD_TABLE = "board_table";
    private static final String DATABASE_NAME = "level3.db";
    // Column names...
    public static final String BOARD_ID = "_id";//Icon Primary key ID
    public static final String BOARD_TITLE = "board_title";//Icon Title
    public static final String BOARD_JSON = "icon_p1";//First Level Parent

    private static final String[] COLUMNS =
            {BOARD_ID, BOARD_TITLE, BOARD_JSON};
    // Build the SQL query that creates the table.
    private static final String ICON_LIST_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + BOARD_TABLE + " (" + BOARD_ID + " INTEGER PRIMARY KEY, "
                    + BOARD_TITLE + " TEXT," //Board Title
                    + BOARD_JSON + " TEXT  );";//Board Json
    private SQLiteDatabase mReadableDB;
    public BoardDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    public void createTable(SQLiteDatabase db) {
        db.execSQL(ICON_LIST_TABLE_CREATE);
    }

    /**
     * Adds the initial data set to the database.
     * According to the docs, onCreate for the open helper does not run on the UI thread.
     * @param db Database to fill with data since the member variables are not initialized yet.
     */
    public void fillDatabaseWithData(SQLiteDatabase db,Context context) {

    }
    public void addBoardToDatabase(SQLiteDatabase db,Board board)
    {
        ContentValues values = new ContentValues();
        values.put(BOARD_ID,board.boardID);
        values.put(BOARD_TITLE,board.boardTitle);
        values.put(BOARD_JSON,new Gson().toJson(board));
        db.insert(BOARD_TABLE, null, values);
    }

    public Board getBoard(String Name)
    {
        Board board=null;
        String selectQuery = "SELECT * FROM "+ BOARD_TABLE +" WHERE "+ BOARD_TITLE +" LIKE '" + Name + "%' LIMIT 1";
        Cursor cursor = null;
        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                String Json=cursor.getString(cursor.getColumnIndex(BOARD_JSON));
                board=new Gson().fromJson(Json,Board.class);


            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor!=null)
                cursor.close();

        }

        return board;
    }


    @Nullable
    public Board getBoardById(String boardID)
    {
        Board board=null;
        String selectQuery = "SELECT * FROM "+ BOARD_TABLE +" WHERE "+ BOARD_ID +" = '" + boardID + "' ";
        Cursor cursor = null;
        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(selectQuery, null);

                    if (cursor != null)
                    {
                        cursor.moveToFirst();
                        if(cursor.getCount()>0)
                        board=new Gson().fromJson(cursor.getString(cursor.getColumnIndex(BOARD_JSON)),Board.class);
                    }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor!=null)
                cursor.close();

        }
        return board;
    }
    @Nullable
    public ArrayList<Board> getAllBoards()
    {
        ArrayList<Board> boardList=new ArrayList<>();
        String selectQuery = "SELECT * FROM "+ BOARD_TABLE;
        Cursor cursor = null;
        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String Json=cursor.getString(cursor.getColumnIndex(BOARD_JSON));
                        Board board=new Gson().fromJson(Json,Board.class);
                        boardList.add(board);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor!=null)
                cursor.close();

        }
    return boardList;
    }

    /**
     *  Function to Drop Table
     * */
    public void dropTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + BOARD_TABLE);
    }

    /**
     * Gets the number of rows in the icon list table.
     * @return The number of entries in BOARD_TABLE.
     */
    public long count()
    {
        if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
        return DatabaseUtils.queryNumEntries(mReadableDB, BOARD_TABLE);
    }

    /**
     * Called when a database needs to be upgraded. The most basic version of this method drops
     * the tables, and then recreates them. All data is lost, which is why for a production app,
     * you want to back up your data first. If this method fails, changes are rolled back.
     * @param db
     * @param oldVersion
     * @param newVersion
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + BOARD_TABLE);
        onCreate(db);
    }

    public void updateBoardIntoDatabase(SQLiteDatabase db,Board board) {
        String json=new Gson().toJson(board);
        Log.d("NewBoard","Board: "+board+"  "+json);

        String updateQuery = "UPDATE "+ BOARD_TABLE +" SET "+ BOARD_TITLE +" = '"+board.boardTitle+"', "+BOARD_JSON+" = '"+json+"' WHERE "+BOARD_ID+" = '"+board.boardID+"'";
        Log.d("UpdateQuery",updateQuery);
        db.execSQL(updateQuery);
    }

    public void deleteBoard(String boardID,SQLiteDatabase db) {

        String deletingQuery ="DELETE FROM "+BOARD_TABLE+" WHERE "+BOARD_ID+" = '"+boardID+"'";
        Log.d("Delete Query",deletingQuery);
        db.execSQL(deletingQuery);

    }
}

//END