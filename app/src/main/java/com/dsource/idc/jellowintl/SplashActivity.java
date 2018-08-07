package com.dsource.idc.jellowintl;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.dsource.idc.jellowintl.utility.DefaultExceptionHandler;
import com.dsource.idc.jellowintl.utility.EvaluateDisplayMetricsUtils;
import com.dsource.idc.jellowintl.utility.JellowTTSService;
import com.dsource.idc.jellowintl.utility.LanguageHelper;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.util.Timer;
import java.util.TimerTask;

import static com.dsource.idc.jellowintl.MainActivity.isTTSServiceRunning;
import static com.dsource.idc.jellowintl.utility.Analytics.isAnalyticsActive;
import static com.dsource.idc.jellowintl.utility.Analytics.resetAnalytics;

/**
 * Created by ekalpa on 7/12/2016.
 */
public class SplashActivity extends AppCompatActivity {

    private String mYes, mNO, mExit, mErrDialogMsg, mExitDialogMsg;
    //Field to create IconDatabase
    CreateDataBase iconDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize default exception handler for this activity.
        // If any exception occurs during this activity usage,
        // handle it using default exception handler.
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        new DataBaseHelper(this).createDataBase();

        if(isTTSServiceRunning((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)))
            stopTTsService();
        startTTsService();
        PlayGifView pGif = findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.jellow_j);
        {
            SessionManager sManager = new SessionManager(this);
            if (sManager.isRequiredToPerformDbOperations()) {
                performDatabaseOperations();
                sManager.setCompletedDbOperations(true);
            }
            if((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) &&
                (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED))
                sManager.setEnableCalling(false);
            sManager = null;
        }
        EvaluateDisplayMetricsUtils displayMetricsUtils = new EvaluateDisplayMetricsUtils(this);
        displayMetricsUtils.calculateStoreDeviceHeightWidth();
        displayMetricsUtils.calculateStoreShadowRadiusAndBorderWidth();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.INIT_SERVICE");
        filter.addAction("com.dsource.idc.jellowintl.INIT_SERVICE_ERR");
        registerReceiver(receiver, filter);
        iconDatabase=new CreateDataBase(this);
        iconDatabase.execute();
        mYes = getString(R.string.yes);
        mNO = getString(R.string.no);
        mExit = getString(R.string.exit);
        mErrDialogMsg = getString(R.string.err_dialog_msg);
        mExitDialogMsg = getString(R.string.exit_dialog_msg);
     }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext((LanguageHelper.onAttach(newBase)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!isAnalyticsActive()) {
            resetAnalytics(this, new SessionManager(this).getCaregiverNumber().substring(1));
        }
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.INIT_SERVICE":
                    checkIfDatabaseCreated();
                    break;
                case "com.dsource.idc.jellowintl.INIT_SERVICE_ERR":
                    showErrorDialog();
                    break;
            }
        }
    };

    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder
            .setPositiveButton(mYes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                    dialog.dismiss();
                    finish();
                }
            })
            .setNegativeButton(mNO, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    showExitDialog();
                }
            })
            // Set other dialog properties
            .setCancelable(false)
            .setMessage(mErrDialogMsg);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        // Show the AlertDialog
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.colorAccent));
        positiveButton.setTextSize(18f);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.colorAccent));
        negativeButton.setTextSize(18f);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder
            .setPositiveButton(mNO, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                    dialog.dismiss();
                    finish();
                }
            })
            .setNegativeButton(mExit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.dismiss();
                    finish();
                }
            })
            // Set other dialog properties
            .setCancelable(false)
            .setMessage(mExitDialogMsg);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        // Show the AlertDialog
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(getResources().getColor(R.color.colorAccent));
        positiveButton.setTextSize(18f);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(getResources().getColor(R.color.colorAccent));
        negativeButton.setTextSize(18f);
    }

    private void checkIfDatabaseCreated()
    {
        if(!(new SessionManager(this).isLanguageChanged()==2))
            startApp();//if changes are their in the app then check whether the data base is created or not
        else
            startJellow();//If no change in language then simply start the app.
    }

    private void startApp() {
        final Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(iconDatabase.getStatus()== AsyncTask.Status.FINISHED)
                {
                    startJellow();
                    timer.cancel();
                }
            }
        },0,100);
    }

    private void startJellow() {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        try{
            unregisterReceiver(receiver);
        } catch(IllegalArgumentException | NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void performDatabaseOperations() {
        DataBaseHelper helper = new DataBaseHelper(this);
        helper.openDataBase();
        helper.addLanguageDataToDatabase();
    }

    private void startTTsService() {
        startService(new Intent(getApplication(), JellowTTSService.class));
    }

    private void stopTTsService() {
        Intent intent = new Intent("com.dsource.idc.jellowintl.STOP_SERVICE");
        sendBroadcast(intent);
    }
}