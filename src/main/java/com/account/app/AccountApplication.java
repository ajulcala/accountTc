package com.account.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableEurekaClient
@EnableMongoRepositories
@SpringBootApplication
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

}
