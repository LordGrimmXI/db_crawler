package com.vistora.db_crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.vistora.db_crawler")
public class DatabaseCrawlerApplication {
	public static void main(String[] args) {
		SpringApplication.run(DatabaseCrawlerApplication.class, args);
	}

}
