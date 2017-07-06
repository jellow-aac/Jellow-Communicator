package com.dsource.idc.jellow.Utility;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

/**
 * Created by ekalpa on 7/4/2017.
 */
public class JellowTTSService extends Service{
    private TextToSpeech mTts;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new BackgroundSpeechOperationsAsync(this).execute();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellow.SPEECH_TEXT");
        filter.addAction("com.dsource.idc.jellow.SPEECH_PITCH");
        filter.addAction("com.dsource.idc.jellow.SPEECH_SPEED");
        filter.addAction("com.dsource.idc.jellow.SPEECH_STOP");
        filter.addAction("com.dsource.idc.jellow.STOP_SERVICE");
        registerReceiver(receiver, filter);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        mTts.shutdown();
    }

    private void say(String speechText){
        mTts.speak(speechText, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void setSpeechRate(float rate){
        mTts.setSpeechRate(rate);
    }

    private void setPitch(float pitch){
        mTts.setPitch(pitch);
    }

    private void stopTtsSay(){
        mTts.stop();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellow.SPEECH_TEXT":
                    say(intent.getStringExtra("speechText")); break;
                case "com.dsource.idc.jellow.SPEECH_PITCH":
                    setPitch(intent.getFloatExtra("speechPitch", new SessionManager(context).getPitch())); break;
                case "com.dsource.idc.jellow.SPEECH_SPEED":
                    setSpeechRate(intent.getFloatExtra("speechSpeed", new SessionManager(context).getSpeed())); break;
                case "com.dsource.idc.jellow.SPEECH_STOP":
                    stopTtsSay(); break;
                case "com.dsource.idc.jellow.STOP_SERVICE":
                    stopSelf(); break;
            }
        }
    };

    private class BackgroundSpeechOperationsAsync extends AsyncTask<Void, Void, Void> {
        private Context mContext;
        private  SessionManager mSession;
        public BackgroundSpeechOperationsAsync(Context context) {
            mContext = context;
            mSession = new SessionManager(mContext);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mTts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    try {
                        mTts.setEngineByPackageName("com.google.android.tts");
                        mTts.setLanguage(new Locale("hin", "IND"));
                        mTts.setSpeechRate((float) mSession.getSpeed()/50);
                        mTts.setPitch((float) mSession.getPitch()/50);
                    } catch (Exception e) {
                        new UserDataMeasure(mContext).reportException(e);
                        new UserDataMeasure(mContext).reportLog("Failed to set language.", Log.ERROR);
                    }
                }
            });
            return null;
        }
    }
}
