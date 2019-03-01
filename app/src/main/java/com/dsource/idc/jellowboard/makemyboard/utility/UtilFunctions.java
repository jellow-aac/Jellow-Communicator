package com.dsource.idc.jellowboard.makemyboard.utility;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.dsource.idc.jellowboard.makemyboard.MyBoards;
import com.dsource.idc.jellowboard.makemyboard.utility.IconDatabase;
import com.dsource.idc.jellowboard.utility.JellowIcon;
import com.dsource.idc.jellowboard.utility.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * All the logic functions of the Make my board capability are declared here
 * I did this because it will help us to make the unit test for make my board easily
 * @Author Ayaz Alam
 */
public class UtilFunctions {

    /**
     * Checks a list is the sublist of the other symbol
     * @param selectedList
     * @param iconList
     * @return
     */
    public static boolean getSelection(ArrayList<JellowIcon> selectedList,ArrayList<JellowIcon> iconList)
    {
        boolean isOkay=true;
        //return  false if selected icon list is shorter than icon list
        if(selectedList.size()<iconList.size())
            return false;

        for(int i=0;i<iconList.size();i++)
        {
            isOkay=listContainsIcon(iconList.get(i),selectedList);
            if(!isOkay)//Return if there's any element that is not present in the list
                return false;
        }
        return isOkay;
    }

    /***
     * This function checks whether a icon is present in the list or not
     * @param icon
     * @param list
     * @return boolean
     */
    public static boolean listContainsIcon(JellowIcon icon, ArrayList<JellowIcon> list)
    {
        boolean present=false;
        for(int i=0;i<list.size();i++)
            if(list.get(i).isEqual(icon))
                present=true;
        Log.d("Selection: ","Present "+present);
        return present;
    }
    public static void storeImageToStorage(Bitmap bitmap, String fileID,Context context) {
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
