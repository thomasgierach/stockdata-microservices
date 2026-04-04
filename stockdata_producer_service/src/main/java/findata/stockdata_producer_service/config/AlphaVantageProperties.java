package findata.stockdata_producer_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;

@Component
@ConfigurationProperties(prefix = "alpha-vantage")
public class AlphaVantageProperties {
    private String baseUrl;
    private String apiKey;
    
    private String function_name;
    private String symbol;


    public String getBaseUrl() {
        return baseUrl;
    }
    
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getFunctionName() {
        return function_name;
    }

    public void setFunctionName(String function_name) {
        this.function_name = function_name;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    /* 
    @PostConstruct
    public void debug() {
        System.out.println("Base URL: " + baseUrl);
        System.out.println("API Key: " + apiKey);
    }
    */
}

