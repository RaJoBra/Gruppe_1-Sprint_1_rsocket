package com.jbgbh.rSocket.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
public class StockExchange {

    private static final Integer ID = 1;
    private String _name;
    private LocalDateTime _timestamp;
    private String _id;


    public StockExchange() {
        _id = String.valueOf((ID ));
        _name = generateName();
        _timestamp = LocalDateTime.now();

    }

    public StockExchange(String Id, String Name) {
        _id = Id;
        _name = Name;
        _timestamp = LocalDateTime.now();

    }

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

}
