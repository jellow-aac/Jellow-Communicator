#!/usr/bin/env bash

set -e

if [ "$RUN_SCRIPT" == "language_pref" ]; then
    ./home/travis/build/jellow-aac/Jellow-Communicator/app/src/debug/language_reset_pref_about_jellow_test.sh
elif [ "$RUN_SCRIPT" == "feedback_profile" ]; then
    ./home/travis/build/jellow-aac/Jellow-Communicator/app/src/debug/feedback_profile_test.sh
elif [ "$RUN_SCRIPT" == "main_class_one" ]; then
    ./home/travis/build/jellow-aac/Jellow-Communicator/app/src/debug/main_class_test_one.sh
elif [ "$RUN_SCRIPT" == "main_class_two" ]; then
    ./home/travis/build/jellow-aac/Jellow-Communicator/app/src/debug/main_class_test_two.sh
else
    echo "That module doesn't exist, now does it? ;)"
    exit 1
fi