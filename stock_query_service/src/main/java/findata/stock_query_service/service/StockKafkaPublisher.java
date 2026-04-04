package findata.stock_query_service.service;

import findata.common.model.StockEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StockKafkaPublisher {
    private static final Logger log = LoggerFactory.getLogger(StockKafkaPublisher.class);
    private final KafkaTemplate<String, StockEvent> kafkaTemplate;
    private final String topicName;

    public StockKafkaPublisher(
            KafkaTemplate<String, StockEvent> kafkaTemplate,
            @Value("${app.kafka.topic}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void publish(StockEvent event) {
        log.info("Publishing stock event for {} to Kafka topic {}", event.getSymbol(), topicName);
        kafkaTemplate.send(topicName, event.getSymbol(), event);
    }
}