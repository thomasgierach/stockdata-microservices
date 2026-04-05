Spring Boot project involving a REST API for stock prices built in an event-based, microservice architecture which employs Docker, Kafka, Redis, and PostgreSQL. We first check for a cache in Redis, and if the data is not present, we fetch it from PostgreSQL. If the data is not in PostgreSQL, we fetch it from an external API, store it in PostgreSQL, and then cache it in Redis for future requests.

## Setup

1. Copy environment file:
   cp .env.example .env

2. (Optional) Add your own Alpha Vantage API key

3. Run:
   docker compose up --build

## Quick Start

Run tests:

./scripts/test-all.sh

Start demo stack:

./scripts/start-demo.sh

Run smoke test:

./scripts/smoke-test.sh

Stop services:

./scripts/stop-demo.sh