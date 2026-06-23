package findata.data_consumer_service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Testcontainers
class ConsumerRecoveryIntegrationTest {

    // 1. Start a real Kafka broker via Testcontainers
    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.4.0")
    );

    @DynamicPropertySource
    static void registryProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    // 2. Use a Spy to verify the listener eventually processes the message
    @SpyBean
    private MessageConsumer messageConsumer;

    @Test
    void shouldPickUpWhereItLeftOffAfterReboot() throws Exception {
        String topic = "my-topic";
        String testMessage = "Test recovery payload";

        // 3. Simulate CRASH / SHUTDOWN: Stop the Kafka listener container
        registry.stop();
        await().until(() -> !registry.isRunning());

        // 4. Produce a message while the microservice listener is "offline"
        kafkaTemplate.send(new ProducerRecord<>(topic, "key", testMessage)).get();

        // 5. Simulate REBOOT: Restart the Kafka listener container
        registry.start();
        await().until(registry::isRunning);

        // 6. ASSERT: Verify the service woke up and successfully read the bookmark
        await()
            .atMost(10, TimeUnit.SECONDS)
            .pollInterval(Duration.ofMillis(200))
            .untilAsserted(() -> {
                verify(messageConsumer).listen(any(), any());
            });
    }
}