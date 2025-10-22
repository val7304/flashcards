#!/bin/bash
set -e

IMAGE_NAME=${1:-flashcards}
IMAGE_TAG=${2:-latest}

echo "=== Building Docker image: ${IMAGE_NAME}:${IMAGE_TAG} ==="
docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
