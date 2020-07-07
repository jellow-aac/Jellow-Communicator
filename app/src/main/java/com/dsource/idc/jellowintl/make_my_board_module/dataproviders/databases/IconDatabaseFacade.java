package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.VerbiageModel;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.JellowIcon;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Locale;

public class IconDatabaseFacade {

    private AppDatabase database;
    private String languageCode;

    public IconDatabaseFacade(String languageCode, AppDatabase appDatabase) {
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
        else
            L2 = "";
        String lang  = LanguageFactory.getLanguageCode(languageCode);
        String iconID = lang+L1+L2+"%";
        if(level_1==-1 || level_0 == 8 || level_0 == 5)
            list.addAll(getL2Icons(level_0));
        Gson gson = new Gson();
        Icon icon;
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(iconID,languageCode));
        for(VerbiageModel model:vList){
            icon = gson.fromJson(model.getIcon(), Icon.class);
            /*Level two icons are not loaded, here only load level three icons*/
            if(model.getIconId().length()>=10&&!model.getIconId().substring(6,10).equals("0000"))
                list.add(new JellowIcon(model.getIconId(), icon.getDisplay_Label(),
                        icon.getSpeech_Label(), model.getEventTag()));
        }
        return list;
    }

    private ArrayList<JellowIcon> getL2Icons(int level_0) {
        String L1 = String.format(new Locale(SessionManager.ENG_US),"%02d", (level_0+1));
        String lang  = LanguageFactory.getLanguageCode(languageCode);
        String ID = lang+L1+"__0000__";
        ArrayList<JellowIcon> list=new ArrayList<>();
        Gson gson = new Gson();
        Icon icon;
        ArrayList<VerbiageModel> vList = new ArrayList<>(database.verbiageDao().getVerbiageList(ID,languageCode));
        for(VerbiageModel model:vList){
            icon = gson.fromJson(model.getIcon(), Icon.class);
            if(model.getIconId().length()>=10)
                list.add(new JellowIcon(model.getIconId(), icon.getDisplay_Label(),
                    icon.getSpeech_Label(), model.getEventTag()));
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
        Gson gson = new Gson();
        Icon icon;
        ArrayList<VerbiageModel> vList = new ArrayList<>(
                database.verbiageDao().getVerbiageListByTitle(selectQuery,languageCode));
        for(VerbiageModel model:vList){
            icon = gson.fromJson(model.getIcon(), Icon.class);
            if(model.getIconId().length()>=12)
                list.add(new JellowIcon(model.getIconId(), icon.getDisplay_Label().replace("…",""),
                        icon.getSpeech_Label(), model.getEventTag()));
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
