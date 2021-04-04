package com.jbgbh.rSocket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockExchange {

    private String _name;
    private LocalDateTime _timestamp;
    private String _id;

    public StockExchange(String Id, String Name) {
        _id = Id;
        _name = Name;
        _timestamp = LocalDateTime.now();

    }

}
