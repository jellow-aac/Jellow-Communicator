package com.dsource.idc.jellowintl.Presentor;

import android.content.Context;

import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.SearchIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.dsource.idc.jellowintl.factories.PathFactory.getIconDirectory;

public class SearchHelper {

    public static void insertSearchData(Context context, AppDatabase appDatabase){
        ArrayList<SearchIcon> searchIcons = new ArrayList<>();
        //Filling the database for level ONE SearchIcon
        {
            String[] levelOneIcons = getIconLevel1(context);
            String[] levelOneTitles = getIconTitleLevel1(context);
            String[] levelOneSpeech = getIconSpeechLevel1(context);
            if(levelOneIcons != null) {
                for (int i = 0; i < levelOneTitles.length; i++) {
                    SearchIcon si = new SearchIcon();
                    si.setIconTitle(levelOneTitles[i]);
                    si.setIconSpeech(levelOneSpeech[i]);
                    si.setIconDrawable(levelOneIcons[i]);
                    si.setLevelOne(i);
                    si.setLevelTwo(-1);
                    si.setLevelThree(-1);
                    searchIcons.add(si);
                }
            }

            appDatabase.searchIconsDao().insertPreferences(searchIcons);
            searchIcons.clear();
        }
        //Filling the database for level TWO SearchIcon
        {
            String[] levelOneIcons = getIconLevel1(context);
            for (int i = 0; i < levelOneIcons.length; i++) {
                String[] levelTwoIcons = getIconLevel2(i, context);
                String[] levelTwoTitles = getIconTitleLevel2(i, context);
                String[] levelTwoSpeech = getIconSpeechLevel2(i, context);
                if(levelTwoIcons != null) {
                    for (int j = 0; j < levelTwoIcons.length; j++) {
                        SearchIcon si = new SearchIcon();
                        si.setIconTitle(levelTwoTitles[j]);
                        si.setIconSpeech(levelTwoSpeech[j]);
                        si.setIconDrawable(levelTwoIcons[j]);
                        si.setLevelOne(i);
                        si.setLevelTwo(j);
                        si.setLevelThree(-1);
                        searchIcons.add(si);
                    }
                }
            }
            appDatabase.searchIconsDao().insertPreferences(searchIcons);
            searchIcons.clear();
        }
        //Filling the database for Level THREE SearchIcon
        {
            String[] levelOneIcons = getIconLevel1(context);
            for (int i = 0; i < levelOneIcons.length; i++) {
                String[] levelTwoIcons = getIconLevel2(i, context);
                for (int j = 0; j < levelTwoIcons.length; j++) {
                    String[] levelThreeIcons = getIconLevel3(i, j, context);
                    String[] levelThreeTitles = getIconTitleLevel3(i, j, context);
                    String[] levelThreeSpeech = getIconSpeechLevel3(i, j, context);
                     if (levelThreeIcons != null) {
                        for (int k = 0; k < levelThreeIcons.length; k++) {
                            if (setIfNoLevelThree(i, j))
                                break;
                            SearchIcon si = new SearchIcon();
                            si.setIconTitle(levelThreeTitles[k]);
                            si.setIconSpeech(levelThreeSpeech[k]);
                            si.setIconDrawable(levelThreeIcons[k]);
                            si.setLevelOne(i);
                            si.setLevelTwo(j);
                            si.setLevelThree(k);
                            searchIcons.add(si);
                        }

                    }
                }
            }
            appDatabase.searchIconsDao().insertPreferences(searchIcons);
            searchIcons.clear();
        }
        //Reset the language code
        new SessionManager(context).setLanguageChange(2);
    }

    public static void clearSearchDatabase(AppDatabase appDatabase){
        appDatabase.searchIconsDao().clearSearchDatabase();
    }

    public static List<SearchIcon> getSearchIconsList(AppDatabase appDatabase, String searchString) {
        return appDatabase.searchIconsDao().getSearchIconsList(searchString);
    }

    private static String[] getIconLevel1(Context context){
        return IconFactory.getL1Icons(
                getIconDirectory(context),
                LanguageFactory.getCurrentLanguageCode(context)
        );
    }

    private static String[] getIconTitleLevel1(Context context){
        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(context),
                IconFactory.removeFileExtension(getIconLevel1(context))
        );
        return TextFactory.getDisplayText(iconObjects);
    }

    private static String[] getIconSpeechLevel1(Context context) {Icon[] iconObjects = TextFactory.getIconObjects(
            PathFactory.getJSONFile(context),
            IconFactory.removeFileExtension(getIconLevel1(context))
    );
        return TextFactory.getSpeechText(iconObjects);

    }

    private static String[] getIconLevel2(int pos, Context context) {
        return IconFactory.getAllL2Icons(
                PathFactory.getIconDirectory(context),
                LanguageFactory.getCurrentLanguageCode(context),
                getLevel2_3IconCode(pos)
        );
    }

    private static String[] getIconTitleLevel2(int pos, Context context) {
        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(context),
                IconFactory.removeFileExtension(getIconLevel2(pos,context))
        );
        return TextFactory.getDisplayText(iconObjects);
    }

    private static String[] getIconSpeechLevel2(int pos, Context context) {
        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(context),
                IconFactory.removeFileExtension(getIconLevel2(pos,context))
        );
        return TextFactory.getSpeechText(iconObjects);
    }

    private static String[] getIconLevel3(int level1Pos, int level2Pos, Context context) {
        return IconFactory.getL3Icons(
                PathFactory.getIconDirectory(context),
                LanguageFactory.getCurrentLanguageCode(context),
                getLevel2_3IconCode(level1Pos),
                getLevel2_3IconCode(level2Pos)
        );
    }

    private static String[] getIconTitleLevel3(int level1Pos, int level2Pos, Context context) {
        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(context),
                IconFactory.removeFileExtension(getIconLevel3(level1Pos, level2Pos, context))
        );
        return TextFactory.getDisplayText(iconObjects);
    }

    private static String[] getIconSpeechLevel3(int level1Pos, int level2Pos, Context context) {
        Icon[] iconObjects = TextFactory.getIconObjects(
                PathFactory.getJSONFile(context),
                IconFactory.removeFileExtension(getIconLevel3(level1Pos, level2Pos, context))
        );
        return TextFactory.getSpeechText(iconObjects);
    }

    private static String getLevel2_3IconCode(int level1Position){
        if(level1Position+1 <= 9){
            return "0" + (level1Position + 1);
        } else {
            return Integer.toString(level1Position+1);
        }
    }

    private static boolean setIfNoLevelThree(int levelOneItemPos, int levelTwoItemPos) {
        return "1-0,1-1,1-2,1-7,1-8".contains(levelOneItemPos+"-"+levelTwoItemPos);
    }
}
