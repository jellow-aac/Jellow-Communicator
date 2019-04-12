package com.dsource.idc.jellowintl.factories;

import android.content.Context;

import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;

public class PathFactory {

    public static final String JSON_FILE = "map.json";
    public static final String DRAWABLE_FOLDER = "drawables";
    public static final String AUDIO_FOLDER = "audio";
    public static String basePath;
    public static File iconDirectory;
    public static File jsonMap;

    //Used loading Glide images
    public static String getIconPath(Context context, String iconName){
        return getBaseDirectoryPath(context)+ "/" + DRAWABLE_FOLDER + "/" + iconName;
    }

    public static String getBaseDirectoryPath(Context context){
        if(basePath == null){
            String dirName = new SessionManager(context).getLanguage();
            String path = context.getDir(dirName, Context.MODE_PRIVATE).getAbsolutePath();
            basePath = path + "/";
        }
        return basePath;
    }

    public static String getAudioPath(Context context){
        return getBaseDirectoryPath(context) + AUDIO_FOLDER + "/";
    }

    public static File getIconDirectory(Context context){
        if(iconDirectory == null){
            String dirName = new SessionManager(context).getLanguage();
            iconDirectory = new File(context.getDir(dirName, Context.MODE_PRIVATE)+ "/" + DRAWABLE_FOLDER + "/");
        }
        return  iconDirectory;
    }

    public static File getJSONFile(Context context){
        if(jsonMap == null){
            String dirName = new SessionManager(context).getLanguage();
            String path = context.getDir(dirName, Context.MODE_PRIVATE).getAbsolutePath();
            jsonMap = new File(path + "/" + JSON_FILE);
        }
        return jsonMap;
    }

    public static void clearPathCache(){
        basePath = null;
        iconDirectory = null;
        jsonMap = null;
    }
}
