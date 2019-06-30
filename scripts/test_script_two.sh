#!/usr/bin/env bash

#./test_script_two.sh
# Running test for following classes:
# _05_LanguageSelectActivityTest
# _06_LanguageSelectTalkbackActivityTest
# _07_ResetPreferencesActivityTest
# _08_AboutJellowActivityTest

echo "running test_script_two.sh"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._05_LanguageSelectActivityTest,com.dsource.idc.jellowintl._06_LanguageSelectTalkbackActivityTest,com.dsource.idc.jellowintl._07_ResetPreferencesActivityTest,com.dsource.idc.jellowintl._08_AboutJellowActivityTest
echo "completed."