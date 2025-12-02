#!/bin/bash
set -e
echo "=== Running tests ==="
./mvnw -B -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod} test
