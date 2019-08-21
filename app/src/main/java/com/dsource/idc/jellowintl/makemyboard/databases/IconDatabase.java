package com.dsource.idc.jellowintl.makemyboard.databases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.makemyboard.models.VerbiageModel;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.ArrayList;
import java.util.Locale;

public class IconDatabase {

    private AppDatabase database;
    private String languageCode;

    public IconDatabase(String languageCode, AppDatabase appDatabase) {
        this.languageCode =languageCode;
        database = appDatabase;
    }

    /**
     * A function to return list of icon matching with query
     * Queries the database for an entry at a given position.
     *
     * @param level_0 The Nth row in the table.
     * @return a JellowIcon class object ArrayList with the requested database entry.
     */
    @NonNull
    public ArrayList<JellowIcon> myBoardQuery(int level_0, int level_1)
    {
        ArrayList<JellowIcon> list = new ArrayList<>();
        String L1 = String.format(new Locale(SessionManager.ENG_US),"%02d", (level_0+1));
        String L2;
        if(level_1!=-1)
            L2 = String.format(new Locale(SessionManager.ENG_US),"%02d", (level_1+1));
        else L2 = "";
        String lang  = LanguageFactory.getLanguageCode(languageCode);
        String iconID = lang+L1+L2+"%";
        if(level_1==-1)
            list.addAll(getL2Icons(level_0));
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(iconID,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=10&&model.getIconId().charAt(9)!='0')
                 list.add(new JellowIcon(model.getIconId(),model.getTitle(),model.getEventTag()));
        }
        return list;
    }

    private ArrayList<JellowIcon> getL2Icons(int level_0) {
        String L1 = String.format(new Locale(SessionManager.ENG_US),"%02d", (level_0+1));
        String lang  = LanguageFactory.getLanguageCode(languageCode);
        String ID = lang+L1+"__0000__";
        ArrayList<JellowIcon> list=new ArrayList<>();
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(ID,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=10)
                list.add(new JellowIcon(model.getIconId(),model.getTitle(),model.getEventTag()));
        }
        return list;

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
        ArrayList<JellowIcon>list =new ArrayList<>();
        String selectQuery = iconTitle+"%";
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageListByTitle(selectQuery,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=12)
            list.add(new JellowIcon(model.getIconId(),model.getTitle(),model.getEventTag()));
        }
        return list;
    }

    @Nullable
    public ArrayList<JellowIcon> getAllIcons() {
        ArrayList<JellowIcon>list =new ArrayList<>();
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getAllVerbiage(languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=10)
                list.add(new JellowIcon(model.getIconId(),model.getTitle(),model.getEventTag()));
        }
        return list;
    }

    @NonNull
    public ArrayList<String> getLevelOneIconsTitles() {
        ArrayList<String> list = new ArrayList<>();
        String lang_code = LanguageFactory.getLanguageCode(languageCode);
        String query  = lang_code+"__000000GG";

        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(query,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=10)
                list.add(model.getTitle());
        }
        return list;
    }

    @Nullable
    public ArrayList<String> getLevelTwoIconsTitles(int level1) {
        ArrayList<String> list = new ArrayList<>();
        String lang_code = LanguageFactory.getLanguageCode(languageCode);
        String L1Parent = String.format(new Locale(SessionManager.ENG_US),"%02d",(level1+1));
        String query  = lang_code+L1Parent+"__0000__";

        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(query,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=10)
                list.add(model.getTitle());
        }
        return list;
    }

    @Nullable
    public ArrayList<String> getLevelThreeIconsTitles(int level1,int level2)
    {
        ArrayList<String> list = new ArrayList<>();
        String L1Parent = String.format(new Locale(SessionManager.ENG_US),"%02d", (level1+1));
        String L2Parent = String.format(new Locale(SessionManager.ENG_US),"%02d", (level2+1));
        String lang_code = LanguageFactory.getLanguageCode(languageCode);
        String query  = lang_code+L1Parent+L2Parent;
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(query,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=10)
                list.add(model.getTitle());
        }
        return list;
    }
}
