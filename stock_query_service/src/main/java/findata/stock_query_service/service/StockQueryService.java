package findata.stock_query_service.service;

import findata.common.model.Stock;
import findata.common.model.StockEvent;
import findata.stock_query_service.repository.StockRepository;
import findata.stock_query_service.service.AlphaVantageClient;
import findata.stock_query_service.service.StockKafkaPublisher;
import findata.stock_query_service.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class StockQueryService {
    private static final Logger log = LoggerFactory.getLogger(StockQueryService.class);
    private final RedisCacheService redisCacheService;
    private final StockRepository stockRepository;
    private final AlphaVantageClient alphaVantageClient;
    private final StockKafkaPublisher stockKafkaPublisher;

    public StockQueryService(
            RedisCacheService redisCacheService,
            StockRepository stockRepository,
            AlphaVantageClient alphaVantageClient,
            StockKafkaPublisher stockKafkaPublisher) {
        this.redisCacheService = redisCacheService;
        this.stockRepository = stockRepository;
        this.alphaVantageClient = alphaVantageClient;
        this.stockKafkaPublisher = stockKafkaPublisher;
    }

    public StockEvent getStock(String symbol) {
        String normalizedSymbol = symbol.toUpperCase();

        StockEvent cached = redisCacheService.get(normalizedSymbol);
        if (cached != null) {
            log.info("Cache HIT for {}, returning cached result", normalizedSymbol);
            return cached;
        }

        StockEvent dbResult = stockRepository.findTopBySymbolOrderByTradingDateDesc(normalizedSymbol)
                .map(this::mapToEvent)
                .orElse(null);

        if (dbResult != null) {
            log.info("Cache MISS for {}, found in DB, caching result", normalizedSymbol);
            redisCacheService.put(dbResult);
            return dbResult;
        }

        StockEvent fresh = alphaVantageClient.fetchQuote(normalizedSymbol);
        redisCacheService.put(fresh);
        stockKafkaPublisher.publish(fresh);
        return fresh;
    }

    public StockEvent mapToEvent(Stock stock) {
        return new StockEvent(
                stock.getSymbol(), 
                stock.getOpenPrice(),
                stock.getClosePrice(),
                stock.getHighPrice(),
                stock.getLowPrice(),
                stock.getTradingDate(),
                stock.getVolume(),
                stock.getSource()
        );
    }
}
