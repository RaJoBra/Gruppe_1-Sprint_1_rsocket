package com.jbgbh.rSocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jbgbh.rSocket.entity.Message;
import com.jbgbh.rSocket.entity.MockDB;
import com.jbgbh.rSocket.entity.StockExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Controller
public class rSocketController {

    private final List<RSocketRequester> CLIENTS = new ArrayList<>();

    @Autowired
    MockDB mockdb;

    @PreDestroy
    void shutdown() {
        log.info("Detaching all remaining clients...");
        CLIENTS.stream().forEach(requester -> requester.rsocket().dispose());
        log.info("Shutting down.");
    }

    @ConnectMapping("shell-client")
    void connectShellClientAndAskForTelemetry(RSocketRequester requester, @Payload String client) {

        requester.rsocket()
                .onClose()
                .doFirst(() -> {
                    // Add all new clients to a client list
                    log.info("Client: {} CONNECTED.", client);
                    CLIENTS.add(requester);
                })
                .doOnError(error -> {
                    // Warn when channels are closed by clients
                    log.warn("Channel to client {} CLOSED", client);
                })
                .doFinally(consumer -> {
                    // Remove disconnected clients from the client list
                    CLIENTS.remove(requester);
                    log.info("Client {} DISCONNECTED", client);
                })
                .subscribe();


        // Callback to client, confirming connection
        requester.route("client-status")
                .data("OPEN")
                .retrieveFlux(String.class)
                .doOnNext(s -> log.info("Client: {} Free Memory: {}.",client,s))
                .subscribe();
    }

    @MessageMapping("find-trade")
    StockExchange findTrade(String request) throws Exception {
        // Create Inital stockExchange Object to recive
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();

        Integer requestId = jsonObject.get("_id").getAsInt();

        log.info("Received request-response request for Stock Exchange with ID: {}", requestId);

        StockExchange result = mockdb.findById(requestId);
        System.out.println("result:" + result);
        if(result.get_id() != "-1") {
            return result;
        } else {
            throw new Exception("404_NOT_FOUND");
        }

    }

    @MessageMapping("create-trade")
    Message createTrade(String request) throws Exception {
        log.info("Received createTrade request for Stock Exchange: {}", request);

        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();

        StockExchange result = new StockExchange(jsonObject);

        System.out.println("created new Stockexchange:");
        System.out.println(result);

        if(!result.get_id().equals("-1")) {
            if (mockdb.insert(result)) {
                return new Message("Created successfully!");
            } else {
                return new Message("500_CANNOT_REACH_DB");
            }
        } else {
            throw new Exception("400_BAD_REQUEST");
        }

    }

    @MessageMapping("delete-trade")
    Message deleteTrade(String request) throws Exception {
        // Create Inital Object to store recieved data
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();

        Integer requestId = jsonObject.get("_id").getAsInt();

        log.info("Received delete-trade request for Stock Exchange with ID: {}", requestId);

        boolean result = mockdb.deleteById(requestId);
        System.out.println("result:" + result);
        if(result) {
            return new Message("Deleted trade with id " + requestId + "!");
        } else {
            throw new Exception("404_NOT_FOUND");
        }

    }

    @MessageMapping("stream-selection")
    Flux<StockExchange> streamSelection(String request) throws Exception{
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();

        Integer requestedMinutes = jsonObject.get("minutes").getAsInt();

        log.info("Recevied stream requester for the duration of {} minutes", requestedMinutes);

        return Flux
                .fromIterable(mockdb.findPast(requestedMinutes))
                .delayElements(Duration.ofSeconds(2));
    }

    @MessageMapping("stream-all")
    Flux<StockExchange> streamAll(Integer streamDuration) throws Exception{
        log.info("Recevied stream requester for the duration of {} seconds", streamDuration);
        if(streamDuration < 0 ) {
            throw new Exception("400_BAD_REQUEST");
        } else {
            return Flux
                    .fromIterable(mockdb.db)
                    .delayElements(Duration.ofSeconds(2));

        }
    }
}
