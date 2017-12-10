#!/usr/bin/env bash
MODEL_DIR=ml_pendlaren1/v01/1512944065
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--json-instances ./testpendlare.json