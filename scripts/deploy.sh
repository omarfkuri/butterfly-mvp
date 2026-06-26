#!/bin/bash

set -e

PROJECT_ID=omarfkuri
REGION=us-central1
REPOSITORY=docker-repo

gcloud auth configure-docker ${REGION}-docker.pkg.dev

docker build \
  -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-api:latest \
  ./api

docker build \
  -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-web:latest \
  ./front/web

docker build \
  -t ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-proxy:latest \
  ./nginx

docker push \
  ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-api:latest

docker push \
  ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-web:latest

docker push \
  ${REGION}-docker.pkg.dev/${PROJECT_ID}/${REPOSITORY}/social-proxy:latest

kubectl apply -f k8s/api-deployment.yml
kubectl apply -f k8s/api-service.yml

kubectl apply -f k8s/web-deployment.yml
kubectl apply -f k8s/web-service.yml

kubectl apply -f k8s/proxy-deployment.yml
kubectl apply -f k8s/proxy-service.yml

kubectl rollout restart deployment/social-api
kubectl rollout restart deployment/social-web
kubectl rollout restart deployment/social-proxy

kubectl get services