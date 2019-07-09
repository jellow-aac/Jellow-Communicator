package com.dsource.idc.jellowintl.utility;

import android.content.Context;
import android.content.Intent;

import static com.dsource.idc.jellowintl.factories.PathFactory.getAudioPath;

public class SpeechUtils {

    private static Boolean isNoTTSLang;

    public static void speak(Context context, String speechData){
        if(isNoTTSLang == null){
            isNoTTSLang = isNoTTSLanguage(context);
        }
        if(isNoTTSLang)
            playAudio(context,getAudioPath(context)+speechData);
        else
            speakSpeech(context,speechData);
    }

    public static void speakInQueue(Context context, String speechData){
        speechData = getAudioPath(context)+ speechData;
        if (speechData.contains("GGL")){
            speechData = speechData+","+ getAudioPath(context)+ "name.mp3";
        }else if (speechData.contains("GGY")){
            speechData = speechData+","+ getAudioPath(context)+ "email.mp3";
        }else if (speechData.contains("GGM")){
            speechData = speechData+","+  getAudioPath(context)+ "contact.mp3";
        }else if (speechData.contains("GGD")){
            speechData = speechData+","+  getAudioPath(context)+ "caregiverName.mp3";
        }else if (speechData.contains("GGN")){
            speechData = speechData+","+  getAudioPath(context)+ "address.mp3";
        }else {
            speechData = speechData+","+  getAudioPath(context)+ "bloodGroup.mp3";
        }
        playAudioInQueue(context, speechData);
    }

    public static void updateSpeechParam(Context context){
        isNoTTSLang = isNoTTSLanguage(context);
    }

    public static boolean isNoTTSLanguage(Context context){
        SessionManager sessionManager = new SessionManager(context);
        return SessionManager.NoTTSLang.contains(sessionManager.getLanguage());
    }


    private static void speakSpeech(Context context, String speechText) {
        Intent intent = new Intent("com.dsource.idc.jellowintl.SPEECH_TEXT");
        intent.putExtra("speechText", speechText.toLowerCase());
        context.sendBroadcast(intent);
    }

    private static void playAudio(Context context, String audioPath) {
        Intent intent = new Intent("com.dsource.idc.jellowintl.AUDIO_PATH");
        intent.putExtra("audioPath", audioPath);
        context.sendBroadcast(intent);
    }

    private static void playAudioInQueue(Context context, String audioPath) {
        Intent intent = new Intent("com.dsource.idc.jellowintl.AUDIO_IN_QUEUE");
        intent.putExtra("speechTextInQueue", audioPath);
        context.sendBroadcast(intent);
    }



}
