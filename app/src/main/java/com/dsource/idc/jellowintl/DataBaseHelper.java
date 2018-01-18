package com.dsource.idc.jellowintl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.dsource.idc.jellowintl.Utility.SessionManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static com.dsource.idc.jellowintl.Utility.Analytics.reportException;
import static com.dsource.idc.jellowintl.Utility.Analytics.reportLog;

/**
 * Created by ekalpa on 6/27/2016.
 */

class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.dsource.idc.jellowintl/databases/";
    private static String DB_NAME = "level3.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    private SessionManager mSession;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        mSession = new SessionManager(this.myContext);
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(dbExist){
            //do nothing - database already exist
        }else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
               reportLog("Error copying database.", Log.ERROR);
               reportException(e);
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
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

        }catch(SQLiteException e){
            reportLog("database does't exist yet.", Log.ERROR);
            reportException(e);
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
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
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
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
            super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void delete() {
        myContext.deleteDatabase(DB_NAME);
    }
    // Getting single contact
    public String getlevel(int layer_1_id, int layer_2_id) {
        String userLang = "";
        if(mSession.getLanguage().equals("en-rUS") || mSession.getLanguage().equals("en-rGB"))
            userLang = "en-rUS,en-rGB";
        else
            userLang = "hi-rIN,en-rIN";
        if (layer_1_id == 7 && layer_2_id == 6 && mSession.getLanguage().equals(SessionManager.HI_IN))
            layer_2_id = layer_2_id+1;
        Cursor cursor = myDataBase.query("three", new String[]{"_id", "layer_1_id", "layer_2_id", "layer_3", "language"}, "layer_1_id='" + layer_1_id + "' AND layer_2_id='" + layer_2_id + "' AND language ='"+userLang+"'", null, null, null, null);
        if (cursor.getCount()>0) {
            cursor.moveToFirst();
            String a = cursor.getString(3);
            return a;
        }
        return "false";
    }

    public void setlevel(int layer_1_id, int layer_2_id, String n) {
        String userLang = "";
        if(mSession.getLanguage().equals("en-rUS") || mSession.getLanguage().equals("en-rGB"))
            userLang = "en-rUS,en-rGB";
        else
            userLang = "hi-rIN,en-rIN";
        if (layer_1_id == 7 && layer_2_id == 6 && mSession.getLanguage().equals(SessionManager.HI_IN))
            layer_2_id = layer_2_id+1;
        ContentValues dataToInsert = new ContentValues();
        dataToInsert.put("layer_3", n);
        myDataBase.update("three", dataToInsert, "layer_1_id='" + layer_1_id + "' AND layer_2_id='" + layer_2_id + "' AND language ='"+userLang+"'", null);
    }

    public boolean addLanguageDataToDatabase(){
         myDataBase.execSQL("alter table three add \"language\" text;");
         myDataBase.execSQL("update three set language =\"en-rUS,en-rGB\" where layer_1_id > -1;");

         ArrayList<String> levelOneIds = new ArrayList<String>();
            levelOneIds.add("0");levelOneIds.add("1");levelOneIds.add("2");
            levelOneIds.add("3");levelOneIds.add("4");levelOneIds.add("7");

        ArrayList<String> levelTwoIds = new ArrayList<>();
            levelTwoIds.add("0,1,2,3"); levelTwoIds.add("3,4,5,6");levelTwoIds.add("0,1,2,3,4,5,6,7");
            levelTwoIds.add("0,1,2,5"); levelTwoIds.add("0,1,2,3,4,5,6,7,8");levelTwoIds.add("5,6,7");

        Long result = 0L;
        for (int i=0; i< levelOneIds.size(); ++i) {
            for (int j=0;j<levelTwoIds.size();++j) {
                if(i != j) continue;
                String ids[] = levelTwoIds.get(j).split(",");
                for (int k = 0; k < ids.length; k++) {
                    ContentValues cv = new ContentValues();
                    cv.put("layer_1_id",levelOneIds.get(i));
                    cv.put("layer_2_id",ids[k]);
                    cv.put("layer_3","0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,");
                    cv.put("language", "hi-rIN,en-rIN");
                    result += myDataBase.insertOrThrow("three","",cv);
                }
                break;
            }
        }
        levelOneIds = levelTwoIds = null;
        return result >= 30;
    }
}