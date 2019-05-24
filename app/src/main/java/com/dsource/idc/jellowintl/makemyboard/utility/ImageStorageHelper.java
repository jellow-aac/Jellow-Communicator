package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.dsource.idc.jellowintl.makemyboard.models.IconModel;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.io.FileOutputStream;

public class ImageStorageHelper {

    public static void deleteAllCustomImage(Context context, IconModel iconModel){
        //if parent node itself is a custom icon
        if(iconModel.getIcon()!=null)
        if(iconModel.getIcon().isCustomIcon())
            deleteImageFromStorage(iconModel.getIcon().IconDrawable,context);

        //Deleting all the custom icons of level two
        for(IconModel mod:iconModel.getChildren())
            if(mod.getIcon().isCustomIcon())
                deleteImageFromStorage(mod.getIcon().IconDrawable,context);

         //Deleting all the custom icons of level three
        for(IconModel mod:iconModel.getChildren())
            for(IconModel model:mod.getChildren())
                if(model.getIcon().isCustomIcon())
                     deleteImageFromStorage(model.getIcon().IconDrawable,context);


    }

    public static void storeImageToStorage(Bitmap bitmap, String fileID, Context context) {
        FileOutputStream fos;
        File en_dir = context.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            if (!root.exists()) {
                //noinspection ResultOfMethodCallIgnored
                root.mkdirs();
            }
            File file = new File(root, fileID+ ".png");

            try {
                if(file.exists())
                {
                    //noinspection ResultOfMethodCallIgnored
                    file.delete();//Delete the previous image if image is a replace
                    file = new File(root,fileID+".png");
                }
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteImageFromStorage(String fileID,Context context) {
        File en_dir = context.getDir(SessionManager.ENG_IN, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath() + "/boardicon";
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File root = new File(path);
            File file = new File(root, fileID+ ".png");
            if(file.exists())
                //noinspection ResultOfMethodCallIgnored
                file.delete();//Delete the previous image
        }

    }




}
