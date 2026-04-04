package findata.common.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class StockEvent {
    private String symbol;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
    private LocalDate date;
    private String source;
    private Integer volume;

    public StockEvent() {
    }

    public StockEvent(String symbol, 
                      BigDecimal open, 
                      BigDecimal close, 
                      BigDecimal high, 
                      BigDecimal low, 
                      LocalDate date, 
                      Integer volume,
                      String source) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.close = close;
        this.date = date;
        this.source = source;
        
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public Integer getVolume() {
        return volume;
    }
    public void setVolume(Integer volume) {
        this.volume = volume;
    }
    
    public void print() {
        System.out.println("symbol " + this.getSymbol());
        System.out.println("open " + this.getOpen());
        System.out.println("close " + this.getClose());
        System.out.println("high " + this.getHigh());
        System.out.println("low " + this.getLow());
        System.out.println("tradingDate " + this.getDate());
        System.out.println("volume " + this.getVolume());
        System.out.println("source " + this.getSource());
    }
    
}
