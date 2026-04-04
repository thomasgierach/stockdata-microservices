package com.findata.consumer.repository;

import findata.common.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.Optional;


public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findBySymbolAndTradingDate(String symbol, LocalDate tradingDate);

        
}