#!/bin/sh
# Executed inside the official Trivy Alpine image (no bash available)

set -eu

IMAGE=${1:?Image archive required}
EXIT_CODE=${2:-1}
IGNOREFILE=${3:-}

if [ -n "$IGNOREFILE" ]; then
    trivy image \
        --input "$IMAGE" \
        --severity HIGH,CRITICAL \
        --exit-code "$EXIT_CODE" \
        --ignore-unfixed \
        --ignorefile "$IGNOREFILE" \
        --timeout 10m \
        --cache-dir ~/.cache/trivy \
        --format table \
        --no-progress
else
    trivy image \
        --input "$IMAGE" \
        --severity HIGH,CRITICAL \
        --exit-code "$EXIT_CODE" \
        --ignore-unfixed \
        --timeout 10m \
        --cache-dir ~/.cache/trivy \
        --format table \
        --no-progress
fi