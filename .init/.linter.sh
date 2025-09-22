#!/bin/bash
cd /home/kavia/workspace/code-generation/personal-notes-organizer-89204-89232/kotlin_frontend
./gradlew lint
LINT_EXIT_CODE=$?
if [ $LINT_EXIT_CODE -ne 0 ]; then
   exit 1
fi

