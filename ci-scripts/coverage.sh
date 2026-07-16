#!/bin/sh
set -eu

PROFILE=${1:-coverage-main}

echo "=== Coverage (${PROFILE}) ==="
./mvnw -B jacoco:report -P"$PROFILE"
