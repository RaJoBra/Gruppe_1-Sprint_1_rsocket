package com.jbgbh.rSocket;

import com.jbgbh.rSocket.entity.StockExchange;
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
import java.util.UUID;

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
    public void requestResponse() throws Exception {
        log.info("\n Sending one request. Waiting for one response...");
        StockExchange stockExchange = this.rsocketRequester
                .route("request-response")
                .data("1") // Hard coded Stock Exchange id
                .retrieveMono(StockExchange.class)
                .block();
        log.info("\n Response was: {}", stockExchange);
    }

    @ShellMethod("Send one request. Many responses (stream) will be printed.")
    public void stream() {
        log.info("\n\n**** Request-Stream\n**** Send one request.\n**** Log responses.\n**** Type 's' to stop.");
        Object disposable = this.rsocketRequester
                .route("stream")
                .data(10)
                .retrieveFlux(StockExchange.class)
                .subscribe(stockExchange -> log.info("Response: {} (Type 's' to stop.)", stockExchange));
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
