package com.dsource.idc.jellowintl;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.speech.tts.Voice;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
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

public class LevelScreenBaseActivity extends BaseActivity {
    private static TextToSpeech sTts;
    private static Boolean isNoTTSLang;
    private int mFailedToSynthesizeTextCount = 0;
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
        /*IntentFilter filter = new IntentFilter();
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
        registerReceiver(receiver, filter);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.profile:
                startActivity(new Intent(this, ProfileFormActivity.class));
                break;
            case R.id.aboutJellow:
                startActivity(new Intent(this, AboutJellowActivity.class));
                break;
            case R.id.tutorial:
                startActivity(new Intent(this, TutorialActivity.class));
                break;
            case R.id.keyboardInput:
                startActivity(new Intent(this, KeyboardInputActivity.class));
                break;
            case R.id.languageSelect:
                if (!isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, LanguageSelectActivity.class));
                } else {
                    startActivity(new Intent(this, LanguageSelectTalkBackActivity.class));
                }
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.accessibilitySetting:
                startActivity(new Intent(this, AccessibilitySettingsActivity.class));
                break;
            case R.id.resetPreferences:
                startActivity(new Intent(this, ResetPreferencesActivity.class));
                break;
            case R.id.feedback:
                if(isAccessibilityTalkBackOn((AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE))) {
                    startActivity(new Intent(this, FeedbackActivityTalkBack.class));
                }
                else {
                    startActivity(new Intent(this, FeedbackActivity.class));
                }
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


    public void setLevelActionBar(String title){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.yellow_bg));
        getSupportActionBar().setTitle(title);
    }


    private void setupSpeechEngine() {
        sTts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                try {
                    if(status == TextToSpeech.ERROR || (sTts != null &&
                            !sTts.getEngines().toString().contains(getTTsEngineName("")))){
                        return;
                    }
                    setSpeechEngineLanguage(sTts, getSession().getLanguage());
                    checkSpeechEngineLanguageInSettings();
                    checkSpeechEngineLanguageForAccessibility();
                    sTts.setSpeechRate(getTTsSpeed(getSession().getLanguage()));
                    sTts.setPitch(getTTsPitch(getSession().getLanguage()));
                    if (getSession().getLanguage().endsWith(MR_IN))
                        createUserProfileRecordingsUsingTTS();
                    /*sendBroadcast(new Intent("com.dsource.idc.jellowintl.INIT_SERVICE"));*/
                } catch (Exception e) {
                    Crashlytics.logException(e);
                }
            }
        }, getTTsEngineName(getSession().getLanguage()));

        sTts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
            }

            @Override
            public void onDone(String utteranceId) {
            }

            @Override
            public void onError(String utteranceId) {
                //Text synthesize process failed third time then show TTs error.
                if(++mFailedToSynthesizeTextCount > 2)
                    mErrorHandler.sendFailedToSynthesizeError(getString(R.string.txt_actLangSel_complete_mainscreen_msg));
            }
        });

    }

    private void setSpeechEngineLanguage(TextToSpeech tts, String language) {
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
            case ENG_IN:
                tts.setLanguage(new Locale("en","IN"));
                break;
            case HI_IN:
            case MR_IN:
            default:
                tts.setLanguage(new Locale("hi","IN"));
                break;
        }
    }

    private void checkSpeechEngineLanguageInSettings() {
        //Below if is true when
        //      1) app language is equal to "-r" OR
        //      2) app language is bengali and tts language does matches
        //         with every type of bengali language OR
        //      3) app language and Text-to-speech language are different THEN
        //         show error toast and set incorrect language setting to true.
        if((Build.VERSION.SDK_INT < 21) && !getSession().getLanguage().equals(LangMap.get("मराठी"))) {
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

    public String getSpeechEngineLanguage(){
        String infoLang="", infoCountry="";
        try {
            if (Build.VERSION.SDK_INT < 18) {
                infoLang = sTts.getLanguage().getLanguage();
                infoCountry = sTts.getLanguage().getCountry();
            } else if (Build.VERSION.SDK_INT < 21) {
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

    public void speak(String speechText){
        stopSpeaking();
        if(isNoTTSLang == null){
            isNoTTSLang = isNoTTSLanguage();
        }
        if(isNoTTSLang)
            playAudio(getAudioPath(this)+speechText);
        else
            sTts.speak(speechText, TextToSpeech.QUEUE_FLUSH, map);
    }

    public void stopSpeaking(){
        sTts.stop();
    }

    public void setSpeechRate(float rate){
        sTts.setSpeechRate(rate);
    }

    public void setSpeechPitch(float pitch){
        sTts.setPitch(pitch);
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
        if(infoLang.concat("-r".concat(infoCountry)).equals(ENG_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(ENG_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(infoLang.concat("-r".concat(infoCountry)).equals(BE_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(BN_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(infoLang.concat("-r".concat(infoCountry)).equals(BN_IN) &&
                intent.getStringExtra("saveSelectedLanguage").equals(BN_IN))
            dataIntent.putExtra("saveUserLanguage", true);
        else if(intent.getStringExtra("saveSelectedLanguage").
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
        setSpeechEngineLanguage(tts, intent.getStringExtra("language"));
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


    private void createUserProfileRecordingsUsingTTS() {
        SessionManager session = new SessionManager(this);
        final String path = getDir("mr-rIN", Context.MODE_PRIVATE).getAbsolutePath() + "/audio/";
        sTts.setLanguage(new Locale("hi", "IN"));
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
                sTts.synthesizeToFile(session.getName(), null, name, UTTERANCE_ID);
                sTts.synthesizeToFile(emailId, null, email, UTTERANCE_ID);
                sTts.synthesizeToFile(contactNo, null, contact, UTTERANCE_ID);
                sTts.synthesizeToFile(session.getCaregiverName(), null, caregiverName, UTTERANCE_ID);
                sTts.synthesizeToFile(session.getAddress(), null, address, UTTERANCE_ID);
                sTts.synthesizeToFile(getBloodGroup(session.getBlood()), null, bloodGroup, UTTERANCE_ID);
            }else {
                sTts.synthesizeToFile(session.getName(), null, path + "name.mp3");
                sTts.synthesizeToFile(emailId, null, path + "email.mp3");
                sTts.synthesizeToFile(contactNo, null, path + "contact.mp3");
                sTts.synthesizeToFile(session.getCaregiverName(), null, path + "caregiverName.mp3");
                sTts.synthesizeToFile(session.getAddress(), null, path + "address.mp3");
                sTts.synthesizeToFile(getBloodGroup(session.getBlood()), null, path + "bloodGroup.mp3");
            }
            setSpeechEngineLanguage(sTts, session.getLanguage());
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


    private boolean isNoTTSLanguage(){
        return SessionManager.NoTTSLang.contains(getSession().getLanguage());
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
                return (float) getSession().getPitch()/50;
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
                return (float) (getSession().getSpeed()/50);
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

