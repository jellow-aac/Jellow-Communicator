#!/usr/bin/env bash

#./test_script_three.sh
# Running test for following classes:
# _09_MainActivityUITest
# _10_01LevelTwoActivityUITest

echo "running test_script_three.sh"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._09_MainActivityUITest,com.dsource.idc.jellowintl._10_01LevelTwoActivityUITest
echo "completed."