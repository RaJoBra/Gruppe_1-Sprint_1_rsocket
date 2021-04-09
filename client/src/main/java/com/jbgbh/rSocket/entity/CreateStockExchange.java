package com.jbgbh.rSocket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateStockExchange {
    private String _name;
    private LocalDateTime _timestamp;
    private String _id;

    public CreateStockExchange(String Name) {
        _name = Name;
        _timestamp = LocalDateTime.now();

    }
}
