package com.jbgbh.rSocket.entity;

import lombok.Data;

@Data
public class Message {

    private String _state;

    public Message(String Message) {
        this._state = Message;
    }
}
