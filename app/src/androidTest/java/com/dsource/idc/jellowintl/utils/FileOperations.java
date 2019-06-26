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
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;

public class FileOperations {

    public static void copyAssetsToInternalStorage(Context appContext) {
        Context testContext = getInstrumentation().getContext();
        AssetManager assets = testContext.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assets.open(ENG_IN+".zip", Context.MODE_PRIVATE);
            File outFile = appContext.getDir(ENG_IN, Context.MODE_PRIVATE);
            outFile.mkdir();
            out = new FileOutputStream(outFile+"/"+ENG_IN+".zip");
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

    public static void extractLanguagePackageZipFile(Context context) {
        File en_dir = context.getDir(ENG_IN, Context.MODE_PRIVATE);
        ZipArchive.unzip(en_dir.getPath()+"/"+ENG_IN+".zip",en_dir.getPath(),"");
        File zip = new File(en_dir.getPath(),ENG_IN+".zip");
        if(zip.exists()) zip.delete();
    }
}
