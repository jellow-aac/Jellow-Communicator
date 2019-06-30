#!/usr/bin/env bash

#./main_class_test_one.sh
#	-	_09_MainActivityUITest
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._09_MainActivityUITest
#	-	_10_01LevelTwoActivityUITest
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._10_01LevelTwoActivityUITest