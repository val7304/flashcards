#!/usr/bin/env bash
set -euo pipefail

IMAGE="gcr.io/distroless/java17-debian12:latest"
STATE_FILE="ci-scripts/.distroless-java17-debian12.digest"

docker pull "$IMAGE" >/dev/null

CURRENT_DIGEST=$(docker image inspect "$IMAGE" --format '{{index .RepoDigests 0}}' | sed 's/.*@//')

if [[ -f "$STATE_FILE" ]]; then
  PREVIOUS_DIGEST=$(cat "$STATE_FILE")

  if [[ "$CURRENT_DIGEST" == "$PREVIOUS_DIGEST" ]]; then
    echo "No update: $IMAGE is unchanged."
    echo "Digest: $CURRENT_DIGEST"
    exit 0
  fi

  echo "Update detected!"
  echo "Previous: $PREVIOUS_DIGEST"
  echo "Current:  $CURRENT_DIGEST"
else
  echo "No previous digest recorded."
  echo "Current digest: $CURRENT_DIGEST"
fi

echo "$CURRENT_DIGEST" > "$STATE_FILE"
