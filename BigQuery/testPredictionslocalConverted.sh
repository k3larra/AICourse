#!/usr/bin/env bash
MODEL_DIR=tmp/iris4_model/try1/1512495980
gcloud ml-engine local predict \
--model-dir $MODEL_DIR \
--text-instances \
./conv.txt
#--json-instances \
#./conv1.json


#--text-instances \
#./conv.txt
#--json-instances \
#./test1.json