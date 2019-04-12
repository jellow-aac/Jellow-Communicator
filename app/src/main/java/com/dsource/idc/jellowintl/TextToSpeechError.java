package com.dsource.idc.jellowintl;

public interface TextToSpeechError{
  void sendFailedToSynthesizeError(String message);
  void sendLanguageIncompatibleError(String message);
  void sendLanguageIncompatibleForAccessibility();
  void speechEngineNotFoundError();
}
