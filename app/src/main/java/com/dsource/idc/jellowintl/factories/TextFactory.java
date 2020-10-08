package com.dsource.idc.jellowintl.factories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.models.ExpressiveIcon;
import com.dsource.idc.jellowintl.models.Icon;
import com.dsource.idc.jellowintl.models.MiscellaneousIcon;
import com.dsource.idc.jellowintl.models.SeqNavigationButton;
import com.google.firebase.database.annotations.NotNull;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class TextFactory {

    static JSONObject JSON = null;

    public static void clearJson(){
        JSON = null;
    }

    public static String getStringFromFile(@NonNull File file) {

        StringBuilder text = new StringBuilder();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                text.append(line);
            }
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return text.toString();

    }

    public static String[] getDisplayText(Icon[] iconObjects){

        ArrayList<String> displayText = new ArrayList<>();
        for(Icon icon : iconObjects){
            displayText.add(icon.getDisplay_Label());
        }

        String[] displayTextArray = new String[displayText.size()];

        return displayText.toArray(displayTextArray);
    }

    public static String[] getTitle(MiscellaneousIcon[] iconObjects){

        ArrayList<String> title = new ArrayList<>();
        for(MiscellaneousIcon icon : iconObjects){
            title.add(icon.getTitle());
        }

        String[] titleArray = new String[title.size()];

        return title.toArray(titleArray);
    }

    public static String[] getExpressiveSpeechText(ExpressiveIcon[] iconObjects){

        ArrayList<String> title = new ArrayList<>();
        for(ExpressiveIcon icon : iconObjects){
            title.add(icon.getL());
            title.add(icon.getLL());
        }

        String[] titleArray = new String[title.size()];

        return title.toArray(titleArray);
    }

    public static String[] getSpeechText(Icon[] iconObjects){

        ArrayList<String> speechText = new ArrayList<>();
        for(Icon icon : iconObjects){
            speechText.add(icon.getSpeech_Label());
        }

        String[] speechTextArray = new String[speechText.size()];

        return speechText.toArray(speechTextArray);
    }


    /*RAHUL*/
    public static Icon[] getIconObjects(@NotNull String[] iconNames) {
        ArrayList<Icon> iconObjects = new ArrayList<>();
        for (String iconName : iconNames) {
            try {
                String jsonString = JSON.getJSONObject(iconName).toString();
                Icon icon = new Gson().fromJson(jsonString, Icon.class);
                iconObjects.add(icon);
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        Icon[] generalIconObjects = new Icon[iconObjects.size()];
        iconObjects.toArray(generalIconObjects);
        return generalIconObjects;
    }

    public static MiscellaneousIcon[] getMiscellaneousIconObjects(@NotNull String[] iconNames) {
        ArrayList<MiscellaneousIcon> iconObjects = new ArrayList<>();
        for (String iconName : iconNames) {
            try {
                String jsonString = JSON.getJSONObject(iconName).toString();
                MiscellaneousIcon icon = new Gson().fromJson(jsonString, MiscellaneousIcon.class);
                iconObjects.add(icon);
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        MiscellaneousIcon[] miscellaneousIconObjects = new MiscellaneousIcon[iconObjects.size()];
        iconObjects.toArray(miscellaneousIconObjects);
        return miscellaneousIconObjects;
    }

    public static ExpressiveIcon[] getExpressiveIconObjects(@NotNull String[] iconNames){
        ArrayList<ExpressiveIcon> iconObjects = new ArrayList<>();
        for (String iconName : iconNames) {
            try {
                String jsonString = JSON.getJSONObject(iconName).toString();
                ExpressiveIcon icon = new Gson().fromJson(jsonString, ExpressiveIcon.class);
                iconObjects.add(icon);
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        ExpressiveIcon[] expressiveIconObjects = new ExpressiveIcon[iconObjects.size()];
        iconObjects.toArray(expressiveIconObjects);
        return expressiveIconObjects;
    }

    public static SeqNavigationButton[] getMiscellaneousNavigationIconObjects(@NotNull String[] iconNames){
        ArrayList<SeqNavigationButton> iconObjects = new ArrayList<>();
        for (String iconName : iconNames) {
            try {
                String jsonString = JSON.getJSONObject(iconName).toString();
                SeqNavigationButton icon = new Gson().fromJson(jsonString, SeqNavigationButton.class);
                iconObjects.add(icon);
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        SeqNavigationButton[] expressiveIconObjects = new SeqNavigationButton[iconObjects.size()];
        iconObjects.toArray(expressiveIconObjects);
        return expressiveIconObjects;
    }
}
