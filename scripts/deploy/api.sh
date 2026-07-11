#!/bin/bash

set -e

PROJECT_ID=omarfkuri
REGION=us-central1
REPOSITORY=docker-repo

gcloud auth configure-docker ${REGION}-docker.pkg.dev

docker build \
  -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-api:latest \
  ./api

docker push \
  ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-api:latest

kubectl apply -f k8s/api-deployment.yml
kubectl apply -f k8s/api-service.yml

kubectl rollout restart deployment/social-api
