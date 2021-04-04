package com.jbgbh.rSocket.entity;

import com.sun.jna.WString;
import lombok.Data;
import lombok.ToString;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

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

    public String toString() {
//        return this._year + "-" + this._month + "-" + this._day + "T" + this._hour + ":" + this._minute + ":" + this._second + "." + this._nano;
        return  String.format("%04d-%02d-%02dT%02d:%02d:%02d.%09d", this._year, this._month, this._day, this._hour, this._minute, this._second, this._nano);
    }

}
