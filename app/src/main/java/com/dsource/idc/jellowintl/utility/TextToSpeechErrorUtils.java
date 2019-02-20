package com.dsource.idc.jellowintl.utility;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;

import com.dsource.idc.jellowintl.R;

import androidx.appcompat.app.AlertDialog;

public class TextToSpeechErrorUtils {
    private Activity mActivity;
    private String mYes, mNO, mExit, mErrDialogMsg, mExitDialogMsg;

    public TextToSpeechErrorUtils(Activity activity) {
        mActivity = activity;
        mYes = mActivity.getString(R.string.yes);
        mNO = mActivity.getString(R.string.no);
        mExit = mActivity.getString(R.string.exit);
        mErrDialogMsg = mActivity.getString(R.string.err_dialog_msg);
        mExitDialogMsg = mActivity.getString(R.string.exit_dialog_msg);
    }

    public void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        // Add the buttons
        builder
                .setPositiveButton(mYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                        dialog.dismiss();
                        mActivity.finish();
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
        positiveButton.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        positiveButton.setTextSize(18f);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        negativeButton.setTextSize(18f);
    }

    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        // Add the buttons
        builder
                .setPositiveButton(mNO, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.tts")));
                        dialog.dismiss();
                        mActivity.finish();
                    }
                })
                .setNegativeButton(mExit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.dismiss();
                        mActivity.finish();
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
        positiveButton.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        positiveButton.setTextSize(18f);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setTextColor(mActivity.getResources().getColor(R.color.colorAccent));
        negativeButton.setTextSize(18f);
    }
}
