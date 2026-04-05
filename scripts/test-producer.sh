#!/usr/bin/env bash
set -e
./mvnw -pl common,stockdata_producer_service test
