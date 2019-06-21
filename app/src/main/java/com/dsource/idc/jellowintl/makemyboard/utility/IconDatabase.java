package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dsource.idc.jellowintl.makemyboard.verbiage_model.VerbiageDatabaseHelper;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;

/**
 * Created by Ayaz Alam on 31/05/2018.
 */
public class IconDatabase extends SQLiteOpenHelper {

    Context context;
    // Declaring all these as constants makes code a lot more readable, and looking like SQL.
    // Versions has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE = "new_icon_database";
    private static final String DATABASE_NAME = "jellow_board_database.db";
    // Column names...
    private static final String ICON_ID = "_id";//Icon Primary key ID
    private static final String ICON_TITLE = "icon_title";//Icon Title
    private static final String ICON_DRAWABLE = "icon_drawable";//Icon Drawable
    private static final String KEY_P1 = "icon_p1";//First Level Parent
    private static final String KEY_P2 = "icon_p2";//Second Level Parent
    private static final String KEY_P3 = "icon_p3";//Third Level Parent
    // ... and a string array of columns.
    private static final String[] COLUMNS =
            {ICON_ID, ICON_TITLE, ICON_DRAWABLE, KEY_P1, KEY_P2, KEY_P3};
    // Build the SQL query that creates the table.
    private static final String ICON_LIST_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE + " (" + ICON_ID + " INTEGER PRIMARY KEY, "
                    + ICON_TITLE + " TEXT," //Icon Title
                    + ICON_DRAWABLE + " TEXT,"//Icon's Drawable resource
                    + KEY_P1 + " INTERGER,"//Level 1 parent
                    + KEY_P2 + " INTEGER, " //Level 2 parent
                    + KEY_P3 + " INTEGER  );";//Level 3 parent
    private SQLiteDatabase mReadableDB;

    SQLiteDatabase db;
    public IconDatabase(Context context,SQLiteDatabase db) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.db = this.getWritableDatabase();
    }

    public IconDatabase(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
        this.db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void createTable() {
        db.execSQL(ICON_LIST_TABLE_CREATE);
        //Fill the Table if the table is not created yet.
        fillDatabaseWithData(db, context);
    }

    /**
     * Adds the initial data set to the database.
     * According to the docs, onCreate for the open helper does not run on the UI thread.
     *
     * @param db Database to fill with data since the member variables are not initialized yet.
     */
    public void fillDatabaseWithData(SQLiteDatabase db, Context context) {
        // Create a container for the data.
        ContentValues values = new ContentValues();
        //Level 1 JellowIcon
        VerbiageDatabaseHelper databaseHelper  = new VerbiageDatabaseHelper(context);

        int max  = databaseHelper.count();
        int current  = 0;
        if(onProgressChangedListener!=null)
            onProgressChangedListener.onProgressChanged(0,max);
        for (int i = 0; i < databaseHelper.getLevelOneIconCount(); i++) {
            // Put column/value pairs into the container. put() overwrites existing values.
            String ID = Nomenclature.getIconName(i,-1,-1,context);
            values.put(ICON_TITLE, databaseHelper.getVerbiageById(ID).getDisplay_Label());
            values.put(ICON_DRAWABLE, ID);
            values.put(KEY_P1, i);
            values.put(KEY_P2, -1);
            values.put(KEY_P3, -1);
            db.insert(TABLE, null, values);
            if(onProgressChangedListener!=null)
                onProgressChangedListener.onProgressChanged(++current,max);
        }
        //Filling the database for Level 2
        for (int i = 0; i < databaseHelper.getLevelOneIconCount(); i++) {
            for (int j = 0; j < databaseHelper.getLevelTwoIconCount(i); j++) {
                // Put column/value pairs into the container. put() overwrites existing values.
                String ID = Nomenclature.getIconName(i,j,-1,context);
                Log.d("ID",""+ID);
                values.put(ICON_TITLE, databaseHelper.getVerbiageById(ID).getDisplay_Label());
                values.put(ICON_DRAWABLE, ID);
                values.put(KEY_P1, i);
                values.put(KEY_P2, j);
                values.put(KEY_P3, -1);
                db.insert(TABLE, null, values);
                if(onProgressChangedListener!=null)
                    onProgressChangedListener.onProgressChanged(++current,max);
            }
        }
        //Filling the database for Level Three
        for (int i = 0; i < databaseHelper.getLevelOneIconCount(); i++) {
            for (int j = 0; j < databaseHelper.getLevelTwoIconCount(i); j++) {

                        for (int k = 0; k < databaseHelper.getLevelThreeIconCount(i,j); k++) {

                            String ID = Nomenclature.getIconName(i,j,k,context);
                            values.put(ICON_TITLE, databaseHelper.getVerbiageById(ID).getDisplay_Label());
                            values.put(ICON_DRAWABLE, ID);
                            values.put(KEY_P1, i);
                            values.put(KEY_P2, j);
                            values.put(KEY_P3, k);
                            db.insert(TABLE, null, values);
                            if(onProgressChangedListener!=null)
                                onProgressChangedListener.onProgressChanged(++current,max);
                        }
            }
        }
        //Reset the language code
        if(onProgressChangedListener!=null)
            onProgressChangedListener.onComplete();
        new SessionManager(context).setLanguageChange(2);
    }

    public void dropTable()
    {
        String dropQuery = "DROP TABLE IF EXISTS "+ TABLE;
        db.execSQL(dropQuery);
    }

    /**
     * A function to return list of icon matching with query
     * Queries the database for an entry at a given position.
     *
     * @param level_0 The Nth row in the table.
     * @return a JellowIcon class object ArrayList with the requested database entry.
     */
    @Nullable
    public JellowIcon fetchIcon(int level_0, int level_1, int level_3)
    {
        String selectQuery;
        selectQuery="SELECT * FROM "+TABLE +" WHERE "+KEY_P1+" = " + level_0 + " AND "+KEY_P2+" = "+level_1+ " AND "+KEY_P3+" = "+level_3;
        Cursor cursor = null;
        JellowIcon thisIcon=null;
        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();

                if (cursor != null) {
                    String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                    String icon_drawable = cursor.getString(cursor.getColumnIndex(ICON_DRAWABLE));
                    int iconP1 = cursor.getInt(cursor.getColumnIndex(KEY_P1));
                    int iconP2 = cursor.getInt(cursor.getColumnIndex(KEY_P2));
                    int iconP3 = cursor.getInt(cursor.getColumnIndex(KEY_P3));
                    thisIcon = new JellowIcon(iconName, icon_drawable, iconP1, iconP2, iconP3);
                }

            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor!=null)
                cursor.close();
            return thisIcon;
        }
    }
    /**
     * A function to return list of icon matching with query
     * Queries the database for an entry at a given position.
     *
     * @param level_0 The Nth row in the table.
     * @return a JellowIcon class object ArrayList with the requested database entry.
     */
    @Nullable
    public ArrayList<JellowIcon> myBoardQuery(int level_0,int level_1)
    {
        String selectQuery;
        if(level_1==-1)
            selectQuery= "SELECT * FROM "+TABLE +" WHERE "+KEY_P1+" = " + level_0 + "";
        else
            selectQuery="SELECT * FROM "+TABLE +" WHERE "+KEY_P1+" = " + level_0 + " AND "+KEY_P2+" = "+level_1;
        Cursor cursor = null;
        JellowIcon thisIcon=null;
        ArrayList<JellowIcon> iconList=new ArrayList<>();
        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        String icon_drawable = cursor.getString(cursor.getColumnIndex(ICON_DRAWABLE));
                        int iconP1 = cursor.getInt(cursor.getColumnIndex(KEY_P1));
                        int iconP2 = cursor.getInt(cursor.getColumnIndex(KEY_P2));
                        int iconP3 = cursor.getInt(cursor.getColumnIndex(KEY_P3));
                        thisIcon = new JellowIcon(iconName, icon_drawable, iconP1, iconP2, iconP3);
                        iconList.add(thisIcon);
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
            return iconList;
        }
    }

    /**
     * A function to return list of icon matching with query
     * Queries the database for an entry at a given position.
     *
     * @param iconTitle The Nth row in the table.
     * @return a JellowIcon class object ArrayList with the requested database entry.
     */
    @Nullable
    public ArrayList<JellowIcon> query(String iconTitle) {
        String selectQuery = "SELECT * FROM " + TABLE + " WHERE " + ICON_TITLE + " LIKE '" + iconTitle + "%' LIMIT 20";
        Cursor cursor = null;
        JellowIcon thisIcon = null;
        ArrayList<JellowIcon> iconList = new ArrayList<>();
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        String icon_drawable = cursor.getString(cursor.getColumnIndex(ICON_DRAWABLE));
                        int iconP1 = cursor.getInt(cursor.getColumnIndex(KEY_P1));
                        int iconP2 = cursor.getInt(cursor.getColumnIndex(KEY_P2));
                        int iconP3 = cursor.getInt(cursor.getColumnIndex(KEY_P3));
                        thisIcon = new JellowIcon(iconName, icon_drawable, iconP1, iconP2, iconP3);
                        iconList.add(thisIcon);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor != null)
                cursor.close();
            return iconList;
        }
    }

    @Nullable
    public ArrayList<JellowIcon> getAllIcons() {
        String selectQuery = "SELECT * FROM " + TABLE + "";

        Cursor cursor = null;
        JellowIcon thisIcon = null;
        ArrayList<JellowIcon> iconList = new ArrayList<>();
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        String icon_drawable = cursor.getString(cursor.getColumnIndex(ICON_DRAWABLE));
                        int iconP1 = cursor.getInt(cursor.getColumnIndex(KEY_P1));
                        int iconP2 = cursor.getInt(cursor.getColumnIndex(KEY_P2));
                        int iconP3 = cursor.getInt(cursor.getColumnIndex(KEY_P3));
                        thisIcon = new JellowIcon(iconName, icon_drawable, iconP1, iconP2, iconP3);
                        iconList.add(thisIcon);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor != null)
                cursor.close();
            return iconList;
        }
    }

    @Nullable
    public ArrayList<JellowIcon> getLevelOneIcons()
    {
        String selectQuery = "SELECT * FROM " + TABLE + " Where "+KEY_P1+" != "+-1+" and "+KEY_P2+" = -1";

        Cursor cursor = null;
        JellowIcon thisIcon = null;
        ArrayList<JellowIcon> iconList = new ArrayList<>();
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        String icon_drawable = cursor.getString(cursor.getColumnIndex(ICON_DRAWABLE));
                        int iconP1 = cursor.getInt(cursor.getColumnIndex(KEY_P1));
                        int iconP2 = cursor.getInt(cursor.getColumnIndex(KEY_P2));
                        int iconP3 = cursor.getInt(cursor.getColumnIndex(KEY_P3));
                        thisIcon = new JellowIcon(iconName, icon_drawable, iconP1, iconP2, iconP3);
                        iconList.add(thisIcon);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor != null)
                cursor.close();
            return iconList;
        }

    }
    @Nullable
    public ArrayList<String> getLevelOneIconsTitles()
    {
        String selectQuery = "SELECT * FROM " + TABLE + " Where "+KEY_P1+" != "+-1+" and "+KEY_P2+" = -1";

        Cursor cursor = null;
        JellowIcon thisIcon = null;
        ArrayList<String> iconList = new ArrayList<>();
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        iconList.add(iconName);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor != null)
                cursor.close();
            return iconList;
        }

    }
    @Nullable
    public ArrayList<String> getLevelTwoIconsTitles(int level1)
    {
        String selectQuery = "SELECT * FROM " + TABLE + " Where "+KEY_P1+" = "+level1+ " and "+KEY_P3+ " = -1";

        Cursor cursor = null;
        JellowIcon thisIcon = null;
        ArrayList<String> iconList = new ArrayList<>();
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        iconList.add(iconName);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor != null)
                cursor.close();
            return iconList;
        }

    }
    @Nullable
    public ArrayList<String> getLevelThreeIconsTitles(int level1,int level2)
    {
        String selectQuery = "SELECT * FROM " + TABLE + " Where "+KEY_P1+" = "+level1+" and "+KEY_P2+" = "+level2;

        Cursor cursor = null;
        JellowIcon thisIcon = null;
        ArrayList<String> iconList = new ArrayList<>();
        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                iconList.clear();
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor != null) {
                        String iconName = cursor.getString(cursor.getColumnIndex(ICON_TITLE));
                        iconList.add(iconName);
                        cursor.moveToNext();
                    }
                }
            }
        } catch (Exception e) {
            //Catch Query Exception
        } finally {
            // Must close cursor and db now that we are done with it.
            if (cursor != null)
                cursor.close();
            return iconList;
        }

    }


    private ProgressListener onProgressChangedListener;
    public interface ProgressListener
    {
        void onProgressChanged(int progress, int max);
        void onComplete();
    }
    public void setOnProgressChangeListener(ProgressListener onProgressChangedListener)
    {
        this.onProgressChangedListener  =onProgressChangedListener;
    }
}//END