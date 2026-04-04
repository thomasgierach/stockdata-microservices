package findata.stock_query_service.repository;

import findata.common.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findTopBySymbolOrderByTradingDateDesc(String symbol);
}
