package com.jbgbh.rSocket.entity;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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

        // empty List to save found trades
        List<StockExchange> foundStocktrades = new ArrayList<>();

        // current time and time (input) minutes ago
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime compareTime = currentTime.minusMinutes(minutes);

        // parse db and save trades from less than (input) minutes ago
        for (StockExchange stock : db) {
            if(compareTime.isBefore(stock.get_timestamp())) {
                foundStocktrades.add(stock);
            }
        }

        // return found trades
        return foundStocktrades;
    }

    public boolean insert(StockExchange insert) {
        //initial value for the id of the new trade
        Integer newId = 0;

        // switch to save if an id is available or not
        Boolean searchForId = true;

        // index to parse db
        Integer index = 1;

        // search for every id starting from 1 to find a deleted/ free id for the new trade
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
        try {
            db.add(insert);

            System.out.println("Insert successful!");
            return true;
        } catch (Exception e) {
            System.out.println("Error on Insert: " + e);
            return false;
        }
    }

    public StockExchange findById(Integer id) {
        System.out.println("Looking for:" + id);

        // search for id in db and return if found
        for (StockExchange stock : db) {
            if(stock.get_id().equals(id.toString())) {
                System.out.println("Found Stock By id: " + id + " Stock: " + stock.toString());
                return stock;
            }
        }

        // return an invalid object that is processed to a faulty response if no trade with id was found
        return new StockExchange("-1", "error", LocalDateTime.now());
    }

    public boolean deleteById(Integer id) {
        System.out.println("Looking for:" + id);

        // find trade with given id and remove it
        for (StockExchange stock : db) {
            if(stock.get_id().equals(id.toString())) {
                System.out.println("Found Stock to remove By id: " + id + " Stock: " + stock.toString());

                // try to remove the trade and return the result
                try {
                    db.remove(stock);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error on delete: " + e);
                    return false;
                }
            }
        }
        // if no trade was found return false
        return false;
    }

    // scheduled task to get difference in the db over past 2 seconds
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

    // generate a Date between two given datetimes
    public LocalDateTime generateDate() {
        long minDayTime = LocalDateTime.of(2021, 4, 9, 0, 5).toEpochSecond(ZoneOffset.ofHours(0));
        long maxDayTime = LocalDateTime.of(2021, 4, 9, 0, 10).toEpochSecond(ZoneOffset.ofHours(0));
        long randomDay = ThreadLocalRandom.current().nextLong(minDayTime, maxDayTime);
        LocalDateTime randomDate = LocalDateTime.ofEpochSecond(randomDay, 0, ZoneOffset.ofHours(0));
        System.out.println(randomDate);

        return randomDate;
    }

    // generate random names
    public String generateName() {
        ArrayList<String> companyNameList = new ArrayList<>();
        companyNameList.add("Amazon");
        companyNameList.add("Google");
        companyNameList.add("Facebook");
        companyNameList.add("JBGBKH");
        companyNameList.add("HSKA");
        companyNameList.add("VW");

        return companyNameList.get(((int) (Math.random() * 6 + 1) - 1));
    }

    // scheduled task to get difference in the db over past 2 seconds
    @Scheduled(fixedDelay = 1000)
    public void simulateTraffic() {
        StockExchange tmp = new StockExchange(null, generateName(), generateDate());

        this.insert(tmp);
    }
}
