#!/usr/bin/env bash
set -e

curl "http://localhost:8085/stocks/AAPL"
echo ""
curl "http://localhost:8085/stocks/MSFT"
echo ""
curl "http://localhost:8085/stocks/NVDA"
