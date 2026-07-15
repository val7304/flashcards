#!/bin/sh
# Executed inside the official Trivy Alpine image (no bash available)
set -eu

EXIT_CODE=${1:-1}

trivy fs . \
    --severity HIGH,CRITICAL \
    --format table \
    --offline-scan \
    --scanners vuln \
    --exit-code "${EXIT_CODE}" \
    --no-progress
