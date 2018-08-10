package com.dsource.idc.jellowintl.verbiage_model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.dsource.idc.jellowintl.Nomenclature;
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
    ArrayList<MiscellaneousIcons> otherIconList;
    ArrayList<String> otherIconKeyList;
    ArrayList<String> keyList;
    int max=0;
    int current=0;
    public void fillDatabase() {
        verbiageList=new ArrayList<>();
        keyList=new ArrayList<>();
        otherIconList=new ArrayList<>();
        otherIconKeyList=new ArrayList<>();
        mRef=FirebaseDatabase.getInstance().getReference("testing")
        .child("verbiage_data").child(new SessionManager(context).getLanguage());
        mRef.keepSynced(false);
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    max = (int)dataSnapshot.getChildrenCount();
                    if(onProgressChangedListener!=null)
                        onProgressChangedListener.onProgressChanged(0,max);
                    for(DataSnapshot d:dataSnapshot.getChildren()) {

                        try {
                            if(d.getKey().contains("EE")||d.getKey().contains("MS")) {
                                otherIconList.add(d.getValue(MiscellaneousIcons.class));
                                otherIconKeyList.add(d.getKey());
                            }
                            else {
                                verbiageList.add(d.getValue(JellowVerbiageModel.class));
                                keyList.add(d.getKey());
                            }
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
                pushOtherIconToDatabase();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void pushOtherIconToDatabase() {
        for(int i=0;i<otherIconList.size();i++)
        {
            ContentValues values = new ContentValues();
            values.put(ICON_ID,otherIconKeyList.get(i));
            values.put(ICON_TITLE,otherIconList.get(i).Title);
            values.put(ICON_VERBIAGE,new Gson().toJson(otherIconList.get(i)));
            db.insert(TABLE_NAME, null, values);
            if(onProgressChangedListener!=null)
                onProgressChangedListener.onProgressChanged(++current,max);
            Log.d("VerbiageDatabase","ExpressiveIconAdded"+otherIconList.get(i).Title+" "+otherIconList.get(i).L);
        }
        if(onProgressChangedListener!=null)
            onProgressChangedListener.onComplete();

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
        String selectQuery = "SELECT * FROM "+ TABLE_NAME +" WHERE "+ ICON_ID +" LIKE '" + IconID + "%' LIMIT 1";
       // Log.d("SelectQuery",selectQuery);
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

    public MiscellaneousIcons getMiscellaneousIconsById(String IconID) {
        MiscellaneousIcons verbiage=null;
        String selectQuery = "SELECT * FROM "+ TABLE_NAME +" WHERE "+ ICON_ID +" LIKE '" + IconID + "%' LIMIT 1";
        Log.d("SelectQuery",selectQuery);
        Cursor cursor = null;
        try {
            if (db == null) {db = getReadableDatabase();}
            cursor = db.rawQuery(selectQuery, null);

            if (cursor != null)
            {
                cursor.moveToFirst();
                if(cursor.getCount()>0)
                    verbiage=new Gson().fromJson(cursor.getString(cursor.getColumnIndex(ICON_VERBIAGE)),MiscellaneousIcons.class);
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

    public void addNewVerbiage(String id, JellowVerbiageModel jellowVerbiageModel) {
        VerbiageHolder holder = new VerbiageHolder(id+"",jellowVerbiageModel.Display_Label,jellowVerbiageModel);
        ContentValues values = new ContentValues();
        values.put(ICON_ID,holder.getIconID());
        values.put(ICON_TITLE,holder.getIconName());
        values.put(ICON_VERBIAGE,new Gson().toJson(holder.getIconVerbaige()));
        db.insert(TABLE_NAME, null, values);
        Log.d("VerbiageDatabase","Icon inserted into database "+holder.getIconName()+" ID "+holder.getIconID());

    }

    public int getLevelOneIconCount()
    {
        String lang_code = Nomenclature.getLanguageCode(context);
        String query  = "SELECT * FROM "+TABLE_NAME+" WHERE "+ICON_ID+" LIKE '" +lang_code+"__000000%'";
       // Log.d("Query 1",query);
        Cursor cursor =  db.rawQuery(query,null);
        if(cursor!=null) {
            int count = cursor.getCount();
            cursor.close();
            return count;
        }
        return -1;
    }
    public int getLevelTwoIconCount(int p1)
    {

        String lang_code = Nomenclature.getLanguageCode(context);
        String L1Parent = String.format("%02d", (p1+1));
        String query  = "SELECT * FROM "+TABLE_NAME+" WHERE "+ICON_ID+" LIKE '" +lang_code+L1Parent+"__0000%'";
        //
        Cursor cursor =  db.rawQuery(query,null);
        if(cursor!=null)
        {
            int count = cursor.getCount();
            cursor.close();
            return --count;
        }
        return -1;
    }
    public int getLevelThreeIconCount(int p1,int p2)
    {
        String L1Parent = String.format("%02d", (p1+1));
        String L2Parent = String.format("%02d", (p2+1));
        String lang_code = Nomenclature.getLanguageCode(context);
        String query  = "SELECT * FROM "+TABLE_NAME+" WHERE "+ICON_ID+" LIKE '" +lang_code+L1Parent+L2Parent+"%'";
       // Log.d("Query 3",query);
        Cursor cursor =  db.rawQuery(query,null);
        if(cursor!=null)
        {
            int count = cursor.getCount();
            cursor.close();
            return --count;
        }
        return -1;
    }

    public int count() {

        String Query = "SELECT * FROM "+TABLE_NAME;
        Cursor cursor = db.rawQuery(Query,null);
        if (cursor!=null)
            return cursor.getCount();
        else return 0;
    }

    private ProgressListener onProgressChangedListener;
    public interface ProgressListener
    {
        void onProgressChanged(int progress,int max);
        void onComplete();
    }
    public void setOnProgressChangeListener(ProgressListener onProgressChangedListener)
    {
        this.onProgressChangedListener  =onProgressChangedListener;
    }
}
