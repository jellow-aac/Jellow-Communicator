package com.dsource.idc.jellowintl.utility.interfaces;

public interface TextToSpeechCallBacks {


  void sendSpeechEngineLanguageNotSetCorrectlyError();


  void speechEngineNotFoundError();


  void speechSynthesisCompleted();
}