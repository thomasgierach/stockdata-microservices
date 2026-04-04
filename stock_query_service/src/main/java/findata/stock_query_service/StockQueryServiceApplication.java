package findata.stock_query_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "findata.common.model")
@ComponentScan(basePackages = {"findata.stock_query_service", "findata.common"})
public class StockQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockQueryServiceApplication.class, args);
	}

}
