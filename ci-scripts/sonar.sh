#!/bin/sh
set -eu

./mvnw -B sonar:sonar \
 -Dsonar.host.url="$SONAR_HOST_URL" \
 -Dsonar.login="$SONAR_TOKEN" \
 -Dsonar.qualitygate.wait=true
