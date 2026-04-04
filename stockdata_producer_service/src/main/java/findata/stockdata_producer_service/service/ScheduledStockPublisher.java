package findata.stockdata_producer_service.service;

import findata.common.model.StockEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

@Service
public class ScheduledStockPublisher {
    private static final Logger log = LoggerFactory.getLogger(ScheduledStockPublisher.class);
    private final AlphaVantageClient alphaVantageClient;
    private final KafkaTemplate<String, StockEvent> kafkaTemplate;
    private final String topicName;
    private final List<String> symbols;

    public ScheduledStockPublisher(
            AlphaVantageClient alphaVantageClient,
            KafkaTemplate<String, StockEvent> kafkaTemplate,
            @Value("${app.kafka.topic}") String topicName,
            @Value("${app.symbols}") String symbolsCsv) {
        this.alphaVantageClient = alphaVantageClient;
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
        this.symbols = Arrays.stream(symbolsCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(String::toUpperCase)
                .toList();
    }
    /* We will put the stock event inside of Kafka Producer */
    @Scheduled(fixedRateString = "${app.scheduler.fixed-rate-ms}")
    public void publishStocks() {
        if (symbols.isEmpty()) {
            log.warn("No stock symbols configured to publish.");
            
            return;
        }
        for (String symbol : symbols) {
            try {
                StockEvent event = alphaVantageClient.fetchQuote(symbol);
                toProducer(event);
            } catch (Exception ex) {
                System.err.println("Failed to publish symbol " + symbol + ": " + ex.getMessage());
            }
        }
    }
    public void toProducer(StockEvent event) {
            log.info("Publishing stock event for {} to Kafka topic {}", event.getSymbol(), topicName);
            kafkaTemplate.send(topicName, event.getSymbol(), event);
    }
}
