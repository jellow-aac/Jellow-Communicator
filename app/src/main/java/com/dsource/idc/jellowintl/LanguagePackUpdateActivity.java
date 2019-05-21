package com.dsource.idc.jellowintl;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dsource.idc.jellowintl.eventbus.MapDownloadResult;
import com.dsource.idc.jellowintl.eventbus.UpdateTaskResult;
import com.dsource.idc.jellowintl.factories.LanguageFactory;
import com.dsource.idc.jellowintl.eventbus.DownloadProgressEvent;
import com.dsource.idc.jellowintl.utility.FileUtils;
import com.dsource.idc.jellowintl.eventbus.FinalDownloadResult;
import com.dsource.idc.jellowintl.eventbus.HashMapDownloadResult;
import com.dsource.idc.jellowintl.eventbus.IconDownloadTaskResult;
import com.dsource.idc.jellowintl.eventbus.RetryFailedDownloadsEvent;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static com.dsource.idc.jellowintl.utility.FileUtils.cleanUpdateFiles;
import static com.dsource.idc.jellowintl.utility.FileUtils.doesExist;
import static com.dsource.idc.jellowintl.utility.FileUtils.getBaseDir;
import static com.dsource.idc.jellowintl.utility.FileUtils.getFile;
import static com.dsource.idc.jellowintl.utility.FileUtils.getUpdateDir;
import static com.dsource.idc.jellowintl.utility.FileUtils.getUpdateFile;
import static com.dsource.idc.jellowintl.utility.FileUtils.writeToFile;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class LanguagePackUpdateActivity extends BaseActivity {

    private static final String TAG = "batchDown";
    private static final String BASE_STORAGE_LOCATION = BuildConfig.DB_TYPE;
    private static final String HMAP_JSON_FIREBASE = "hmap.json";
    private static final String MAP_JSON_FIREBASE = "map.json";
    private static final String HMAP_NEW_FILE_NAME = "hmapN.json";
    private static final String HMAP_OLD_FILE_NAME = "hmap.json";
    private static final String HMAP_UPDATE_FILE_NAME = "hmapU.json";
    private static final String DRAWABLES_FOLDER_FIREBASE = "drawables";
    private static final int FAILED_DOWNLOADS_RETRY_LIMIT = 3;
    private static final int MINIBATCH_SIZE = 25;
    private static final Boolean MINIBATCH_ENABLED = FALSE;
    public static final String UPDATES_FOLDER_FIREBASE = "updates";
    private static ArrayList<String> failedDownloads;
    //private static ArrayList<String> successfulDownloads;
    private static Queue<String> downloadQueue;
    private static HashMap<String,String> hashMapN;
    private static HashMap<String,String> hashMapO;

    TextView updateCounter;

    private int downloadSuccessCount = 0;
    private int downloadFailedCount = 0;
    private int currentMiniBatchSize = 0;
    private int miniBatchDownloadSuccessCount = 0;
    private int miniBatchDownloadFailedCount = 0;
    private int failedDownloadsRetryCount = 0;
    private int startDownloadMethodCallCount = 0;
    private int downloadTotalCount = 0;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_pack_update);


        //Var Init
        //successfulDownloads = new ArrayList<>();
        failedDownloads = new ArrayList<>();
        downloadQueue = new LinkedList<>();
        hashMapN = new HashMap<>();
        hashMapO = new HashMap<>();

        //UI Init
        updateCounter = findViewById(R.id.update);

        //DB Init
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference baseStorageRef = getBaseUpdateLocRef(getApplicationContext());
        StorageReference hmapRef = baseStorageRef.child(HMAP_JSON_FIREBASE);


        File downloadDir = getUpdateDir(getApplicationContext());
        File hmapNFile = new File(downloadDir.getAbsolutePath(), HMAP_NEW_FILE_NAME);

        Log.d(TAG,hmapRef.getPath());


        hmapRef.getFile(hmapNFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                EventBus.getDefault().post(new HashMapDownloadResult(true));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                exception.printStackTrace();
                EventBus.getDefault().post(new HashMapDownloadResult(false));
            }
        });



    }


    private HashMap<String,String> getHashMap(File hmapJSON){

        FileReader hmapJSONReader = null;
        HashMap<String,String> hashMap = null;

        try {
            hmapJSONReader = new FileReader(hmapJSON);
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }

        Type typeOfHashMap = new TypeToken<HashMap<String,String>>(){}.getType();
        Gson gson = new Gson();
        if(hmapJSONReader != null){
            hashMap = gson.fromJson(hmapJSONReader,typeOfHashMap);
        }

        return hashMap;

    }

    private void startIconDownloads(File downloadDirectory, int miniBatchSize){
        startDownloadMethodCallCount++;
        Log.d(TAG,"Starting Download Round:"+ startDownloadMethodCallCount);

        resetMiniBatchCounters();

        while (downloadQueue.peek() != null && miniBatchSize != 0){
            currentMiniBatchSize++;
            String iconName = downloadQueue.poll();
            StorageReference iconRef = generateIconRef(iconName,getApplicationContext());
            File iconFile = new File(downloadDirectory.getAbsolutePath(),iconName);
            downloadIconFile(iconRef,iconFile);
            miniBatchSize--;
        }
    }

    private void startIconDownloads(File downloadDirectory){

        Log.d(TAG,"Starting Download");

        while (downloadQueue.peek() != null){
            String iconName = downloadQueue.poll();
            StorageReference iconRef = generateIconRef(iconName,getApplicationContext());
            File iconFile = new File(downloadDirectory.getAbsolutePath(),iconName);
            downloadIconFile(iconRef,iconFile);
        }
    }

    private Queue<String> generateDownloadQueue(HashMap<String,String> hashMapN,HashMap<String,String> hashMapO){

        Queue<String> downloadQueue = new LinkedList<>();
        Set<String> iconNamesN = hashMapN.keySet();
        for(String iconName:iconNamesN){
            if(iconName != null){
                if(hashMapO.containsKey(iconName)){
                    if(!hashMapN.get(iconName).equals(hashMapO.get(iconName))){
                        downloadQueue.add(iconName);
                    }
                } else {
                    downloadQueue.add(iconName);
                }
            }
        }
        Log.d(TAG,"Generated download queue size: "+ downloadQueue.size());
        return downloadQueue;
    }

    private void resetMiniBatchCounters() {
        miniBatchDownloadSuccessCount = 0;
        miniBatchDownloadFailedCount = 0;
        currentMiniBatchSize = 0;
    }

    private static StorageReference generateIconRef(String iconName,Context context){
        StorageReference baseRef = getBaseUpdateLocRef(context);
        return baseRef.child(DRAWABLES_FOLDER_FIREBASE).child(iconName);
    }

    private void downloadIconFile(StorageReference iconRef,File iconFile){
        final String iconName = iconFile.getName();
        iconRef.getFile(iconFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local file has been created
                EventBus.getDefault().post(new IconDownloadTaskResult(true,iconName));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                EventBus.getDefault().post(new IconDownloadTaskResult(false,iconName));
            }
        });
    }

    private void miniBatchDownloadFinishedCheck() {
        if(MINIBATCH_ENABLED){
            if (miniBatchDownloadSuccessCount + miniBatchDownloadFailedCount == currentMiniBatchSize) {
                if(downloadQueue.peek() != null){
                    startIconDownloads(getUpdateDir(getApplicationContext()),MINIBATCH_SIZE);
                }else {
                    Log.d(TAG,"Download Queue Empty");
                }
            } else {
                Log.d(TAG,"miniBatch:"+ startDownloadMethodCallCount +",miniBatchDownloadSuccessCount:"+ miniBatchDownloadSuccessCount);
            }
        } else {
            Log.d(TAG,"error: miniBatch not enabled");
        }
    }

    private void iconFileDownloadsFinishedCheck() {
        if (downloadSuccessCount + downloadFailedCount == downloadTotalCount) {
            if (downloadSuccessCount == downloadTotalCount) {
                EventBus.getDefault().post(new FinalDownloadResult(
                        TRUE,downloadSuccessCount,downloadFailedCount,downloadTotalCount));
            } else {
                if(failedDownloadsRetryCount <= FAILED_DOWNLOADS_RETRY_LIMIT){
                    EventBus.getDefault().post(new RetryFailedDownloadsEvent(downloadFailedCount));
                }else {
                    EventBus.getDefault().post(new FinalDownloadResult(
                            FALSE,downloadSuccessCount,downloadFailedCount,downloadTotalCount));
                }
            }
        } else if(downloadSuccessCount + downloadFailedCount > downloadTotalCount){
            Log.d(TAG, "error: success count + failed count > total count");
        }
    }

    private void retryFailedDownloads() {
        downloadQueue.addAll(failedDownloads);
        failedDownloads.clear();
        downloadFailedCount = 0;
        startDownloadProcess();
        failedDownloadsRetryCount++;
    }

    private void startDownloadProcess() {

        File downloadDirectory = getUpdateDir(getApplicationContext());

        if (MINIBATCH_ENABLED)
            startIconDownloads(downloadDirectory, MINIBATCH_SIZE);
        else
            startIconDownloads(downloadDirectory);

    }

    private void updateTextView(){
        updateCounter.setText("Success: "+ downloadSuccessCount +"/"+ downloadTotalCount+"\n"+"Failed: "+ downloadFailedCount +"/"+ downloadTotalCount);
    }

    private void updateHMapFile() {

        File updateDir = getUpdateDir(getApplicationContext());
        File baseDir = getBaseDir(getApplicationContext());

        File hmapNFile = new File(updateDir.getAbsolutePath(), HMAP_NEW_FILE_NAME);
        File hmapOFile = new File(baseDir.getAbsolutePath(), HMAP_OLD_FILE_NAME);
        File hmapUFile = new File(baseDir.getAbsolutePath(),HMAP_UPDATE_FILE_NAME);


        HashMap<String,String> hashMapN = getHashMap(hmapNFile);
        HashMap<String,String> hashMapO = getHashMap(hmapOFile);

        generateUpdatedHashMap(hashMapN, hashMapO);

        Gson gson = new Gson();
        String hmapUJS = gson.toJson(hashMapO);

        boolean writeSuccess = writeToFile(hmapUFile,hmapUJS);

        if(writeSuccess){
            Log.d(TAG,"Updated HashMap file successfully created");
        } else {
            Log.d(TAG,"Error creating updated HashMap JSON file");
        }


        if(hmapUFile.exists()){
            boolean success = hmapUFile.renameTo(hmapOFile);
            if(success){
                Log.d(TAG,"Success: updated HashMap file -> old HashMap file");
            } else {
                Log.d(TAG,"Failed: updated HashMap file -> old HashMap file");
                FileUtils.deleteFile(hmapUFile);
            }
        }

        cleanUpdateFiles(getApplicationContext());

        EventBus.getDefault().post(new UpdateTaskResult(true));
    }

    private void downloadMapJSON(){

        StorageReference baseStorageRef = getBaseUpdateLocRef(getApplicationContext());
        StorageReference mapRef = baseStorageRef.child(MAP_JSON_FIREBASE);


        File downloadDir = getUpdateDir(getApplicationContext());
        File mapFile = new File(downloadDir.getAbsolutePath(), MAP_JSON_FIREBASE);


        mapRef.getFile(mapFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                EventBus.getDefault().post(new MapDownloadResult(true));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                EventBus.getDefault().post(new MapDownloadResult(false));
            }
        });

    }

    private void updateMapFile(){
        File downloadDir = getUpdateDir(getApplicationContext());
        File mapFile = new File(downloadDir.getAbsolutePath(), MAP_JSON_FIREBASE);
        File baseDir = getBaseDir(getApplicationContext());

        File mapFileO = new File(baseDir.getAbsolutePath(), MAP_JSON_FIREBASE);

        if(mapFileO.exists()){
            boolean success = mapFile.renameTo(mapFileO);
            if(success){
                Log.d(TAG,"Success: updated Map file -> old Map file");
            } else {
                Log.d(TAG,"Failed: updated Map file -> old Map file");
                FileUtils.deleteFile(mapFile);
            }
        }
    }


    private void generateUpdatedHashMap(HashMap<String, String> hashMapN, HashMap<String, String> hashMapO) {
        Set<String> iconNamesN = hashMapN.keySet();
        for(String iconName:iconNamesN){
            if(hashMapO.containsKey(iconName)){
                if(!hashMapN.get(iconName).equals(hashMapO.get(iconName))){
                    if(!failedDownloads.contains(iconName)){
                        hashMapO.put(iconName,hashMapN.get(iconName));
                    }
                }
            } else {
                if(!failedDownloads.contains(iconName)){
                    hashMapO.put(iconName,hashMapN.get(iconName));
                }
            }
        }
    }


    private static StorageReference getBaseUpdateLocRef(Context context){
        StorageReference base = FirebaseStorage.getInstance().getReference(BASE_STORAGE_LOCATION);
        String currentLanguage = LanguageFactory.getCurrentLocaleCode(context);
        return base.child(UPDATES_FOLDER_FIREBASE).child(currentLanguage);
    }

    @Subscribe
    public void onHashMapDownloadTaskResult(HashMapDownloadResult result){

        if(result.isSuccess()){

            Log.d(TAG,"New HashMap JSON file successfully downloaded ");

            File hmapNFile = getUpdateFile(getApplicationContext(),HMAP_NEW_FILE_NAME);
            File hmapOFile = getFile(getApplicationContext(),HMAP_OLD_FILE_NAME);

            if(!doesExist(hmapNFile)){
                Log.d(TAG,"Error: New HashMap JSON file not found");
                return;
            }

            if(!doesExist(hmapOFile)){
                Log.d(TAG,"Error: Old HashMap JSON file not found");
                return;
            }

            hashMapN = getHashMap(hmapNFile);

            hashMapO = getHashMap(hmapOFile);

            downloadQueue = generateDownloadQueue(hashMapN,hashMapO);

            downloadTotalCount = downloadQueue.size();

            if(downloadTotalCount == 0){
                Log.d(TAG,"No new/updated icons found");
                EventBus.getDefault().post(new DownloadProgressEvent(
                        downloadTotalCount,downloadSuccessCount,downloadFailedCount));
                cleanUpdateFiles(getApplicationContext());
            } else {
                startDownloadProcess();
            }

        } else {
            Log.d(TAG,"Error: HashMap JSON file download failed");
        }

    }

    @Subscribe
    public void onMapDownloadTaskResult(MapDownloadResult result){
        if(result.isSuccess()){
            Log.d(TAG,"Success: Updated " + MAP_JSON_FIREBASE + " downloaded");

            File downloadDir = getUpdateDir(getApplicationContext());
            File mapFile = new File(downloadDir.getAbsolutePath(), MAP_JSON_FIREBASE);

            if(!doesExist(mapFile))
                return;

            updateMapFile();
            updateHMapFile();

        } else {
            Log.d(TAG,"Error: Updated " + MAP_JSON_FIREBASE + " download failed");
        }
    }

    @Subscribe
    public void onIconDownloadTaskResult(IconDownloadTaskResult result){

        if(result.getSuccess()){
            /*if(successfulDownloads != null)
                successfulDownloads.add(result.iconName);*/
            downloadSuccessCount++;
            if(MINIBATCH_ENABLED){
                miniBatchDownloadSuccessCount++;
            }

        }else {
            if(failedDownloads != null){
                failedDownloads.add(result.getIconName());
            }
            downloadFailedCount++;
            if(MINIBATCH_ENABLED){
                miniBatchDownloadFailedCount++;
            }
            Log.d(TAG,"Error: " + result.getIconName() + "icon download failed");
        }

        EventBus.getDefault().post(new DownloadProgressEvent(downloadTotalCount,
                downloadSuccessCount,downloadFailedCount));

        if(MINIBATCH_ENABLED){
            miniBatchDownloadFinishedCheck();
        }

        iconFileDownloadsFinishedCheck();
    }

    @Subscribe
    public void onDownloadProgress(DownloadProgressEvent progressEvent){
        updateTextView();
    }

    @Subscribe
    public void onRetryDownloads(RetryFailedDownloadsEvent event){
        Log.d(TAG,"Retrying failed downloads: " + event.getRetryDownloadCount());
        retryFailedDownloads();
    }

    @Subscribe
    public void onDownloadProcessComplete(FinalDownloadResult result){
        if(result.getSuccess()){
            Log.d(TAG,"All icon files downloaded");

        } else {
            Log.d(TAG,"Failed downloads retry limit exceeded: " + result.getFailedDownloads());
        }

        downloadMapJSON();

    }

    @Subscribe
    public void onUpdateTaskResult(UpdateTaskResult result){
        if(result.isSuccess()){
            Log.d(TAG,"Success: Update process complete");
            // If update is success, update icon search data
            getSession().setLanguageChange(0);
        }else {
            Log.d(TAG,"Error: Update process failed");
        }
    }
}

