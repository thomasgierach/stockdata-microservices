package findata.stock_query_service;

import findata.common.model.Stock;
import findata.common.model.StockEvent;
import findata.stock_query_service.repository.StockRepository;
import findata.stock_query_service.service.AlphaVantageClient;
import findata.stock_query_service.service.StockKafkaPublisher;
import findata.stock_query_service.service.RedisCacheService;
import findata.stock_query_service.service.StockQueryService;

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

import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigInteger;
import java.util.Random;

@ExtendWith(MockitoExtension.class)
public class StockQueryServiceTest  {
    private Random rand = new Random();
    private int randomInt = new Random().nextInt(100001); 
    private BigDecimal open = new BigDecimal(BigInteger.valueOf(randomInt), 2);
    private BigDecimal close = new BigDecimal(BigInteger.valueOf(randomInt + 1), 2);
    private BigDecimal high = new BigDecimal(BigInteger.valueOf(randomInt + 2), 2);
    private BigDecimal low = new BigDecimal(BigInteger.valueOf(randomInt - 1), 2);

    private String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private String symbol;

    @Mock
    private StockRepository stockRepository;

    @Mock
    private RedisCacheService redisCacheService;

    @Mock
    private AlphaVantageClient alphaVantageClient;

    @Mock
    private StockKafkaPublisher stockKafkaPublisher;

    private StockQueryService stockQueryService;

    @BeforeEach
    void setUp() {
        stockQueryService = new StockQueryService(
            redisCacheService,
            stockRepository,
            alphaVantageClient,
            stockKafkaPublisher
        );
    }

    String appendBuilder() {
        StringBuilder sb = new StringBuilder(4);
        for (int i = 0; i < 4; i++) {
            sb.append(
                chars.charAt(
                    rand.nextInt(
                        chars.length()
                    )
                )
            );
        }
        return sb.toString();
    }

    @Test 
    void verifyMapToEvent() {
        Stock stock = new Stock(
            symbol,
            open,
            close,
            high,
            low,
            LocalDate.parse("2026-03-16"),
            randomInt,
            "ALPHAVANTAGE"
        );

        StockEvent event = stockQueryService.mapToEvent(stock);
        //stock.print();
        //event.print();

        assertEquals(stock.getSymbol(), event.getSymbol());
        assertEquals(stock.getOpenPrice(), event.getOpen());
        assertEquals(stock.getClosePrice(), event.getClose());
        assertEquals(stock.getHighPrice(), event.getHigh());
        assertEquals(stock.getLowPrice(), event.getLow());
        assertEquals(stock.getTradingDate(), event.getDate());
        assertEquals(stock.getVolume(), event.getVolume());
    }
    /* 
    @Test
    void verifyNewSymbol() {
        symbol = appendBuilder();
        //System.out.println(symbol);
        StockEvent event = new StockEvent(
            symbol,
            open,
            close,
            high,
            low,
            LocalDate.parse("2026-03-16"),
            randomInt,
            "ALPHAVANTAGE"
        );

        when(stockRepository.findBySymbolAndTradingDate(symbol, LocalDate.parse("2026-03-16")))
        .thenReturn(java.util.Optional.empty());
        
        stockconsumer.consume(event);
        ArgumentCaptor<Stock> captor = ArgumentCaptor.forClass(Stock.class);
        verify(stockRepository).save(captor.capture());

        Stock saved = captor.getValue();
      
        assertEquals(symbol, saved.getSymbol());
        assertEquals(open, saved.getOpenPrice());
        assertEquals(close, saved.getClosePrice());
        assertEquals(high, saved.getHighPrice());
        assertEquals(low, saved.getLowPrice());
        assertEquals(LocalDate.parse("2026-03-16"), saved.getTradingDate());
        assertEquals(randomInt, saved.getVolume());
        assertEquals("ALPHAVANTAGE", saved.getSource());
        
    }
    
    @Test
    void verifyRedis() {
        symbol = appendBuilder();
        //System.out.println(symbol);
        StockEvent event = new StockEvent(
            symbol,
            open,
            close,
            high,
            low,
            LocalDate.parse("2026-03-16"),
            randomInt,
            "ALPHAVANTAGE"
        );

        when(stockRepository.findBySymbolAndTradingDate(symbol, LocalDate.parse("2026-03-16")))
        .thenReturn(java.util.Optional.empty());

        stockconsumer.consume(event);
        
        verify(redisCacheService).put(eq(event));
        System.out.println("Redis cache put method called with event: " + event.getSymbol() + " on date: " + event.getDate());
    }
    */
}
