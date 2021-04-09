package com.jbgbh.rSocket.entity;

import lombok.Data;

@Data
public class FindDuration {
    private Integer minutes;

    public FindDuration(Integer minutesInput) {
        minutes = minutesInput;
    }
}
