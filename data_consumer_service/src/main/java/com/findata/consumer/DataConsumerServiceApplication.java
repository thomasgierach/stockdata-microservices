package com.findata.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "findata.common.model")
public class DataConsumerServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataConsumerServiceApplication.class, args);
	}

}
