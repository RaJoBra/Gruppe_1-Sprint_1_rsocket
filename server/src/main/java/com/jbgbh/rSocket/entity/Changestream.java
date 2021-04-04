package com.jbgbh.rSocket.entity;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.BsonDocument;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class Changestream {
//    ConnectionString connectionString = "mongodb+srv://AvG:AvG_Passwort@avg.xabms.mongodb.net/myFirstDatabase?retryWrites=true&w=majority";
//    CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
//    CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), pojoCodecRegistry);
//    MongoClientSettings clientSettings = MongoClientSettings.builder()
//            .applyConnectionString(connectionString)
//            .codecRegistry(codecRegistry)
//            .build();

//        try (MongoClient mongoClient = MongoClients.create(clientSettings)) {
//        MongoDatabase db = mongoClient.getDatabase("AvG");
//        MongoCollection stocks = db.getCollection("StockExchange");
//    }

}
