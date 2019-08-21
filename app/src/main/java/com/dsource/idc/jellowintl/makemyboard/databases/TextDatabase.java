package com.dsource.idc.jellowintl.makemyboard.databases;

import android.content.Context;

import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.makemyboard.models.VerbiageHolder;
import com.dsource.idc.jellowintl.makemyboard.models.VerbiageModel;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
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
    private void pushMiscellaneousIconToDatabase(ArrayList<MiscellaneousIcon> list, ArrayList<String> keys) {
        /*for(int i=0;i<list.size();i++)
        {
            ContentValues values = new ContentValues();
            values.put(ICON_ID,keys.get(i));
            values.put(ICON_TITLE,list.get(i).getTitle());
            values.put(VERBIAGE,new Gson().toJson(list.get(i)));
           db.insert(TABLE, null, values);
        }*/
    }

    public MiscellaneousIcon getMiscellaneousIconsById(String IconID) {
        MiscellaneousIcon verbiage=null;
        verbiage = new Gson().fromJson(database.verbiageDao().getVerbiageById(IconID,langCode).getIcon(),MiscellaneousIcon.class);
        return verbiage;
    }

    public ExpressiveIcon getExpressiveIconsById(String IconID) {
        ExpressiveIcon verbiage=null;
        VerbiageModel model =  database.verbiageDao().getVerbiageById(IconID,langCode);
        if(model!=null)
            verbiage = new Gson().fromJson(model.getIcon(),ExpressiveIcon.class);
        return verbiage;
    }

    public void addNewVerbiage(String id, Icon jellowVerbiageModel) {
        VerbiageModel verbiageModel =new VerbiageModel();
        verbiageModel.setLanguageCode(langCode);
        verbiageModel.setIconId(id);
        verbiageModel.setTitle(jellowVerbiageModel.getDisplay_Label());
        verbiageModel.setIcon(new Gson().toJson(jellowVerbiageModel));
        verbiageModel.setEventTag(id);
         database.verbiageDao().insertVerbiage(verbiageModel);
    }
    public void updateVerbiage(String id, Icon jellowVerbiageModel) {
        VerbiageModel verbiageModel =new VerbiageModel();
        verbiageModel.setLanguageCode(langCode);
        verbiageModel.setIconId(id);
        verbiageModel.setTitle(jellowVerbiageModel.getDisplay_Label());
        verbiageModel.setIcon(new Gson().toJson(jellowVerbiageModel));
        database.verbiageDao().updateVerbiage(verbiageModel);
    }

    public int getLevelOneIconCount() {
        String id = LanguageFactory.getLanguageCode(langCode)+"__000000%";
        return database.verbiageDao().getLevelOneIconCount(id,langCode);

    }
    public int getLevelTwoIconCount(int p1){
        String lang_code = LanguageFactory.getLanguageCode(langCode);
        String L1Parent = String.format("%02d", (p1+1));
        String id  =lang_code+L1Parent+"__0000%";
        return database.verbiageDao().getLevelOneIconCount(id,lang_code);
    }
    public int getLevelThreeIconCount(int p1,int p2){
        String L1Parent = String.format("%02d", (p1+1));
        String L2Parent = String.format("%02d", (p2+1));
        String lang_code = LanguageFactory.getLanguageCode(langCode);
        String id  = lang_code+L1Parent+L2Parent+"%";
        return database.verbiageDao().getLevelOneIconCount(id,lang_code);
    }

    public int countEntries(){
        return database.verbiageDao().getAllVerbiage(langCode).size();
    }

    public Icon getVerbiageById(String verbiageID){
        VerbiageModel model = database.verbiageDao().getVerbiageById(verbiageID,langCode);
        Icon icon = new Gson().fromJson(model.getIcon(),Icon.class);
        return icon;
    }
}
