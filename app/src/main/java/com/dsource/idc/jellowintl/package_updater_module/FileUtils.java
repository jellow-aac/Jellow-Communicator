package com.dsource.idc.jellowintl.package_updater_module;

import android.content.Context;
import android.util.Log;

import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    private static final String TAG = "jellowDebug";
    private static final String PACKAGE_VERSION_DIR = "vc";
    private static final String DRAWABLES = "drawables";
    private static final String UPDATE_DIR = "update";
    private static final String HMAP_DIR = "hmaps";
    private static final String TYPE_ICON = "icons";
    private static final String TYPE_VERBIAGE = "verbiage";

    public static File getUpdateDir(Context context){
        File updateDir = new File(getBaseDir(context), UPDATE_DIR);
        if(!updateDir.exists()){
            boolean created = updateDir.mkdirs();
            if(created){
                Log.d(TAG,"Success: Update Directory created");
            } else {
                Log.d(TAG,"Error: Update Directory creation failed");
            }
        }
        return updateDir;
    }

    public static File getUpdateIconsDir(Context context){
        File updateIcon = new File(getUpdateDir(context), TYPE_ICON);
        if(!updateIcon.exists()){
            boolean created = updateIcon.mkdirs();
            if(created){
                Log.d(TAG,"Success: Icon Update Directory created");
            } else {
                Log.d(TAG,"Error: Icon Update Directory creation failed");
            }
        }
        return updateIcon;
    }

    public static File getIconsDir(Context context){
        return new File(getBaseDir(context), DRAWABLES);
    }

    public static File getUpdateVerbiageDir(Context context){
        File updateVerbiage = new File(getUpdateDir(context), TYPE_VERBIAGE);
        if(!updateVerbiage.exists()){
            boolean created = updateVerbiage.mkdirs();
            if(created){
                Log.d(TAG,"Success: Verbiage Update Directory created");
            } else {
                Log.d(TAG,"Error: Verbiage Update Directory creation failed");
            }
        }
        return updateVerbiage;
    }

    public static File getBaseDir(Context context){
        return context.getDir(SessionManager.UNIVERSAL_PACKAGE, Context.MODE_PRIVATE);
    }

    public static boolean deleteDir(File dir){
        if(dir.isDirectory()){
            for(File file : dir.listFiles()){
                if(file.isDirectory()){
                    deleteDir(file);
                } else {
                    deleteFile(file);
                }
            }
        }

        boolean deletionSuccess = dir.delete();

        if(deletionSuccess){
            Log.d(TAG,"Directory deleted: "+ dir.getName());
        } else {
            Log.d(TAG,"Error deleting directory: "+ dir.getName());
        }
        return deletionSuccess;
    }

    public static boolean deleteFile(File file){
        boolean success = file.delete();
        if(success){
            Log.d(TAG,"File deleted: "+file.getName());
        } else {
            Log.d(TAG,"Error deleting: "+file.getName());
        }
        return success;
    }

    public static File getUpdateFile(Context context,String fileName){
        File downloadDir = getUpdateDir(context);
        return new File(downloadDir.getAbsolutePath(),fileName);
    }

    public static File getVersionCodeFile(Context context,String fileName){
        File vCodeDir = new File (getBaseDir(context), PACKAGE_VERSION_DIR);
        if(!vCodeDir.exists()){
            boolean created = vCodeDir.mkdirs();
            if(created){
                Log.d(TAG,"Success: Hmap Directory created");
            } else {
                Log.d(TAG,"Error: Hmap Directory creation failed");
            }
        }
        return new File(vCodeDir.getAbsolutePath(),fileName);
    }

    public static File getHmapFile(Context context,String fileName){
        File hmapDir = new File (getBaseDir(context), HMAP_DIR);
        if(!hmapDir.exists()){
            boolean created = hmapDir.mkdirs();
            if(created){
                Log.d(TAG,"Success: Hmap Directory created");
            } else {
                Log.d(TAG,"Error: Hmap Directory creation failed");
            }
        }
        return new File(hmapDir.getAbsolutePath(),fileName);
    }

    public static boolean writeToFile(File file, String content){
        try{
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(content.getBytes());
            outputStream.close();
            Log.d(TAG,"Success: writing to "+file.getName());
            return true;
        } catch (FileNotFoundException e){
            e.printStackTrace();
            Log.d(TAG,"Error: "+file.getName()+" file not found for writing");
            return false;
        } catch (IOException e){
            e.printStackTrace();
            Log.d(TAG,"Error: I/O exception while accessing file "+file.getName());
            return false;
        }

    }

    public static boolean renameFile(File oldFile,File newFile){
        boolean renameSuccess = oldFile.renameTo(newFile);
        if(renameSuccess){
            Log.d(TAG,"Success: "+oldFile.getName()+" -> "+newFile.getName());
        } else {
            Log.d(TAG,"Failed: "+oldFile.getName()+" -> "+newFile.getName());
        }
        return renameSuccess;
    }

    public static boolean doesExist(File file){
        boolean exists = file.exists();
        if(exists){
            Log.d(TAG,"File Found: "+file.getName());
        } else {
            Log.d(TAG,"File Not Found: "+file.getName());
        }
        return exists;
    }
}
