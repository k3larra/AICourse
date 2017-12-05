#!/usr/bin/env bash
MODEL_NAME=pendlaren
gcloud config set project skanependlaren
gcloud ml-engine predict \
--model $MODEL_NAME \
--version v1 \
--text-instances \
./hepp.txt
#--json-instances \
#./test.json