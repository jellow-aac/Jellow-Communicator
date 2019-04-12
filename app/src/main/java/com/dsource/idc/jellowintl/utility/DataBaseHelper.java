package com.dsource.idc.jellowintl.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.crashlytics.android.Crashlytics;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;

/**
 * Created by ekalpa on 6/27/2016.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static String DB_NAME = "level3.db";
    private SQLiteDatabase mSqlDB;
    private final Context mContext;
    private SessionManager mSession;
    private String mDbPath;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.mContext = context;
        mSession = new SessionManager(this.mContext);
        mDbPath = mContext.getDatabasePath("level3.db").getAbsolutePath();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() {
        if(!checkDataBase()){
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                Crashlytics.logException(new Exception("Failed to copy database"));
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            checkDB = SQLiteDatabase.openDatabase(mDbPath, null, SQLiteDatabase.OPEN_READWRITE);
        }catch(SQLiteException e){
            Crashlytics.log("Database do not exist yet");
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DB_NAME);
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(mDbPath);
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        mSqlDB = SQLiteDatabase.openDatabase(mDbPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if(mSqlDB != null)
            mSqlDB.close();
            super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void delete() {
        mContext.deleteDatabase(DB_NAME);
    }
    // Getting single contact
    public String getLevel(int layer_1_id, int layer_2_id) {
        String userLang = "";
        if(mSession.getLanguage().equals(ENG_US) || mSession.getLanguage().equals(ENG_UK) ||
                mSession.getLanguage().equals(ENG_AU))
            userLang = "en-rUS,en-rGB";
        else
            userLang = "hi-rIN,en-rIN";
        if (layer_1_id == 7 && layer_2_id == 6 && mSession.getLanguage().equals(SessionManager.HI_IN))
            layer_2_id = layer_2_id+1;
        Cursor cursor = mSqlDB.query("three", new String[]{"_id", "layer_1_id", "layer_2_id", "layer_3", "language"}, "layer_1_id='" + layer_1_id + "' AND layer_2_id='" + layer_2_id + "' AND language ='"+userLang+"'", null, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String a = cursor.getString(3);
            return a;
        }
        return "false";
    }

    public void setLevel(int layer_1_id, int layer_2_id, String n) {
        String userLang = "";
        if(mSession.getLanguage().equals(ENG_US) || mSession.getLanguage().equals(ENG_UK))
            userLang = "en-rUS,en-rGB";
        else
            userLang = "hi-rIN,en-rIN";
        if (layer_1_id == 7 && layer_2_id == 6 && mSession.getLanguage().equals(SessionManager.HI_IN))
            layer_2_id = layer_2_id+1;
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put("layer_3", n);
        int result = mSqlDB.update("three", dataToInsert, "layer_1_id='" + layer_1_id + "' AND layer_2_id='" + layer_2_id + "' AND language ='"+userLang+"'", null);
        if (result == 0){
            ContentValues cv = new ContentValues();
            cv.put("layer_1_id",layer_1_id);
            cv.put("layer_2_id",layer_2_id);
            cv.put("layer_3",n);
            cv.put("language", userLang);
            mSqlDB.insertOrThrow("three","",cv);
        }
    }

    public boolean addLanguageDataToDatabase(){
        removeMoreFromDatabasePreferences(0, 0);
        removeMoreFromDatabasePreferences(0, 1);
        removeMoreFromDatabasePreferences(0, 2);
        removeMoreFromDatabasePreferences(0, 3);

        removeMoreFromDatabasePreferences(1, 3);
        removeMoreFromDatabasePreferences(1, 4);
        removeMoreFromDatabasePreferences(1, 5);
        removeMoreFromDatabasePreferences(1, 6);

        removeMoreFromDatabasePreferences(2, 0);
        removeMoreFromDatabasePreferences(2, 1);
        removeMoreFromDatabasePreferences(2, 2);
        removeMoreFromDatabasePreferences(2, 3);
        removeMoreFromDatabasePreferences(2, 4);
        removeMoreFromDatabasePreferences(2, 5);
        removeMoreFromDatabasePreferences(2, 6);
        removeMoreFromDatabasePreferences(2, 7);

        removeMoreFromDatabasePreferences(3, 0);
        removeMoreFromDatabasePreferences(3, 1);
        removeMoreFromDatabasePreferences(3, 2);
        removeMoreFromDatabasePreferences(3, 5);

        removeMoreFromDatabasePreferences(4, 0);
        removeMoreFromDatabasePreferences(4, 1);
        removeMoreFromDatabasePreferences(4, 2);
        removeMoreFromDatabasePreferences(4, 3);
        removeMoreFromDatabasePreferences(4, 4);
        removeMoreFromDatabasePreferences(4, 5);
        removeMoreFromDatabasePreferences(4, 6);
        removeMoreFromDatabasePreferences(4, 7);
        removeMoreFromDatabasePreferences(4, 8);

        removeMoreFromDatabasePreferences(7, 5);
        removeMoreFromDatabasePreferences(7, 7);

        ArrayList<String> levelOneIds = new ArrayList<String>();
        levelOneIds.add("0");levelOneIds.add("1");levelOneIds.add("2");
        levelOneIds.add("3");levelOneIds.add("4");levelOneIds.add("7");

        ArrayList<String> levelTwoIds = new ArrayList<>();
        levelTwoIds.add("0,1,2,3"); levelTwoIds.add("3,4,5,6");levelTwoIds.add("0,1,2,3,4,5,6,7");
        levelTwoIds.add("0,1,2,5"); levelTwoIds.add("0,1,2,3,4,5,6,7,8");levelTwoIds.add("5,6,7");

        mSqlDB.execSQL("alter table three add \"language\" text;");
        mSqlDB.execSQL("update three set language =\"en-rUS,en-rGB\" where layer_1_id > -1;");

        Long result = 0L;
        for (int i = 0; i < levelOneIds.size(); i++) {
            for (int j = 0; j < levelTwoIds.size(); j++) {
                if(i != j) continue;
                String ids[] = levelTwoIds.get(j).split(",");
                for (int k = 0; k < ids.length; k++) {
                    ContentValues cv = new ContentValues();
                    cv.put("layer_1_id",levelOneIds.get(i));
                    cv.put("layer_2_id",ids[k]);
                    cv.put("layer_3","0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
                    cv.put("language", "hi-rIN,en-rIN");
                    result += mSqlDB.insertOrThrow("three","",cv);
                }
                break;
            }
        }
        levelOneIds = levelTwoIds = null;
        Crashlytics.log("addLanguageDataToDatabase");
        return result >= 30;
    }

    /**
     * <p>In older version of Jellow app. It has "more" icon at index 8 in category icon. It will
     *  load next 8 cateogry icons when pressed. In later version, more button is removed with
     *  scroll function. But, the old installations has older data strings. In old data string
     *  every 9th value represent more. When scroll is added, the removal of "more" values from
     *  database is must. The following function will remove the values.</p>
     * */
    private void removeMoreFromDatabasePreferences(int layer_1_id, int layer_2_id) {
        String prefString = getRawPrefString(layer_1_id, layer_2_id);
        // The preferences for Time & Weather -> Birthday's in database are stored to 7,7 place
        // instead of 7,6.
        if(layer_1_id == 7 && layer_2_id == 7)
            layer_2_id = 6;
        StringTokenizer token = new StringTokenizer(prefString, ",");
        Integer[] arr = new Integer[prefString.split(",").length];
        for (int i = 0; i < prefString.split(",").length; i++) {
            arr[i] = Integer.parseInt(token.nextToken());
        }
        StringBuilder newPrefString = new StringBuilder();
        int skipCount = 0;
        for (int i = 0; i < arr.length; i++) {
            skipCount++;
            if(skipCount != 9 || i == arr.length - 1)
                newPrefString.append(String.valueOf(arr[i]).concat(","));
            else skipCount = 0;
        }
        for(int i = arr.length; i < 100; ++i)
            newPrefString.append("0,");
        setCorrectedRawPrefString(layer_1_id,layer_2_id, newPrefString.toString());
    }

    private String getRawPrefString(int layer_1_id, int layer_2_id) {
        Cursor cursor = mSqlDB.query("three", new String[]{"_id", "layer_1_id", "layer_2_id", "layer_3"}, "layer_1_id='" + layer_1_id + "' AND layer_2_id='" + layer_2_id + "'", null, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String prefString = cursor.getString(3);
            return prefString;
        }
        return  "";
    }

    private void setCorrectedRawPrefString(int layer_1_id, int layer_2_id, String prefString) {
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put("layer_3", prefString);
        mSqlDB.update("three", dataToInsert, "layer_1_id='" + layer_1_id + "' AND layer_2_id='" + layer_2_id + "'", null);
    }
}