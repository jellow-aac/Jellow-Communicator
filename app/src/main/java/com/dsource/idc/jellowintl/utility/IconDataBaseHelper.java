package com.dsource.idc.jellowintl.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.dsource.idc.jellowintl.R;

import java.util.ArrayList;

/**
 * Created by Ayaz Alam on 31/05/2018.
 */
public class IconDataBaseHelper extends SQLiteOpenHelper {

    Context context;
    // Declaring all these as constants makes code a lot more readable, and looking like SQL.
    // Versions has to be 1 first time or app will crash.
    private static final int DATABASE_VERSION = 1;
    private static final String ICON_LIST_TABLE = "icons";
    private static final String DATABASE_NAME = "level3.db";
    // Column names...
    public static final String ICON_ID = "_id";//Icon Primary key ID
    public static final String ICON_TITLE = "icon_title";//Icon Title
    public static final String ICON_DRAWABLE = "icon_drawable";//Icon Drawable
    public static final String KEY_P1 = "icon_p1";//First Level Parent
    public static final String KEY_P2 = "icon_p2";//Second Level Parent
    public static final String KEY_P3 = "icon_p3";//Third Level Parent
    // ... and a string array of columns.
    private static final String[] COLUMNS =
            {ICON_ID, ICON_TITLE,ICON_DRAWABLE,KEY_P1,KEY_P2,KEY_P3};
    // Build the SQL query that creates the table.
    private static final String ICON_LIST_TABLE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + ICON_LIST_TABLE + " (" + ICON_ID + " INTEGER PRIMARY KEY, "
                    + ICON_TITLE + " TEXT," //Icon Title
                    + ICON_DRAWABLE + " TEXT,"//Icon's Drawable resource
                    + KEY_P1 + " INTERGER,"//Level 1 parent
                    + KEY_P2 + " INTEGER, " //Level 2 parent
                    + KEY_P3 +" INTEGER  );";//Level 3 parent
    private SQLiteDatabase mReadableDB;
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
        String[] levelOneIcon = context.getResources().getStringArray(R.array.arrLevelOneIconAdapter);
        String[] levelOneTitles = context.getResources().getStringArray(R.array.arrLevelOneActionBarTitle);
        //Level 1 JellowIcon
        for (int i = 0; i < levelOneIcon.length; i++) {
            // Put column/value pairs into the container. put() overwrites existing values.
            values.put(ICON_TITLE, levelOneTitles[i]);
            values.put(ICON_DRAWABLE, levelOneIcon[i]);
            values.put(KEY_P1, i);
            values.put(KEY_P2, -1);
            values.put(KEY_P3, -1);
            db.insert(ICON_LIST_TABLE, null, values);
        }
        //Filling the database for Level 2
        for (int i = 0; i < levelOneIcon.length; i++) {
            String[] levelTwoIcons = getIconLevel2(i);
            String[] levelTwoTitles = getIconTitleLevel2(i);
            for (int j = 0; j < levelTwoIcons.length; j++) {
                // Put column/value pairs into the container. put() overwrites existing values.
                values.put(ICON_TITLE, levelTwoTitles[j]);
                values.put(ICON_DRAWABLE, levelTwoIcons[j]);
                values.put(KEY_P1, i);
                values.put(KEY_P2, j);
                values.put(KEY_P3, -1);
                db.insert(ICON_LIST_TABLE, null, values);

            }
        }
        //Filling the database for Level Three
        for (int i = 0; i < levelOneIcon.length; i++) {
            String[] levelTwoIcons = getIconLevel2(i);
            for (int j = 0; j < levelTwoIcons.length; j++) {
                noChildInThird = false;
                if (loadArraysFromResources(i, j)) {
                    if (thirdLevelTitles != null) {
                        String[] levelThreeIcons = thirdLevelIcons;
                        String[] levelThreeTitle = thirdLevelTitles;
                        for (int k = 0; k < thirdLevelTitles.length; k++) {
                            // Put column/value pairs into the container. put() overwrites existing values.
                            if (noChildInThird)
                                break;
                            values.put(ICON_TITLE, levelThreeTitle[k]);
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

    private String[] getIconLevel2(int pos)
    {
        String arr[]=null;
     switch (pos)
     {
         case 0:arr=context.getResources().getStringArray(R.array.arrLevelTwoGreetFeelIconAdapter);
             break;
         case 1:arr=context.getResources().getStringArray(R.array.arrLevelTwoDailyActIconAdapter);
             break;
         case 2:arr=context.getResources().getStringArray(R.array.arrLevelTwoEatingIconAdapter);
             break;
         case 3:arr=context.getResources().getStringArray(R.array.arrLevelTwoFunIconAdapter);
             break;
         case 4:arr=context.getResources().getStringArray(R.array.arrLevelTwoLearningIconAdapter);
             break;
         case 5:arr=context.getResources().getStringArray(R.array.arrLevelTwoPeopleIcon);
             break;
         case 6:arr=context.getResources().getStringArray(R.array.arrLevelTwoPlacesIcon);
             break;
         case 7:arr=context.getResources().getStringArray(R.array.arrLevelTwoTimeIconAdapter);
             break;
         case 8:arr=context.getResources().getStringArray(R.array.arrLevelTwoHelpIconAdapter);
             break;
         default:
     }
     return arr;
    }

    /**
     * A function to return array of Titles of level 2
     *
     * */
    private String[] getIconTitleLevel2(int pos)
    {
        String arr[]=null;
        switch (pos)
        {
            case 0:arr=context.getResources().getStringArray(R.array.arrLevelTwoGreetFeelAdapterText);
                break;
            case 1:arr=context.getResources().getStringArray(R.array.arrLevelTwoDailyActAdapterText);
                break;
            case 2:arr=context.getResources().getStringArray(R.array.arrLevelTwoEatAdapterText);
                break;
            case 3:arr=context.getResources().getStringArray(R.array.arrLevelTwoFunAdapterText);
                break;
            case 4:arr=context.getResources().getStringArray(R.array.arrLevelTwoLearningAdapterText);
                break;
            case 5:arr=context.getResources().getStringArray(R.array.arrLevelTwoPeopleAdapterText);
                break;
            case 6:arr=context.getResources().getStringArray(R.array.arrLevelTwoPlacesAdapterText);
                break;
            case 7:arr=context.getResources().getStringArray(R.array.arrLevelTwoTimeWeatherAdapterText);
                break;
            case 8:arr=context.getResources().getStringArray(R.array.arrLevelTwoHelpAdapterText);
                break;
            default:
        }
        return arr;
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

    private boolean noChildInThird =false;
    private boolean loadArraysFromResources(int levelOneItemPos, int levelTwoItemPos)
    {
        if (levelOneItemPos == 0) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelGreetingAdapterText));
                    break;
                case 1: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelFeelingsAdapterText));
                    break;
                case 2: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelRequestsAdapterText));
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeGreetFeelQuestionsAdapterText));
                    break;
            }
        } else if (levelOneItemPos == 1) {
            switch(levelTwoItemPos){
                case 0:
                    noChildInThird =true;break;
                case 1:
                    noChildInThird =true;break;
                case 2:
                    noChildInThird =true;break;
                case 3: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActClothesAccAdapterText));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActGetReadyAdapterText));
                    break;
                case 5: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActSleepAdapterText));
                    break;
                case 6: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActTherapyAdapterText));
                    break;
                case 7:
                    noChildInThird =true;break;
                case 8:
                    noChildInThird =true;break;
                case 9: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeDailyActHabitsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeDailyActHabitsAdapterText));
                    break;
            }
        } else if (levelOneItemPos == 2) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksBreakfastAdapterText));
                    break;
                case 1: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksLunchDinnerAdapterText));
                    break;
                case 2: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSweetsAdapterText));
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksSnacksAdapterText));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksFruitsAdapterText));
                    break;
                case 5: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksDrinksAdapterText));
                    break;
                case 6: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksCutleryAdapterText));
                    break;
                case 7: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFoodDrinksAddonAdapterText));
                    break;
            }
        } else if (levelOneItemPos == 3) {
            switch(levelTwoItemPos){
                case 0: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunInDGamesAdapterText));
                    break;
                case 1: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunOutDGamesAdapterText));
                    break;
                case 2: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFunSportsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunSportsAdapterText));
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFunTvIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunTvAdapterText));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFunMusicIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunMusicAdapterText));
                    break;
                case 5: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeFunActivitiesAdapterText));
                    break;
            }
        } else if (levelOneItemPos == 4) {
            switch(levelTwoItemPos) {
                case 0: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningAnimBirdsAdapterText));
                    break;
                case 1: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningBodyPartsAdapterText));
                    break;
                case 2: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningBooksIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningBooksAdapterText));
                    break;
                case 3: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningColorsIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningColorsAdapterText));
                    break;
                case 4: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningShapesIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningShapesAdapterText));
                    break;
                case 5: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningStationaryAdapterText));
                    break;
                case 6: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningSchoolObjAdapterText));
                    break;
                case 7: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningHomeIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningHomeObjAdapterText));
                    break;
                case 8: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningTransportationIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningTransportAdapterText));
                    break;
                case 9: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeLearningMoneyIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeLearningMoneyAdapterText));
                    break;
            }
        }
        else if(levelOneItemPos==5)
        {
            noChildInThird =true;
        }
        else if(levelOneItemPos==6){
            switch(levelTwoItemPos) {
                case 0: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesMyHouseIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesMyHouseAdapterText);
                    break;
                case 1: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesSchoolIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesSchoolAdapterText);
                    break;
                case 2: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesMallIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesMallAdapterText);
                    break;
                case 3: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesMuseumIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesMuseumAdapterText);
                    break;
                case 4: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesRestaurantIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesRestaurantAdapterText);
                    break;
                case 5: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesTheatreIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesTheatreAdapterText);
                    break;
                case 6: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesPlaygroundIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesPlaygroundAdapterText);
                    break;
                case 7: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesParkIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesParkAdapterText);
                    break;
                case 8: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesStoreIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesStoreAdapterText);
                    break;
                case 9: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesFriendHouseIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesFriendHouseAdapterText);
                    break;
                case 10: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesRelativeHouseIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesRelativeHouseAdapterText);
                    break;
                case 11: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesHospitalIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesHospitalAdapterText);
                    break;
                case 12: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesClinicIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesClinicAdapterText);
                    break;
                case 13: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesLibraryIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesLibraryAdapterText);
                    break;
                case 14: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesZooIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesZooAdapterText);
                    break;
                case 15: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreePlacesWorshipIconAdapter);
                    thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreePlacesWorshipAdapterText);
                    break;
            }
        }
        else if (levelOneItemPos == 7) {
            switch(levelTwoItemPos) {
                case 0: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaTimeAdapterText);
                    break;
                case 1: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaDayAdapterText);
                    break;
                case 2: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaMonthAdapterText);
                    break;
                case 3: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaWeatherAdapterText);
                    break;
                case 4: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaSeasonsAdapterText);
                    break;
                case 5: thirdLevelIcons=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestIconAdapter);
                        thirdLevelTitles=context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaHoliFestAdapterText);
                    break;
                case 6: loadAdapterMenuTextIconsWithoutSort(context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysIconAdapter),
                        context.getResources().getStringArray(R.array.arrLevelThreeTimeWeaBirthdaysAdapterText));
                    break;
            }
        }
        else if(levelOneItemPos==8)
        {
            noChildInThird =true;
        }

        return true;
    }//End of the function

    String[] thirdLevelIcons=null;
    String[] thirdLevelTitles=null;
    private void loadAdapterMenuTextIconsWithoutSort(String[] typeIconArray, String[] stringBelowTextArray)
    {
    thirdLevelIcons=typeIconArray;
    thirdLevelTitles=stringBelowTextArray;
    }

}

//END