package com.ranindu.SentinelVault.AI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class})
public class SentinelVaultAiApplication {
	public static void main(String[] args) {
		SpringApplication.run(SentinelVaultAiApplication.class, args);
	}

}
