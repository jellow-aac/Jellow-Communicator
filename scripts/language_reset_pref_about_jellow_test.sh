#!/usr/bin/env bash

echo "running language_reset_pref_about_jellow_test.sh"

#./language_reset_pref_about_jellow_test.sh

#	-	_05_LanguageSelectActivityTest
echo "running _05_LanguageSelectActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._05_LanguageSelectActivityTest

#	-	_06_LanguageSelectTalkbackActivityTest
echo "running _06_LanguageSelectTalkbackActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._06_LanguageSelectTalkbackActivityTest

#	- 	_07_ResetPreferencesActivityTest
echo "running _07_ResetPreferencesActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._07_ResetPreferencesActivityTest

#	-	_08_AboutJellowActivityTest
echo "running _08_AboutJellowActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._08_AboutJellowActivityTest
echo "completed.