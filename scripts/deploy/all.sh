#!/bin/bash

set -e

./scripts/deploy/api.sh
./scripts/deploy/web.sh
./scripts/deploy/proxy.sh

kubectl get services