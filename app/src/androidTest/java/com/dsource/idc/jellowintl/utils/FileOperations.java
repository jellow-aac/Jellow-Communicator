package com.dsource.idc.jellowintl.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ir.mahdi.mzip.zip.ZipArchive;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

public class FileOperations {

    public static void copyAssetsToInternalStorage(Context appContext, String language) {
        Context testContext = getInstrumentation().getContext();
        AssetManager assets = testContext.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assets.open(language+".zip", Context.MODE_PRIVATE);
            File outFile = appContext.getDir(language, Context.MODE_PRIVATE);
            outFile.mkdir();
            out = new FileOutputStream(outFile+"/"+language+".zip");
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out.flush();
            out = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int read;
        try {
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void extractLanguagePackageZipFile(Context context, String language) {
        File en_dir = context.getDir(language, Context.MODE_PRIVATE);
        ZipArchive.unzip(en_dir.getPath()+"/"+language+".zip",en_dir.getPath(),"");
        File zip = new File(en_dir.getPath(),language+".zip");
        if(zip.exists()) zip.delete();
    }

    public static void deletePackageZipFile(Context context, String language) {
        File file = context.getDir(language, Context.MODE_PRIVATE);
        if (file.exists()) {
            deleteRecursive(file);
        }
        file.delete();
    }

    private static void deleteRecursive(File fileObj) {
        if (fileObj.isDirectory())
            for (File child : fileObj.listFiles())
                deleteRecursive(child);

        fileObj.delete();
    }
}
