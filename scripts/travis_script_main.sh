#!/usr/bin/env bash

set -e

if [[ "$RUN_SCRIPT" == "test_script_one.sh" ]]; then
    ./scripts/test_script_one.sh
elif [[ "$RUN_SCRIPT" == "test_script_two.sh" ]]; then
    ./scripts/test_script_two.sh
elif [[ "$RUN_SCRIPT" == "test_script_three.sh" ]]; then
    ./scripts/test_script_three.sh
elif [[ "$RUN_SCRIPT" == "test_script_four.sh" ]]; then
    ./scripts/test_script_four.sh
else
    echo "That module doesn't exist, now does it? ;)"
    exit 1
fi