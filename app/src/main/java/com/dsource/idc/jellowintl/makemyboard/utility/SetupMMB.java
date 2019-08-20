package com.dsource.idc.jellowintl.makemyboard.utility;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.dsource.idc.jellowintl.BuildConfig;
import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.makemyboard.MyBoardsActivity;
import com.dsource.idc.jellowintl.makemyboard.verbiage_model.VerbiageDatabaseHelper;
import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.SessionManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;

import ir.mahdi.mzip.zip.ZipArchive;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;

public class SetupMMB extends AppCompatActivity {
    DownloadMan manager;
    RoundCornerProgressBar progressBar;
    private SessionManager mSession;
    String langCode = "en-rIN";
    private String mCheckConn;
    Boolean isConnected;
    TextView progressText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_download);
        if(getSupportActionBar()!=null) getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        mSession = new SessionManager(this);
        progressBar = findViewById(R.id.pg);
        progressText = findViewById(R.id.progress_text);
        /*if(mSession.isDownloaded(SessionManager.ENG_IN)&&!mSession.isBoardDatabaseCreated())
        {
            progressText.setText("Setting things up, Please wait...");
            progressBar.setProgress(0);
            progressBar.setMax(0);
            createDatabase();
            return;
        }*/
        if(mSession.isBoardDatabaseCreated())
        {
            startActivity(new Intent(this, MyBoardsActivity.class));
            finish();
        }
        else {
            progressText.setText("Please wait while the icons are being downloaded. Do ensure there is an active internet connection at the time of download");
            progressBar.setMax(1);

            mCheckConn = getString(R.string.checkConnectivity);


            // Download the language if it's not already present
            /*if (!mSession.isDownloaded(SessionManager.ENG_IN)) {
                DownloadMan.ProgressReciever progressReciever = new DownloadMan.ProgressReciever() {
                    @Override
                    public void onprogress(int soFarBytes, int totalBytes) {
                        progressBar.setProgress((float) soFarBytes / totalBytes);
                    }

                    @Override
                    public void onComplete() {
                        //mSession.setDownloaded(langCode);
                        progressText.setText("Setting things up, Please wait...");
                        progressBar.setProgress(0);
                        progressBar.setMax(0);
                        createDatabase();
                    }
                };


                if (langCode != null) {
                    try {
                        isConnected = isConnected();
                        if (isConnected) {
                            manager = new DownloadMan(langCode, this, progressReciever);
                            manager.start();
                        } else {

                            Toast.makeText(this, mCheckConn, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            else { // if language is already installed then skip downloading the image
                createDatabase();
                progressText.setText("Setting things up, Please wait...");
                progressBar.setProgress(100);
                progressBar.setMax(100);
            }*/
        }

    }

    private void createDatabase() {
        VerbiageDatabaseHelper databaseHelper = new VerbiageDatabaseHelper(this);
        databaseHelper.setOnProgressChangeListener(new VerbiageDatabaseHelper.ProgressListener() {
            @Override
            public void onProgressChanged(int progress, int max) {
                if(progressBar.getMax()==0)
                progressBar.setMax(max);
                Log.d("Max",max+"");
                progressBar.setProgress(progress);
            }

            @Override
            public void onComplete() {
                progressBar.setProgress(0);
                progressBar.setVisibility(View.GONE);
                progressText.setText("Finalizing setup, please wait...");
                createIconDatabase();
            }
        });
       new downloadVerbiage(this,databaseHelper).execute();

    }

    private void createIconDatabase() {
        IconDatabase iconDatabase = new IconDatabase(this,null);
        iconDatabase.setOnProgressChangeListener(new IconDatabase.ProgressListener() {
            @Override
            public void onProgressChanged(int progress, int max) {

            }

            @Override
            public void onComplete() {
                startActivity(new Intent(SetupMMB.this, MyBoardsActivity.class));
                mSession.setBoardDatabaseStatus(1);
                finish();
            }
        });
        new jellowDatabase(this,iconDatabase).execute();
    }

    private boolean isConnected()
    {
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


}

class DownloadMan {

   private FileDownloadListener fileDownloadListener;
   int id;
   private String localeCode;
   Context context;
   private ProgressReciever progressReciever;
   FirebaseAuth mAuth;

   public DownloadMan(String localeCode, Context context,ProgressReciever progressReciever) {
       this.localeCode = localeCode;
       this.context = context;
       this.progressReciever = progressReciever;
   }

   // any class using DownloadManager should implement this ProgressReceiver for getting callbacks
   public interface ProgressReciever{
       void onprogress(int soFarBytes, int totalBytes);

       void onComplete();
   }

   public void start()
   {
       FirebaseStorage storage =  FirebaseStorage.getInstance(); // get an instance of storage


       StorageReference storageRef = storage.getReference(); // get a reference to a particular location

        //TODO Change the storage reference before launch
       StorageReference pathReference = storageRef.child(BuildConfig.DB_TYPE+"/en-rIN"+".zip"); // select a particular file from that reference location

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
       File en_dir = context.getDir(localeCode, Context.MODE_PRIVATE);

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

               progressReciever.onprogress(soFarBytes,totalBytes);

           }

           @Override
           protected void blockComplete(BaseDownloadTask task) {
           }

           @Override
           protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
           }

           @Override
           protected void completed(BaseDownloadTask task) {

               progressReciever.onprogress(1,1);
               extractZip();
               progressReciever.onComplete();
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
       File en_dir = context.getDir(localeCode, Context.MODE_PRIVATE);
       ZipArchive.unzip(en_dir.getPath()+"/"+localeCode+".zip",en_dir.getPath(),"");
       File zip = new File(en_dir.getPath(),localeCode+".zip");
       if(zip.exists()) zip.delete();

       //registerEvent();


   }

   // for Analytics Purpose
   private void registerEvent() {

       Bundle bundle = new Bundle();
       bundle.putString("Downloaded Language",localeCode);
       bundleEvent("Language",bundle);
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

class jellowDatabase extends AsyncTask
{

    Context context;
    IconDatabase database;
    public jellowDatabase(Context context,IconDatabase database) {
        this.context = context;
        this.database = database;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        database.createTable();
        return null;
    }
}

class downloadVerbiage extends AsyncTask
{

    Context context;
    VerbiageDatabaseHelper database;
    public downloadVerbiage(Context context,VerbiageDatabaseHelper database) {
        this.context = context;
        this.database = database;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    @Override
    protected void onProgressUpdate(Object[] values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onCancelled(Object o) {
        super.onCancelled(o);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        database.createTable();
        return null;
    }
}


