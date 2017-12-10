#!/usr/bin/env bash
gsutil rm gs://modelscommute/version/**
gsutil cp -r output_local2/export/Servo/ gs://modelscommute/version