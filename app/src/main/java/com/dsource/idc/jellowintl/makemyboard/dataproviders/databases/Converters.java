package com.dsource.idc.jellowintl.makemyboard.dataproviders.databases;


import androidx.room.TypeConverter;

import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.models.Icon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Converters {

    @TypeConverter
    public static IconModel getStringFromIconModel(String value) {
        return new Gson().fromJson(value, IconModel.class);
    }

    @TypeConverter
    public static String getJson(IconModel iconModel) {
        return new Gson().toJson(iconModel);
    }

    @TypeConverter
    public static Icon getIcon(String value) {
        return new Gson().fromJson(value, Icon.class);
    }

    @TypeConverter
    public static String getJson(Icon iconModel) {
        return new Gson().toJson(iconModel);
    }

    @TypeConverter
    public static ArrayList<String> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    @TypeConverter
    public static String getJson(ArrayList<String> list) { return new Gson().toJson(list); }
}