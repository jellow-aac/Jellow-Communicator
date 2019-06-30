#!/usr/bin/env bash

echo "running main_class_test_two.sh"
#./main_class_test_two.sh

echo "running _11_LevelThreeActivityUITest"
#	-	_11_LevelThreeActivityUITest
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._11_LevelThreeActivityUITest

echo "running _12_SequenceActivityUITest"
#	-	_12_SequenceActivityUITest
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._12_SequenceActivityUITest

echo "running jacocoUnifiedUnitInstrumentationTestReport"
./gradlew jacocoUnifiedUnitInstrumentationTestReport
echo "completed.