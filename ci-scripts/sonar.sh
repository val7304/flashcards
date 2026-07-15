#!/bin/sh
set -eu

./mvnw -B sonar:sonar \
 -Dsonar.host.url="$SONAR_HOST_URL" \
 -Dsonar.login="$SONAR_TOKEN" \
 -Dsonar.organization="$SONAR_ORGANIZATION" \
 -Dsonar.projectKey="$SONAR_PROJECT_KEY" \
 -Dsonar.qualitygate.wait=true
