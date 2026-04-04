package findata.stock_query_service.controller;

import findata.common.model.StockEvent;
import findata.stock_query_service.service.StockQueryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stocks")
public class StockController {

    private final StockQueryService stockQueryService;

    public StockController(StockQueryService stockQueryService) {
        this.stockQueryService = stockQueryService;
    }

    @GetMapping("/{symbol}")
    public StockEvent getStock(@PathVariable String symbol) {
        return stockQueryService.getStock(symbol);
    }
}