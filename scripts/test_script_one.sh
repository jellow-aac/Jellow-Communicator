#!/usr/bin/env bash

#./test_script_one.sh
# Running test for following classes:
# _02_01_FeedbackActivityTest
# _03_01_FeedbackTalkbackActivityTest
# _04_ProfileFormActivityTest
# testDebugUnitTest

echo "running test_script_one.sh"
./gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._02_01_FeedbackActivityTest,com.dsource.idc.jellowintl._03_01_FeedbackTalkbackActivityTest,com.dsource.idc.jellowintl._04_ProfileFormActivityTest app:testDebugUnitTest
#	- unit test
#echo "running testDebugUnitTest"
#./gradlew app:testDebugUnitTest
echo "completed."