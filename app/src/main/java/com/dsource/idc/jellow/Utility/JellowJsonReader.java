package com.dsource.idc.jellow.Utility;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ekalpa on 12/15/2017.
 */

public class JellowJsonReader {
    private Context mContext;
    private SessionManager mSession;

    public JellowJsonReader(Context context){
        mContext = context;
        mSession = new SessionManager(mContext);
    }

    public String readFile(int level){
        File file = null;
        FileInputStream fis = null;
        String fileName = "";
        switch(level){
            case 2:
                fileName = mContext.getApplicationInfo().dataDir
                        +"/app_en-rIN/levelTwoDrawables.json";
                break;
            case 3:
                fileName = mContext.getApplicationInfo().dataDir
                        +"/app_en-rIN/levelThreeDrawables.json";
                break;
        }

        try {
            file = new File(fileName);
            fis = new FileInputStream(file);
            BufferedReader r = new BufferedReader(new InputStreamReader(fis));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            return total.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
