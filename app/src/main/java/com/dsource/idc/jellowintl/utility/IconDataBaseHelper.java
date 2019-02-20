package com.dsource.idc.jellowintl.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;

import java.util.ArrayList;

import androidx.annotation.Nullable;

import static com.dsource.idc.jellowintl.factories.PathFactory.getIconDirectory;

/**
 * Created by Ayaz Alam on 31/05/2018.
 */
public class IconDataBaseHelper extends SQLiteOpenHelper {

    // Declaring all these as constants makes code a lot more readable, and looking like SQL.
    // Versions has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;
    private static final String ICON_LIST_TABLE = "icons";
    private static final String DATABASE_NAME = "level3.db";
    // Column names...
    public static final String ICON_ID = "_id";//Icon Primary key ID
    public static final String ICON_TITLE = "icon_title";//Icon Title
    public static final String ICON_SPEECH = "icon_speech";//Icon Drawable
    public static final String ICON_DRAWABLE = "icon_drawable";//Icon Drawable
    public static final String KEY_P1 = "icon_p1";//First Level Parent
    public static final String KEY_P2 = "icon_p2";//Second Level Parent
    public static final String KEY_P3 = "icon_p3";//Third Level Parent
    // ... and a string array of columns.
    private static final String[] COLUMNS =
            {ICON_ID, ICON_TITLE,ICON_SPEECH,ICON_DRAWABLE,KEY_P1,KEY_P2,KEY_P3};
    // Build the SQL query that creates the table.
    private static final String ICON_LIST_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ICON_LIST_TABLE + " (" + ICON_ID + " INTEGER PRIMARY KEY, "
                    + ICON_TITLE + " TEXT," //Icon Title
                    + ICON_SPEECH + " TEXT," //Icon Speech
                    + ICON_DRAWABLE + " TEXT,"//Icon's Drawable resource
                    + KEY_P1 + " INTERGER,"//Level 1 parent
                    + KEY_P2 + " INTEGER, " //Level 2 parent
                    + KEY_P3 +" INTEGER  );";//Level 3 parent
    Context context;
    String[] thirdLevelIcons=null;
    String[] thirdLevelTitles=null;
    String[] thirdLevelSpeech=null;
    private SQLiteDatabase mReadableDB;
    private boolean noChildInThird =false;

    public IconDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    public void createTable(SQLiteDatabase db) {
        db.execSQL(ICON_LIST_TABLE_CREATE);
        //Fill the Table if the table is not created yet.
        fillDatabaseWithData(db,context);
    }

    /**
     * Adds the initial data set to the database.
     * According to the docs, onCreate for the open helper does not run on the UI thread.
     * @param db Database to fill with data since the member variables are not initialized yet.
     */
    public void fillDatabaseWithData(SQLiteDatabase db,Context context) {
        // Create a container for the data.
        ContentValues values = new ContentValues();
        //Filling the database for level 1 JellowIcon
        String[] level1Icons =  IconFactory.getL1Icons(
                getIconDirectory(context),
                LanguageFactory.getCurrentLanguageCode(context)
        );

        Icon[] level1IconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(context),
                IconFactory.removeFileExtension(level1Icons)
        );

        String[] levelOneTitles = TextFactory.getDisplayText(level1IconObjects);
        String[] levelOneSpeech = TextFactory.getSpeechText(level1IconObjects);

        //Level 1 JellowIcon
        for (int i = 0; i < level1Icons.length; i++) {
            // Put column/value pairs into the container. put() overwrites existing values.
            values.put(ICON_TITLE, levelOneTitles[i]);
            values.put(ICON_SPEECH,levelOneSpeech[i]);
            values.put(ICON_DRAWABLE, level1Icons[i]);
            values.put(KEY_P1, i);
            values.put(KEY_P2, -1);
            values.put(KEY_P3, -1);
            db.insert(ICON_LIST_TABLE, null, values);
        }
        //Filling the database for Level 2
        for (int i = 0; i < level1Icons.length; i++) {
            String[] levelTwoIcons = getIconLevel2(i,context);
            String[] levelTwoTitles = getIconTitleLevel2(i,context);
            String[] levelTwoSpeech = getIconSpeechLevel2(i,context);
            for (int j = 0; j < levelTwoIcons.length; j++) {
                // Put column/value pairs into the container. put() overwrites existing values.
                values.put(ICON_TITLE, levelTwoTitles[j]);
                values.put(ICON_SPEECH,levelTwoSpeech[j]);
                values.put(ICON_DRAWABLE, levelTwoIcons[j]);
                values.put(KEY_P1, i);
                values.put(KEY_P2, j);
                values.put(KEY_P3, -1);
                db.insert(ICON_LIST_TABLE, null, values);

            }
        }
        //Filling the database for Level Three
        for (int i = 0; i < level1Icons.length; i++) {
            String[] levelTwoIcons = getIconLevel2(i,context);
            for (int j = 0; j < levelTwoIcons.length; j++) {
                noChildInThird = false;
                if (loadArraysFromResources(i, j,context)) {
                    if (thirdLevelTitles != null) {
                        String[] levelThreeIcons = thirdLevelIcons;
                        String[] levelThreeTitle = thirdLevelTitles;
                        String[] levelThreeSpeech = thirdLevelSpeech;
                        for (int k = 0; k < thirdLevelTitles.length; k++) {
                            // Put column/value pairs into the container. put() overwrites existing values.
                            if (noChildInThird)
                                break;
                            values.put(ICON_TITLE, levelThreeTitle[k]);
                            values.put(ICON_SPEECH,levelThreeSpeech[k]);
                            values.put(ICON_DRAWABLE, levelThreeIcons[k]);
                            values.put(KEY_P1, i);
                            values.put(KEY_P2, j);
                            values.put(KEY_P3, k);
                            db.insert(ICON_LIST_TABLE, null, values);
                        }

                    }
                }

            }
        }
        //Reset the language code
        new SessionManager(context).setLanguageChange(2);
    }

    /**
     * A function to return array of Icon of level two
     *
     * */

    private String[] getIconLevel2(int pos,Context mContext)
    {

        return IconFactory.getAllL2Icons(
                PathFactory.getIconDirectory(mContext),
                LanguageFactory.getCurrentLanguageCode(mContext),
                getLevel2_3IconCode(pos)
        );

    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
    private String[] getIconTitleLevel2(int pos,Context mContext)
    {

        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(mContext),
                IconFactory.removeFileExtension(getIconLevel2(pos,mContext))
        );

        return TextFactory.getDisplayText(iconObjects);
    }

    private String[] getIconSpeechLevel2(int pos,Context mContext)
    {

        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(mContext),
                IconFactory.removeFileExtension(getIconLevel2(pos,mContext))
        );

        return TextFactory.getSpeechText(iconObjects);
    }

    /**
     * A function to return list of icon matching with query
     * Queries the database for an entry at a given position.
     *
     * @param iconTitle The Nth row in the table.
     * @return a JellowIcon class object ArrayList with the requested database entry.
     */
    @Nullable
    public ArrayList<JellowIcon> query(String iconTitle)
    {
        String selectQuery = "SELECT * FROM "+ICON_LIST_TABLE +" WHERE "+ICON_TITLE+" LIKE '" + iconTitle + "%' LIMIT 20";
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
                        String iconSpeech = cursor.getString(cursor.getColumnIndex(ICON_SPEECH));
                        String icon_drawable = cursor.getString(cursor.getColumnIndex(ICON_DRAWABLE));
                        int iconP1 = cursor.getInt(cursor.getColumnIndex(KEY_P1));
                        int iconP2 = cursor.getInt(cursor.getColumnIndex(KEY_P2));
                        int iconP3 = cursor.getInt(cursor.getColumnIndex(KEY_P3));
                        thisIcon = new JellowIcon(iconName, iconSpeech, icon_drawable, iconP1, iconP2, iconP3);
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
     *  Function to Drop Table
     * */
    public void dropTable(SQLiteDatabase db)
    {
        db.execSQL("DROP TABLE IF EXISTS " + ICON_LIST_TABLE);
    }

    /**
     * Gets the number of rows in the icon list table.
     * @return The number of entries in ICON_LIST_TABLE.
     */
    public long count()
    {
        if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
        return DatabaseUtils.queryNumEntries(mReadableDB, ICON_LIST_TABLE);
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
        db.execSQL("DROP TABLE IF EXISTS " + ICON_LIST_TABLE);
        onCreate(db);
    }


    private boolean loadArraysFromResources(int levelOneItemPos, int levelTwoItemPos, Context mContext)
    {
        if(levelOneItemPos == 1 && (levelTwoItemPos == 0 || levelTwoItemPos == 1 || levelTwoItemPos == 2 ||
        levelTwoItemPos == 7 || levelTwoItemPos == 8)){
            noChildInThird =true;
        } else if(levelOneItemPos == 5){
            noChildInThird =true;
        } else if(levelOneItemPos == 8){
            noChildInThird =true;
        } else {

            String[] icons = IconFactory.getL3Icons(
                    PathFactory.getIconDirectory(mContext),
                    LanguageFactory.getCurrentLanguageCode(mContext),
                    getLevel2_3IconCode(levelOneItemPos),
                    getLevel2_3IconCode(levelTwoItemPos)
            );

            Icon[] iconObjects = TextFactory.getIconObjects(
                    PathFactory.getJSONFile(mContext),
                    IconFactory.removeFileExtension(icons)
            );

            String[] level3DisplayText = TextFactory.getDisplayText(iconObjects);

            String[] level3Speech = TextFactory.getSpeechText(iconObjects);

            loadAdapterMenuTextIconsWithoutSort(icons,level3DisplayText,level3Speech);
        }

        return true;
    }//End of the function

    private void loadAdapterMenuTextIconsWithoutSort(String[] typeIconArray, String[] displayText,String[] speechText)
    {
        thirdLevelIcons=typeIconArray;
        thirdLevelTitles=displayText;
        thirdLevelSpeech=speechText;
    }

    private String getLevel2_3IconCode(int level1Position){
        if(level1Position+1 <= 9){
            return "0" + Integer.toString(level1Position+1);
        } else {
            return Integer.toString(level1Position+1);
        }
    }
}
//END
