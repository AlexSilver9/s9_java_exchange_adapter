#!/bin/sh
docker buildx build --platform linux/amd64 -t europe-central2-docker.pkg.dev/silver9/docker-registry/s9_java_exchange_adapter:0.0.1-SNAPSHOT .