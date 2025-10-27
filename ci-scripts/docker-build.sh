#!/bin/bash
set -e

IMAGE_NAME=${1:-flashcards}
IMAGE_TAG=${2:-latest}

echo "=== Building Docker image: ${IMAGE_NAME}:${IMAGE_TAG} ==="
docker login -u "$DOCKERHUB_USERNAME" -p "$DOCKERHUB_TOKEN"
docker build -t "$DOCKERHUB_USERNAME/flashcards:$IMAGE_TAG" .

echo "=== Pushing image to DockerHub ==="
docker push "$DOCKERHUB_USERNAME/flashcards:$IMAGE_TAG"