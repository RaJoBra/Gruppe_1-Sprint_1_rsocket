package com.jbgbh.rSocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class RSocketApplication {


	public static void main(String[] args) {
		SpringApplication.run(RSocketApplication.class, args);
	}

}
