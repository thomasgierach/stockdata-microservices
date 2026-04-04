package com.findata.consumer.service;

import findata.common.model.Stock;
import findata.common.model.StockEvent;
import com.findata.consumer.repository.StockRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class StockConsumer {
    private static final Logger log = LoggerFactory.getLogger(StockConsumer.class);
    private final StockRepository stockRepository;
    private final RedisCacheService redisCacheService;

    public StockConsumer(StockRepository stockRepository, RedisCacheService redisCacheService) {
        this.stockRepository = stockRepository;
        this.redisCacheService = redisCacheService;
    }

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "stock-group")
    public void consume(StockEvent event) {
        Stock stock = stockRepository
                .findBySymbolAndTradingDate(event.getSymbol(), event.getDate())
                .orElseGet(Stock::new);

        stock.setSymbol(event.getSymbol());
        stock.setTradingDate(event.getDate());
        stock.setOpenPrice(event.getOpen());
        stock.setHighPrice(event.getHigh());
        stock.setLowPrice(event.getLow());
        stock.setClosePrice(event.getClose());
        stock.setVolume(event.getVolume());
        stock.setSource(event.getSource());

        log.info("Consumed stock event for {} on {}, saving to DB and updating cache", event.getSymbol(), event.getDate());
        stockRepository.save(stock);
        redisCacheService.put(event);
    }
}