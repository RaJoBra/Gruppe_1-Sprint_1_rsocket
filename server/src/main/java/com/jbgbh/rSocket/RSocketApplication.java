package com.jbgbh.rSocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@SpringBootApplication
@EnableReactiveMongoRepositories
public class RSocketApplication {


	public static void main(String[] args) {
		SpringApplication.run(RSocketApplication.class, args);
	}

}
