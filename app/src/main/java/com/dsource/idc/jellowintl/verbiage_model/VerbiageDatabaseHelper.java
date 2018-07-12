package com.dsource.idc.jellowintl.verbiage_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;

public class VerbiageDatabaseHelper extends SQLiteOpenHelper{


     private static final int DATABASE_VERSION = 1;
     private static final String DATABASE_NAME = "level3.db";
     private static final String TABLE_NAME="verbiage_table";
     private static final String ICON_ID="icon_id";
     private static final String ICON_TITLE="icon_title";
     private static final String ICON_VERBIAGE="icon_verbiage";

    private Context context;

    private static final String[] COLUMNS =
            {ICON_ID, ICON_TITLE, ICON_VERBIAGE};
    // Build the SQL query that creates the table.
    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ICON_ID + " TEXT PRIMARY KEY, "
                    + ICON_TITLE + " TEXT," //Board Title
                    + ICON_VERBIAGE + " TEXT  );";//Board Json
    private SQLiteDatabase db;

    /**
     * Public constructor
     * @param context
     * @param db
     */
    public VerbiageDatabaseHelper(Context context,SQLiteDatabase db) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.db=db;
        this.context=context;
    }

    public void createTable() {
        db.execSQL(CREATE_TABLE);
        fillDatabase();
    }

    DatabaseReference mRef;
    ArrayList<JellowVerbiageModel> verbiageList;
    ArrayList<String> keyList;
    public void fillDatabase() {
        verbiageList=new ArrayList<>();
        keyList=new ArrayList<>();
        mRef=FirebaseDatabase.getInstance().getReference("testing")
        .child("verbiage_data").child(new SessionManager(context).getLanguage());
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for(DataSnapshot d:dataSnapshot.getChildren()) {
                        try {
                            verbiageList.add(d.getValue(JellowVerbiageModel.class));
                            keyList.add(d.getKey());
                            Log.d("ICON_LIST_ADDED",""+d.getKey());
                        }
                        catch (Exception e)
                        {
                            Log.d("Task1","Exception : "+d.getKey()+" Exception "+e.getMessage());
                        }
                    }
                } else Log.d("Task1","DataSnapshot does not exists | ");
                Log.d("Task1","List size: "+verbiageList.size());
                pushDataToDatabase(verbiageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void pushDataToDatabase(ArrayList<JellowVerbiageModel> iconList) {

        VerbiageHolder holder;
        for(int i=0;i<iconList.size();i++)
        {
            JellowVerbiageModel model=iconList.get(i);
            holder=new VerbiageHolder(keyList.get(i),model.Display_Label,model);
            addIconToDatabase(holder);
        }
    }

    private void addIconToDatabase(VerbiageHolder holder) {
        ContentValues values = new ContentValues();
        values.put(ICON_ID,holder.getIconID());
        values.put(ICON_TITLE,holder.getIconName());
        values.put(ICON_VERBIAGE,new Gson().toJson(holder.getIconVerbaige()));
        db.insert(TABLE_NAME, null, values);
        Log.d("VerbiageDatabase","Icon inserted into database "+holder.getIconName()+" ID "+holder.getIconID());
    }

    public JellowVerbiageModel getVerbiageById(String IconID){
        JellowVerbiageModel verbiage=null;
        String selectQuery = "SELECT * FROM "+ TABLE_NAME +" WHERE "+ ICON_ID +" = '" + IconID + "' ";
        Cursor cursor = null;
        try {
            if (db == null) {db = getReadableDatabase();}
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                if(cursor.getCount()>0)
                    verbiage=new Gson().fromJson(cursor.getString(cursor.getColumnIndex(ICON_VERBIAGE)),JellowVerbiageModel.class);
            }
        } catch (Exception e) {
            Log.d("ExceptionRaised",e.getMessage());
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor!=null)
                cursor.close();

        }
        return verbiage;
    }

    public JellowVerbiageModel getVerbiageByTitle(String IconTitle){
        JellowVerbiageModel verbiage=null;
        String selectQuery = "SELECT * FROM "+ TABLE_NAME +" WHERE "+ ICON_TITLE +" = '" + IconTitle + "' ";
        Cursor cursor = null;
        try {
            if (db == null) {db = getReadableDatabase();}
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                if(cursor.getCount()>0)
                    verbiage=new Gson().fromJson(cursor.getString(cursor.getColumnIndex(ICON_VERBIAGE)),JellowVerbiageModel.class);
            }
        } catch (Exception e) {
            Log.d("ExceptionRaised",e.getMessage());
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor!=null)
                cursor.close();

        }
        return verbiage;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     *  Function to Drop Table
     * */
    public void dropTable()
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

}
