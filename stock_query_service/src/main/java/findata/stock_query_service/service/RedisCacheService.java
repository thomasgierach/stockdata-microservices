package findata.stock_query_service.service;

import findata.common.model.StockEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;

@Service
public class RedisCacheService {
    private static final Logger log = LoggerFactory.getLogger(RedisCacheService.class);
    private final RedisTemplate<String, StockEvent> redisTemplate;
    private final long ttlSeconds;

    public RedisCacheService(
            RedisTemplate<String, StockEvent> redisTemplate,
            @Value("${app.cache.ttl-seconds}") long ttlSeconds) {
        this.redisTemplate = redisTemplate;
        this.ttlSeconds = ttlSeconds;
    }

    public StockEvent get(String symbol) {
        log.info("Cache GET for {}", symbol);
        return redisTemplate.opsForValue().get(buildKey(symbol));
    }

    public void put(StockEvent event) {
        log.info("Cache PUT for {}", event.getSymbol());
        String key = buildKey(event.getSymbol());
        redisTemplate.opsForValue().set(key, event, Duration.ofSeconds(ttlSeconds));
    }

    public String buildKey(String symbol) {
        return "stock:" + symbol.toUpperCase();
    }
}
