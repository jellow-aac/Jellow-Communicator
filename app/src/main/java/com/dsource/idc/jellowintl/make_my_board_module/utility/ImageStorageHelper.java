package com.dsource.idc.jellowintl.make_my_board_module.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.dsource.idc.jellowintl.make_my_board_module.datamodels.BoardIconModel;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.io.FileOutputStream;

public class ImageStorageHelper {

    public static void deleteAllCustomImage(Context context, BoardIconModel iconModel){
        if(iconModel==null) return;
        //if parent node itself is a custom icon
        if(iconModel.getIcon()!=null)
        if(iconModel.getIcon().isCustomIcon())
            deleteImageFromStorage(iconModel.getIcon().getIconDrawable(),context);

        //Deleting all the custom icons of level two
        for(BoardIconModel mod:iconModel.getChildren())
            if(mod.getIcon().isCustomIcon())
                deleteImageFromStorage(mod.getIcon().getIconDrawable(),context);

         //Deleting all the custom icons of level three
        for(BoardIconModel mod:iconModel.getChildren())
            for(BoardIconModel model:mod.getChildren())
                if(model.getIcon().isCustomIcon())
                     deleteImageFromStorage(model.getIcon().getIconDrawable(),context);


    }

    public static void storeImageToStorage(Bitmap bitmap, String fileID, Context context) {
        FileOutputStream fos;
        File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
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

    private static void deleteImageFromStorage(String fileID, Context context) {
        File en_dir = context.getDir(SessionManager.BOARD_ICON_LOCATION, Context.MODE_PRIVATE);
        String path = en_dir.getAbsolutePath();
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
