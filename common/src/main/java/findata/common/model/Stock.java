package findata.common.model;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.GenerationType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "stocks", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"symbol", "tradingDate"})
})
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String symbol;
    private LocalDate tradingDate;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal openPrice;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal highPrice;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal lowPrice;
    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal closePrice;
    @Column(nullable = false)
    private Integer volume;
    private String source;

    public Stock() {
    }

    public Stock(String symbol, 
                 BigDecimal openPrice, 
                 BigDecimal closePrice, 
                 BigDecimal highPrice,
                 BigDecimal lowPrice,   
                 LocalDate tradingDate, 
                 Integer volume, 
                 String source) {

        this.symbol = symbol;
        this.openPrice = openPrice;
        this.highPrice = highPrice;
        this.lowPrice = lowPrice;
        this.closePrice = closePrice;
        this.tradingDate = tradingDate;
        this.volume = volume;
        this.source = source;
    }   

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public LocalDate getTradingDate() {
        return tradingDate;
    }

    public void setTradingDate(LocalDate tradingDate) {
        this.tradingDate = tradingDate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }
    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
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
        System.out.println("open " + this.getOpenPrice());
        System.out.println("close " + this.getClosePrice());
        System.out.println("high " + this.getHighPrice());
        System.out.println("low " + this.getLowPrice());
        System.out.println("tradingDate " + this.getTradingDate());
        System.out.println("volume " + this.getVolume());
        System.out.println("source " + this.getSource());
    }
}
