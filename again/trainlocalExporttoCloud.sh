#!/usr/bin/env bash
gcloud config set project skanependlaren
TRAIN_DATA=$(pwd)/data/adult.data.csv
EVAL_DATA=$(pwd)/data/adult.test.csv
MODEL_DIR=gs://modelscommute/version2
rm -rf $MODEL_DIR/*
gcloud ml-engine local train \
    --module-name trainer.task \
    --package-path trainer/ \
    -- \
    --train-files $TRAIN_DATA \
    --eval-files $EVAL_DATA \
    --train-steps 10 \
    --job-dir $MODEL_DIR \
    --eval-steps 1
