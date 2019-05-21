package com.dsource.idc.jellowintl.packageUpdate;

import android.content.Context;
import android.util.Log;

import com.dsource.idc.jellowintl.factories.LanguageFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ir.mahdi.mzip.zip.ZipArchive;

public class FileUtils {

    private static final String TAG = "jellowDebug";
    private static final String UPDATE_DIR = "update";

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

    public static File getBaseDir(Context context){
        String currentLanguage = LanguageFactory.getCurrentLocaleCode(context);
        if(currentLanguage == null){
            Log.d(TAG,"Error: Current language code is NULL");
            return null;
        }
        return context.getDir(currentLanguage, Context.MODE_PRIVATE);
    }

    public static void unzip(String zipFilePath,String outputPath) {
        ZipArchive zipArchive = new ZipArchive();
        try {
            zipArchive.unzip(zipFilePath, outputPath, "");
        } catch (Exception e) {
            Log.d(TAG, "Error: unzipping " + zipFilePath);
        }
    }

    public static void cleanUpdateFiles(Context context) {
        File updateDir = getUpdateDir(context);
        deleteDir(updateDir);
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

    public static File getFile(Context context,String fileName){
        File baseDir = getBaseDir(context);
        return new File(baseDir.getAbsolutePath(),fileName);
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
