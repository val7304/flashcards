#!/usr/bin/env bash
set -euo pipefail

TRIVY_VERSION="0.72.0" 

echo "[INFO] Installing Trivy dependencies..."
sudo apt-get update
sudo apt-get install -y wget gnupg

echo "[INFO] Adding Trivy official repository..."
wget -qO - https://get.trivy.dev/deb/public.key | \
gpg --dearmor | sudo tee /usr/share/keyrings/trivy.gpg > /dev/null

echo \
"deb [signed-by=/usr/share/keyrings/trivy.gpg] https://get.trivy.dev/deb generic main" | \
sudo tee /etc/apt/sources.list.d/trivy.list

sudo apt-get update

echo "[INFO] Requested Trivy version: ${TRIVY_VERSION}"

if apt-cache madison trivy | grep -q "$TRIVY_VERSION"; then
  echo "[INFO] Installing pinned Trivy version..."
  sudo apt-get install -y trivy=$TRIVY_VERSION
else
  echo "[WARNING] Version not found → fallback to latest"
  sudo apt-get install -y trivy
fi

echo "[INFO] Installed Trivy version:"
trivy --version