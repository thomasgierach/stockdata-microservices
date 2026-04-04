package findata.stock_query_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import findata.common.model.StockEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Map;

@Service
public class AlphaVantageClient {

    private final RestClient restClient;
    private final String apiKey;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String FUNCTION = "TIME_SERIES_DAILY";

    public AlphaVantageClient(
            RestClient.Builder builder,
            @Value("${alphavantage.base-url}") String baseUrl,
            @Value("${alphavantage.api-key}") String apiKey) {
        this.restClient = builder
                .baseUrl(baseUrl)
                .build();
        this.apiKey = apiKey;
    }

    public StockEvent fetchQuote(String symbol) {
        final String json = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/query")
                        .queryParam("function", FUNCTION)
                        .queryParam("symbol", symbol)
                        .queryParam("apikey", apiKey)
                        .build())
                .retrieve()
                .body(String.class);

        try {
            Map<String, Object> root = objectMapper.readValue(
                    json, new TypeReference<Map<String, Object>>() {});

            if (root.containsKey("Note")) {
                throw new IllegalStateException("AlphaVantage rate limit hit: " + root.get("Note"));
            }

            if (root.containsKey("Information")) {
                throw new IllegalStateException("AlphaVantage returned info/error: " + root.get("Information"));
            }

            @SuppressWarnings("unchecked")
            Map<String, Map<String, String>> timeSeries =
                    (Map<String, Map<String, String>>) root.get("Time Series (Daily)");

            if (timeSeries == null || timeSeries.isEmpty()) {
                throw new IllegalStateException("Missing 'Time Series (Daily)' in API response for symbol: " + symbol);
            }

            String latestDateString = timeSeries.keySet().stream()
                    .max(Comparator.naturalOrder())
                    .orElseThrow(() -> new IllegalStateException("No trading dates found for symbol: " + symbol));

            Map<String, String> latestRow = timeSeries.get(latestDateString);
            if (latestRow == null) {
                throw new IllegalStateException("No data row found for date: " + latestDateString);
            }

            LocalDate tradingDate = LocalDate.parse(latestDateString);

            BigDecimal open = new BigDecimal(latestRow.get("1. open"));
            BigDecimal high = new BigDecimal(latestRow.get("2. high"));
            BigDecimal low = new BigDecimal(latestRow.get("3. low"));
            BigDecimal close = new BigDecimal(latestRow.get("4. close"));
            Integer volume = Integer.parseInt(latestRow.get("5. volume"));

            return new StockEvent(
                    symbol.toUpperCase(),
                    open,
                    close,
                    high,
                    low,
                    tradingDate,
                    volume,
                    "ALPHAVANTAGE"
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch or parse AlphaVantage response for symbol: " + symbol, e);
        }
    }
}