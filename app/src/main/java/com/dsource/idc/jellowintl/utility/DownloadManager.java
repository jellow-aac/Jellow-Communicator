package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.factories.PathFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import ir.mahdi.mzip.zip.ZipArchive;

/**
 * Created by ravipoovaiah on 14/12/17.
 */

public class DownloadManager {

    FileDownloadListener fileDownloadListener;
    int id;
    String localeCode;
    Context context;
    ProgressReceiver progressReceiver;


    public DownloadManager(String localeCode, Context context, ProgressReceiver progressReceiver) {
        this.localeCode = localeCode;
        this.context = context;
        this.progressReceiver = progressReceiver;

    }

    public void setLanguage(String localeCode){
        this.localeCode = localeCode;
    }

    // any class using DownloadManager should implement this ProgressReceiver for getting callbacks
    public interface ProgressReceiver {
        void onprogress(int soFarBytes, int totalBytes);

        void onComplete();
    }

    public void start()
    {
        FirebaseStorage storage =  FirebaseStorage.getInstance(); // get an instance of storage


        StorageReference storageRef = storage.getReference(); // get a reference to a particular location


        StorageReference pathReference = storageRef.child(BuildConfig.DB_TYPE +"/"+localeCode+".zip"); // select a particular file from that reference location

        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

                if(uri != null)
                startDownload(uri);
                // Got the download URL for 'locale.zip'


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(context,exception.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void startDownload(Uri url)
    {
        // get a reference to internal directory
        File en_dir = context.getDir(PathFactory.UNIVERSAL_FOLDER, Context.MODE_PRIVATE);
        // setup file downloader
        FileDownloader.setup(context);
        // add listener for callbacks
        fileDownloadListener = new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void started(BaseDownloadTask task) {
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                progressReceiver.onprogress(soFarBytes,totalBytes);

            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
            }

            @Override
            protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
            }

            @Override
            protected void completed(BaseDownloadTask task) {

                progressReceiver.onprogress(1,1);
                extractZip();
                progressReceiver.onComplete();
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                e.printStackTrace();
            }

            @Override
            protected void warn(BaseDownloadTask task) {

            }
        };


        // create a download task
        id = FileDownloader.getImpl().create(url.toString())
                .setPath(en_dir.getPath()+"/"+localeCode+".zip")
                .setForceReDownload(true)
                .setListener(fileDownloadListener).start();

    }

    private void extractZip() {
        File en_dir = context.getDir(PathFactory.UNIVERSAL_FOLDER, Context.MODE_PRIVATE);
        ZipArchive.unzip(en_dir.getPath()+"/"+localeCode+".zip",en_dir.getPath(),"");
        File zip = new File(en_dir.getPath(),localeCode+".zip");
        if(zip.exists()) zip.delete();
    }


    public void pause()
    {
        FileDownloader.getImpl().pause(id);
    }

    public void resume()
    {
        if(id != 0)
        {
            // if file is not downloaded then start the download
            if(FileDownloader.getImpl().getSoFar(id) < FileDownloader.getImpl().getTotal(id))
            {
                start();
            }
        }
    }
}