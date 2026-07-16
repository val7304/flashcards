#!/bin/sh
set -eu

PROFILE=${1:-}

echo "=== Running Maven tests ==="

if [ -n "$PROFILE" ]; then
    ./mvnw -B clean verify -P"$PROFILE"
else
    ./mvnw -B clean verify
fi
