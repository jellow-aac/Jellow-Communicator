package com.dsource.idc.jellowintl.makemyboard.JsonDatabase;

import android.content.Context;
import android.util.Log;

import com.dsource.idc.jellowintl.makemyboard.Board;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class BoardJsonHelper {

    final String FileName="Board_records.json";
    public void saveBoard(Board newBoard, Context context)
    {


    }
    public void readBoard(String BoardID)
    {


    }

    /**
     * This function is to write string data to the file
     * @param data
     * @param context
     */
    private void writeJsonStringToFile(String data, Context context) {
        File path = context.getExternalFilesDir(null);
        File file = new File(path, FileName);
        Log.d("Path",path.getAbsolutePath());
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            stream.write(data.getBytes());
            Log.d("Verbiage:","Written Successfully");

        }
        catch (IOException e)
        {

        }
        finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("Jellow.json", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
