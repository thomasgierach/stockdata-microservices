#!/usr/bin/env bash
set -e
./mvnw -pl common,data_consumer_service test
