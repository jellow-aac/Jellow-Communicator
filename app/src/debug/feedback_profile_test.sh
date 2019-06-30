#./feedback_profile_test.sh
#	-	_02_01_FeedbackActivityTest
gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._02_01_FeedbackActivityTest
#	-	_03_01_FeedbackTalkbackActivityTest
gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._03_01_FeedbackTalkbackActivityTest
#	- 	_04_ProfileFormActivityTest
gradlew app:connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.dsource.idc.jellowintl._04_ProfileFormActivityTest
#
#	- unit test
gradlew app:testDebugUnitTest