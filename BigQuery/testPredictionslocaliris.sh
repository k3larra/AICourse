#!/usr/bin/env bash
MODEL_DIR=tmp/iris_model/try1/1512555211
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--json-instances ./iris.json
#--text-instances ./iris.txt
