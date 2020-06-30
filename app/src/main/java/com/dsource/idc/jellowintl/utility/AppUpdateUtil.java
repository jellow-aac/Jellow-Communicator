package com.dsource.idc.jellowintl.utility;

import android.content.DialogInterface;
import android.content.IntentSender;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.SplashActivity;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;

public class AppUpdateUtil{
    public final static int UPDATE_REQUEST_CODE = 99;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo;

    public enum UpdateStatus {
        INIT, SHOW_RATIONALE, START, RUNNING, CANCELED, FAILED, NOT_AVAILABLE
    }

    public void executeUpdateFlow(UpdateStatus status, SplashActivity context){
            Log.i("JellowApp","Created appUpdateManager");
        switch(status){
            case INIT:
                checkIfNewVersionAvailable(context);
                break;
            case SHOW_RATIONALE:
                showUpdateRationaleToUser(context);
                break;
            case START:
                startUpdateProcess(context);
                break;
            case RUNNING:
                callUpdateUIToForegroundIfRunning(context);
                break;
            case CANCELED:
            case FAILED:
            case NOT_AVAILABLE:
                context.continueLoadingTheApp();
                break;
        }
    }

    public void checkIfNewVersionAvailable(final SplashActivity context) {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                AppUpdateUtil.this.appUpdateInfo = appUpdateInfo;
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)) {
                    executeUpdateFlow(UpdateStatus.SHOW_RATIONALE, context);
                }else{
                    executeUpdateFlow(UpdateStatus.FAILED, context);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                executeUpdateFlow(UpdateStatus.FAILED, context);
            }
        });
    }

    private void showUpdateRationaleToUser(final SplashActivity context) {
        String updateNow = context.getString(R.string.update_now);
        String updateLater = context.getString(R.string.update_later);
        String message = context.getString(R.string.app_update_message);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Add the buttons
        builder
            .setPositiveButton(updateNow, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    executeUpdateFlow(UpdateStatus.START, context);
                    dialog.dismiss();
                }
            })
            .setNegativeButton(updateLater, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    executeUpdateFlow(UpdateStatus.CANCELED, context);
                    dialog.dismiss();
                }
            })
            // Set other dialog properties
            .setCancelable(true)
            .setMessage(message);

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        // Show the AlertDialog
        dialog.show();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                executeUpdateFlow(UpdateStatus.CANCELED, context);
                dialog.dismiss();
            }
        });
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        positiveButton.setTextSize(18f);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(context.getResources().getColor(R.color.colorAccent));
        negativeButton.setTextSize(18f);
    }

    public void startUpdateProcess(SplashActivity context){
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo, IMMEDIATE,
                    context, UPDATE_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            executeUpdateFlow(UpdateStatus.FAILED, context);
            e.printStackTrace();
        }catch (NullPointerException e){
            executeUpdateFlow(UpdateStatus.FAILED, context);
            e.printStackTrace();
        }
    }

    public void callUpdateUIToForegroundIfRunning(final SplashActivity context) {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        appUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                @Override
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability()
                            == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                        // If an in-app update is already running, resume the update.
                        try {
                            appUpdateManager.startUpdateFlowForResult(
                                    appUpdateInfo,
                                    IMMEDIATE,
                                    context,
                                    UPDATE_REQUEST_CODE);
                        } catch (IntentSender.SendIntentException e) {
                            executeUpdateFlow(UpdateStatus.FAILED, context);
                            e.printStackTrace();
                        }
                    }
                }
            });
    }
}