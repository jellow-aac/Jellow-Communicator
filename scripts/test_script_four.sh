#!/usr/bin/env bash


#./test_script_four.sh
# Running test for following classes:
# _11_LevelThreeActivityUITest
# _12_SequenceActivityUITest
# jacocoUnifiedUnitInstrumentationTestReport

echo "running test_script_four.sh"
#	-	_11_LevelThreeActivityUITest
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._11_LevelThreeActivityUITest,com.dsource.idc.jellowintl._12_SequenceActivityUITest
echo "completed."

echo "running jacocoUnifiedUnitInstrumentationTestReport"
./gradlew jacocoUnifiedUnitInstrumentationTestReport
echo "completed."