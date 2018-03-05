package com.dsource.idc.jellowintl.Utility;

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

import java.util.HashMap;
import java.util.Locale;

import static com.dsource.idc.jellowintl.Utility.Analytics.reportException;

/**
 * Created by ekalpa on 7/4/2017.
 */
public class JellowTTSService extends Service{
    private TextToSpeech mTts;
    HashMap<String, String> map;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        map = new HashMap<String, String>();
        String UTTERANCE_ID = "com.dsource.idc.jellowintl.utterence.id";
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
        new BackgroundSpeechOperationsAsync().execute(this);
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
        unregisterReceiver(receiver);
        mTts.shutdown();
        startService(new Intent(getApplication(), JellowTTSService.class));
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

    private void changeTtsLanguage(String language) {
        switch (language){
                case SessionManager.ENG_UK:
                    mTts.setLanguage(Locale.UK);
                    break;
                case SessionManager.ENG_US:
                    mTts.setLanguage(Locale.US);
                    break;
                case SessionManager.HI_IN:
                case SessionManager.ENG_IN:
                default:
                    mTts.setLanguage(new Locale("hi","IN"));
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
                    changeTtsLanguage(intent.getStringExtra("speechLanguage")); break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_REQ":
                    Intent broadcastIntent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
                    String lang="", country="";
                    if(Build.VERSION.SDK_INT < 18) {
                        lang = mTts.getLanguage().getLanguage();
                        country = mTts.getLanguage().getCountry();
                    }else if(Build.VERSION.SDK_INT > 17 && Build.VERSION.SDK_INT < 21) {
                        lang = mTts.getDefaultLanguage().getLanguage().substring(0,2);
                        country = mTts.getDefaultLanguage().getCountry().substring(0,2);
                    }else if(Build.VERSION.SDK_INT > 21) {
                        lang = mTts.getDefaultVoice().getLocale().getLanguage();
                        country = mTts.getDefaultVoice().getLocale().getCountry();
                    }
                    broadcastIntent.putExtra("systemTtsRegion", lang.concat("-r".concat(country)));
                    if(lang.concat("-r".concat(country)).equals("hi-rIN") && intent.getStringExtra("saveSelectedLanguage").equals("en-rIN"))
                        broadcastIntent.putExtra("saveUserLanguage", true);
                    else if(!intent.getStringExtra("saveSelectedLanguage").equals("en-rIN") &&
                            intent.getStringExtra("saveSelectedLanguage").equals(lang.concat("-r".concat(country))))
                        broadcastIntent.putExtra("saveUserLanguage", true);
                    else broadcastIntent.putExtra("saveUserLanguage", false);
                    if(!intent.getStringExtra("saveSelectedLanguage").equals(""))
                        broadcastIntent.putExtra("showError", true);
                    sendBroadcast(broadcastIntent);
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_REQ":
                    Intent responceIntent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_VOICE_AVAIL_RES");
                    boolean isVoiceAvail = isVoiceAvailableForLanguage(intent.getStringExtra("language"));
                    responceIntent.putExtra("isVoiceAvail", isVoiceAvail);
                    changeTtsLanguage(intent.getStringExtra("language"));
                    sendBroadcast(responceIntent);
                    break;
                case "com.dsource.idc.jellowintl.SPEECH_STOP":
                    stopTtsSay(); break;
                case "com.dsource.idc.jellowintl.STOP_SERVICE":
                    stopSelf(); break;
            }
        }
    };

    private boolean isVoiceAvailableForLanguage(String language) {
        if(language.equals("en-rIN"))
            language = "hi-rIN";
        if(Build.VERSION.SDK_INT > 20){
            Locale locale = new Locale(language.split("-r")[0],language.split("-r")[1]);
            for (Voice voice : mTts.getVoices())
                if(voice.getLocale().equals(locale) && !voice.getFeatures().contains("notInstalled"))
                    return true;
        }
        return  false;
    }

    private class BackgroundSpeechOperationsAsync extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... params) {
            final Context context = params[0];
            final SessionManager session = new SessionManager(context);
            mTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    try {
                        mTts.setEngineByPackageName("com.google.android.tts");
                        changeTtsLanguage(session.getLanguage());
                        mTts.setSpeechRate((float) session.getSpeed()/100);
                        mTts.setPitch((float) session.getPitch()/100);
                        sendBroadcast(new Intent("com.dsource.idc.jellowintl.INIT_SERVICE"));
                    } catch (Exception e) {
                        reportException(e);
                    }
                }
            });
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
    }
}