#!/usr/bin/env bash
MODEL_NAME=census
MODEL_DIR=output_local2/export/Servo/1512841224
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--json-instances ./test.json
#--text-instances ./hepp.txt
