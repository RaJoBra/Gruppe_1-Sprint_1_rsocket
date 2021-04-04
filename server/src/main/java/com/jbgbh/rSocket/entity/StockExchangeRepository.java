package com.jbgbh.rSocket.entity;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import reactor.core.publisher.Flux;

public class StockExchangeRepository {
//    public StockExchange findById(String id);

    public String createNew (String stock) {

        ObjectMapper objectMapper = new ObjectMapper();
//        List<StockExchange> stockList =
//        Flux.fromIterable(stockList)
//                .flatMap(this.reactiveMongoTemplate::save)
//                .doOnComplete(() -> System.out.println("Complete"))
//                .subscribe();
        return "done";
    }
}
