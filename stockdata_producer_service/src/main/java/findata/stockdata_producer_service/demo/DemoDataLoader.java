package findata.stockdata_producer_service.demo;

import findata.common.model.StockEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


@Component
@Profile("demo")
public class DemoDataLoader {

    private final KafkaTemplate<String, StockEvent> kafkaTemplate;
    
    private final String topic;
    private final LocalDate now = LocalDate.now();
    private static final Logger log = LoggerFactory.getLogger(DemoDataLoader.class);

    DemoDataLoader(KafkaTemplate<String, StockEvent> kafkaTemplate, @Value("${app.kafka.topic}") String topic) {
        log.info("DemoDataLoader created");
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }
    @PostConstruct
    public void load() {
        log.info("Loading demo stock data into Kafka topic 'stock-events'...");
        kafkaTemplate.send(topic, 
                           new StockEvent(
                                          "AAPL", 
                                          new BigDecimal("221.10"), 
                                          new BigDecimal("223.90"), 
                                          new BigDecimal("224.50"), 
                                          new BigDecimal("219.80"), 
                                          now, 
                                          51234000, 
                                          "demo"));
        kafkaTemplate.send(topic, 
                            new StockEvent("MSFT", 
                                            new BigDecimal(412.30), 
                                            new BigDecimal(413.75), 
                                            new BigDecimal(415.00), 
                                            new BigDecimal(409.60), 
                                            now, 22112000, 
                                            "demo"));
        kafkaTemplate.send(topic, 
                            new StockEvent(
                                            "NVDA", 
                                            new BigDecimal("118.20"), 
                                            new BigDecimal("120.85"),
                                            new BigDecimal("121.40"), 
                                            new BigDecimal("117.90"), 
                                            now, 
                                            68451000, 
                                            "demo"));
    }
}