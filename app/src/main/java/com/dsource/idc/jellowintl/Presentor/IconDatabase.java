package com.dsource.idc.jellowintl.Presentor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.models.VerbiageModel;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.gson.Gson;

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
     * @param iconTitle The Nth row in the table.
     * @return a JellowIcon class object ArrayList with the requested database entry.
     */
    @Nullable
    public ArrayList<JellowIcon> query(String iconTitle) {
        ArrayList<JellowIcon>list =new ArrayList<>();
        Gson gson = new Gson();
        Icon icon;
        String selectQuery = iconTitle+"%";
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageListByTitle(selectQuery,languageCode));
        for(VerbiageModel model:vList){
            if(model.getIconId().length()>=12) {
                icon = gson.fromJson(model.getIcon(), Icon.class);
                list.add(new JellowIcon(model.getIconId(), icon.getDisplay_Label().replace("…",""),
                        icon.getSpeech_Label(), model.getEventTag()));
            }
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
}