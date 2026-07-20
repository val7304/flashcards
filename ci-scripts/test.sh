#!/bin/sh
set -eu

PROFILE=${1:-}

echo "=== Running Maven tests ==="

if [ -n "$PROFILE" ]; then
    exec ./mvnw -B clean verify jacoco:report -P"$PROFILE"
else
    exec ./mvnw -B clean verify jacoco:report
fi