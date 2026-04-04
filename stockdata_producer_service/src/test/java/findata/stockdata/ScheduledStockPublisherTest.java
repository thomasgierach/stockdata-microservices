
package findata.stockdata_producer_service.service;

import findata.common.model.StockEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.math.BigDecimal;
import java.time.LocalDate;


@ExtendWith(MockitoExtension.class)
class ScheduledStockPublisherTest {

    @Mock
    private AlphaVantageClient alphaVantageClient;

    @Mock
    private KafkaTemplate<String, StockEvent> kafkaTemplate;

    private ScheduledStockPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new ScheduledStockPublisher(
                alphaVantageClient,
                kafkaTemplate,
                "stock-data",
                "AAPL"
        );
    }

    @Test
    void shouldSendEventToKafka() {
        StockEvent event = new StockEvent(
            "AAPL",
            new BigDecimal("400.00"),
            new BigDecimal("401.00"),
            new BigDecimal("402.00"),
            new BigDecimal("399.00"),
            LocalDate.parse("2026-03-16"),
            10000000,
            "ALPHAVANTAGE"
        );
        
        
        
        when(alphaVantageClient.fetchQuote("AAPL")).thenReturn(event);

        publisher.publishStocks();

        verify(alphaVantageClient).fetchQuote("AAPL");
        verify(kafkaTemplate).send(eq("stock-data"), eq("AAPL"), eq(event));
        
        
    }
}