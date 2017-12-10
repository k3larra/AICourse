#!/usr/bin/env bash
gcloud config set project skanependlaren
NUMBER=1512944065
VERSION=v10
gsutil rm gs://modelscommute/version/**
gsutil cp -r ml_pendlaren1/v01/$NUMBER gs://modelscommute/version
MODEL_BINARIES=gs://modelscommute/version/$NUMBER/
MODEL_NAME=pendlaren
gcloud ml-engine versions create $VERSION \
--model $MODEL_NAME \
--origin $MODEL_BINARIES \
--runtime-version 1.2