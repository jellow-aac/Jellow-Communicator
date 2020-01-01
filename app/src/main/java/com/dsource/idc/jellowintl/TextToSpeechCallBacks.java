package com.dsource.idc.jellowintl;

public interface TextToSpeechCallBacks {


  void sendSpeechEngineLanguageNotSetCorrectlyError();


  void speechEngineNotFoundError();


  void speechSynthesisCompleted();
}