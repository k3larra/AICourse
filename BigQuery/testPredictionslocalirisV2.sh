#!/usr/bin/env bash
MODEL_DIR=tmp/iris_new_model/try8/1512568717
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--text-instances ./iris.txt
#--json-instances ./iris.json