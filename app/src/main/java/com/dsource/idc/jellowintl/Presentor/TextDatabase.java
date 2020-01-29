package com.dsource.idc.jellowintl.Presentor;

import android.content.Context;

import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.VerbiageHolder;
import com.dsource.idc.jellowintl.models.VerbiageModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class TextDatabase {

    private String langCode;
    private Context context;
    private AppDatabase database;

    public TextDatabase(Context context,String langCode, AppDatabase appDatabase){
        this.context = context;
        this.langCode =langCode;
        database = appDatabase;
    }
    public void fillDatabase() {

        CacheManager.clearCache();

        String[] iconCodes = IconFactory.getAllIconsCodes(PathFactory.getJSONFile(context,langCode));
        String[] expCodes = IconFactory.getExpressiveIconCodes(PathFactory.getJSONFile(context,langCode),langCode);

        Icon[] icons = TextFactory.getIconObjects(iconCodes);
        ArrayList<String> keyList = new ArrayList<>(Arrays.asList(iconCodes));
        ArrayList<Icon> verbiageList = new ArrayList<>(Arrays.asList(icons));
        ArrayList<String> otherIconKeyList = new ArrayList<>(Arrays.asList(expCodes));
        ArrayList<ExpressiveIcon> otherIconList = new ArrayList<>(Arrays.asList(TextFactory.getExpressiveIconObjects(expCodes)));

        pushNormalIconsDataToDatabase(verbiageList, keyList);
        pushOtherExpressiveToDatabase(otherIconList,otherIconKeyList);
    }
    public boolean checkForTableExists(){
        return database.verbiageDao().getAllVerbiage(langCode).size() > 100;
    }

    private void pushNormalIconsDataToDatabase(ArrayList<Icon> iconList, ArrayList<String> keyList) {
        VerbiageHolder holder;
        for(int i=0;i<iconList.size();i++) {
            Icon model=iconList.get(i);
            holder=new VerbiageHolder(keyList.get(i),model.getDisplay_Label(),model);
            addIconToDatabase(holder);
        }
    }

    private void addIconToDatabase(VerbiageHolder holder) {
        VerbiageModel model = new VerbiageModel();
        model.setIconId(holder.getIconID());
        model.setIcon(new Gson().toJson(holder.getIconVerbaige()));
        model.setLanguageCode(langCode);
        model.setTitle(holder.getIconName());
        model.setEventTag(holder.getIconVerbaige().getEvent_Tag());
        model.setSearchTag(holder.getIconVerbaige().getSearchTag());
        database.verbiageDao().insertVerbiage(model);
    }

    private void pushOtherExpressiveToDatabase(ArrayList<ExpressiveIcon> list, ArrayList<String> keys) {
        VerbiageModel model = new VerbiageModel();
        for(int i=0;i<list.size();i++)
        {
            model.setTitle(list.get(i).getTitle());
            model.setIcon(new Gson().toJson(list.get(i)));
            model.setIconId(keys.get(i));
            model.setLanguageCode(langCode);
            database.verbiageDao().insertVerbiage(model);
        }
    }
}
