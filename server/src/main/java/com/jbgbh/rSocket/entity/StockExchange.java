package com.jbgbh.rSocket.entity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Data;
import org.apache.commons.validator.GenericValidator;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

@Document
@Data
public class StockExchange {

    private static final Integer ID = 1;
    private String _name;
    private LocalDateTime _timestamp;
    @Id
    private String _id;


    public StockExchange() {
        _id = String.valueOf((ID ));
        _name = generateName();
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

    // generiere Transaktions-Datensatz aus Json
    public StockExchange generateFromString(String input) {
        JsonObject jsonObject = new JsonParser().parse(input).getAsJsonObject();
        JsonObject Time = jsonObject.get("_timestamp").getAsJsonObject();
        System.out.println(Time);
        Time createTime = new Time(
                Time.get("year").getAsInt(),
                Time.get("monthValue").getAsInt(),
                Time.get("dayOfMonth").getAsInt(),
                Time.get("hour").getAsInt(),
                Time.get("minute").getAsInt(),
                Time.get("second").getAsInt(),
                Time.get("nano").getAsInt()
        );

        System.out.println("Create Time ToString");
        System.out.println(createTime.toString());

        System.out.println("Check");
        System.out.println(GenericValidator.isDate(createTime.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", true));

        // prüfe ob valide Werte eingetragen sind
        if(
                jsonObject.isJsonObject()
                && GenericValidator.isDate(createTime.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", false)
                && GenericValidator.isInt(jsonObject.get("_id").getAsString())
                && !(GenericValidator.isBlankOrNull(jsonObject.get("_name").getAsString()))
        ) {
            StockExchange result = new StockExchange();
            result._id = jsonObject.get("_id").getAsString();
            result._name = jsonObject.get("_name").getAsString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            LocalDateTime dateTime = LocalDateTime.parse(createTime.toString(), formatter);

            result._timestamp = dateTime;

            return result;
        } else {
            // gebe ein Fehlerhaftes Objekt zurück
            StockExchange result = new StockExchange();
            result._id = "-1";
            result._timestamp = LocalDateTime.now();
            result._name = "error";

            return result;
        }
    }

}
