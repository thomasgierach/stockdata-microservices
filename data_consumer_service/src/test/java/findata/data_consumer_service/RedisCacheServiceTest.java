package com.findata.consumer;

import findata.common.model.StockEvent;
import com.findata.consumer.repository.StockRepository;
import com.findata.consumer.service.RedisCacheService;
import com.findata.consumer.service.StockConsumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

@SpringBootTest(
    classes = DataConsumerServiceApplication.class,
    properties = {
        "spring.data.redis.host=localhost",
        "spring.data.redis.port=6379",
        "app.cache.ttl-seconds=60"
    }
)
@EnableAutoConfiguration(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class,
    KafkaAutoConfiguration.class
})
class RedisCacheServiceTest {

    @MockBean
    private StockRepository stockRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private StockConsumer stockConsumer;

    @Test
    void verifyRedis() {
        String symbol = "AAPL";
        LocalDate date = LocalDate.parse("2026-03-16");

        StockEvent event = new StockEvent(
            symbol,
            new BigDecimal("400.00"),
            new BigDecimal("401.00"),
            new BigDecimal("402.00"),
            new BigDecimal("399.00"),
            date,
            10000000,
            "ALPHAVANTAGE"
        );

        when(stockRepository.findBySymbolAndTradingDate(symbol, date))
            .thenReturn(Optional.empty());

        stockConsumer.consume(event);

        StockEvent redisEvent = redisCacheService.get(symbol);

        assertEquals(symbol, redisEvent.getSymbol());
        assertEquals(event.getOpen(), redisEvent.getOpen());
        assertEquals(event.getClose(), redisEvent.getClose());
        assertEquals(event.getHigh(), redisEvent.getHigh());
        assertEquals(event.getLow(), redisEvent.getLow());
        assertEquals(date, redisEvent.getDate());
        assertEquals(10000000, redisEvent.getVolume());
        assertEquals("ALPHAVANTAGE", redisEvent.getSource());
    }
}