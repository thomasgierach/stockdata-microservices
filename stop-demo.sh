#!/usr/bin/env bash
set -e

docker compose -f docker-compose.infra.yml -f docker-compose.app.yml down