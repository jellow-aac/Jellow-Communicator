package com.dsource.idc.jellowintl.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.LevelThreeActivity;
import com.dsource.idc.jellowintl.activities.LevelTwoActivity;
import com.dsource.idc.jellowintl.activities.MainActivity;
import com.dsource.idc.jellowintl.activities.SequenceActivity;
import com.dsource.idc.jellowintl.activities.SpeechEngineBaseActivity;

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
                String speechText = ((EditText) dialogView.findViewById(R.id.et_keyboard_utterances))
                        .getText().toString();
                //Firebase event
                Bundle bundle = new Bundle();
                bundle.putString("InputName", Settings.Secure.getString(mContext.getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD));
                bundle.putString("utterence", speechText);
                if (!speechText.isEmpty())
                    bundleEvent("Keyboard", bundle);
                ((SpeechEngineBaseActivity)mContext).speak(speechText.toLowerCase().concat("_"));
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
        dialogView.findViewById(R.id.dialog_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                dialogView.findViewById(R.id.dialogTitle).setVisibility(View.GONE);
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                ((SpeechEngineBaseActivity)mContext).stopSpeaking();
                if (mContext instanceof MainActivity){
                    ((MainActivity)mContext).hideCustomKeyboardDialog();
                }else if (mContext instanceof LevelTwoActivity){
                    ((LevelTwoActivity)mContext).hideCustomKeyboardDialog();
                }else if (mContext instanceof LevelThreeActivity){
                    ((LevelThreeActivity)mContext).hideCustomKeyboardDialog();
                }else{
                    ((SequenceActivity)mContext).hideCustomKeyboardDialog();
                }
            }
        });
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.findViewById(R.id.dialog_back).sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        InputMethodManager imm = (InputMethodManager) mContext.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(dialogView.findViewById(R.id.et_keyboard_utterances),
                InputMethodManager.SHOW_IMPLICIT);
    }
}