#!/usr/bin/env bash
set -euo pipefail

IMAGE="gcr.io/distroless/java17-debian12:latest"
STATE_FILE="ci-scripts/.distroless-java17-debian12.digest"

echo "[INFO] Checking crane availability..."

if ! command -v crane >/dev/null 2>&1; then
  echo "[WARNING] crane not found → skipping digest check"
  echo "digest_changed=false" >> "$GITHUB_OUTPUT"
  exit 0
fi

echo "[INFO] Fetching current digest..."
CURRENT_DIGEST=$(crane digest "$IMAGE")

if [[ -f "$STATE_FILE" ]]; then
  PREVIOUS_DIGEST=$(cat "$STATE_FILE")
else
  PREVIOUS_DIGEST=""
fi

echo "[INFO] Current:  $CURRENT_DIGEST"
echo "[INFO] Previous: $PREVIOUS_DIGEST"

if [[ "$CURRENT_DIGEST" == "$PREVIOUS_DIGEST" ]]; then
  echo "[INFO] No update detected"
  echo "digest_changed=false" >> "$GITHUB_OUTPUT"
else
  echo "[INFO] Update detected"
  echo "digest_changed=true" >> "$GITHUB_OUTPUT"
  echo "digest_value=$CURRENT_DIGEST" >> "$GITHUB_OUTPUT"
fi
