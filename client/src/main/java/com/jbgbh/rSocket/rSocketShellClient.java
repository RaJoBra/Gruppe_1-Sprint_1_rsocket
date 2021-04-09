package com.jbgbh.rSocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jbgbh.rSocket.entity.*;
import io.rsocket.SocketAcceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import javax.annotation.PreDestroy;
import java.time.Duration;

@ShellComponent
@Slf4j
public class rSocketShellClient {
    private final RSocketRequester rsocketRequester;
    private static Disposable disposable;

    @Deprecated
    @Autowired
    public rSocketShellClient(RSocketRequester.Builder rsocketRequesterBuilder, RSocketStrategies strategies) {
        String client = "JBGBH rSocket Client";
        log.info("Connecting using client ID: {}", client);

        SocketAcceptor responder = RSocketMessageHandler.responder(strategies, new ClientHandler());

        this.rsocketRequester = rsocketRequesterBuilder
                .setupRoute("shell-client")
                .setupData(client)
                .rsocketStrategies(strategies)
                .rsocketConnector(connector -> connector.acceptor(responder))
                .connectTcp("127.0.0.1", 9080)
                .block();

        this.rsocketRequester.rsocket()
                .onClose()
                .doOnError(error -> log.warn("Connection CLOSED"))
                .doFinally(consumer -> log.info("Client DISCONNECTED"))
                .subscribe();
    }

    public rSocketShellClient(RSocketRequester rsocketRequester) {
        this.rsocketRequester = rsocketRequester;
    }

    @PreDestroy
    void shutdown() {
        rsocketRequester.rsocket().dispose();
    }

    @ShellMethod("Send one request. One response will be printed.")
    public void findTrade() throws Exception {

        // userinput
        System.out.println("Enter the id of the Trade you want to find");
        String id = System.console().readLine();

        // try to parse input and print out result or return exception
        try {
            // validate input
            Integer searchId = Integer.parseInt(id);
            FindStockExchange find = new FindStockExchange(searchId);

            // convert to json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(find);

            log.info("\n Sending one request. Waiting for one response...");

            // execute request and save result
            StockExchange stockExchange = this.rsocketRequester
                    .route("find-trade")
                    .data(json)
                    .retrieveMono(StockExchange.class)
                    .block();

            log.info("\n Response was: {}", stockExchange);
        } catch (Exception e) {
            System.out.println("The Input was not a valid Number");
        }
    }

    @ShellMethod("Send one request. One response will be printed.")
    public void createTrade() throws Exception {

        // userinput
        System.out.println("Enter a name:");
        String name = System.console().readLine();

        CreateStockExchange newExchange = new CreateStockExchange(name);

        // convert to json
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(newExchange);

        log.info("\n Sending one request. Waiting for one response...");

        // execute request and save result
        Message message = this.rsocketRequester
                .route("create-trade")
//                .data("{_id:\"3\",_timestamp:\"2021-04-03T23:39:51.687369800\",_name:\"NEW\"}") // TESTDATA
                .data(json)
                .retrieveMono(Message.class)
                .block();

        log.info("\n Response was: {}", message.get_state());
    }

    @ShellMethod("Send one request. One response will be printed.")
    public void deleteTrade() throws Exception {

        // userinput
        System.out.println("Enter the id of the Trade you want to delete");
        String id = System.console().readLine();

        // try to parse input and print out result or return exception
        try {
            // validate data
            Integer searchId = Integer.parseInt(id);
            FindStockExchange delete = new FindStockExchange(searchId);

            // build json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(delete);

            log.info("\n Sending one request. Waiting for one response...");

            // execute request and save result
            Message message = this.rsocketRequester
                    .route("delete-trade")
                    .data(json)
                    .retrieveMono(Message.class)
                    .block();

            log.info("\n Response was: {}", message.get_state());
        } catch (Exception e) {
            System.out.println("The Input was not a valid Number");
        }
    }

    @ShellMethod("Send one request. Many responses (stream) will be printed.")
    public void streamAll() {
        log.info("\n\n**** Request-Stream\n**** Send one request.\n**** Log responses.\n**** Type 's' to stop.");
        Object disposable = this.rsocketRequester
                .route("stream-all")
                .retrieveFlux(StockExchange.class)
                .subscribe(stockExchange -> log.info("Response: {} (Type 's' to stop.)", stockExchange));
    }

    @ShellMethod("Send one request. Many responses (stream) will be printed.")
    public void streamSelection() {
        log.info("\n\n**** Request-Stream\n**** Send one request.\n**** Log responses.\n**** Type 's' to stop.");

        // userinput
        System.out.println("Enter a Duration in Minutes: ");
        String duration = System.console().readLine();

        Integer toWatch = 0;

        try {
            // validate data
            toWatch = Integer.parseInt(duration);
            FindDuration toFind = new FindDuration(toWatch);

            // convert to json
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(toFind);

            // check if input time is valid
            if (toWatch != 0) {
                Object disposable = this.rsocketRequester
                        .route("stream-selection")
                        .data(json)
                        .retrieveFlux(StockExchange.class)
                        .subscribe(stockExchange -> log.info("Response: {} (Type 's' to stop.)", stockExchange));
            }
        } catch (Exception e) {
            System.out.println("The Input was not a valid Number");
        }

    }

    @ShellMethod("Stops Streams or Channels.")
    public void s() {
        if (null != disposable) {
            log.info("Stopping the incoming stream.");
            disposable.dispose();
            log.info("Stream stopped.");
        }
    }
}

@Slf4j
class ClientHandler {

    @MessageMapping("client-status")
    public Flux<String> statusUpdate(String status) {
        log.info("Connection {}", status);
        //return Mono.just(System.getProperty("java.vendor") + " v" + System.getProperty("java.version"));
        return Flux.interval(Duration.ofSeconds(5)).map(index -> String.valueOf(Runtime.getRuntime().freeMemory()));
    }
}
