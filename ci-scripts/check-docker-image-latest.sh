#!/usr/bin/env bash
set -euo pipefail

IMAGE="gcr.io/distroless/java17-debian12:latest"
STATE_FILE="ci-scripts/.distroless-java17-debian12.digest"

docker pull "$IMAGE" >/dev/null

CURRENT_DIGEST=$(docker image inspect "$IMAGE" --format '{{index .RepoDigests 0}}' | sed 's/.*@//')

if [[ -f "$STATE_FILE" ]]; then
  PREVIOUS_DIGEST=$(cat "$STATE_FILE")
else
  PREVIOUS_DIGEST=""
fi

if [[ "$CURRENT_DIGEST" == "$PREVIOUS_DIGEST" ]]; then
  echo "No update: $IMAGE is unchanged."
  echo "Digest: $CURRENT_DIGEST"
  echo "digest_changed=false" >> "$GITHUB_OUTPUT"
else
  echo "Update detected: $IMAGE has changed."
  echo "Old: $PREVIOUS_DIGEST"
  echo "New: $CURRENT_DIGEST"

  echo "$CURRENT_DIGEST" > "$STATE_FILE"

  echo "digest_changed=true" >> "$GITHUB_OUTPUT"
fi
