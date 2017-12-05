#!/usr/bin/env bash
PROJECT_ID=$(gcloud config list project --format "value(core.project)")
BUCKET_NAME=${PROJECT_ID}
echo $BUCKET_NAME
STAGING_BUCKET=gs://$BUCKET_NAME
VERSION=v2
MODEL=pendlaren
ORIGIN=tmpen/try4_model/try2/1511968880
gcloud ml-engine versions create \
$VERSION \
--model=$MODEL \
--origin=$ORIGIN \
--staging-bucket=$STAGING_BUCKET