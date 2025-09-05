#!/bin/bash

echo "=== Starting Kind Cluster ==="

kind create cluster --name microservices-cloud-project --config kind-config.yaml

echo "=== Loading Docker Images into Kind Cluster ==="

chmod +x ./kind-load.sh
./kind-load.sh

echo "=== Kind Cluster Started ==="

