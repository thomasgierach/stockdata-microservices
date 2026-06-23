package findata.stock_query_service;

import findata.common.model.Stock;
import findata.stock_query_service.repository.StockRepository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = StockDataPostgresIntegrationTest.JpaOnlyTestConfig.class)
class StockDataPostgresIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine")
                    .withDatabaseName("stockdata_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        registry.add("spring.datasource.hikari.maximum-pool-size", () -> "2");
        registry.add("spring.datasource.hikari.minimum-idle", () -> "0");
        registry.add("spring.datasource.hikari.idle-timeout", () -> "10000");
        registry.add("spring.datasource.hikari.max-lifetime", () -> "30000");
    }

    @Autowired
    private StockRepository stockRepository;

    @Test
    void savesAndFindsLatestStockDataBySymbol() {
        String symbol = "AAPL";

        BigDecimal open = new BigDecimal("195.10");
        BigDecimal close = new BigDecimal("196.25");
        BigDecimal high = new BigDecimal("198.75");
        BigDecimal low = new BigDecimal("193.40");

        Stock stock = new Stock(
                symbol,
                open,
                close,
                high,
                low,
                LocalDate.parse("2026-03-16"),
                100000,
                "ALPHAVANTAGE"
        );

        stockRepository.saveAndFlush(stock);

        var result = stockRepository.findTopBySymbolOrderByTradingDateDesc(symbol);

        assertThat(result).isPresent();
        assertThat(result.get().getHighPrice()).isEqualByComparingTo(high);
    }

    @TestConfiguration
    @EntityScan(basePackages = "findata.common.model")
    @EnableJpaRepositories(basePackages = "findata.stock_query_service.repository")
    static class JpaOnlyTestConfig {
    }
}