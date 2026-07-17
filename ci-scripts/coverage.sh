#!/bin/sh
set -eu

PROFILE=${1:-coverage-main}

echo "=== Coverage (${PROFILE}) ==="
./mvnw -B clean verify jacoco:report -P"$PROFILE"