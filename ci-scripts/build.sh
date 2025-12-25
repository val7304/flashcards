#!/bin/bash
set -e
echo "=== Building app ==="
./mvnw clean package -DskipTests
