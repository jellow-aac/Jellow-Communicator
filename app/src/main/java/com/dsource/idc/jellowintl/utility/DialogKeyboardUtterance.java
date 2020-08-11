package com.dsource.idc.jellowintl.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.dsource.idc.jellowintl.R;
import com.dsource.idc.jellowintl.activities.LevelThreeActivity;
import com.dsource.idc.jellowintl.activities.LevelTwoActivity;
import com.dsource.idc.jellowintl.activities.MainActivity;
import com.dsource.idc.jellowintl.activities.SequenceActivity;
import com.dsource.idc.jellowintl.activities.SpeechEngineBaseActivity;
import com.dsource.idc.jellowintl.make_my_board_module.activity.HomeActivity;
import com.dsource.idc.jellowintl.utility.interfaces.TextToSpeechCallBacks;

import static com.dsource.idc.jellowintl.utility.Analytics.bundleEvent;

public class DialogKeyboardUtterance{

    public void show(Context context) {
        final SpeechEngineBaseActivity activity = (SpeechEngineBaseActivity) context;
        final Drawable speaker=activity.getResources().getDrawable(R.drawable.ic_search_list_speaker);
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View dialogView = LayoutInflater.from(activity).inflate(R.layout.keyboard_layout, null);

        final ImageView mBtnPlay=dialogView.findViewById(R.id.speak_button);
        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity.isEngineSpeaking()){
                    activity.stopSpeaking();
                    mBtnPlay.setImageDrawable(speaker);
                    return;
                }

                String speechText = ((EditText) dialogView.findViewById(R.id.et_keyboard_utterances))
                        .getText().toString();
                //Firebase event
                Bundle bundle = new Bundle();
                bundle.putString("InputName", Settings.Secure.getString(activity.getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD));
                bundle.putString("utterence", speechText);
                if (!speechText.isEmpty())
                    bundleEvent("Keyboard", bundle);
                activity.speak(speechText.toLowerCase().concat("_"));
                mBtnPlay.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_stop));
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
                            setContentDescription(activity.getString(R.string.back));
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
                activity.stopSpeaking();
                if (activity instanceof MainActivity){
                    ((MainActivity)activity).hideCustomKeyboardDialog();
                }else if (activity instanceof LevelTwoActivity){
                    ((LevelTwoActivity)activity).hideCustomKeyboardDialog();
                }else if (activity instanceof LevelThreeActivity){
                    ((LevelThreeActivity)activity).hideCustomKeyboardDialog();
                }else if (activity instanceof SequenceActivity){
                    ((SequenceActivity)activity).hideCustomKeyboardDialog();
                }else{
                    ((HomeActivity)activity).hideCustomKeyboardDialog();
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
        InputMethodManager imm = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(dialogView.findViewById(R.id.et_keyboard_utterances),
                InputMethodManager.SHOW_IMPLICIT);
        TextToSpeechCallBacks callBacks = new TextToSpeechCallBacks() {
            @Override
            public void sendSpeechEngineLanguageNotSetCorrectlyError() {}

            @Override
            public void speechEngineNotFoundError() {}

            @Override
            public void speechSynthesisCompleted() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnPlay.setImageDrawable(speaker);
                        mBtnPlay.refreshDrawableState();
                    }
                });
            }
        };
        activity.registerSpeechEngineErrorHandle(callBacks);
    }
}