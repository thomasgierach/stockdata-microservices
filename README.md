# Stock Data Microservices System

Spring Boot-based microservices system for stock price retrieval using an **event-driven architecture**.

## Tech Stack

- Kafka (event streaming)
- PostgreSQL (persistence)
- Redis (caching)
- Docker (orchestration)

---

## Data Flow

1. Query service checks Redis cache  
2. If cache miss → query PostgreSQL  
3. If not found → fetch from external API  
4. Persist to PostgreSQL  
5. Cache result in Redis for future requests  

---

## Demo Mode

By default, the application runs in **demo mode**:

- Uses mock stock data (AAPL, MSFT, NVDA)
- Data is published to Kafka via a demo producer
- No external API key required
- Full pipeline (Kafka → consumer → DB → cache) is exercised

---

## 🌐 Live Mode (Optional)

To use real data:

1. Add your API key to `.env`
2. Set:

```bash
APP_DATA_MODE=live
```

---

## Setup

```bash
cp .env.example .env
```

(Optional) Add your Alpha Vantage API key.

---

## Quick Start

### Run tests
```bash
./scripts/test-all.sh
```

### Start demo stack
```bash
./scripts/start-demo.sh
```

### Query data
```bash
./scripts/smoke-test.sh
curl "http://localhost:8085/stocks/AAPL"
```

### Stop services
```bash
./scripts/stop-demo.sh
```

---

## System Architecture

```
Producer (demo/live)
        ↓
      Kafka
        ↓
     Consumer
        ↓
    PostgreSQL
        ↓
       Redis
        ↓
   Query Service
```
