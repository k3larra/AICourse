#!/usr/bin/env bash
gsutil rm gs://modelscommute/version/**
gsutil cp -r tmp/iris_model/try9/1512941114 gs://modelscommute/version