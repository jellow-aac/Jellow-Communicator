package com.dsource.idc.jellowintl.utility;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.support.annotation.Nullable;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.R;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import static com.dsource.idc.jellowintl.utility.SessionManager.BE_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

/**
 * Created by ekalpa on 7/4/2017.
 */
public class JellowTTSService extends Service{
    private final String UTTERANCE_ID = "com.dsource.idc.jellowintl.utterence.id";
    private MediaPlayer mMediaPlayer;
    private TextToSpeech mTts;
    HashMap<String, String> map;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        map = new HashMap<String, String>();
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
        filter.addAction("com.dsource.idc.jellowintl.AUDIO_PATH");
        filter.addAction("com.dsource.idc.jellowintl.AUDIO_IN_QUEUE");
        filter.addAction("com.dsource.idc.jellowintl.CREATE_ABOUT_ME_RECORDING_REQ");
        filter.addAction("com.dsource.idc.jellowintl.AUDIO_STOP");
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
            case ENG_AU:
                tts.setLanguage(new Locale("en", "AU"));
                break;
            case BN_IN:
            case BE_IN:
                tts.setLanguage(new Locale("bn", "IN"));
                break;
            case SessionManager.HI_IN:
            case ENG_IN:
            case MR_IN:
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
                case "com.dsource.idc.jellowintl.AUDIO_PATH":
                    stopAudio();
                    playAudio(intent.getStringExtra("audioPath"));
                    break;
                case "com.dsource.idc.jellowintl.CREATE_ABOUT_ME_RECORDING_REQ":
                    createUserProfileRecordingsUsingTTS();
                    break;
                case "com.dsource.idc.jellowintl.AUDIO_IN_QUEUE":
                    stopAudio();
                    playInQueue(intent.getStringExtra("speechTextInQueue"));
                    break;
                case "com.dsource.idc.jellowintl.AUDIO_STOP":
                    stopAudio();
                    break;
            }
        }
    };

    private void createUserProfileRecordingsUsingTTS() {
        SessionManager session = new SessionManager(this);
        final String path = getDir("mr-rIN", Context.MODE_PRIVATE).getAbsolutePath() + "/audio/";
        mTts.setLanguage(new Locale("hi", "IN"));
        try {
            String emailId = session.getEmailId().replaceAll(".", "$0 ").replace(".", "dot");
            String contactNo = session.getCaregiverNumber();
            contactNo = contactNo.substring(0, contactNo.length()-3);
            contactNo = contactNo.replaceAll(".", "$0 ").replace("+", "plus");
            if(Build.VERSION.SDK_INT >= 21) {
                File name = new File(path+ "name.mp3");
                Log.e("File : ", String.valueOf(name.createNewFile()));
                File email = new File(path+ "email.mp3");
                Log.e("File : ", String.valueOf(email.createNewFile()));
                File contact = new File(path+ "contact.mp3");
                Log.e("File : ", String.valueOf(contact.createNewFile()));
                File caregiverName = new File(path+ "caregiverName.mp3");
                Log.e("File : ", String.valueOf(caregiverName.createNewFile()));
                File address = new File(path+ "address.mp3");
                Log.e("File : ", String.valueOf(address.createNewFile()));
                File bloodGroup = new File(path+ "bloodGroup.mp3");
                Log.e("File : ", String.valueOf(bloodGroup.createNewFile()));
                mTts.synthesizeToFile(session.getName(), null, name, UTTERANCE_ID);
                mTts.synthesizeToFile(emailId, null, email, UTTERANCE_ID);
                mTts.synthesizeToFile(contactNo, null, contact, UTTERANCE_ID);
                mTts.synthesizeToFile(session.getCaregiverName(), null, caregiverName, UTTERANCE_ID);
                mTts.synthesizeToFile(session.getAddress(), null, address, UTTERANCE_ID);
                mTts.synthesizeToFile(getBloodGroup(session.getBlood()), null, bloodGroup, UTTERANCE_ID);
            }else {
                mTts.synthesizeToFile(session.getName(), null, path + "name.mp3");
                mTts.synthesizeToFile(emailId, null, path + "email.mp3");
                mTts.synthesizeToFile(contactNo, null, path + "contact.mp3");
                mTts.synthesizeToFile(session.getCaregiverName(), null, path + "caregiverName.mp3");
                mTts.synthesizeToFile(session.getAddress(), null, path + "address.mp3");
                mTts.synthesizeToFile(getBloodGroup(session.getBlood()), null, path + "bloodGroup.mp3");
            }
            changeTtsLanguage(mTts, session.getLanguage());
            sendBroadcast(new Intent("com.dsource.idc.jellowintl.CREATE_ABOUT_ME_RECORDING_RES"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * <p>This function will find and return the blood group of user
     * @return blood group of user.</p>
     * */
    private String getBloodGroup(int blood) {
        switch(blood){
            case  1: return getString(R.string.aPos);
            case  2: return getString(R.string.aNeg);
            case  3: return getString(R.string.bPos);
            case  4: return getString(R.string.bNeg);
            case  5: return getString(R.string.abPos);
            case  6: return getString(R.string.abNeg);
            case  7: return getString(R.string.oPos);
            case  8: return getString(R.string.oNeg);
            default: return "";
        }
    }

    public synchronized void playAudio(String audioPath) {
        try {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(audioPath);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playInQueue(final String speechTextInQueue) {
        try {
            final int[] count = {0};
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(speechTextInQueue.split(",")[0]);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (count[0] < 2){
                        mMediaPlayer.release();
                        mMediaPlayer = null;mMediaPlayer = new MediaPlayer();
                        try {
                            mMediaPlayer.setDataSource(speechTextInQueue.split(",")[1]);
                            mMediaPlayer.prepare();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        mMediaPlayer.start();
                        count[0]++;
                    }
                }
            });
            mMediaPlayer.prepare();
            mMediaPlayer.start();
            count[0]++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stopAudio() {
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void broadcastTtsData(TextToSpeech tts, Intent intent) {
        Intent dataIntent = new Intent("com.dsource.idc.jellowintl.SPEECH_SYSTEM_LANG_RES");
        String infoLang="", infoCountry="";
        try {
            if (Build.VERSION.SDK_INT < 18) {
                infoLang = tts.getLanguage().getLanguage();
                infoCountry = tts.getLanguage().getCountry();
            } else if (Build.VERSION.SDK_INT < 21) {
                infoLang = tts.getDefaultLanguage().getLanguage().substring(0, 2);
                infoCountry = tts.getDefaultLanguage().getCountry().substring(0, 2);
            } else {
                //When below if case is false then variables infoLang & infoCountry are empty.
                //Keeping infoLang & infoCountry variables empty and sending them with broadcast have no
                //impact. Whenever they are empty, the broadcast is sent only to {@LanguageSelectActivity}
                //class. And these broadcast intent response is not used when api is Lollipop or above.
                if (tts.getDefaultVoice() != null) {
                    infoLang = tts.getDefaultVoice().getLocale().getLanguage();
                    infoCountry = tts.getDefaultVoice().getLocale().getCountry();
                }
            }
        }catch(Exception e){
            //This error is ignored. Their might no language is set previously to text-to-speech
            // engine and hence above retrieval of lang, country {infoLang, infoCountry} fails.
            // If it is empty, the broadcast is sent to receiver with empty ("-r" value) tts language
            // name and this gives error message to user in receiver code.
        }
        dataIntent.putExtra("systemTtsRegion", infoLang.concat("-r".concat(infoCountry)));
        if(infoLang.concat("-r".concat(infoCountry)).equals(HI_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(ENG_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(infoLang.concat("-r".concat(infoCountry)).equals(BE_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(BN_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(infoLang.concat("-r".concat(infoCountry)).equals(BN_IN) &&
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
                        if(status == TextToSpeech.ERROR || (mTts != null &&
                                !mTts.getEngines().toString().contains(getTTsEngineName("")))){
                            sendBroadcast(new Intent("com.dsource.idc.jellowintl.INIT_SERVICE_ERR"));
                            return;
                        }
                        changeTtsLanguage(mTts, mSession.getLanguage());
                        mTts.setSpeechRate(getTTsSpeed(mSession.getLanguage()));
                        mTts.setPitch(getTTsPitch(mSession.getLanguage()));
                        if (mSession.getLanguage().endsWith(MR_IN))
                            createUserProfileRecordingsUsingTTS();
                        sendBroadcast(new Intent("com.dsource.idc.jellowintl.INIT_SERVICE"));
                    } catch (Exception e) {
                        Crashlytics.logException(e);
                    }
                }
            }, getTTsEngineName(mSession.getLanguage()));

            mTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String utteranceId) {
                }

                @Override
                public void onDone(String utteranceId) {
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
                case ENG_AU:
                case HI_IN:
                case ENG_IN:
                case BN_IN:
                case BE_IN:
                case MR_IN:
                default:
                    return (float) mSession.getPitch()/50;
            }
        }

        private float getTTsSpeed(String language){
            switch(language){
                case ENG_UK:
                case ENG_US:
                case ENG_AU:
                case HI_IN:
                case ENG_IN:
                case BN_IN:
                case BE_IN:
                case MR_IN:
                default:
                    return (float) (mSession.getSpeed()/50);
            }
        }

        private String getTTsEngineName(String language) {
            switch(language){
                case ENG_UK:
                case ENG_US:
                case ENG_AU:
                case HI_IN:
                case ENG_IN:
                case BN_IN:
                case BE_IN:
                case MR_IN:
                default:
                    return "com.google.android.tts";
            }
        }
    }
}