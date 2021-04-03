package com.jbgbh.rSocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Flux;

import java.time.Duration;

@ShellComponent
@Slf4j
public class rSocketShellClient {

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
