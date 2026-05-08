#!/usr/bin/env bash
set -euo pipefail

CRANE_VERSION="v0.21.5"

echo "[INFO] Installing crane ${CRANE_VERSION}..."

curl -fsSL \
  "https://github.com/google/go-containerregistry/releases/download/${CRANE_VERSION}/go-containerregistry_Linux_x86_64.tar.gz" \
  | tar -xz crane

sudo mv crane /usr/local/bin/

echo "[INFO] crane installed: "
crane version