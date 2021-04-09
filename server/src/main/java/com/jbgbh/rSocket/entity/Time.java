package com.jbgbh.rSocket.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Time {

    private Integer _year;
    private Integer _month;
    private Integer _day;
    private Integer _hour;
    private Integer _minute;
    private Integer _second;
    private Integer _nano;

    public Time(Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second, Integer nano) {
        this._year = year;
        this._month = month;
        this._day = day;
        this._hour = hour;
        this._minute = minute;
        this._second = second;
        this._nano = nano;
    }

    public LocalDateTime toDateTime() {
//        return this._year + "-" + this._month + "-" + this._day + "T" + this._hour + ":" + this._minute + ":" + this._second + "." + this._nano;
        try {
            String timeAsString = String.format("%04d-%02d-%02dT%02d:%02d:%02d.%09d", this._year, this._month, this._day, this._hour, this._minute, this._second, this._nano);
            return LocalDateTime.parse(timeAsString);
        } catch (Exception e) {
            System.out.println("invalid Date! " + this.toString());
            return LocalDateTime.parse("1000-01-01T00:00:00.000000000");
        }
    }

}
