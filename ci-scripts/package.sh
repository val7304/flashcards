#!/bin/sh
set -eu

PROFILE=${1:-}
RESOLVE=${2:-false}

echo "=== Running package ==="

if [ "$RESOLVE" = "true" ]; then
    ./mvnw -B dependency:resolve dependency:resolve-plugins
fi

if [ -n "$PROFILE" ]; then
    ./mvnw -B package jacoco:report -DskipTests -P"$PROFILE"
else
    ./mvnw -B package jacoco:report -DskipTests
fi