package com.dsource.idc.jellowintl.cache;

import java.util.HashMap;

public class IconCache {
    private static String[] ExpressiveIcons;
    private static String[] L1Icons;
    private static HashMap<String, String[]> AllL2Icons;
    private static HashMap<String, String[]> L3Icons;
    private static HashMap<String, String[]> L3SeqIcons;
    private static String[] MiscellaneousIcons;

    //private static HashMap<String, String[]> L2Icons;
    //private static HashMap<String, String[]> L2SeqIcons;

    public static void clearIconCache() {
        setL1Icons(null);
        setAllL2Icons(null);
        setL3Icons(null);
        setL3SeqIcons(null);
        setExpressiveIcons(null);
        setMiscellaneousIcons(null);

        //setL2Icons(null);
        //setL2SeqIcons(null);
    }

    public static String[] getExpressiveIcons() {
        return ExpressiveIcons;
    }

    public static void setExpressiveIcons(String[] expressiveIcons) {
        ExpressiveIcons = expressiveIcons;
    }

    public static String[] getL1Icons() {
        return L1Icons;
    }

    public static void setL1Icons(String[] l1Icons) {
        L1Icons = l1Icons;
    }

    public static HashMap<String, String[]> getAllL2Icons() {
        return AllL2Icons;
    }

    public static void setAllL2Icons(HashMap<String, String[]> allL2Icons) {
        AllL2Icons = allL2Icons;
    }

    public static HashMap<String, String[]> getL3Icons() {
        return L3Icons;
    }

    public static void setL3Icons(HashMap<String, String[]> l3Icons) {
        L3Icons = l3Icons;
    }

    public static HashMap<String, String[]> getL3SeqIcons() {
        return L3SeqIcons;
    }

    public static void setL3SeqIcons(HashMap<String, String[]> l3SeqIcons) {
        L3SeqIcons = l3SeqIcons;
    }

    public static String[] getMiscellaneousIcons() {
        return MiscellaneousIcons;
    }

    public static void setMiscellaneousIcons(String[] miscellaneousIcons) {
        MiscellaneousIcons = miscellaneousIcons;
    }

    /*public static HashMap<String, String[]> getL2Icons() {
        return L2Icons;
    }

    public static void setL2Icons(HashMap<String, String[]> l2Icons) {
        L2Icons = l2Icons;
    }

    public static HashMap<String, String[]> getL2SeqIcons() {
        return L2SeqIcons;
    }

    public static void setL2SeqIcons(HashMap<String, String[]> l2SeqIcons) {
        L2SeqIcons = l2SeqIcons;
    }*/
}
