package com.jbgbh.rSocket.entity;

import com.google.gson.JsonObject;
import lombok.Data;
import org.apache.commons.validator.GenericValidator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Document
@Data
public class StockExchange {

    private static final Integer ID = 1;
    private String _name;
    private LocalDateTime _timestamp;
    @Id
    private String _id;


    public StockExchange(String id, String name, LocalDateTime time) {
        _id = id;
        _name = name;
        _timestamp = time;

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
                && GenericValidator.isInt(input.get("_id").getAsString())
                && !(GenericValidator.isBlankOrNull(input.get("_name").getAsString()))
        ) {
            this._timestamp = createTime.toDateTime();
            this._id = input.get("_id").getAsString();
            this._name = input.get("_name").getAsString();
        } else {
            // gebe ein Fehlerhaftes Objekt zurück
            this._id = "-1";
            this._timestamp = LocalDateTime.now();
            this._name = "error";
        }
    }

}
