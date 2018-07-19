package com.dsource.idc.jellowintl.utility;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.HashMap;
import java.util.Locale;

import static com.dsource.idc.jellowintl.utility.SessionManager.BE_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;

/**
 * Created by ekalpa on 7/4/2017.
 */
public class JellowTTSService extends Service{
    private TextToSpeech mTts;
    HashMap<String, String> map;
    private static final String TAG = "JellowTTSService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        map = new HashMap<String, String>();
        String UTTERANCE_ID = "com.dsource.idc.jellowintl.utterence.id";
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
        new BackgroundSpeechOperationsAsync(this).execute();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_TEXT");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_STOP");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_PITCH");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SPEED");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_LANG");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ");
        filter.addAction("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_REQ");
        filter.addAction("com.dsource.idc.jellowintl.STOP_SERVICE");
        registerReceiver(receiver, filter);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            mTts.shutdown();
            unregisterReceiver(receiver);
            startService(new Intent(getApplication(), JellowTTSService.class));
        } catch(IllegalArgumentException | NullPointerException | IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void say(String speechText){
        mTts.speak(speechText, TextToSpeech.QUEUE_FLUSH, map);
    }

    private void setSpeechRate(float rate){
        mTts.setSpeechRate(rate);
    }

    private void setPitch(float pitch){
        mTts.setPitch(pitch);
    }

    private void changeTtsLanguage(TextToSpeech tts, String language) {
        switch (language){
                case ENG_UK:
                    tts.setLanguage(Locale.UK);
                    break;
                case ENG_US:
                    tts.setLanguage(Locale.US);
                    break;
                case SessionManager.BN_IN:
                    tts.setLanguage(new Locale("bn", "IN"));
                    break;
                case SessionManager.BE_IN:
                    tts.setLanguage(new Locale("be", "IN"));
                    break;
                case SessionManager.HI_IN:
                case ENG_IN:
                default:
                    tts.setLanguage(new Locale("hi","IN"));
                    break;
        }
    }

    private void stopTtsSay(){
        mTts.stop();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, Intent intent) {
            switch (intent.getAction()){
                case "com.dsource.idc.jellowintl.SPEECH_TEXT":
                    say(intent.getStringExtra("speechText")); break;
                case "com.dsource.idc.jellowintl.SPEECH_PITCH":
                    setPitch(intent.getFloatExtra("speechPitch", new SessionManager(context).getPitch())); break;
                case "com.dsource.idc.jellowintl.SPEECH_SPEED":
                    setSpeechRate(intent.getFloatExtra("speechSpeed", new SessionManager(context).getSpeed())); break;
                case "com.dsource.idc.jellowintl.SPEECH_LANG":
                    changeTtsLanguage(mTts, intent.getStringExtra("speechLanguage")); break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ":
                    broadcastTtsData(mTts, intent);
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_REQ":
                    broadcastTtsDataPostKitkatDevices(mTts, intent);
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_STOP":
                    stopTtsSay(); break;
                case "com.dsource.idc.jellowintl.STOP_SERVICE":
                    stopSelf(); break;
            }
        }
    };

    private void broadcastTtsData(TextToSpeech tts, Intent intent) {
        Intent dataIntent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        String infoLang="", infoCountry="";
        if(Build.VERSION.SDK_INT < 18) {
            infoLang = tts.getLanguage().getLanguage();
            infoCountry = tts.getLanguage().getCountry();
        }else if(Build.VERSION.SDK_INT < 21) {
            infoLang = tts.getDefaultLanguage().getLanguage().substring(0,2);
            infoCountry = tts.getDefaultLanguage().getCountry().substring(0,2);
        }else {
            infoLang = tts.getDefaultVoice().getLocale().getLanguage();
            infoCountry = tts.getDefaultVoice().getLocale().getCountry();
        }
        Log.d(TAG, "broadcastTtsData: harshit/TTS Language = " + infoLang.concat("-r".concat(infoCountry)));
        Log.d(TAG, "broadcastTtsData: harshit/User Selected Language = " + intent.getStringExtra("saveSelectedLanguage"));
        dataIntent.putExtra("systemTtsRegion", infoLang.concat("-r".concat(infoCountry)));
        if(infoLang.concat("-r".concat(infoCountry)).equals(HI_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(ENG_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(infoLang.concat("-r".concat(infoCountry)).equals(BE_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(BN_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(!intent.getStringExtra("saveSelectedLanguage").equals(ENG_IN) &&
                intent.getStringExtra("saveSelectedLanguage").
                        equals(infoLang.concat("-r".concat(infoCountry))))
            dataIntent.putExtra("saveUserLanguage", true);
        else dataIntent.putExtra("saveUserLanguage", false);

        if(!intent.getStringExtra("saveSelectedLanguage").equals(""))
            dataIntent.putExtra("showError", true);
        sendBroadcast(dataIntent);
    }

    private void broadcastTtsDataPostKitkatDevices(TextToSpeech tts, Intent intent){
        Intent responseIntent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_RES");
        boolean isVoiceAvail = isVoiceAvailableForLanguage(tts, intent.getStringExtra("language"));
        responseIntent.putExtra("isVoiceAvail", isVoiceAvail);
        changeTtsLanguage(tts, intent.getStringExtra("language"));
        sendBroadcast(responseIntent);
    }

    private boolean isVoiceAvailableForLanguage(TextToSpeech tts, String language) {
        if(language.equals(ENG_IN))
            language = HI_IN;
        if(Build.VERSION.SDK_INT > 20){
            Locale locale = new Locale(language.split("-r")[0],language.split("-r")[1]);
            for (Voice voice : tts.getVoices())
                if(voice.getLocale().equals(locale) && !voice.getFeatures().contains("notInstalled"))
                    return true;
        }
        return  false;
    }

    private class BackgroundSpeechOperationsAsync extends AsyncTask<Void, Void, Void> {
        private SessionManager mSession;

        BackgroundSpeechOperationsAsync(Context context) {
            mSession = new SessionManager(context);
        }

        @Override
        protected Void doInBackground(Void... params) {
            mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    try {
                        changeTtsLanguage(mTts, mSession.getLanguage());
                        mTts.setSpeechRate(getTTsSpeed(mSession.getLanguage()));
                        mTts.setPitch(getTTsPitch(mSession.getLanguage()));
                        sendBroadcast(new Intent("com.dsource.idc.jellowintl.INIT_SERVICE"));
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                    }
                }
            }, getTTsEngineName(mSession.getLanguage()));

            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                    String s = utteranceId;
                }

                @Override
                public void onDone(String utteranceId) {
                    String s = utteranceId;
                }

                @Override
                public void onError(String utteranceId) {
                    sendBroadcast(new Intent("com.dsource.idc.jellowintl.SPEECH_TTS_ERROR"));
                }
            });
            return null;
        }

        private float getTTsPitch(String language) {
            switch(language){
                case ENG_UK:
                case ENG_US:
                case HI_IN:
                case ENG_IN:
                case BN_IN:
                case BE_IN:
                default:
                    return (float) mSession.getPitch()/50;
            }
        }

        private float getTTsSpeed(String language){
            switch(language){
                case ENG_UK:
                case ENG_US:
                case HI_IN:
                case ENG_IN:
                case BN_IN:
                case BE_IN:
                default:
                    return (float) (mSession.getSpeed()/50);
            }
        }

        private String getTTsEngineName(String language) {
            switch(language){
                case ENG_UK:
                case ENG_US:
                case HI_IN:
                case ENG_IN:
                case BN_IN:
                case BE_IN:
                default:
                    return "com.google.android.tts";
            }
        }
    }
}