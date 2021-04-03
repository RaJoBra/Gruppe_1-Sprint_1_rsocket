package com.jbgbh.rSocket.entity;

import org.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;

public class Changestream {
    ConnectionString connString = new ConnectionString(
            "mongodb+srv://AvG:AvG_Passwort@avg.xabms.mongodb.net/myFirstDatabase?retryWrites=true&w=majority"
    );
    MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connString)
            .retryWrites(true)
            .build();
    MongoClient mongoClient = MongoClients.create(settings);
    public MongoDatabase db = mongoClient.getDatabase("AvG");
}
