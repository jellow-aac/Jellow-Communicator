package com.dsource.idc.jellowintl;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import com.crashlytics.android.Crashlytics;
import com.dsource.idc.jellowintl.utility.SessionManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import static com.dsource.idc.jellowintl.factories.PathFactory.getAudioPath;
import static com.dsource.idc.jellowintl.utility.SessionManager.BE_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.BN_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_AU;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_UK;
import static com.dsource.idc.jellowintl.utility.SessionManager.ENG_US;
import static com.dsource.idc.jellowintl.utility.SessionManager.HI_IN;
import static com.dsource.idc.jellowintl.utility.SessionManager.LangMap;
import static com.dsource.idc.jellowintl.utility.SessionManager.MR_IN;

public class SpeechEngineBaseActivity extends BaseActivity{
    private static TextToSpeech sTts;
    private static int mFailedToSynthesizeTextCount = 0;
    private final String UTTERANCE_ID = "com.dsource.idc.jellowintl.utterence.id";
    private HashMap<String, String> map;
    private TextToSpeechError mErrorHandler;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupSpeechEngine();
        map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UTTERANCE_ID);
    }

    private void setupSpeechEngine() {
        sTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                try {
                    if(status == TextToSpeech.ERROR || (sTts != null &&
                            !sTts.getEngines().toString().contains(getTTsEngineNameForLanguage("")))){
                        mErrorHandler.speechEngineNotFoundError();
                        return;
                    }
                    setSpeechEngineLanguage(getSession().getLanguage());
                    checkSpeechEngineLanguageInSettings();
                    checkSpeechEngineLanguageForAccessibility();
                    sTts.setSpeechRate(getTTsSpeedForLanguage(getSession().getLanguage()));
                    sTts.setPitch(getTTsPitchForLanguage(getSession().getLanguage()));
                    if (getSession().getLanguage().endsWith(MR_IN))
                        createUserProfileRecordingsUsingTTS();
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }
            }
        }, getTTsEngineNameForLanguage(getSession().getLanguage()));

        sTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override public void onStart(String utteranceId) {}

            @Override public void onDone(String utteranceId) {}

            @Override
            public void onError(String utteranceId) {
                //Text synthesize process failed third time then show TTs error.
                if(++mFailedToSynthesizeTextCount > 2)
                    mErrorHandler.sendFailedToSynthesizeError(getString(R.string.txt_actLangSel_complete_mainscreen_msg));
            }
        });

    }

    private void checkSpeechEngineLanguageInSettings() {
        //Below if is true when
        //      1) app language is equal to "-r" OR
        //      2) app language is bengali and tts language does matches
        //         with every type of bengali language OR
        //      3) app language and Text-to-speech language are different THEN
        //         show error toast and set incorrect language setting to true.
        if((Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
                && !getSession().getLanguage().equals(LangMap.get("मराठी"))) {
            if (getSpeechEngineLanguage().equals("-r") ||
                    (getSession().getLanguage().equals(BN_IN) && !getSpeechEngineLanguage().equals(BN_IN) && !(getSpeechEngineLanguage().equals(BE_IN)) ||
                            (!getSession().getLanguage().equals(BN_IN) && !getSession().getLanguage().equals(getSpeechEngineLanguage())))) {
                mErrorHandler.sendLanguageIncompatibleError(getString(R.string.speech_engin_lang_sam));
            }
        }

    }

    private void checkSpeechEngineLanguageForAccessibility() {
        if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE)) &&
                !getSession().isChangeLanguageNeverAsk() ) {
            if(getSession().getLanguage().equals(BN_IN) &&(getSpeechEngineLanguage().equals(BN_IN) || getSpeechEngineLanguage().equals(BE_IN)))
            {}
            else if(!getSession().getLanguage().equals(BN_IN) && getSession().getLanguage().equals(getSpeechEngineLanguage()))
            {}
            else {
                mErrorHandler.sendLanguageIncompatibleForAccessibility();
            }
        }
    }

    public void registerSpeechEngineErrorHandle(TextToSpeechError handler){
        mErrorHandler = handler;
    }

    public String getSpeechEngineLanguage(){
        String infoLang="", infoCountry="";
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                infoLang = sTts.getLanguage().getLanguage();
                infoCountry = sTts.getLanguage().getCountry();
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                infoLang = sTts.getDefaultLanguage().getLanguage().substring(0, 2);
                infoCountry = sTts.getDefaultLanguage().getCountry().substring(0, 2);
            } else {
                //When below if case is false then variables infoLang & infoCountry are empty.
                //Keeping infoLang & infoCountry variables empty and sending them with broadcast have no
                //impact. Whenever they are empty, the broadcast is sent only to {@LanguageSelectActivity}
                //class. And these broadcast intent response is not used when api is Lollipop or above.
                if (sTts.getDefaultVoice() != null) {
                    infoLang = sTts.getDefaultVoice().getLocale().getLanguage();
                    infoCountry = sTts.getDefaultVoice().getLocale().getCountry();
                }
            }
        }catch(Exception e){
            //This error is ignored. Their might no language is set previously to text-to-speech
            // engine and hence above retrieval of lang, country {infoLang, infoCountry} fails.
            // If it is empty, the broadcast is sent to receiver with empty ("-r" value) tts language
            // name and this gives error message to user in receiver code.
        }
        return infoLang.concat("-r".concat(infoCountry));
    }

    public void setSpeechEngineLanguage(String language) {
        switch (language){
            case ENG_UK:
                sTts.setLanguage(Locale.UK);
                break;
            case ENG_US:
                sTts.setLanguage(Locale.US);
                break;
            case ENG_AU:
                sTts.setLanguage(new Locale("en", "AU"));
                break;
            case BN_IN:
            case BE_IN:
                sTts.setLanguage(new Locale("bn", "IN"));
                break;
            case ENG_IN:
                sTts.setLanguage(new Locale("en","IN"));
                break;
            case HI_IN:
            case MR_IN:
            default:
                sTts.setLanguage(new Locale("hi","IN"));
                break;
        }
    }

    public void speak(String speechText){
        stopSpeaking();
        if(isNoTTSLanguage())
            playAudio(getAudioPath(this)+speechText);
        else
            sTts.speak(speechText, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void stopSpeaking(){
        sTts.stop();
        stopAudio();
    }

    public void setSpeechRate(float rate){
        sTts.setSpeechRate(rate);
    }

    public void setSpeechPitch(float pitch){
        sTts.setPitch(pitch);
    }

    public boolean isVoiceAvailableForLanguage(String language) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH){
            Locale locale = new Locale(language.split("-r")[0],language.split("-r")[1]);
            for (Voice voice : sTts.getVoices())
                if(voice.getLocale().equals(locale) && !voice.getFeatures().contains("notInstalled"))
                    return true;
        }
        return  false;
    }

    public void createUserProfileRecordingsUsingTTS() {
        final String path = getDir(MR_IN, Context.MODE_PRIVATE).getAbsolutePath() + "/audio/";
        sTts.setLanguage(new Locale("hi", "IN"));
        try {
            String emailId = getSession().getEmailId().replaceAll(".", "$0 ").replace(".", "dot");
            String contactNo = getSession().getCaregiverNumber();
            contactNo = contactNo.substring(0, contactNo.length()-3);
            contactNo = contactNo.replaceAll(".", "$0 ").replace("+", "plus");
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
                sTts.synthesizeToFile(getSession().getName(), null, name, UTTERANCE_ID);
                sTts.synthesizeToFile(emailId, null, email, UTTERANCE_ID);
                sTts.synthesizeToFile(contactNo, null, contact, UTTERANCE_ID);
                sTts.synthesizeToFile(getSession().getCaregiverName(), null, caregiverName, UTTERANCE_ID);
                sTts.synthesizeToFile(getSession().getAddress(), null, address, UTTERANCE_ID);
                sTts.synthesizeToFile(getBloodGroup(getSession().getBlood()), null, bloodGroup, UTTERANCE_ID);
            }else {
                sTts.synthesizeToFile(getSession().getName(), null, path + "name.mp3");
                sTts.synthesizeToFile(emailId, null, path + "email.mp3");
                sTts.synthesizeToFile(contactNo, null, path + "contact.mp3");
                sTts.synthesizeToFile(getSession().getCaregiverName(), null, path + "caregiverName.mp3");
                sTts.synthesizeToFile(getSession().getAddress(), null, path + "address.mp3");
                sTts.synthesizeToFile(getBloodGroup(getSession().getBlood()), null, path + "bloodGroup.mp3");
            }
            setSpeechEngineLanguage(getSession().getLanguage());
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

    public void playInQueue(final String speechTextInQueue) {
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

    public void speakInQueue(String speechData){
        speechData = getAudioPath(this)+ speechData;
        if (speechData.contains("GGL")){
            speechData = speechData+","+ getAudioPath(this)+ "name.mp3";
        }else if (speechData.contains("GGY")){
            speechData = speechData+","+ getAudioPath(this)+ "email.mp3";
        }else if (speechData.contains("GGM")){
            speechData = speechData+","+  getAudioPath(this)+ "contact.mp3";
        }else if (speechData.contains("GGD")){
            speechData = speechData+","+  getAudioPath(this)+ "caregiverName.mp3";
        }else if (speechData.contains("GGN")){
            speechData = speechData+","+  getAudioPath(this)+ "address.mp3";
        }else {
            speechData = speechData+","+  getAudioPath(this)+ "bloodGroup.mp3";
        }
        playInQueue(speechData);
    }

    public boolean isNoTTSLanguage(){
        return SessionManager.NoTTSLang.contains(getSession().getLanguage());
    }

    private float getTTsPitchForLanguage(String language) {
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
                return (float) getSession().getPitch()/50;
        }
    }

    private float getTTsSpeedForLanguage(String language){
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
                return (float) (getSession().getSpeed()/50);
        }
    }

    private String getTTsEngineNameForLanguage(String language) {
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

