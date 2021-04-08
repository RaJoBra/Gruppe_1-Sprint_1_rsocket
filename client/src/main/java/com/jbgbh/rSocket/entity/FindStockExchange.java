package com.jbgbh.rSocket.entity;

import lombok.Data;

@Data
public class FindStockExchange {
    private Integer _id;

    public FindStockExchange(Integer id) {
        _id = id;
    }
}
