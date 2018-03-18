package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by ekalpa on 2/5/2018.
 */

public class ToastWithCustomTime {
    public ToastWithCustomTime(Context context, String message, int time){
        showToast(context, message, time);
    }

    void showToast(Context context, String message, int time){
        final Toast mToastToShow = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                mToastToShow.show();
            }
            public void onFinish() {
                mToastToShow.cancel();
            }
        }.start();
    }
}
