package com.jbgbh.rSocket.entity;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MockDB {

    public List<StockExchange> db = new ArrayList<>() {{
        add(new StockExchange("1", "Bing", LocalDateTime.of(2021, 4, 4, 17, 18)));
        add(new StockExchange("2", "Tesla", LocalDateTime.of(2021, 4, 4, 17, 19)));
        add(new StockExchange("3", "Google", LocalDateTime.of(2021, 4, 4, 17, 20)));
    }};

    public boolean insert(StockExchange insert) {
        if(db.stream().map(StockExchange::get_id).noneMatch(stock -> stock.equals(insert.get_id()))) {
            System.out.println("No Stock found with id: " + insert.get_id() + " insert Object: " + insert.toString());
            db.add(insert);
            System.out.println("Insert successful!");
            return true;
        } else {
            return false;
        }
    }

    public StockExchange findById(String id) {
        for (StockExchange stock : db) {
            if(stock.get_id() == id) {
                System.out.println("Found Stock By id: " + id + " Stock: " + stock.toString());
                return stock;
            }
        }
        return new StockExchange("-1", "error", LocalDateTime.now());
    }

    public List<StockExchange> getDb() {
        return db;
    }

    public boolean deleteById(String id) {
        for (StockExchange stock : db) {
            if(stock.get_id() == id) {
                System.out.println("Found Stock to remove By id: " + id + " Stock: " + stock.toString());
                db.remove(stock);
                return true;
            }
        }
        return false;
    }
}
