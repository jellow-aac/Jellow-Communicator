package com.dsource.idc.jellowintl.make_my_board_module.dataproviders.helper_classes;


import androidx.room.TypeConverter;

import com.dsource.idc.jellowintl.make_my_board_module.datamodels.BoardIconModel;
import com.dsource.idc.jellowintl.models.Icon;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomTypeConverter {

    @TypeConverter
    public static BoardIconModel getStringFromIconModel(String value) {
        return new Gson().fromJson(value, BoardIconModel.class);
    }

    @TypeConverter
    public static String getJson(BoardIconModel iconModel) {
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