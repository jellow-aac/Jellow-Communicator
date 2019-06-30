#!/usr/bin/env bash

echo "running feedback_profile_test.sh"

#./feedback_profile_test.sh
#	-	_02_01_FeedbackActivityTest
echo "running _02_01_FeedbackActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._02_01_FeedbackActivityTest

#	-	_03_01_FeedbackTalkbackActivityTest
echo "running _03_01_FeedbackTalkbackActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._03_01_FeedbackTalkbackActivityTest

#	- 	_04_ProfileFormActivityTest
echo "running _04_ProfileFormActivityTest"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._04_ProfileFormActivityTest

#	- unit test
echo "running testDebugUnitTest"
./gradlew app:testDebugUnitTest
echo "completed."