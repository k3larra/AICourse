#!/usr/bin/env bash
MODEL_DIR=tmp/iris_model/try9/1512941114
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--json-instances ./iris.json