package com.dsource.idc.jellowintl.factories;

import com.dsource.idc.jellowintl.cache.IconCache;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class IconFactory {

    private static final String EXTENSION = ".png";
    private static final char EXTENSION_SEPARATOR = '.';

    // L3 : "[0-9]{6}([1-9][0-9]{3}|[0-9][1-9][0-9]{2}|[0-9]{2}[1-9][0-9]|[0-9]{3}[1-9])GG"
    //  or "\d{6}([1-9]\d{3}|\d{3}[1-9]|\d{2}[1-9]\d|\d[1-9]\d{2})GG"
    //  Java version : "\\d{6}([1-9]\\d{3}|\\d{3}[1-9]|\\d{2}[1-9]\\d|\\d[1-9]\\d{2})GG"
    // L2 : "[0-9]{4}([1-9][1-9]|[0-9][1-9]|[1-9][0-9])0{4}GG"
    // or "[0-9]{4}([1-9][1-9]|[0][1-9]|[1-9][0])0{4}GG"
    // L1 : "[0-9]{4}0{6}GG"

    /**
     * @param dir - directory where icons are located
     * @param langCode - language code
     */

    public static String[] getL1Icons(@NonNull File dir, String langCode) {

        if (IconCache.getL1Icons() != null && IconCache.getL1Icons().length != 0) {
            return IconCache.getL1Icons();
        }

        String regex = langCode + "[0-9]{2}" + "0{6}GG" + EXTENSION;
        ArrayList<String> iconNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                iconNames.add(fileName);
            }
        }

        Collections.sort(iconNames);

        String[] level1Icons = new String[iconNames.size()];

        iconNames.toArray(level1Icons);

        IconCache.setL1Icons(level1Icons);
        return IconCache.getL1Icons();
    }


    public static String[] getAllL2Icons(@NonNull File dir, String langCode, String level1IconCode) {

        if (!(IconCache.getAllL2Icons() == null || IconCache.getAllL2Icons().isEmpty())) {
            String key = langCode + level1IconCode;
            if (IconCache.getAllL2Icons().containsKey(key)) {
                return IconCache.getAllL2Icons().get(key);
            }
        }

        String regex = langCode + level1IconCode +
                "([1-9][1-9]|[0-9][1-9]|[1-9][0-9])" + "0{4}(GG|SS)" + EXTENSION;
        ArrayList<String> iconNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                iconNames.add(fileName);
            }
        }

        Collections.sort(iconNames);

        String[] level2Icons = new String[iconNames.size()];

        iconNames.toArray(level2Icons);

        if (IconCache.getAllL2Icons() == null) {
            IconCache.setAllL2Icons(new HashMap<String, String[]>());
        }
        String key = langCode + level1IconCode;
        IconCache.getAllL2Icons().put(key, level2Icons);

        return level2Icons;
    }



    public static String[] getL3Icons(@NonNull File dir, String langCode, String level1IconCode, String level2IconCode) {

        if (!(IconCache.getL3Icons() == null || IconCache.getL3Icons().isEmpty())) {
            String key = langCode + level1IconCode + level2IconCode;
            if (IconCache.getL3Icons().containsKey(key)) {
                return IconCache.getL3Icons().get(key);
            }
        }

        String regex = langCode + level1IconCode + level2IconCode +
                "([1-9][0-9]{3}|[0-9][1-9][0-9]{2}|[0-9]{2}[1-9][0-9]|[0-9]{3}[1-9])GG" + EXTENSION;
        ArrayList<String> iconNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                iconNames.add(fileName);
            }
        }

        Collections.sort(iconNames);

        String[] level3Icons = new String[iconNames.size()];

        iconNames.toArray(level3Icons);

        if (IconCache.getL3Icons() == null) {
            IconCache.setL3Icons(new HashMap<String, String[]>());
        }

        String key = langCode + level1IconCode + level2IconCode;

        IconCache.getL3Icons().put(key, level3Icons);

        return level3Icons;
    }

    public static String[] getL3SeqIcons(@NonNull File dir, String langCode, String level1IconCode, String level2IconCode) {

        if (!(IconCache.getL3SeqIcons() == null || IconCache.getL3SeqIcons().isEmpty())) {
            String key = langCode + level1IconCode + level2IconCode;

            if (IconCache.getL3SeqIcons().containsKey(key)) {
                return IconCache.getL3SeqIcons().get(key);
            }
        }

        String regex = langCode + level1IconCode + level2IconCode +
                "([1-9][0-9]{3}|[0-9][1-9][0-9]{2}|[0-9]{2}[1-9][0-9]|[0-9]{3}[1-9])SS" + EXTENSION;
        ArrayList<String> iconNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                iconNames.add(fileName);
            }
        }

        Collections.sort(iconNames);

        String[] level3SeqIcons = new String[iconNames.size()];

        iconNames.toArray(level3SeqIcons);

        if (IconCache.getL3SeqIcons() == null) {
            IconCache.setL3SeqIcons(new HashMap<String, String[]>());
        }

        String key = langCode + level1IconCode + level2IconCode;

        IconCache.getL3SeqIcons().put(key, level3SeqIcons);

        return level3SeqIcons;
    }

    public static String[] getExpressiveIcons(@NonNull File dir, String langCode) {

        if (IconCache.getExpressiveIcons() != null && IconCache.getExpressiveIcons().length != 0) {
            return IconCache.getExpressiveIcons();
        }

        String regex = langCode + "[0-9]{2}" + "EE" + EXTENSION;
        ArrayList<String> iconNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                iconNames.add(fileName);
            }
        }

        Collections.sort(iconNames);

        String[] expressiveIcons = new String[iconNames.size()];

        iconNames.toArray(expressiveIcons);
        IconCache.setExpressiveIcons(expressiveIcons);
        return IconCache.getExpressiveIcons();
    }


    public static String[] getMiscellaneousIcons(@NonNull File dir, String langCode) {

        if (IconCache.getMiscellaneousIcons() != null && IconCache.getMiscellaneousIcons().length != 0) {
            return IconCache.getMiscellaneousIcons();
        }

        String regex = langCode + "[0-9]{2}" + "MS" + EXTENSION;
        ArrayList<String> iconNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                iconNames.add(fileName);
            }
        }

        Collections.sort(iconNames);

        String[] miscellaneousIcons = new String[iconNames.size()];

        iconNames.toArray(miscellaneousIcons);
        IconCache.setMiscellaneousIcons(miscellaneousIcons);
        return IconCache.getMiscellaneousIcons();
    }

    /** This function used when a non-TTs language parsing the data
     *  for previous and next buttons in Sequence activity.
     ***/
    public static String[] getCategoryNavigationButtons(@NonNull File dir, String langCode) {
        String regex = langCode + "0[4-5]" + "MSTT.mp3";
        ArrayList<String> fileNames = new ArrayList<>();
        for (String fileName : dir.list()) {
            if (fileName.matches(regex)) {
                fileNames.add(fileName.replace("TT", ""));
            }
        }

        Collections.sort(fileNames);

        String[] categoryNavigationButtons = new String[fileNames.size()];

        fileNames.toArray(categoryNavigationButtons);
        return categoryNavigationButtons;
    }

    public static String[] removeFileExtension(String[] iconNamesWithExtension) {
        String[] iconNames = iconNamesWithExtension.clone();
        for (int i = 0; i < iconNames.length; i++) {
            iconNames[i] = iconNames[i].
                    substring(0, iconNames[i].lastIndexOf(EXTENSION_SEPARATOR));
        }
        return iconNames;
    }

}
