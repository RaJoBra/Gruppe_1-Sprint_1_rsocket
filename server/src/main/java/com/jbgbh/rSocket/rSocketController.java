package com.jbgbh.rSocket;

import com.jbgbh.rSocket.entity.StockExchange;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class rSocketController {

    private final List<RSocketRequester> CLIENTS = new ArrayList<>();

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

    @MessageMapping("request-response")
    StockExchange requestResponse(String request) throws Exception {
        // Create Inital stockExchange Object to recive
        StockExchange stockExchange = new StockExchange();
        log.info("Received request-response request for Stock Exchange: {}", request);
        if(stockExchange.get_id().equals(request)) {
            return stockExchange;
        } else {
            throw new Exception("404_NOT_FOUND");
        }
        
    }

    @MessageMapping("stream")
    Flux<StockExchange> stream(Integer streamDuration) throws Exception{
        log.info("Recevied stream requester for the duration of {} seconds", streamDuration);
        if(streamDuration < 0 ) {
            throw new Exception("400_BAD_REQUEST");
        } else {

            return Flux
                    .interval(Duration.ofSeconds(1)) // Stream Duration in Seconds and not in Minuts
                    .map(index -> new StockExchange());
        }
    }
}
