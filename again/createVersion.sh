#!/usr/bin/env bash
gcloud config set project skanependlaren
MODEL_BINARIES=gs://modelscommute/version/Servo/1512841224/
MODEL_NAME=pendlaren
gcloud ml-engine versions create v6 \
--model $MODEL_NAME \
--origin $MODEL_BINARIES \
--runtime-version 1.2