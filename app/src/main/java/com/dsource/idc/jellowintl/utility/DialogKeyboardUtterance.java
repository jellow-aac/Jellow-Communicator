package com.dsource.idc.jellowintl.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.EditText;

import com.dsource.idc.jellowintl.LevelBaseActivity;
import com.dsource.idc.jellowintl.R;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;

public class DialogKeyboardUtterance {
    private Context mContext;
    public DialogKeyboardUtterance(Context context) {
        mContext = context;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final View dialogView = LayoutInflater.from(mContext).inflate(R.layout.keyboard_layout, null);

        dialogView.findViewById(R.id.speak_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String speechText = ((EditText) dialogView.findViewById(R.id.et_keyboard_utterances)).getText().toString();
                //Firebase event
                Bundle bundle = new Bundle();
                bundle.putString("InputName", Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD));
                bundle.putString("utterence", speechText);
                if (!speechText.isEmpty())
                    bundleEvent("Keyboard", bundle);
                ((LevelBaseActivity)mContext).speak(speechText.toLowerCase());

            }
        });
        dialogView.findViewById(R.id.dialog_back).setAccessibilityDelegate( new View.AccessibilityDelegate(){
            @Override
            public void onPopulateAccessibilityEvent(View host, AccessibilityEvent event) {
                super.onPopulateAccessibilityEvent(host, event);
                if(event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED){
                    dialogView.findViewById(R.id.et_keyboard_utterances).setFocusable(true);
                    dialogView.findViewById(R.id.et_keyboard_utterances).setFocusableInTouchMode(true);
                    dialogView.findViewById(R.id.dialogTitle).setVisibility(View.GONE);
                    dialogView.findViewById(R.id.dialog_back).
                            setContentDescription(mContext.getString(R.string.back));
                }
            }
        });
        builder.setView(dialogView);
        final AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setView(dialogView);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
        dialogView.findViewById(R.id.dialog_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.findViewById(R.id.dialog_back).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
    }
}