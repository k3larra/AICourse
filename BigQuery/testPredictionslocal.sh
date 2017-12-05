#!/usr/bin/env bash
MODEL_NAME=pendlaren
MODEL_DIR=tmpen/try4_model/try2/1512474957
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--text-instances \
./hepp.txt
#--json-instances \
#./test1.json