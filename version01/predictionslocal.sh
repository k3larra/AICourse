#!/usr/bin/env bash
MODEL_DIR=ml_pendlaren/v01/1512941960
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--json-instances ./iris.json