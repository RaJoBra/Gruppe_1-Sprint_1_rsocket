package com.jbgbh.rSocket.entity;

import com.google.gson.JsonObject;
import lombok.Data;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDateTime;

@Data
public class StockExchange {

    private static final Integer ID = 1;
    private String _name;
    private LocalDateTime _timestamp;
    private String _id;

    public StockExchange(String id, String name, LocalDateTime time) {
        _id = id;
        _name = name;
        _timestamp = time;

    }

    // generiere Transaktions-Datensatz aus Json
    public StockExchange (JsonObject input) {
        JsonObject Time = input.get("_timestamp").getAsJsonObject();
        Time createTime = new Time(
                Time.get("year").getAsInt(),
                Time.get("monthValue").getAsInt(),
                Time.get("dayOfMonth").getAsInt(),
                Time.get("hour").getAsInt(),
                Time.get("minute").getAsInt(),
                Time.get("second").getAsInt(),
                Time.get("nano").getAsInt()
        );

        // prüfe ob valide Werte eingetragen sind
        if(
                input.isJsonObject()
                && !(GenericValidator.isBlankOrNull(input.get("_name").getAsString()))
        ) {
            this._timestamp = createTime.toDateTime();
            this._name = input.get("_name").getAsString();
            this._id = "0";
        } else {
            // gebe ein Fehlerhaftes Objekt zurück
            this._id = "-1";
            this._timestamp = LocalDateTime.now();
            this._name = "error";
        }
    }

}
