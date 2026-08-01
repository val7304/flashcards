#!/bin/sh
set -eu

IMAGE_NAME=${1:?Image name missing}
SOURCE_TAG=${2:?Source tag missing}
SHA_TAG=${3:?SHA tag missing}
RELEASE_TAG=${4:?Release tag missing}

echo "====================================="
echo "Push Docker image"
echo "Image : ${IMAGE_NAME}:${SOURCE_TAG}"
echo "====================================="

echo "$DOCKERHUB_TOKEN" | docker login \
  -u "$DOCKERHUB_USERNAME" \
  --password-stdin

docker tag "${IMAGE_NAME}:${SOURCE_TAG}" \
  "$DOCKERHUB_USERNAME/${IMAGE_NAME}:${SHA_TAG}"

docker tag "${IMAGE_NAME}:${SOURCE_TAG}" \
  "$DOCKERHUB_USERNAME/${IMAGE_NAME}:${RELEASE_TAG}"

docker tag "${IMAGE_NAME}:${SOURCE_TAG}" \
  "$DOCKERHUB_USERNAME/${IMAGE_NAME}:latest"

docker push "$DOCKERHUB_USERNAME/${IMAGE_NAME}:${SHA_TAG}"
docker push "$DOCKERHUB_USERNAME/${IMAGE_NAME}:${RELEASE_TAG}"
docker push "$DOCKERHUB_USERNAME/${IMAGE_NAME}:latest"

echo "Docker image successfully pushed"
