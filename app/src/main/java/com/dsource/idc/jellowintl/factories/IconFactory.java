package com.dsource.idc.jellowintl.factories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.models.Icon;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static com.dsource.idc.jellowintl.factories.TextFactory.JSON;
import static com.dsource.idc.jellowintl.factories.TextFactory.getStringFromFile;

public class IconFactory {

    public static final String EXTENSION = ".png";
    private static final char EXTENSION_SEPARATOR = '.';

    // L3 : "[0-9]{6}([1-9][0-9]{3}|[0-9][1-9][0-9]{2}|[0-9]{2}[1-9][0-9]|[0-9]{3}[1-9])GG"
    //  or "\d{6}([1-9]\d{3}|\d{3}[1-9]|\d{2}[1-9]\d|\d[1-9]\d{2})GG"
    //  Java version : "\\d{6}([1-9]\\d{3}|\\d{3}[1-9]|\\d{2}[1-9]\\d|\\d[1-9]\\d{2})GG"
    // L2 : "[0-9]{4}([1-9][1-9]|[0-9][1-9]|[1-9][0-9])0{4}GG"
    // or "[0-9]{4}([1-9][1-9]|[0][1-9]|[1-9][0])0{4}GG"
    // L1 : "[0-9]{4}0{6}GG"

    public static String getIconCode(String langCode, int levelOne, int levelTwo){
        String iconCode = langCode.concat("AABB0000GG");
        if((levelOne+1) < 10)
            iconCode = iconCode.replace("AA","0"+(levelOne+1));
        else
            iconCode = iconCode.replace("AA", String.valueOf(levelOne+1));
        if(levelTwo == -1)
            iconCode = iconCode.replace("BB","00");
        else if((levelTwo+1) < 10)
            iconCode = iconCode.replace("BB","0"+(levelTwo+1));
        else
            iconCode = iconCode.replace("BB", String.valueOf((levelTwo+1)));
        return iconCode;
    }


    /*RAHUL*/
    public static String[] getIconNames(Icon[] iconObjects) {
        ArrayList<String> iconName = new ArrayList<>();
        for(Icon icon : iconObjects){
            iconName.add(icon.getEvent_Tag());
        }

        String[] iconNameArray = new String[iconName.size()];

        return iconName.toArray(iconNameArray);
    }

    public static String[] getL1IconCodes(@NonNull File file, String langCode){
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        String regex = langCode + "[0-9]{2}" + "0{6}GG";
        ArrayList<String> iconNames = new ArrayList<>();
        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }
        Collections.sort(iconNames);
        String[] iconNameArray = new String[iconNames.size()];
        return iconNames.toArray(iconNameArray);
    }

    public static String[] getL2IconCodes(@NonNull File file, String langCode, String level1IconCode){
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        String regex = langCode + level1IconCode +
                "([1-9][1-9]|[0-9][1-9]|[1-9][0-9])" + "0{4}(GG|SS)";
        ArrayList<String> iconNames = new ArrayList<>();

        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }

        Collections.sort(iconNames);
        String[] iconNameArray = new String[iconNames.size()];
        return iconNames.toArray(iconNameArray);
    }

    public static String[] getL3IconCodes(@NonNull File file, String langCode, String level1IconCode, String level2IconCode){
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        String regex = langCode + level1IconCode + level2IconCode +
                "([1-9][0-9]{3}|[0-9][1-9][0-9]{2}|[0-9]{2}[1-9][0-9]|[0-9]{3}[1-9])GG";
        ArrayList<String> iconNames = new ArrayList<>();
        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }

        Collections.sort(iconNames);

        String[] iconNameArray = new String[iconNames.size()];
        return iconNames.toArray(iconNameArray);
    }

    public static String[] getSequenceIconCodes(@NonNull File file, String langCode, String level2IconCode){
        final String level1IconCode = "02";
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        String regex = langCode + level1IconCode + level2IconCode +
                "([1-9][0-9]{3}|[0-9][1-9][0-9]{2}|[0-9]{2}[1-9][0-9]|[0-9]{3}[1-9])SS";
        ArrayList<String> iconNames = new ArrayList<>();
        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }
        Collections.sort(iconNames);
        String[] iconNameArray = new String[iconNames.size()];
        return iconNames.toArray(iconNameArray);
    }

    public static String[] getMiscellaneousIconCodes(@NonNull File file, String langCode){
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        String regex = langCode + "[0-9]{2}" + "MS";
        ArrayList<String> iconNames = new ArrayList<>();
        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }

        Collections.sort(iconNames);
        String[] miscellaneousIcons = new String[iconNames.size()];
        return iconNames.toArray(miscellaneousIcons);
        /*IconCache.setMiscellaneousIcons(miscellaneousIcons);
        return IconCache.getMiscellaneousIcons();*/
    }

    public static String[] getExpressiveIconCodes(@NonNull File file, String langCode){
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }

        String regex = langCode + "[0-9]{2}" + "EE";
        ArrayList<String> iconNames = new ArrayList<>();
        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }
        Collections.sort(iconNames);
        String[] expressiveIcons = new String[iconNames.size()];
        return iconNames.toArray(expressiveIcons);
        /*IconCache.setExpressiveIcons(expressiveIcons);
        return IconCache.getExpressiveIcons();*/
    }

    public static String[] getMiscellaneousNavigationIconCodes(@NonNull File file, String langCode){
        if (JSON == null) {
            try {
                JSON = new JSONObject(getStringFromFile(file));
            } catch (Exception e) {
                Log.d("JSON", e.getMessage());
            }
        }
        String regex = langCode + "0[4-5]MS";
        ArrayList<String> iconNames = new ArrayList<>();
        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
            String key = it.next();
            if(key.matches(regex))
                iconNames.add(key);
        }
        Collections.sort(iconNames);
        String[] expressiveIcons = new String[iconNames.size()];
        return iconNames.toArray(expressiveIcons);
    }


    public static String[] getAllIconsCodes(@NonNull File file){

         try {
            JSON = new JSONObject(getStringFromFile(file));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> iconNames = new ArrayList<>();

        Iterator<String> it = JSON.keys();
        while (it.hasNext()){
                    iconNames.add(it.next());
        }
        Collections.sort(iconNames);
        String[] iconCodes = new String[iconNames.size()];
        return iconNames.toArray(iconCodes);
    }
}
