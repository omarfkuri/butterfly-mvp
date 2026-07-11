#!/bin/bash

set -e

PROJECT_ID=omarfkuri
REGION=us-central1
REPOSITORY=docker-repo

gcloud auth configure-docker ${REGION}-docker.pkg.dev

docker build \
  -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-web:latest \
  ./front/web

docker push \
  ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-web:latest

kubectl apply -f k8s/web-deployment.yml
kubectl apply -f k8s/web-service.yml

kubectl rollout restart deployment/social-web