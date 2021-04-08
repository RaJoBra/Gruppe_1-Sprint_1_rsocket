package com.jbgbh.rSocket.entity;

import org.springframework.scheduling.annotation.Scheduled;
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

    public List<StockExchange> sinceLastCheck = new ArrayList<>() {{ }};

    private Integer lastLength = 0;

    public List<StockExchange> findPast(Integer minutes) {
        System.out.println("Looking Trades over the past: " + minutes + "minutes");
        List<StockExchange> foundStocktrades = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();

        LocalDateTime compareTime = currentTime.minusMinutes(minutes);

        for (StockExchange stock : db) {
            if(compareTime.isBefore(stock.get_timestamp())) {
                foundStocktrades.add(stock);
            }
        }
        return foundStocktrades;
    }

    public boolean insert(StockExchange insert) {
        Integer newId = 0;
        Boolean searchForId = true;
        Integer index = 1;

        while (searchForId) {
            StockExchange result = findById(index);
            if (result.get_id() == "-1") {
                searchForId = false;
                newId = index;
            }
            index ++;
        }

        insert.set_id("" + newId);
        System.out.println("insert Object: " + insert.toString());
        db.add(insert);
        System.out.println("Insert successful!");
        return true;

    }

    public StockExchange findById(Integer id) {
        System.out.println("Looking for:" + id);
        for (StockExchange stock : db) {
            if(stock.get_id().equals(id.toString())) {
                System.out.println("Found Stock By id: " + id + " Stock: " + stock.toString());
                return stock;
            }
        }
        return new StockExchange("-1", "error", LocalDateTime.now());
    }

    public List<StockExchange> getDb() {
        return db;
    }

    public boolean deleteById(Integer id) {
        System.out.println("Looking for:" + id);
        for (StockExchange stock : db) {
            if(stock.get_id().equals(id.toString())) {
                System.out.println("Found Stock to remove By id: " + id + " Stock: " + stock.toString());
                db.remove(stock);
                return true;
            }
        }
        return false;
    }

    @Scheduled(fixedDelay=2000)
    public void checkChanges() {
        if(db.size() >= lastLength) {
            sinceLastCheck = new ArrayList<>() {{ }};
            for (int i = lastLength; i < db.size(); i++) {
                System.out.println("new Stock since last Check: "+ db.get(i));
                System.out.println("Stock marked for clearing and processing for batch-processing on central server!");
                sinceLastCheck.add(db.get(i));
            }
            lastLength = db.size();
        }
    }
}
