#!/bin/bash
set -e
IMAGE_NAME="flashcards-app"
echo "=== Building Docker image: $IMAGE_NAME ==="
docker build -t $IMAGE_NAME .

echo "Docker image '${IMAGE_NAME}' built successfully!"
docker images | grep ${IMAGE_NAME}