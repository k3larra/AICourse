#!/usr/bin/env bash
gcloud config set project skanependlaren
MODEL_BINARIES=gs://modelscommute/version/1512941114/
MODEL_NAME=pendlaren
gcloud ml-engine versions create v8 \
--model $MODEL_NAME \
--origin $MODEL_BINARIES \
--runtime-version 1.2