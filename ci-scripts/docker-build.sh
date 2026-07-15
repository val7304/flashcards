#!/bin/sh
set -eu

IMAGE_NAME=${1:-flashcards}
IMAGE_TAG=${2:-latest}
DOCKERFILE=${3:-Dockerfile}

echo "====================================="
echo "Building Docker image"
echo "Image      : ${IMAGE_NAME}:${IMAGE_TAG}"
echo "Dockerfile : ${DOCKERFILE}"
echo "====================================="

docker build \
  -f "${DOCKERFILE}" \
  -t "${IMAGE_NAME}:${IMAGE_TAG}" \
  .

echo "Docker image successfully built"