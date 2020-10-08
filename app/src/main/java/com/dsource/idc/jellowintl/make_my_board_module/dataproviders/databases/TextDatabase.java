package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.databases;

import android.content.Context;

import com.dsource.idc.jellowintl.cache.CacheManager;
import com.dsource.idc.jellowintl.factories.IconFactory;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.dsource.idc.jellowintl.factories.TextFactory;
import com.dsource.idc.jellowintl.make_my_board_module.datamodels.VerbiageHolder;
import com.dsource.idc.jellowintl.make_my_board_module.dataproviders.data_models.VerbiageModel;
import com.dsource.idc.jellowintl.models.AppDatabase;
import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

public class TextDatabase {

    private String langCode;
    private Context context;
    private AppDatabase database;
    private ProgressUpdates handler;

    public TextDatabase(Context context, String langCode, AppDatabase appDatabase) {
        this.context = context;
        this.langCode = langCode;
        database = appDatabase;
    }

    public void fillDatabase(ProgressUpdates handler) {
        this.handler = handler;
        CacheManager.clearCache();

        String[] iconCodes = IconFactory.getAllIconsCodes(PathFactory.getJSONFile(context, langCode));
        String[] expCodes = IconFactory.getExpressiveIconCodes(PathFactory.getJSONFile(context, langCode), langCode);

        Icon[] icons = TextFactory.getIconObjects(iconCodes);
        ArrayList<String> keyList = new ArrayList<>(Arrays.asList(iconCodes));
        ArrayList<Icon> verbiageList = new ArrayList<>(Arrays.asList(icons));
        ArrayList<String> otherIconKeyList = new ArrayList<>(Arrays.asList(expCodes));
        ArrayList<ExpressiveIcon> otherIconList = new ArrayList<>(Arrays.asList(TextFactory.getExpressiveIconObjects(expCodes)));

        pushNormalIconsDataToDatabase(verbiageList, keyList);
        pushOtherExpressiveToDatabase(otherIconList, otherIconKeyList);
        PathFactory.clearPathCache();
        TextFactory.clearJson();
    }

    public boolean checkForTableExists() {
        return database.verbiageDao().getAllVerbiage(langCode).size() > 100;
    }

    private void pushNormalIconsDataToDatabase(ArrayList<Icon> iconList, ArrayList<String> keyList) {
        VerbiageHolder holder;
        if (handler != null) handler.setMaxProgress(iconList.size());
        for (int i = 0; i < iconList.size(); i++) {
            Icon model = iconList.get(i);
            String displayLabel = model.getDisplay_Label();
            if(displayLabel != null)
                displayLabel = displayLabel.replace("…","");
            model.setDisplay_Label(displayLabel);
            holder = new VerbiageHolder(keyList.get(i), model.getDisplay_Label(), model);
            addIconToDatabase(holder);
            if (handler != null) handler.setCurrentProgress(i+1);
        }
    }

    private void addIconToDatabase(VerbiageHolder holder) {
        VerbiageModel model = new VerbiageModel();
        model.setIconId(holder.getIconID());
        model.setIcon(new Gson().toJson(holder.getIconVerbiage()));
        model.setLanguageCode(langCode);
        model.setTitle(holder.getIconName());
        model.setEventTag(holder.getIconVerbiage().getEvent_Tag());
        model.setSearchTag(holder.getIconVerbiage().getSearchTag());
        database.verbiageDao().insertVerbiage(model);
    }

    private void pushOtherExpressiveToDatabase(ArrayList<ExpressiveIcon> list, ArrayList<String> keys) {
        VerbiageModel model = new VerbiageModel();
        for (int i = 0; i < list.size(); i++) {
            model.setTitle(list.get(i).getTitle());
            model.setIcon(new Gson().toJson(list.get(i)));
            model.setIconId(keys.get(i));
            model.setLanguageCode(langCode);
            database.verbiageDao().insertVerbiage(model);
        }
    }

    public ExpressiveIcon getExpressiveIconsById(String IconID) {
        ExpressiveIcon verbiage = null;
        VerbiageModel model = database.verbiageDao().getVerbiageById(IconID, langCode);
        if (model != null)
            verbiage = new Gson().fromJson(model.getIcon(), ExpressiveIcon.class);
        return verbiage;
    }

    public void addNewVerbiage(String id, Icon jellowVerbiageModel) {
        VerbiageModel verbiageModel = new VerbiageModel();
        verbiageModel.setLanguageCode(langCode);
        verbiageModel.setIconId(id);
        verbiageModel.setTitle(jellowVerbiageModel.getDisplay_Label());
        verbiageModel.setIcon(new Gson().toJson(jellowVerbiageModel));
        verbiageModel.setEventTag(id);
        database.verbiageDao().insertVerbiage(verbiageModel);
    }

    public void updateVerbiage(String id, Icon jellowVerbiageModel) {
        VerbiageModel verbiageModel = new VerbiageModel();
        verbiageModel.setLanguageCode(langCode);
        verbiageModel.setIconId(id);
        verbiageModel.setTitle(jellowVerbiageModel.getDisplay_Label());
        verbiageModel.setIcon(new Gson().toJson(jellowVerbiageModel));
        database.verbiageDao().updateVerbiage(verbiageModel);
    }

    public Icon getVerbiageById(String verbiageID) {
        VerbiageModel model = database.verbiageDao().getVerbiageById(verbiageID, langCode);
        if (model != null)
            return new Gson().fromJson(model.getIcon(), Icon.class);
        else return null;
    }

    public void dropTable() {
        database.verbiageDao().deleteVerbiage(langCode);
    }

    public interface ProgressUpdates{
        void setMaxProgress(int progressSize);
        void setCurrentProgress(int progress);
    }
}
